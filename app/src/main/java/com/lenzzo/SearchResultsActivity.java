package com.lenzzo;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.adapter.ProductListAdapter;
import com.lenzzo.adapter.SortByAdapter;
import com.lenzzo.api.API;
import com.lenzzo.interfacelenzzo.RefreshProductList;
import com.lenzzo.interfacelenzzo.SortByInterface;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.model.ProductList;
import com.lenzzo.model.ProductSearchModel;
import com.lenzzo.model.SortByModel;
import com.lenzzo.model.UserWishlist;
import com.lenzzo.utility.CommanClass;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class SearchResultsActivity extends BaseActivity implements View.OnClickListener, SortByInterface, RefreshProductList {

    //private GifImageView gifImageView;
    private ProductListAdapter productListAdapter;
    private List<UserWishlist> userWishlists =new ArrayList<>();
    private RecyclerView productLists_recycler;
    private SessionManager sessionManager;
    private TextView number;
    private int total_value;
    private String total_count="";
    private Dialog dialog1;
    private List<SortByModel> sortByModelList;
    private String key="";
    private String value="";
    private Dialog dialog;
    private List<ProductSearchModel> searchProductLists;
    private List<ProductList> productLists;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private String search_text="";
    private ImageView search_image;
    private ImageView back_image;
    private LinearLayout liner;
    private AutoCompleteTextView searchView;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        sessionManager = new SessionManager(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.cart_image).setOnClickListener(this);
        findViewById(R.id.layout1).setOnClickListener(this);
        findViewById(R.id.layout).setOnClickListener(this);
        number = (TextView)findViewById(R.id.number);
        findViewById(R.id.search_image).setOnClickListener(this);
        //gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        productLists_recycler = (RecyclerView)findViewById(R.id.productLists_recycler);
        back_image = (ImageView)findViewById(R.id.back_image);
        liner = (LinearLayout)findViewById(R.id.liner);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }

        searchProductLists = new ArrayList<>();
        productLists = new ArrayList<>();

        List<ProductList> myList = (ArrayList<ProductList>) getIntent().getSerializableExtra("array_of_product_lists");

        /*GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchResultsActivity.this,2);
        productLists_recycler.setLayoutManager(gridLayoutManager);
        ProductListAdapter productListAdapter = new ProductListAdapter(SearchResultsActivity.this,myList,userWishlists, number, SearchResultsActivity.this);
        productLists_recycler.setAdapter(productListAdapter);*/
        if (CommanMethod.isInternetConnected(SearchResultsActivity.this)){
            getWishList();
            CommanClass.getCartValue(this, number);
            getSortBy();

            //searchProduct();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.cart_image:
                Intent intent = new Intent(SearchResultsActivity.this,UserCartActivity.class);
                startActivity(intent);
                break;
            case R.id.layout1:
                //System.out.println("sort");
                sortDialog();
                break;
            case R.id.layout:
                //System.out.println("filter");
                Intent intent1 = new Intent(SearchResultsActivity.this,FilterActivity.class);
                startActivity(intent1);
                break;
            case R.id.search_image:
                sortDialog1();
                break;
        }
    }


    public void getCartValue(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"usercart", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));
                        if(jsonArray!=null && jsonArray.length()> 0) {
                            total_value = jsonArray.length();
                            total_count = String.valueOf(total_value);
                            number.setText(total_count);
                        }else {
                            number.setText("");
                        }
                    }else{
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                    params.put("user_id", sessionManager.getUserId());
                }else{
                    params.put("user_id", sessionManager.getRandomValue());
                }
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

    private void sortDialog(){
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.custom_dialog);
        ImageView dialog_back_image = (ImageView)dialog1.findViewById(R.id.dialog_back_image);
        RecyclerView sort_recycler_view = (RecyclerView)dialog1.findViewById(R.id.sort_recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchResultsActivity.this,1);
        sort_recycler_view.setLayoutManager(gridLayoutManager);
        SortByAdapter sortByAdapter = new SortByAdapter(SearchResultsActivity.this,sortByModelList);
        sort_recycler_view.setAdapter(sortByAdapter);

        dialog_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    public void getSortBy(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"sort_by", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    sortByModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("sortlist_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("sortlist"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            SortByModel sortByModel = new SortByModel();
                            sortByModel.setKey(jsonObject2.getString("key"));
                            sortByModel.setValue(jsonObject2.getString("value"));
                            sortByModel.setTitle(jsonObject2.getString("title"));
                            sortByModelList.add(sortByModel);
                        }
                    }else{
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
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

    private void sortDialog1(){
        dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.search_dialog_box);
        ImageView dialog_back_image = (ImageView)dialog.findViewById(R.id.dialog_back_image);
        searchView = (AutoCompleteTextView)dialog.findViewById(R.id.searchView);
        Button search_button = (Button)dialog.findViewById(R.id.search_button);

        productLists = new ArrayList<>();
        searchView.setDropDownBackgroundResource(R.color.white);
        searchView.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);

        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            dialog_back_image.setImageResource(R.drawable.arrow_30);
        }else{
            dialog_back_image.setImageResource(R.drawable.arrow_right_30);
        }
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("before");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("start");

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("change"+s);
                search_text = s.toString();
                if (CommanMethod.isInternetConnected(SearchResultsActivity.this)){
                    searchProductInSame(search_text);
                }
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                array_of_product_lists = new ArrayList<>();
                ProductList productList = new ProductList();
                productList.setId(searchadapter.getItem(position).getId());
                productList.setQuantity(searchadapter.getItem(position).getQuantity());
                productList.setUser_id(searchadapter.getItem(position).getUser_id());
                productList.setCate_id(searchadapter.getItem(position).getCate_id());
                productList.setCate_name(searchadapter.getItem(position).getCate_name());
                productList.setTitle(searchadapter.getItem(position).getTitle());
                productList.setDescription(searchadapter.getItem(position).getDescription());
                productList.setProduct_image(searchadapter.getItem(position).getProduct_image());
                productList.setProduct_images(searchadapter.getItem(position).getProduct_images());
                productList.setModel_no(searchadapter.getItem(position).getModel_no());
                productList.setSku_code(searchadapter.getItem(position).getSku_code());
                productList.setPrice(searchadapter.getItem(position).getPrice());
                productList.setCurrent_currency(searchadapter.getItem(position).getCurrent_currency());
                productList.setSale_price(searchadapter.getItem(position).getSale_price());
                productList.setNegotiable(searchadapter.getItem(position).getNegotiable());
                productList.setBrand_name(searchadapter.getItem(position).getBrand_name());
                productList.setBrand_id(searchadapter.getItem(position).getBrand_id());
                productList.setVariation_color(searchadapter.getItem(position).getVariation_color());
                productList.setTags(searchadapter.getItem(position).getTags());
                productList.setIs_hide(searchadapter.getItem(position).getIs_hide());
                productList.setReviewed(searchadapter.getItem(position).getReviewed());
                productList.setFeatured(searchadapter.getItem(position).getFeatured());
                productList.setArchived(searchadapter.getItem(position).getArchived());
                productList.setStatus(searchadapter.getItem(position).getStatus());
                productList.setStock_flag(searchadapter.getItem(position).getStock_flag());
                productList.setRating(searchadapter.getItem(position).getRating());
                productList.setReplacement(searchadapter.getItem(position).getReplacement());
                productList.setReleted_product(searchadapter.getItem(position).getReleted_product());
                productList.setOffer_id(searchadapter.getItem(position).getOffer_id());
                productList.setOffer_name(searchadapter.getItem(position).getOffer_name());
                array_of_product_lists.add(productList);

                /*Intent intent = new Intent(SearchResultsActivity.this,SearchResultsActivity.class);
                intent.putExtra("array_of_product_lists",array_of_product_lists);
                startActivity(intent);*/
                Intent intent = new Intent(SearchResultsActivity.this,ProductDetailsActivity.class);
                intent.putExtra("product_id",searchadapter.getItem(position).getId());
                intent.putExtra("current_currency",searchadapter.getItem(position).getCurrent_currency());
                intent.putExtra("title_name",searchadapter.getItem(position).getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });
        dialog_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array_of_product_lists = new ArrayList<>();
                String getSearchText = searchView.getText().toString();
                if(getSearchText.equals("") || getSearchText.length()==0){
                }else {
                    search_text=getSearchText;
                    searchProduct(search_text);
                    /*for(int i=0;i<productLists.size();i++){
                        ProductList productList = new ProductList();
                        productList.setId(productLists.get(i).getId());
                        productList.setQuantity(productLists.get(i).getQuantity());
                        productList.setUser_id(productLists.get(i).getUser_id());
                        productList.setCate_id(productLists.get(i).getCate_id());
                        productList.setCate_name(productLists.get(i).getCate_name());
                        productList.setTitle(productLists.get(i).getTitle());
                        productList.setDescription(productLists.get(i).getDescription());
                        productList.setProduct_image(productLists.get(i).getProduct_image());
                        productList.setProduct_images(productLists.get(i).getProduct_images());
                        productList.setModel_no(productLists.get(i).getModel_no());
                        productList.setSku_code(productLists.get(i).getSku_code());
                        productList.setPrice(productLists.get(i).getPrice());
                        productList.setCurrent_currency(productLists.get(i).getCurrent_currency());
                        productList.setSale_price(productLists.get(i).getSale_price());
                        productList.setNegotiable(productLists.get(i).getNegotiable());
                        productList.setBrand_name(productLists.get(i).getBrand_name());
                        productList.setBrand_id(productLists.get(i).getBrand_id());
                        productList.setVariation_color(productLists.get(i).getVariation_color());
                        productList.setTags(productLists.get(i).getTags());
                        productList.setIs_hide(productLists.get(i).getIs_hide());
                        productList.setReviewed(productLists.get(i).getReviewed());
                        productList.setFeatured(productLists.get(i).getFeatured());
                        productList.setArchived(productLists.get(i).getArchived());
                        productList.setStatus(productLists.get(i).getStatus());
                        productList.setStock_flag(productLists.get(i).getStock_flag());
                        productList.setRating(productLists.get(i).getRating());
                        productList.setReplacement(productLists.get(i).getReplacement());
                        productList.setReleted_product(productLists.get(i).getReleted_product());
                        productList.setOffer_id(productLists.get(i).getOffer_id());
                        productList.setOffer_name(productLists.get(i).getOffer_name());
                        array_of_product_lists.add(productList);
                    }
                    Intent intent = new Intent(SearchResultsActivity.this,SearchResultsActivity.class);
                    intent.putExtra("array_of_product_lists",array_of_product_lists);
                    intent.putExtra("search_string", search_text);
                    startActivity(intent);*/
                }
            }
        });
        dialog.show();
    }

    private void searchProduct(String search_text){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"productlist_of_brand", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        productLists.clear();
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("product_list_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("product_list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String startRange = "", endRange = "";
                            boolean shouldBuyNow = false;
                            JSONObject powerObject = jsonObject2.optJSONObject("power");
                            if (powerObject != null && powerObject.length()>0){
                                if (powerObject.has("start_range")){
                                    startRange = powerObject.getString("start_range");
                                }

                                if (powerObject.has("end_range")){
                                    endRange = powerObject.getString("end_range");
                                }
                            }

                            if ((startRange.equals("0") && endRange.equals("0"))
                                    || (TextUtils.isEmpty(startRange) && TextUtils.isEmpty(endRange))){
                                shouldBuyNow = true;
                            }

                            ProductList productList = new ProductList();
                            productList.setId(jsonObject2.getString("id"));
                            productList.setQuantity(jsonObject2.getString("quantity"));
                            productList.setUser_id(jsonObject2.getString("user_id"));
                            productList.setCate_id(jsonObject2.getString("cate_id"));
                            productList.setCate_name(jsonObject2.getString("cate_name"));
                            productList.setStartRange(startRange);
                            productList.setEndRange(endRange);
                            productList.setShouldBuyNow(shouldBuyNow);
                            productList.setTitle(CommanMethod.getTitle(SearchResultsActivity.this, jsonObject2));
                            productList.setDescription(jsonObject2.getString("description"));
                            productList.setProduct_image(API.ProductURL+jsonObject2.getString("product_image"));
                            productList.setProduct_images(jsonObject2.getString("product_images"));
                            productList.setModel_no(jsonObject2.getString("model_no"));
                            productList.setSku_code(jsonObject2.getString("sku_code"));
                            productList.setPrice(jsonObject2.getString("price"));
                            productList.setCurrent_currency(jsonObject2.getString("current_currency"));
                            if(jsonObject2.has("sale_price")){
                                productList.setSale_price(jsonObject2.getString("sale_price"));
                            }
                            productList.setNegotiable(jsonObject2.getString("negotiable"));
                            productList.setBrand_name(jsonObject2.getString("brand_name"));
                            productList.setBrand_id(jsonObject2.getString("brand_id"));
                            productList.setVariation_color(jsonObject2.getString("variation_color"));
                            productList.setTags(jsonObject2.getString("tags"));
                            productList.setIs_hide(jsonObject2.getString("is_hide"));
                            productList.setReviewed(jsonObject2.getString("reviewed"));
                            productList.setFeatured(jsonObject2.getString("featured"));
                            productList.setArchived(jsonObject2.getString("archived"));
                            productList.setDeleted_at(jsonObject2.getString("deleted_at"));
                            productList.setStatus(jsonObject2.getString("status"));
                            productList.setStock_flag(jsonObject2.getString("stock_flag"));
                            productList.setRating(jsonObject2.getString("rating"));
                            productList.setReplacement(jsonObject2.getString("replacement"));
                            productList.setReleted_product(jsonObject2.getString("releted_product"));
                            productList.setOffer_id(jsonObject2.getString("offer_id"));
                            //productList.setOffer_name(jsonObject2.optString("offer_name",""));
                            String offerData = String.valueOf(jsonObject2.get("offer"));
                            productList.setOffer_name(CommanMethod.getOfferName(SearchResultsActivity.this, offerData));
                            if(userWishlists.size()>0) {
                                for (int j = 0; j < userWishlists.size(); j++) {
                                    if (userWishlists.get(j).getProduct_id().equals(jsonObject2.getString("id"))) {
                                        productList.setSelected(true);
                                        break;
                                    }
                                }
                            }

                            productLists.add(productList);

                        }
                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchResultsActivity.this,2);
                    productLists_recycler.setLayoutManager(gridLayoutManager);
                    ProductListAdapter productListAdapter = new ProductListAdapter(SearchResultsActivity.this,productLists,userWishlists, number, SearchResultsActivity.this);
                    productLists_recycler.setAdapter(productListAdapter);
                }catch (JSONException e){
                    dialog.dismiss();
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                //gifImageView.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("search_text", search_text);
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

    private void searchProductInSame(String search_text){

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"productlist_of_brand", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        //productLists
                        searchProductLists.clear();
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("product_list_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("product_list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            ProductSearchModel productList = new ProductSearchModel();
                            productList.setId(jsonObject2.getString("id"));
                            productList.setQuantity(jsonObject2.getString("quantity"));
                            productList.setUser_id(jsonObject2.getString("user_id"));
                            productList.setCate_id(jsonObject2.getString("cate_id"));
                            productList.setCate_name(jsonObject2.getString("cate_name"));
                            productList.setTitle(CommanMethod.getTitle(SearchResultsActivity.this, jsonObject2));
                            productList.setDescription(jsonObject2.getString("description"));
                            productList.setProduct_image(API.ProductURL+jsonObject2.getString("product_image"));
                            productList.setProduct_images(jsonObject2.getString("product_images"));
                            productList.setModel_no(jsonObject2.getString("model_no"));
                            productList.setSku_code(jsonObject2.getString("sku_code"));
                            productList.setPrice(jsonObject2.getString("price"));
                            productList.setCurrent_currency(jsonObject2.getString("current_currency"));
                            //productList.setSale_price(jsonObject2.getString("sale_price"));
                            productList.setNegotiable(jsonObject2.getString("negotiable"));
                            productList.setBrand_name(jsonObject2.getString("brand_name"));
                            productList.setBrand_id(jsonObject2.getString("brand_id"));
                            productList.setVariation_color(jsonObject2.getString("variation_color"));
                            productList.setTags(jsonObject2.getString("tags"));
                            productList.setIs_hide(jsonObject2.getString("is_hide"));
                            productList.setReviewed(jsonObject2.getString("reviewed"));
                            productList.setFeatured(jsonObject2.getString("featured"));
                            productList.setArchived(jsonObject2.getString("archived"));
                            productList.setDeleted_at(jsonObject2.getString("deleted_at"));
                            productList.setStatus(jsonObject2.getString("status"));
                            productList.setStock_flag(jsonObject2.getString("stock_flag"));
                            productList.setRating(jsonObject2.getString("rating"));
                            productList.setReplacement(jsonObject2.getString("replacement"));
                            productList.setReleted_product(jsonObject2.getString("releted_product"));
                            productList.setOffer_id(jsonObject2.getString("offer_id"));
                            productList.setOffer_name(jsonObject2.getString("offer_name"));

                            searchProductLists.add(productList);

                        }
                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
                }
                searchadapter= new ArrayAdapter<ProductSearchModel>(SearchResultsActivity.this,android.R.layout.simple_list_item_1, searchProductLists);
                searchView.setAdapter(searchadapter);
                searchView.showDropDown();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("search_text", search_text);
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

    @Override
    public void sortByPrice(String key, String value) {
        this.key=key;
        this.value=value;
        this.dialog1.dismiss();
    }

    public void getWishList(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"wishlist", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    userWishlists = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("wishlist_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("wishlist"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            UserWishlist userWishlist = new UserWishlist();
                            userWishlist.setId(jsonObject2.getString("id"));
                            userWishlist.setUser_id(jsonObject2.getString("user_id"));
                            userWishlist.setProduct_id(jsonObject2.getString("product_id"));
                            userWishlist.setProduct_name(jsonObject2.getString("product_name"));
                            userWishlists.add(userWishlist);
                        }

                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    searchProduct(getIntent().getStringExtra("search_string"));

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

    @Override
    public void refreshList() {
        getWishList();
    }
}
