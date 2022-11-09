package com.lenzzo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.PaymentActivity;
import com.lenzzo.ProductDetailsActivity;
import com.lenzzo.ProductListActivity;
import com.lenzzo.R;
import com.lenzzo.UserCartActivity;
import com.lenzzo.api.API;
import com.lenzzo.fragment.ProductListFragment;
import com.lenzzo.interfacelenzzo.RefreshProductList;
import com.lenzzo.model.ProductList;
import com.lenzzo.model.UserWishlist;
import com.lenzzo.utility.CommanClass;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.CustomVolleyRequest;
import com.lenzzo.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<ProductList> productLists;
    private ImageLoader imageLoader;
    private Context context;
    int button = 0;
    private List<UserWishlist> userWishlists;
    private TextView textView;
    private RefreshProductList refreshProductList;

    public ProductListAdapter(Context ctx, List<ProductList> productLists, List<UserWishlist> userWishlists, TextView textView, RefreshProductList refreshProductList){
        inflater = LayoutInflater.from(ctx);
        this.productLists = productLists;
        this.userWishlists = userWishlists;
        this.context=ctx;
        this.textView = textView;
        this.refreshProductList = refreshProductList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.product_list_recycler_view_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductListAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(productLists.get(position).getProduct_image(), ImageLoader.getImageListener(holder.product_image, R.drawable.no_img, R.drawable.no_img));

        Picasso
                .get()
                .load(productLists.get(position).getProduct_image())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                ///.fitCenter()
                .into(holder.product_image);

        holder.name_text_view.setText(productLists.get(position).getTitle());
        holder.material_name_text_view.setText(Html.fromHtml(productLists.get(position).getDescription()));

        if(Locale.getDefault().getLanguage().equals("en")){
            holder.buy_button.setBackgroundResource(R.drawable.button_corner_radius);
            holder.relative_wishlist.setBackgroundResource(R.drawable.wishlist_button_cornar_radius);
        }else{
            holder.buy_button.setBackgroundResource(R.drawable.button_corner_radius_ar);
            holder.relative_wishlist.setBackgroundResource(R.drawable.wishlist_button_cornar_radius_ar);
        }
        holder.relative_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("wishlist button");
            }
        });

        if((productLists.get(position).getSale_price()!=null
                && !productLists.get(position).getSale_price().equals("0.00")
                && !productLists.get(position).getSale_price().equals("0.000")
                && !productLists.get(position).getSale_price().equals("0")) || productLists.get(position).getIsSpecialOffer().equals("1")){
            //holder.price_text_view.setText(productLists.get(position).getSale_price()+" "+productLists.get(position).getCurrent_currency());
            //holder.sale_price_text_view.setText(productLists.get(position).getPrice()+" "+productLists.get(position).getCurrent_currency());
            if(productLists.get(position).getIsSpecialOffer().equals("1")){
                holder.price_text_view.setText(productLists.get(position).getSpecialPrice()+" "+productLists.get(position).getCurrent_currency());
                holder.sale_price_text_view.setText(productLists.get(position).getPrice()+" "+productLists.get(position).getCurrent_currency());
            }else {
                holder.price_text_view.setText(productLists.get(position).getSale_price()+" "+productLists.get(position).getCurrent_currency());
                holder.sale_price_text_view.setText(productLists.get(position).getPrice()+" "+productLists.get(position).getCurrent_currency());
            }

            holder.sale_price_text_view.setVisibility(View.VISIBLE);
        }else{
            holder.price_text_view.setText(productLists.get(position).getPrice()+" "+productLists.get(position).getCurrent_currency());
            holder.sale_price_text_view.setVisibility(View.GONE);
        }

        if(productLists.get(position).getTags().equals("new")
                || productLists.get(position).getTags().equals("latest")
                || productLists.get(position).getTags().equals("exclusive")){
            if (productLists.get(position).getTags().equals("exclusive")){
                holder.tag_text.setText(context.getResources().getString(R.string.exclusive));
            }else {
                holder.tag_text.setText(context.getResources().getString(R.string.latest));
            }
            holder.latest_text.setVisibility(View.VISIBLE);

        }else {
            holder.latest_text.setVisibility(View.GONE);
        }

        if(CommanMethod.isOutOfStock(productLists.get(position).getStock_flag(), productLists.get(position).getQuantity())){
            holder.buy_button.setText(context.getResources().getString(R.string.out_of_stock_small));
            holder.buy_button.setEnabled(false);
            holder.buy_button.setTextColor(Color.RED);
        }else{
            if (CommanMethod.isOutOfStock(productLists.get(position).getStock_flag(), productLists.get(position).getQuantity())){
                holder.buy_button.setText(context.getResources().getString(R.string.out_of_stock_small));
                holder.buy_button.setEnabled(false);
                holder.buy_button.setTextColor(Color.RED);
            }else {
                holder.buy_button.setEnabled(true);
                holder.buy_button.setText(context.getResources().getString(R.string.buy_now));
                holder.buy_button.setTextColor(Color.WHITE);
            }
        }

        if (productLists.get(position).isSelected()){
            holder.wish_list_image1.setVisibility(View.VISIBLE);
            holder.wish_list_image.setVisibility(View.GONE);
        }else {
            holder.wish_list_image1.setVisibility(View.GONE);
            holder.wish_list_image.setVisibility(View.VISIBLE);
        }

        //|| !productLists.get(position).isShouldBuyNow()
        if (productLists.get(position).getCate_id().equals("2") ){
            holder.buy_button.setVisibility(View.GONE);
            holder.view_detail_button.setVisibility(View.VISIBLE);
        }else {
            holder.buy_button.setVisibility(View.VISIBLE);
            holder.view_detail_button.setVisibility(View.GONE);
        }

       /* if(userWishlists.size()>0) {
            for (int i = 0; i < userWishlists.size(); i++) {
                if (userWishlists.get(i).getProduct_id().equals(productLists.get(position).getId())) {
                    holder.wish_list_image1.setVisibility(View.VISIBLE);
                }
            }
        }*/
        holder.wish_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    wishListAdd(productLists.get(position).getId());
                    holder.wish_list_image.setVisibility(View.GONE);
                    holder.wish_list_image1.setVisibility(View.VISIBLE);
            }
        });
        holder.wish_list_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishListAdd(productLists.get(position).getId());
                holder.wish_list_image1.setVisibility(View.GONE);
                holder.wish_list_image.setVisibility(View.VISIBLE);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id",productLists.get(position).getId());
                intent.putExtra("current_currency",productLists.get(position).getCurrent_currency());
                intent.putExtra("title_name",productLists.get(position).getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        holder.view_detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id",productLists.get(position).getId());
                intent.putExtra("current_currency",productLists.get(position).getCurrent_currency());
                intent.putExtra("title_name",productLists.get(position).getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        holder.buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommanMethod.isInternetConnected(context)) {
                    addToCart(productLists.get(position).getId());
                }
                //dialogBox(productLists.get(position).getId(),productLists.get(position).getBrand_id(),productLists.get(position).getBrand_name());
            }
        });

        holder.free_text_view.setText(productLists.get(position).getOffer_name());

        /*if(productLists.get(position).getOffer_name().isEmpty()){
            holder.free_text_view.setVisibility(View.GONE);
        }else{
            holder.free_text_view.setText(productLists.get(position).getOffer_name());
            holder.free_text_view.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView product_image;
        TextView name_text_view;
        TextView material_name_text_view;
        TextView price_text_view;
        TextView free_text_view;
        Button buy_button, view_detail_button;
        ImageView wish_list_image;
        ImageView wish_list_image1;
        RelativeLayout latest_text;
        TextView sale_price_text_view;
        RelativeLayout relative_wishlist;
        TextView tag_text;

        public MyViewHolder(View itemView) {
            super(itemView);
            product_image = (ImageView)itemView.findViewById(R.id.product_image);
            name_text_view = (TextView)itemView.findViewById(R.id.name_text_view);
            material_name_text_view = (TextView)itemView.findViewById(R.id.material_name_text_view);
            price_text_view = (TextView)itemView.findViewById(R.id.price_text_view);
            free_text_view = (TextView)itemView.findViewById(R.id.free_text_view);
            buy_button = (Button)itemView.findViewById(R.id.buy_button);
            view_detail_button = (Button)itemView.findViewById(R.id.view_detail_button);
            wish_list_image = (ImageView)itemView.findViewById(R.id.wish_list_image);
            wish_list_image1 = (ImageView)itemView.findViewById(R.id.wish_list_image1);
            latest_text = (RelativeLayout)itemView.findViewById(R.id.latest_text);
            sale_price_text_view = (TextView)itemView.findViewById(R.id.sale_price_text_view);
            sale_price_text_view.setPaintFlags(sale_price_text_view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            relative_wishlist = (RelativeLayout)itemView.findViewById(R.id.relative_wishlist);
            tag_text = (TextView)itemView.findViewById(R.id.tag_text);
        }
    }

    private void wishListAdd(final String product_id){
        final SessionManager sessionManager = new SessionManager(context);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"wishlist_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        refreshProductList.refreshList();
                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                    params.put("user_id", sessionManager.getUserId());
                }else{
                    params.put("user_id", sessionManager.getRandomValue());
                }
                params.put("product_id",product_id);
                params.put("shade", "colorDefault");
                params.put("left_eye_power", "0.0");
                params.put("right_eye_power", "0.0");
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

    private void dialogBox(){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.buy_now_dialog);
        TextView continue_text_view = (TextView)dialog.findViewById(R.id.continue_text_view);
        TextView go_to_text_view = (TextView)dialog.findViewById(R.id.go_to_text_view);
        continue_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //addToCart(product_id,getBrand_id,getBrand_Name,"continue");
            }
        });
        go_to_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(context, UserCartActivity.class);
                context.startActivity(intent);
                //addToCart(product_id,getBrand_id,getBrand_Name,"goToCart");
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

   private void addToCart(final String product_id){
       final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
       dialog.show();
       final SessionManager sessionManager = new SessionManager(context);
       RequestQueue mRequestQueue = Volley.newRequestQueue(context);
       //https://lenzzo.com/mobile/api_live_v_2/usercart_add
       StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"usercart_add", new com.android.volley.Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               Log.d("addcartURL",response);
               try {
                   dialog.dismiss();
                   Log.d("usercartresponse",response);
                   JSONObject object = new JSONObject(response);
                   String status = object.getString("status");
                   String message = object.getString("message");

                   if(status.equals("success")){
                       CommanClass.getCartValue(context, textView);
                       dialogBox();
                       /*if(keyValue.equals("continue")){
                           *//*Intent intent = new Intent(context,ProductListActivity.class);
                           intent.putExtra("brand_id",getBrand_id);
                           intent.putExtra("brand_name",getBrand_Name);
                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           context.startActivity(intent);*//*
                           CommanClass.getCartValue(context, textView);
                       }else if(keyValue.equals("goToCart")){
                           Intent intent=new Intent(context, UserCartActivity.class);
                           context.startActivity(intent);
                       }*/

                   }else{
                       openalartdialog(message);
                       //Toast.makeText(context, "hgfthtjhftjh", Toast.LENGTH_SHORT).show();
                   }
               }catch (JSONException e){
                   e.printStackTrace();
               }
           }
       }, new com.android.volley.Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               dialog.dismiss();
           }
       }){
           @Override
           protected Map<String, String> getParams()
           {
               Map<String, String>  params = new HashMap<String, String>();
               if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                   params.put("user_id", sessionManager.getUserId());
               }else{
                   params.put("user_id", sessionManager.getRandomValue());
               }
               params.put("product_id",product_id);
               params.put("quantity","1");
               params.put("shade", "");
               params.put("left_eye_power", "0.0");
               params.put("right_eye_power", "0.0");
               params.put("current_currency", sessionManager.getCurrencyCode());
               Log.d("addcartURL",params.toString());

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

    private void openalartdialog(String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        //ok_tv.setText(getString(R.string.next));
        //info_tv.setText(getString(R.string.only_cod_success));
        info_tv.setText(message);
        ok_tv.setOnClickListener(view ->  {
            /*Intent intent = new Intent(PaymentActivity.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/

            dialog.dismiss();
            //CommanMethod.rateAlert(context, getString(R.string.rate_app_info));
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void getCartValue(){
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"usercart", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));

                        if(jsonArray.length()> 0) {
                            textView.setText(String.valueOf(jsonArray.length()));
                        }else {
                            textView.setText("");
                        }
                    }else{
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    dialog.dismiss();
                    e.printStackTrace();
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
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String>  params = new HashMap<String, String>();
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                    params.put("user_id", sessionManager.getUserId());
                    params.put("current_currency", sessionManager.getCurrencyCode());
                }else{
                    params.put("user_id", sessionManager.getRandomValue());
                    params.put("current_currency", sessionManager.getCurrencyCode());
                }
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
