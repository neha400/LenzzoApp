package com.lenzzo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.HomeActivity;
import com.lenzzo.ProductDetailsActivity;
import com.lenzzo.R;
import com.lenzzo.api.API;
import com.lenzzo.fragment.BrandListFragment;
import com.lenzzo.fragment.CollectionFragment;
import com.lenzzo.fragment.ProductListFragment;
import com.lenzzo.model.BannerList;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.CustomVolleyRequest;
import com.lenzzo.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BannerListAdapter extends RecyclerView.Adapter<BannerListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<BannerList> bannerLists;
    private ImageLoader imageLoader;
    private Context context;

    public BannerListAdapter(Context ctx, List<BannerList> bannerLists){
        inflater = LayoutInflater.from(ctx);
        this.bannerLists = bannerLists;
        this.context=ctx;
    }

    @Override
    public BannerListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.banner_list_recycle_item, parent, false);
        BannerListAdapter.MyViewHolder holder = new BannerListAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BannerListAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(bannerLists.get(position).getPath(), ImageLoader.getImageListener(holder.banner_image, R.drawable.no_img, R.drawable.no_img));
        Picasso.get()
                .load(bannerLists.get(position).getPath())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.banner_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bannerLists.get(position).getCategory_id().equals("0")){
                    //  Brand list
                    Bundle bundle=new Bundle();
                    bundle.putString("category_id", bannerLists.get(position).getCategory_id());
                    BrandListFragment brandListFragment = new BrandListFragment();
                    brandListFragment.setArguments(bundle);
                    ((HomeActivity)context).replaceFragment(brandListFragment);
                }else if (!bannerLists.get(position).getProduct_id().equals("0")){
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("product_id",bannerLists.get(position).getProduct_id());
                    intent.putExtra("current_currency","");
                    intent.putExtra("title_name","");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }else if (!bannerLists.get(position).getBrand_id().equals("0")){
                    if (CommanMethod.isInternetConnected(context)){
                        getBrandChildList(bannerLists.get(position).getBrand_id());
                    }

                }else if (!bannerLists.get(position).getOffer_id().equals("0")){
                    // Open offer fragment
                    if (CommanMethod.isInternetConnected(context)){
                        getOfferList(bannerLists.get(position).getOffer_id());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bannerLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView banner_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            banner_image = (ImageView)itemView.findViewById(R.id.banner_image);
        }
    }

    private void getBrandChildList(final String brand_id){
        SessionManager sessionManager = new SessionManager(context);
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"getchildfrombrand", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("child"));
                        if(jsonArray.length()> 0) {
                            sessionManager.setCollectionBrandId(brand_id);
                            CollectionFragment collectionFragment = new CollectionFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("child_array_list",jsonArray.toString());
                            collectionFragment.setArguments(bundle);
                            ((HomeActivity)context).replaceFragment(collectionFragment);

                        }else {
                            Bundle bundle = new Bundle();
                            bundle.putString("brand_id", brand_id);
                            bundle.putString("brand_name", "");
                            bundle.putString("from","");
                            ProductListFragment productListFragment = new ProductListFragment();
                            productListFragment.setArguments(bundle);
                            ((HomeActivity)context).replaceFragment(productListFragment);
                        }
                    }else{
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("brandid", brand_id);
                params.put("current_currency", sessionManager.getCurrencyCode());
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void getOfferList(final String offerId){
        SessionManager sessionManager = new SessionManager(context);
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"offer_brandlist_of_category", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("offer_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("offer_list"));
                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            JSONObject offer_jsonArray = new JSONObject(jsonObject3.getString("offer"));
                            /*OffersModel offersModel = new OffersModel();
                            //offersModel.setId(jsonObject3.getString("id"));
                            offersModel.setId(jsonObject3.getString("brand_id"));
                            offersModel.setName(jsonObject3.getString("name"));
                            offersModel.setSlug(jsonObject3.getString("slug"));


                            offersModel.setBrand_image(API.ProductURL+offer_jsonArray.getString("image"));
                            if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                                offersModel.setOffer_subtitle(offer_jsonArray.getString("offer_subtitle"));
                            }else{
                                offersModel.setOffer_subtitle(offer_jsonArray.getString("offer_subtitle_ar"));
                            }
                            offersModel.setOffer_id(offer_jsonArray.getString("id"));
                            offersModel.setFamilyId(offer_jsonArray.getString("family_id"));*/
                            //offersModel.setChild_json_array(child_jsonArray);
                            if (offerId.equals(offer_jsonArray.getString("id"))) {
                                Bundle bundle = new Bundle();
                                bundle.putString("brand_id", jsonObject3.getString("brand_id"));
                                bundle.putString("brand_name", jsonObject3.getString("name"));
                                bundle.putString("family_id", offer_jsonArray.getString("family_id"));
                                bundle.putString("from", "");
                                ProductListFragment productListFragment = new ProductListFragment();
                                productListFragment.setArguments(bundle);
                                ((HomeActivity)context).replaceFragment(productListFragment);
                                break;
                            }
                        }

                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("offer_filter", "offer_filter");
                params.put("current_currency", sessionManager.getCurrencyCode());
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }
}
