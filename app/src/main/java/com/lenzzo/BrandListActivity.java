package com.lenzzo;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.adapter.BrandListOfCategoryAdapter;
import com.lenzzo.api.API;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.model.BrandListOfCategory;
import com.lenzzo.model.ProductList;
import com.lenzzo.model.ProductSearchModel;
import com.lenzzo.utility.CommanClass;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.OnSwipeTouchListener;
import com.lenzzo.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class BrandListActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView brandLists_recycler;
    //private GifImageView gifImageView;
    private List<BrandListOfCategory> brandListOfCategoryList;
    private BrandListOfCategoryAdapter brandListOfCategoryAdapter;
    private String category_id;
    private ImageView cart_image;
    private TextView number;
    private int total_value;
    private String total_count="";
    private SessionManager sessionManager;
    private RelativeLayout relativeLayout;
    private Dialog dialog;
    private List<ProductSearchModel> productLists;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private String search_text="";
    private ImageView search_image;
    private ImageView back_image;
    private LinearLayout liner;
    private String category_list;
    private AutoCompleteTextView searchView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_list);
        sessionManager = new SessionManager(this);

        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        findViewById(R.id.back_image).setOnClickListener(this);
        brandLists_recycler = (RecyclerView)findViewById(R.id.brandLists_recycler);
        //gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        cart_image = (ImageView)findViewById(R.id.cart_image);
        findViewById(R.id.cart_image).setOnClickListener(this);
        number = (TextView)findViewById(R.id.number);
        findViewById(R.id.filter_image).setOnClickListener(this);
        findViewById(R.id.search_image).setOnClickListener(this);

        back_image = (ImageView)findViewById(R.id.back_image);
        liner = (LinearLayout)findViewById(R.id.liner);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            category_id = bundle.getString("category_id");
            if(category_id!=null){
                if (CommanMethod.isInternetConnected(BrandListActivity.this)){
                    getBrandList();
                }
            }else{
                //gifImageView.setVisibility(View.GONE);
                 category_list = bundle.getString("category_list");
                try {
                    brandListOfCategoryList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(category_list);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        BrandListOfCategory brandListOfCategory = new BrandListOfCategory();
                        brandListOfCategory.setId(jsonObject3.getString("id"));
                        brandListOfCategory.setName(jsonObject3.getString("name"));
                        brandListOfCategory.setSlug(jsonObject3.getString("slug"));
                        brandListOfCategory.setIcon(jsonObject3.getString("icon"));
                        brandListOfCategory.setStatus(jsonObject3.getString("status"));
                        brandListOfCategory.setBrand_image(API.BrandURL+jsonObject3.getString("brand_image"));
                        brandListOfCategory.setCategory_id(jsonObject3.getString("category_id"));
                        brandListOfCategory.setUi_order(jsonObject3.getString("ui_order"));
                        brandListOfCategory.setStart_range(jsonObject3.getString("start_range"));
                        brandListOfCategory.setEnd_range(jsonObject3.getString("end_range"));
                        brandListOfCategory.setOffer_id(jsonObject3.getString("offer_id"));
                        brandListOfCategory.setOffer_name(jsonObject3.getString("offer_name"));
                        brandListOfCategory.setOffer_subtitle(jsonObject3.getString("offer_subtitle"));
                        JSONArray child_jsonArray = new JSONArray(jsonObject3.getString("child"));
                        //JSONArray offer_jsonArray = new JSONArray(jsonObject3.getString("offer"));

                        brandListOfCategory.setJsonArray(child_jsonArray);

                        brandListOfCategoryList.add(brandListOfCategory);


                    }

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(BrandListActivity.this,3);
                    brandLists_recycler.setLayoutManager(gridLayoutManager);
                    brandListOfCategoryAdapter = new BrandListOfCategoryAdapter(BrandListActivity.this,brandListOfCategoryList, "BrandList");
                    brandLists_recycler.setAdapter(brandListOfCategoryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        if (CommanMethod.isInternetConnected(BrandListActivity.this)){
            CommanClass.getCartValue(this, number);
            //searchProduct();
        }

        /*brandLists_recycler.setOnTouchListener(new OnSwipeTouchListener(BrandListActivity.this) {
            public void onSwipeTop() {
                //Toast.makeText(ProductDetailsActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(ProductDetailsActivity.this, "right", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            public void onSwipeLeft() {
                //Toast.makeText(ProductDetailsActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                //Toast.makeText(ProductDetailsActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(BrandListActivity.this) {
            public void onSwipeTop() {
                //Toast.makeText(ProductDetailsActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(ProductDetailsActivity.this, "right", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            public void onSwipeLeft() {
                //Toast.makeText(ProductDetailsActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                //Toast.makeText(ProductDetailsActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.cart_image:
                Intent intent = new Intent(BrandListActivity.this,UserCartActivity.class);
                startActivity(intent);
                break;
            case R.id.filter_image:
                Intent intent1 = new Intent(BrandListActivity.this,FilterActivity.class);
                startActivity(intent1);
                break;
            case R.id.search_image:
                sortDialog();
                break;
        }
    }

    private void getBrandList(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"brandlist_of_category", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    brandListOfCategoryList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("brand_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("brand_list"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            BrandListOfCategory brandListOfCategory = new BrandListOfCategory();
                            brandListOfCategory.setId(jsonObject3.getString("id"));
                            brandListOfCategory.setName(jsonObject3.getString("name"));
                            brandListOfCategory.setSlug(jsonObject3.getString("slug"));
                            brandListOfCategory.setIcon(jsonObject3.getString("icon"));
                            brandListOfCategory.setStatus(jsonObject3.getString("status"));
                            brandListOfCategory.setBrand_image(API.BrandURL+jsonObject3.getString("brand_image"));
                            brandListOfCategory.setCategory_id(jsonObject3.getString("category_id"));
                            brandListOfCategory.setUi_order(jsonObject3.getString("ui_order"));
                            brandListOfCategory.setStart_range(jsonObject3.getString("start_range"));
                            brandListOfCategory.setEnd_range(jsonObject3.getString("end_range"));
                            brandListOfCategory.setOffer_id(jsonObject3.getString("offer_id"));
                            brandListOfCategory.setOffer_name(jsonObject3.getString("offer_name"));
                            brandListOfCategory.setOffer_subtitle(jsonObject3.getString("offer_subtitle"));
                            JSONArray child_jsonArray = new JSONArray(jsonObject3.getString("child"));
                            //JSONArray offer_jsonArray = new JSONArray(jsonObject3.getString("offer"));

                            brandListOfCategory.setJsonArray(child_jsonArray);

                            brandListOfCategoryList.add(brandListOfCategory);

                        }

                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

                GridLayoutManager gridLayoutManager = new GridLayoutManager(BrandListActivity.this,3);
                brandLists_recycler.setLayoutManager(gridLayoutManager);
                brandListOfCategoryAdapter = new BrandListOfCategoryAdapter(BrandListActivity.this,brandListOfCategoryList,"BrandList");
                brandLists_recycler.setAdapter(brandListOfCategoryAdapter);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                //gifImageView.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("category_id", category_id);
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

    public void getCartValue(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"usercart", new com.android.volley.Response.Listener<String>() {
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
                if (CommanMethod.isInternetConnected(BrandListActivity.this)){
                    searchProduct(search_text);
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

               /* Intent intent = new Intent(BrandListActivity.this,SearchResultsActivity.class);
                intent.putExtra("array_of_product_lists",array_of_product_lists);
                startActivity(intent);*/
                Intent intent = new Intent(BrandListActivity.this,ProductDetailsActivity.class);
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
                    for(int i=0;i<productLists.size();i++){
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
                    Intent intent = new Intent(BrandListActivity.this,SearchResultsActivity.class);
                    intent.putExtra("array_of_product_lists",array_of_product_lists);
                    intent.putExtra("search_string", search_text);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    }

    private void searchProduct(String search_text){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"productlist_of_brand", new com.android.volley.Response.Listener<String>() {
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
                            ProductSearchModel productList = new ProductSearchModel();
                            productList.setId(jsonObject2.getString("id"));
                            productList.setQuantity(jsonObject2.getString("quantity"));
                            productList.setUser_id(jsonObject2.getString("user_id"));
                            productList.setCate_id(jsonObject2.getString("cate_id"));
                            productList.setCate_name(jsonObject2.getString("cate_name"));
                            productList.setTitle(CommanMethod.getTitle(BrandListActivity.this, jsonObject2));
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

                            productLists.add(productList);

                        }
                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }
                searchadapter= new ArrayAdapter<ProductSearchModel>(BrandListActivity.this,android.R.layout.simple_list_item_1, productLists);
                searchView.setAdapter(searchadapter);
                searchView.showDropDown();

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

    @Override
    public void onResume(){
        super.onResume();
        if (CommanMethod.isInternetConnected(BrandListActivity.this)){
            CommanClass.getCartValue(this, number);
        }
    }
}
