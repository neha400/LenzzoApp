package com.lenzzo;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.lenzzo.poweradapter.LeftPowerAdditionAdapter;
import com.lenzzo.poweradapter.LeftPowerOneAdapter;
import com.lenzzo.poweradapter.RightPowerAdditionAdapter;
import com.lenzzo.poweradapter.RightPowerOneAdapter;
import com.lenzzo.adapter.ProductDetailSliderAdapter;
import com.lenzzo.adapter.ProductListAdapter;
import com.lenzzo.api.API;
import com.lenzzo.interfacelenzzo.RefreshProductList;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.model.PowerModel;
import com.lenzzo.model.ProductList;
import com.lenzzo.model.ProductSearchModel;
import com.lenzzo.model.RightPowerModel;
import com.lenzzo.model.UserWishlist;
import com.lenzzo.utility.CommanClass;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.SessionManager;
import com.nineoldandroids.view.ViewHelper;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener, RefreshProductList, ObservableScrollViewCallbacks {

    private String product_id;
    private String current_currency;
    private String title_name;
    private TextView title_text_view;
    private ViewPager slide_viewPager;
    private CirclePageIndicator circlePageIndicator;
    private List<String> aList;
    private ProductDetailSliderAdapter productDetailSliderAdapter;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private TextView product_name;
    private TextView product_code;
    private RatingBar ratingBar;
    //private TextView price_text_view;
    private TextView discription_text_view;
    //private AutoCompleteTextView qty_dropdown_text_view;
    private Button select_power_button;
    //private List<String> qtyList;
    //private  ArrayAdapter<String> adapter;
    private TextView related_pro_text;
    private RecyclerView related_product_recycler_view;
    private List<ProductList> productListList;
    private ProductListAdapter productListAdapter;
    private List<UserWishlist> userWishlists;
    //private GifImageView gifImageView;
    private ImageView wish_list_image;
    //private ImageView wish_list_image1;
    //private Button wishlist_button;
    //private Button remove_wishlist_button;
    private Button add_to_bag;
    private ImageView cart_image;
    private TextView number;
    private int total_value;
    private String total_count="";
    private SessionManager sessionManager;
    private String getProduct_id="";
    private String getBrand_id="";
    private String getBrand_name="", cate_id="";
    private String select_qty="";
    private TextView offer_text_view1;
    private TextView offer_text_view;
    //private NestedScrollView scrollView;
    private Dialog dialog;
    private List<ProductSearchModel> productLists;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private String search_text="";
    private ImageView search_image;
    private LinearLayout liner_layout2;
    private LinearLayout power_layout;
    private TextView left_power_text;
    private TextView right_power_text;
    private String start_range="", end_range ="", diff_range="", range_above = "", diff_range_above = "", power_selection_type = "", low_range="", mid_range ="",high_range="";


    private LeftPowerOneAdapter leftPowerOneAdapter;
    private RightPowerOneAdapter rightPowerOneAdapter;
    private List<RightPowerModel> rightPowerModelList;
    private List<PowerModel> leftPowerModelList;

    private LeftPowerAdditionAdapter leftAdditionAdapter;
    private RightPowerAdditionAdapter rightAdditionAdapter;
    private List<PowerModel> rightAdditionList;
    private List<PowerModel> leftAdditionList;
    private String leftAdditionPower="", rightAdditionPower="";

    private LeftPowerAdditionAdapter leftCylAdapter;
    private RightPowerAdditionAdapter rightCylAdapter;
    private List<PowerModel> rightCylList;
    private List<PowerModel> leftCylList;
    private String leftCylPower="", rightCylPower="", leftCylPowerKey="", rightCylPowerKey="";

    private LeftPowerAdditionAdapter leftAxisAdapter;
    private RightPowerAdditionAdapter rightAxisAdapter;
    private List<PowerModel> rightAxisList;
    private List<PowerModel> leftAxisList;
    private String leftAxisPower="", rightAxisPower="";

    private String leftPower="0.00", rightPower="0.00", price, sale_price="0", currency, singleLeftLensPrice, singleRightLensPrice;
    private String with_power_price;
    private String l_p_n_available="";
    private String r_p_n_available="";
    private List<String> leftPowerList;
    private List<String> rightPowerlist;
    private TextView left_price_tv, left_sale_price_tv, right_price_tv, right_sale_price_tv, left_eye_text, right_eye_text;
    //private String sale_price1;
    private ImageView back_image;
    private LinearLayout liner, share_layout;
    private int rating_count;
    private String avg_rating;
    private TextView review_text_view;
    private ImageView share_image_view;
    private String product_images, quantity = "10";
    private AutoCompleteTextView searchView;
    private String[] powerRangeArray;
    private int powerFlag = 0;

    private String is_special_offer = "", special_price = "", special_discount = "", special_discount_type = "", special_price_with_power = "";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        sessionManager = new SessionManager(this);
        //scrollView = (NestedScrollView)findViewById(R.id.scrollView);
        //scrollView.setNestedScrollingEnabled(false);

        findViewById(R.id.back_image).setOnClickListener(this);
        title_text_view = (TextView)findViewById(R.id.title_text_view);
        slide_viewPager = (ViewPager)findViewById(R.id.slide_viewPager);
        circlePageIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        product_name = (TextView)findViewById(R.id.product_name);
        product_code = (TextView)findViewById(R.id.product_code);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        review_text_view = (TextView)findViewById(R.id.review_text_view);
        //price_text_view = (TextView)findViewById(R.id.price_text_view);
        offer_text_view = (TextView)findViewById(R.id.offer_text_view);
        offer_text_view1 = (TextView)findViewById(R.id.offer_text_view1);
        discription_text_view = (TextView)findViewById(R.id.discription_text_view);
        //qty_dropdown_text_view = (AutoCompleteTextView)findViewById(R.id.qty_dropdown_text_view);
        select_power_button = (Button)findViewById(R.id.select_power_button);
        findViewById(R.id.select_power_button).setOnClickListener(this);
        related_pro_text = (TextView)findViewById(R.id.related_pro_text);
        related_pro_text.setVisibility(View.GONE);
        related_product_recycler_view = (RecyclerView)findViewById(R.id.related_product_recycler_view);
        related_product_recycler_view.setNestedScrollingEnabled(false);
        //gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        wish_list_image = (ImageView)findViewById(R.id.wish_list_image);
        findViewById(R.id.wish_list_image).setOnClickListener(this);
        //wish_list_image1 = (ImageView)findViewById(R.id.wish_list_image1);
        findViewById(R.id.wish_list_image1).setOnClickListener(this);
        //wishlist_button = (Button)findViewById(R.id.wishlist_button);
        //findViewById(R.id.wishlist_button).setOnClickListener(this);
        //remove_wishlist_button = (Button)findViewById(R.id.remove_wishlist_button);
        //findViewById(R.id.remove_wishlist_button).setOnClickListener(this);
        add_to_bag = (Button)findViewById(R.id.add_to_bag);
        findViewById(R.id.add_to_bag).setOnClickListener(this);
        cart_image = (ImageView)findViewById(R.id.cart_image);
        findViewById(R.id.cart_image).setOnClickListener(this);
        number = (TextView)findViewById(R.id.number);
        findViewById(R.id.filter_image).setOnClickListener(this);
        findViewById(R.id.search_image).setOnClickListener(this);
        liner_layout2 = (LinearLayout)findViewById(R.id.liner_layout2);
        power_layout = (LinearLayout)findViewById(R.id.power_layout);
        left_power_text = (TextView)findViewById(R.id.left_power_text);
        right_power_text = (TextView)findViewById(R.id.right_power_text);
        left_price_tv = (TextView)findViewById(R.id.left_price_tv);
        left_sale_price_tv = findViewById(R.id.left_sale_price_tv);
        right_price_tv = findViewById(R.id.right_price_tv);
        right_sale_price_tv = findViewById(R.id.right_sale_price_tv);
        left_price_tv.setPaintFlags(left_price_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        right_price_tv.setPaintFlags(right_price_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        back_image = (ImageView)findViewById(R.id.back_image);
        liner = (LinearLayout)findViewById(R.id.liner);
        share_layout = (LinearLayout)findViewById(R.id.share_layout);
        share_image_view = findViewById(R.id.share_image_view);
        findViewById(R.id.share_image_view).setOnClickListener(this);

        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            share_layout.setGravity(Gravity.END);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            share_layout.setGravity(Gravity.START);
        }
        if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)){
            CommanClass.getCartValue(this, number);
            //getWishlist();
            //searchProduct();

        }

        Intent in = getIntent();
        Uri data = in.getData();

        if(data!=null){
            System.out.println(data.toString().substring(data.toString().indexOf("=")+1));
            product_id = data.toString().substring(data.toString().indexOf("=")+1);
            current_currency = sessionManager.getCurrencyCode();
            if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)){

                getWishList();
                //searchProduct();
            }


        }else{
            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                product_id = bundle.getString("product_id");
                current_currency = bundle.getString("current_currency");
                title_name = bundle.getString("title_name");
                title_text_view.setText(title_name);
                if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)){

                    getWishList();
                    //searchProduct();
                }

            }
        }

        ObservableScrollView mScrollView =  findViewById(R.id.scrollView);
        mScrollView.setScrollViewCallbacks(this);

        findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
        findViewById(R.id.left_right_divider).setVisibility(View.GONE);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                slide_viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 4000);

        left_power_text.setText(getResources().getString(R.string.left_eye_power)+" "+leftPower);
        right_power_text.setText(getResources().getString(R.string.right_eye_power)+" "+rightPower);
        findViewById(R.id.left_plus_tv).setOnClickListener(this);
        findViewById(R.id.left_minus_tv).setOnClickListener(this);
        findViewById(R.id.right_plus_tv).setOnClickListener(this);
        findViewById(R.id.right_minus_tv).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                sessionManager.setLeftEyePower("");
                sessionManager.setRightEyePower("");
                super.onBackPressed();
                break;
            case R.id.wish_list_image:
                if (CommanMethod.isInternetConnected(this)) {
                    wishListAdd(product_id);
                }

                break;

            case R.id.add_to_bag:
                /*if(cate_id.equals("2") && (leftPower.equals("0.00") || rightPower.equals("0.00"))) {
                    if (powerRangeArray != null && powerRangeArray.length>0) {
                        powerDialogBox("add_bag");
                    }
                }else {
                    //dialogBox(getProduct_id,getBrand_id,getBrand_name);
                    if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)) {
                        addToCart(product_id, getBrand_id, getBrand_name, "continue");
                    }
                }*/
                if((cate_id.equals("2") || power_selection_type.equals("2") ||  power_selection_type.equals("3")) && powerFlag == 0) {

                    if (powerRangeArray != null && powerRangeArray.length>0) {
                        powerFlag++;
                        powerDialogBox("add_bag");
                    }
                }else {
                    //dialogBox(getProduct_id,getBrand_id,getBrand_name);
                    if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)) {
                        // Toast.makeText(ProductDetailsActivity.this, ""+quantity, Toast.LENGTH_SHORT).show();

                        addToCart(product_id, getBrand_id, getBrand_name, "continue");
                    }
                }
                break;
            case R.id.cart_image:
                Intent intent = new Intent(ProductDetailsActivity.this,UserCartActivity.class);
                startActivity(intent);
                break;
            case R.id.filter_image:
                Intent intent1 = new Intent(ProductDetailsActivity.this,FilterActivity.class);
                startActivity(intent1);
                break;
            case R.id.search_image:
                sortDialog();
                break;
            case R.id.select_power_button:
                if (powerRangeArray != null && powerRangeArray.length>0) {
                    powerDialogBox("select_power");
                }
                break;
            case R.id.share_image_view:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //String shareBody = "http://lenzzo.com/product?id="+getProduct_id; //old
                //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                String shareBody = "https://lenzzo.com/share.php?productid="+getProduct_id; //new add
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_with)));
                break;

            case R.id.left_plus_tv:

                int quantityInt = Integer.parseInt(quantity);

                if (quantityInt <=10){
                    if (Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) <= quantityInt){
                        ((TextView)findViewById(R.id.left_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) + 1));
                        ((TextView)findViewById(R.id.left_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice)))+" "+currency);
                        //((TextView)findViewById(R.id.left_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice))+" "+currency);
                    }
                }else {
                    if (Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) < 10){
                        ((TextView)findViewById(R.id.left_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) + 1));
                        ((TextView)findViewById(R.id.left_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice)))+" "+currency);
                        //((TextView)findViewById(R.id.left_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice))+" "+currency);
                    }
                }

                break;
            case R.id.left_minus_tv:
                if (Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) != 1){
                    ((TextView)findViewById(R.id.left_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) - 1));
                    ((TextView)findViewById(R.id.left_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice)))+" "+currency);
                    //((TextView)findViewById(R.id.left_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice))+" "+currency);
                }
                break;
            case R.id.right_plus_tv:
                int quantityInt1 = Integer.parseInt(quantity);
                if (quantityInt1 <=10){
                    if (Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) <= quantityInt1){
                        ((TextView)findViewById(R.id.right_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) + 1));
                        ((TextView)findViewById(R.id.right_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice)))+" "+currency);
                        //((TextView)findViewById(R.id.right_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice))+" "+currency);
                    }
                }else {
                    if (Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) < 10){
                        ((TextView)findViewById(R.id.right_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) + 1));
                        ((TextView)findViewById(R.id.right_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice)))+" "+currency);
                        //((TextView)findViewById(R.id.right_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice))+" "+currency);
                    }
                }

                break;
            case R.id.right_minus_tv:
                if (Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) != 1){
                    ((TextView)findViewById(R.id.right_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) - 1));
                    ((TextView)findViewById(R.id.right_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice)))+" "+currency);
                    //((TextView)findViewById(R.id.right_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice))+" "+currency);
                }
                break;
        }
    }

    private void getProductDetail(final Dialog dialog) {
        /*final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();*/
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"product_detail", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Log.e("PDetail", response);
                    //{"response":{"product_detail_Array":{"title":"Product Detail","product_detail":{"id":"801","quantity":"894","sell_quantity":"230","user_id":"0","cate_id":"3","cate_name":"Colored Contact Lenses","title":"Gray Shadow Bella Diamonds","title_ar":"جراي شادو بيلا دياموند","description":"<p><strong>Brand : Bella <\/strong><\/p>\r\n\r\n<p><strong>Material:<\/strong>  Polymacon   <\/p>\r\n\r\n<p><strong>Diameter:<\/strong> 14.5 mm <\/p>\r\n\r\n<p><strong>Water content:<\/strong> 38% <\/p>\r\n\r\n<p><strong>Base Curve:<\/strong> 8.6<\/p>\r\n\r\n<p><strong>Box contain:<\/strong> 2 lens<\/p>\r\n\r\n<p><strong>Replacement Schedule<\/strong><strong>: <\/strong>Reusable Monthly up to 3 month<\/p>","description_ar":"<p><strong>العلامة التجارية: بيلا<\/strong><\/p>\r\n\r\n<p><strong>المادة<\/strong>:&nbsp; Polymacon&nbsp;<\/p>\r\n\r\n<p><strong>القطر<\/strong>:&nbsp;14.5&nbsp;مم<\/p>\r\n\r\n<p><strong>نسبة الماء:<\/strong>&nbsp;38%&nbsp;<\/p>\r\n\r\n<p><strong>انحناء العدسة:<\/strong>&nbsp;8.6<\/p>\r\n\r\n<p><strong>العلبة<\/strong>:&nbsp;2 عدسة<\/p>\r\n\r\n<p><strong>الإستخدام<\/strong>: شهرية لمدة 3 شهور&nbsp;<\/p>\r\n","product_image":"product_1605117246.jpg","product_images":"pro_gallery_1605117246.jpg","model_no":"753","sku_code":null,"price":"12.30","current_currency":"BHD","sale_price":"0.00","with_power_price":"13.53","negotiable":"Yes","brand_name":"Bella Contact lenses","brand_id":"22","family_name":"Bella Diamonds","family_id":"1","variations":null,"variation_color":"-gray-","tags":"","is_hide":"Yes","reviewed":"264","featured":"0","archived":"0","deleted_at":"0","created_at":"2019-12-05 18:45:43","updated_at":"2020-12-15 16:05:52","status":"1","stock_flag":"1","rating":"4","replacement":"3-month","releted_product":"839,864","l_p_n_available":"-0.25","r_p_n_available":"-0.25","power":{"id":"22","name":"Bella Contact lenses","name_ar":"عدسات لاصقة بيلا","slug":"bella-contact-lenses","ui_order":"1","icon":"","created_at":"2019-08-23 05:32:25","updated_at":"2020-07-26 06:36:04","status":"1","brand_image":"brand0.171604001595734564.jpg","category_id":"3","start_range":"-10","end_range":"+0","diff_range":"0.25","range_above":"6","diff_range_above":"0.50","brand_slider_images":"pro_gallery_1576073868_0.jpg,pro_gallery_1576074117_0.jpg,pro_gallery_1576074117_1.jpg","brand_slider_images_order":"2,3","offer_collection":null},"power_range":["-10.00","-9.50","-9.00","-8.50","-8.00","-7.50","-7.00","-6.50","-6.00","-5.75","-5.50","-5.25","-5.00","-4.75","-4.50","-4.25","-4.00","-3.75","-3.50","-3.25","-3.00","-2.75","-2.50","-2.25","-2.00","-1.75","-1.50","-1.25","-1.00","-0.75","-0.50","-0.25","0.00"],"offer":{"id":"83","name":"2+1 Free","offer_subtitle":"2+1 Free","name_ar":"2+1 Free","offer_subtitle_ar":"2+1 Free","buy":"2","get_type":"0","free":"1","discount_type":"","discount":"0","offer_type_id":"1","category_id":null,"brand_id":"22","family_id":"1,2,3,23,24,26,49,108","product_id":null,"start_date":"2020-11-15","end_date":"2021-07-21","status":"1","created_at":"2020-04-29 23:18:24","updated_at":"2021-01-07 08:14:20","image":"brand1605414477.jpg"},"offer_id":"83","offer_name":"2+1 Free"}},"related_product_list_Array":{"title":"Product List","product_list":[{"id":"864","quantity":"986","sell_quantity":"20","user_id":"0","cate_id":"3","cate_name":"Colored Contact Lenses","title":"Gray Olive Bella Elite","title_ar":"جراي أوليف بيلا إليت","description":"<p><strong>Brand : Bella <\/strong><\/p>\r\n\r\n<p><strong>Material:<\/strong>  Polymacon   <\/p>\r\n\r\n<p><strong>Diameter:<\/strong> 14.5 mm <\/p>\r\n\r\n<p><strong>Water content:<\/strong> 38% <\/p>\r\n\r\n<p><strong>Base Curve:<\/strong> 8.6<\/p>\r\n\r\n<p><strong>Box contain:<\/strong> 2 lens<\/p>\r\n\r\n<p><strong>Replacement Schedule<\/strong><strong>: <\/strong>Reusable up to 3 month<\/p>","description_ar":"<p><strong>العلامة التجارية: بيلا <\/strong><\/p>\r\n\r\n<p><strong>المادة:<\/strong>&nbsp; Polymacon&nbsp;&nbsp;&nbsp;<\/p>\r\n\r\n<p><strong>القطر:<\/strong>&nbsp;14.5 mm&nbsp;<\/p>\r\n\r\n<p><strong>نسبة الماء:<\/strong>&nbsp;38%&nbsp;<\/p>\r\n\r\n<p><strong>انحناء العدسة:<\/strong>&nbsp;8.6<\/p>\r\n\r\n<p><strong>العلبة:<\/strong>&nbsp;2 عدسة<\/p>\r\n\r\n<p><strong>الإستخدام<\/strong><strong>:&nbsp;<\/strong>يمكن اعادة استخدام العدسة لفترة 3 شهور<\/p>\r\n","product_image":"product_1605000093.jpg","product_images":"pro_gallery_1605000093.jpg","model_no":"7160","sku_code":null,"price":"15.99","current_currency":"BHD","with_power_price":"18.45","negotiable":"Yes","brand_name":"Bella Contact lenses","brand_id":"22","family_name":"Bella Elite","family_id":"24","variations":null,"variation_color":"-gray-","tags":"","is_hide":"Yes","reviewed":"95","featured":"0","archived":"0","deleted_at":"0","created_at":"2019-12-13 04:32:41","updated_at":"2020-12-16 07:54:42","status":"1","stock_flag":"1","rating":"0","replacement":"3-month","releted_product":"868,985,762","l_p_n_available":"-0.25","r_p_n_available":"-0.25","offer_id":"83","offer_name":"2+1 Free","power":{"id":"22","name":"Bella Contact lenses","name_ar":"عدسات لاصقة بيلا","slug":"bella-contact-lenses","ui_order":"1","icon":"","created_at":"2019-08-23 05:32:25","updated_at":"2020-07-26 06:36:04","status":"1","brand_image":"brand0.171604001595734564.jpg","category_id":"3","start_range":"-10","end_range":"+0","diff_range":"0.25","range_above":"6","diff_range_above":"0.50","brand_slider_images":"pro_gallery_1576073868_0.jpg,pro_gallery_1576074117_0.jpg,pro_gallery_1576074117_1.jpg","brand_slider_images_order":"2,3","offer_collection":null}},{"id":"839","quantity":"984","sell_quantity":"16","user_id":"0","cate_id":"3","cate_name":"Colored Contact Lenses","title":"Gray Freshlook MB","title_ar":"جراي فريش لوك","description":"<p><strong>Brand : Freshlook <\/strong><\/p>\r\n\r\n<p><strong>Material:<\/strong>  polymer (phemfilcon) 45%   <\/p>\r\n\r\n<p><strong>Diameter:<\/strong> 14.5 mm <\/p>\r\n\r\n<p><strong>Water content:<\/strong> 55% <\/p>\r\n\r\n<p><strong>Base Curve:<\/strong> 8.6<\/p>\r\n\r\n<p><strong>Box contain:<\/strong> 2 lenses<\/p>\r\n\r\n<p><strong>Replacement Schedule<\/strong><strong>: <\/strong>up to 3 month<\/p>","description_ar":"<p><strong>&nbsp;العلامة التجارية: فريش لوك<\/strong><\/p>\r\n\r\n<p><strong>المادة:<\/strong>&nbsp; polymer (phemfilcon) 45%&nbsp;&nbsp;&nbsp;<\/p>\r\n\r\n<p><strong>القطر:<\/strong>&nbsp;14.5 mm&nbsp;<\/p>\r\n\r\n<p><strong>نسبة الماء:<\/strong>&nbsp;55%&nbsp;<\/p>\r\n\r\n<p><strong>تقعر العدسة:<\/strong>&nbsp;8.6<\/p>\r\n\r\n<p><strong>العلبة:<\/strong>&nbsp;2 عدسة<\/p>\r\n\r\n<p><strong>الإستخدام<\/strong><strong>:&nbsp;<\/strong>3 شهور&nbsp;<\/p>\r\n","product_image":"product_1605152328.jpg","product_images":"pro_gallery_1605152328.jpg","model_no":"204312820918","sku_code":null,"price":"13.22","current_currency":"BHD","with_power_price":"14.76","negotiable":"Yes","brand_name":"Freshlook Contact lenses","brand_id":"21","family_name":"FreshLook Monthly Blends","family_id":"45","variations":null,"variation_color":"-gray-","tags":"","is_hide":"Yes","reviewed":"134","featured":"0","archived":"0","deleted_at":"0","created_at":"2019-12-12 16:48:59","updated_at":"2020-12-16 07:09:14","status":"1","stock_flag":"1","rating":"1","replacement":"3-month","releted_product":"961,803,1116","l_p_n_available":"-0.25","r_p_n_available":"-0.25","offer_id":0,"offer_name":"","sale_price":0,"power":{"id":"21","name":"Freshlook Contact lenses","name_ar":"عدسات لاصقة فريش لوك","slug":"freshlook-contact-lenses","ui_order":"7","icon":"","created_at":"2019-08-23 05:32:02","updated_at":"2020-07-26 06:30:53","status":"1","brand_image":"brand0.244786001595734253.jpg","category_id":"3","start_range":"-6","end_range":"+0","diff_range":"0.25","range_above":"6","diff_range_above":"0.50","brand_slider_images":null,"brand_slider_images_order":null,"offer_collection":null}}]},"product_rating_list_Array":{"title":"Rating List","rating_list":[{"id":"7","user_id":"104","user_name":"Rizwan","comment":"Excellent ","product_id":"801","product_name":"Bella Diamonds Gray Shadow  - Monthly","rating":"1","created_at":null,"updated_at":"2019-12-12 16:36:58","status":"0","order_id":"14"},{"id":"9","user_id":"100","user_name":"Rahul Ranjan","comment":"Chjjj","product_id":"801","product_name":"Bella Diamonds Gray Shadow","rating":"4","created_at":null,"updated_at":"2019-12-17 00:54:54","status":"0","order_id":"27"},{"id":"10","user_id":"106","user_name":"ajay kumar","comment":"nice product.","product_id":"801","product_name":"Bella Diamonds Gray Shadow  - Monthly","rating":"3","created_at":null,"updated_at":"2019-12-17 01:53:58","status":"0","order_id":"11"},{"id":"12","user_id":"119","user_name":"Rizwan Test ","comment":"4 Star","product_id":"801","product_name":"Bella Diamonds Gray Shadow","rating":"4","created_at":null,"updated_at":"2019-12-23 04:51:05","status":"0","order_id":"47"},{"id":"14","user_id":"119","user_name":"rizwan","comment":"1star","product_id":"801","product_name":"Bella Diamonds Gray Shadow","rating":"1","created_at":null,"updated_at":"2019-12-23 06:22:36","status":"0","order_id":"62"},{"id":"15","user_id":"77","user_name":"rahul ranjan","comment":"vfgbdfgdfhgfhghgfhgh","product_id":"801","product_name":"Bella Diamonds Gray Shadow","rating":"3","created_at":null,"updated_at":"2019-12-23 06:22:41","status":"0","order_id":"61"},{"id":"16","user_id":"77","user_name":"rahul ranjan","comment":" vbvbvcbvcbbcvbcvb","product_id":"801","product_name":"Bella Diamonds Gray Shadow","rating":"2","created_at":null,"updated_at":"2019-12-23 06:27:42","status":"0","order_id":"22"},{"id":"18","user_id":"100","user_name":"Rahul Ranjan","comment":"Dgdgdg","product_id":"801","product_name":"Bella Diamonds Gray Shadow","rating":"3","created_at":null,"updated_at":"2019-12-23 06:35:09","status":"0","order_id":"53"},{"id":"23","user_id":"100","user_name":"Rahul Ranjan","comment":"Did","product_id":"801","product_name":"Bella Diamonds Gray Shadow","rating":"4","created_at":null,"updated_at":"2019-12-24 02:41:18","status":"0","order_id":"44"},{"id":"27","user_id":"100","user_name":"Rahul Ranjan","comment":"S","product_id":"801","product_name":"Bella Diamonds Gray Shadow","rating":"3","created_at":null,"updated_at":"2019-12-24 03:41:59","status":"0","order_id":"46"},{"id":"33","user_id":"119","user_name":"rizwan","comment":"good","product_id":"801","product_name":"Bella Diamonds Gray Shadow","rating":"5","created_at":null,"updated_at":"2020-01-02 05:29:18","status":"1","order_id":"119"},{"id":"37","user_id":"119","user_name":"rizwan","comment":"rr","product_id":"801","product_name":"Bella Diamonds Gray Shadow","rating":"5","created_at":null,"updated_at":"2020-01-02 05:29:54","status":"0","order_id":"116"}],"rating_count":12,"avg_rating":"3.17"}},"status":"success","message":"Record fetched successfully","message_ar":"تم جلب السجل بنجاح"}
                    productListList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("product_detail_Array"));
                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("product_detail"));
                        //JSONObject offerObject = new JSONObject(jsonObject2.getString("offer"));
                        getProduct_id = jsonObject2.getString("id");
                        getBrand_id = jsonObject2.getString("brand_id");
                        getBrand_name = jsonObject2.getString("brand_name");
                        cate_id = jsonObject2.getString("cate_id");
                        product_code.setText(getResources().getString(R.string.product_code)+": "+jsonObject2.getString("model_no"));

                        currency = jsonObject2.getString("current_currency");
                        price = jsonObject2.getString("price");
                        if(jsonObject2.has("sale_price")){
                            //sale_price1=jsonObject2.getString("sale_price");
                            sale_price = jsonObject2.getString("sale_price");
                            if(sale_price.equals("0.00")
                                    || sale_price.equals("0.000")
                                    || sale_price.equals("0")){
                                //price_text_view.setText(sale_price);
                                left_sale_price_tv.setText(price +" "+ currency);
                                right_sale_price_tv.setText(price +" "+ currency);
                                left_price_tv.setVisibility(View.GONE);
                                right_price_tv.setVisibility(View.GONE);
                                //left_price_tv.setVisibility(View.VISIBLE);
                            }else {
                                left_price_tv.setText(price +" "+ currency);
                                right_price_tv.setText(price +" "+ currency);
                                left_sale_price_tv.setText(sale_price +" "+ currency);
                                right_sale_price_tv.setText(sale_price +" "+ currency);
                            }

                        }else {
                            left_sale_price_tv.setText(price +" "+ currency);
                            right_sale_price_tv.setText(price +" "+ currency);
                            left_price_tv.setVisibility(View.GONE);
                            right_price_tv.setVisibility(View.GONE);
                        }
                        String offerData = String.valueOf(jsonObject2.get("offer"));

                        if(jsonObject2.optString("is_special_offer", "0").equals("1")){
                            //sale_price1=jsonObject2.getString("sale_price");
                            is_special_offer = jsonObject2.getString("is_special_offer");
                            special_price = jsonObject2.getString("special_price");
                            special_discount = jsonObject2.getString("special_discount");
                            special_discount_type = jsonObject2.getString("special_discount_type");
                            special_price_with_power = jsonObject2.getString("special_price_with_power");

                            left_price_tv.setText(price +" "+ currency);
                            right_price_tv.setText(price +" "+ currency);
                            left_sale_price_tv.setText(special_price +" "+ currency);
                            right_sale_price_tv.setText(special_price +" "+ currency);
                            left_price_tv.setVisibility(View.VISIBLE);
                            right_price_tv.setVisibility(View.VISIBLE);

                            if (special_discount_type.equals("1") && special_discount.length()>0){
                                offer_text_view1.setText(getString(R.string.special_discount)+" "+ CommanMethod.getTwoDecimalNumber(ProductDetailsActivity.this, special_discount)+"%");
                            }else if (special_discount_type.equals("0") && special_discount.length()>0){
                                offer_text_view1.setText(getString(R.string.special_discount)+" "+ CommanMethod.getCountryWiseDecimalNumber(ProductDetailsActivity.this, special_discount)+currency);
                            }

                        }else {
                            left_sale_price_tv.setText(price +" "+ currency);
                            right_sale_price_tv.setText(price +" "+ currency);
                            left_price_tv.setVisibility(View.GONE);
                            right_price_tv.setVisibility(View.GONE);
                            offer_text_view1.setText(CommanMethod.getOfferName(ProductDetailsActivity.this, offerData));
                        }

                        singleLeftLensPrice = left_sale_price_tv.getText().toString().trim().split(" ")[0];
                        singleRightLensPrice = right_sale_price_tv.getText().toString().trim().split(" ")[0];
                        with_power_price = jsonObject2.getString("with_power_price");
                        l_p_n_available = jsonObject2.getString("l_p_n_available");
                        r_p_n_available = jsonObject2.getString("r_p_n_available");


                        if(!jsonObject2.getString("offer_id").equals("0")
                                && !jsonObject2.getString("offer_name").equals("")){
                            liner_layout2.setVisibility(View.VISIBLE);
                        }

                        if(CommanMethod.isOutOfStock(jsonObject2.getString("stock_flag"), jsonObject2.getString("quantity"))){
                            add_to_bag.setEnabled(false);
                            add_to_bag.setTextColor(Color.RED);
                            add_to_bag.setText(getResources().getString(R.string.out_of_stock));
                        }
                        if(jsonObject2.has("power") && !jsonObject2.optString("power", "null").equals("null")){
                            JSONObject powerJsonObject = new JSONObject(jsonObject2.getString("power"));
                            start_range = powerJsonObject.getString("start_range");
                            end_range = powerJsonObject.getString("end_range");

                            diff_range = powerJsonObject.getString("diff_range");
                            range_above = powerJsonObject.getString("range_above");
                            diff_range_above = powerJsonObject.getString("diff_range_above");
                            power_selection_type = powerJsonObject.getString("power_selection_type");
                            low_range = powerJsonObject.getString("low_range");
                            mid_range = powerJsonObject.getString("mid_range");
                            high_range = powerJsonObject.getString("high_range");

                            leftAdditionList = new ArrayList<>();
                            rightAdditionList = new ArrayList<>();
                            PowerModel lowLeft = new PowerModel();
                            PowerModel mediumLeft = new PowerModel();
                            PowerModel highLeft = new PowerModel();
                            PowerModel lowRight = new PowerModel();
                            PowerModel mediumRight = new PowerModel();
                            PowerModel highRight = new PowerModel();
                            lowLeft.setValue(low_range);
                            mediumLeft.setValue(mid_range);
                            highLeft.setValue(high_range);
                            lowRight.setValue(low_range);
                            mediumRight.setValue(mid_range);
                            highRight.setValue(high_range);
                            leftAdditionList.add(lowLeft);
                            leftAdditionList.add(mediumLeft);
                            leftAdditionList.add(highLeft);
                            rightAdditionList.add(lowRight);
                            rightAdditionList.add(mediumRight);
                            rightAdditionList.add(highRight);

                            if(start_range.isEmpty() && end_range.isEmpty()){
                                findViewById(R.id.line).setVisibility(View.GONE);
                                select_power_button.setVisibility(View.GONE);
                                power_layout.setVisibility(View.GONE);
                                //findViewById(R.id.left_right_divider).setVisibility(View.GONE);
                            }
                        }else{
                            findViewById(R.id.line).setVisibility(View.GONE);
                            select_power_button.setVisibility(View.GONE);
                            power_layout.setVisibility(View.GONE);
                            //findViewById(R.id.left_right_divider).setVisibility(View.GONE);
                        }

                        if(jsonObject2.has("power_range_main") && !jsonObject2.optString("power_range_main", "null").equals("null")){
                            rightCylList = new ArrayList<>();
                            leftCylList = new ArrayList<>();
                            Object mainObject = jsonObject2.get("power_range_main");
                            if (mainObject instanceof  JSONObject){
                                JSONObject powerRangeMainJsonObject = jsonObject2.getJSONObject("power_range_main");
                                String col1 = powerRangeMainJsonObject.optString("col1", "");
                                String col2 = powerRangeMainJsonObject.optString("col2", "");
                                String col3 = powerRangeMainJsonObject.optString("col3", "");
                                String col4 = powerRangeMainJsonObject.optString("col4", "");
                                String col5 = powerRangeMainJsonObject.optString("col5", "");
                                if (!TextUtils.isEmpty(col1)){
                                    PowerModel powerModelLeft = new PowerModel();
                                    PowerModel powerModelRight = new PowerModel();
                                    powerModelLeft.setValue(col1);
                                    powerModelRight.setValue(col1);
                                    powerModelLeft.setTag("col1");
                                    powerModelRight.setTag("col1");
                                    rightCylList.add(powerModelRight);
                                    leftCylList.add(powerModelLeft);
                                }
                                if (!TextUtils.isEmpty(col2)){
                                    PowerModel powerModelLeft = new PowerModel();
                                    PowerModel powerModelRight = new PowerModel();
                                    powerModelLeft.setValue(col2);
                                    powerModelRight.setValue(col2);
                                    powerModelLeft.setTag("col2");
                                    powerModelRight.setTag("col2");
                                    rightCylList.add(powerModelRight);
                                    leftCylList.add(powerModelLeft);
                                }
                                if (!TextUtils.isEmpty(col3)){
                                    PowerModel powerModelLeft = new PowerModel();
                                    PowerModel powerModelRight = new PowerModel();
                                    powerModelLeft.setValue(col3);
                                    powerModelRight.setValue(col3);
                                    powerModelLeft.setTag("col3");
                                    powerModelRight.setTag("col3");
                                    rightCylList.add(powerModelRight);
                                    leftCylList.add(powerModelLeft);

                                }
                                if (!TextUtils.isEmpty(col4)){
                                    PowerModel powerModelLeft = new PowerModel();
                                    PowerModel powerModelRight = new PowerModel();
                                    powerModelLeft.setValue(col4);
                                    powerModelRight.setValue(col4);
                                    powerModelLeft.setTag("col4");
                                    powerModelRight.setTag("col4");
                                    rightCylList.add(powerModelRight);
                                    leftCylList.add(powerModelLeft);
                                }
                                if (!TextUtils.isEmpty(col5)){
                                    PowerModel powerModelLeft = new PowerModel();
                                    PowerModel powerModelRight = new PowerModel();
                                    powerModelLeft.setValue(col5);
                                    powerModelRight.setValue(col5);
                                    powerModelLeft.setTag("col5");
                                    powerModelRight.setTag("col5");
                                    rightCylList.add(powerModelRight);
                                    leftCylList.add(powerModelLeft);
                                }
                            }

                        }


                        JSONArray powerRangeJsonArray = jsonObject2.optJSONArray("power_range");
                        if (powerRangeJsonArray != null && powerRangeJsonArray.length()>0){
                            powerRangeArray = new String[powerRangeJsonArray.length()];
                            for(int i = 0; i< powerRangeJsonArray.length(); i++){
                                powerRangeArray[i] = powerRangeJsonArray.getString(i);
                            }
                        }else{
                            findViewById(R.id.line).setVisibility(View.GONE);
                            select_power_button.setVisibility(View.GONE);
                            power_layout.setVisibility(View.GONE);
                        }

                        discription_text_view.setMovementMethod(LinkMovementMethod.getInstance());
                        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                            product_name.setText(jsonObject2.getString("title"));
                            title_text_view.setText(jsonObject2.getString("title"));
                            //offer_text_view1.setText(offerObject.optString("name", ""));
                            discription_text_view.setText(Html.fromHtml(jsonObject2.getString("description")));

                        }else{
                            product_name.setText(jsonObject2.getString("title_ar"));
                            title_text_view.setText(jsonObject2.getString("title_ar"));
                            discription_text_view.setText(Html.fromHtml(jsonObject2.getString("description_ar")));
                            //offer_text_view1.setText(offerObject.optString("name_ar",""));
                        }

                        if(userWishlists!=null && userWishlists.size()>0) {
                            boolean isLiked = false;
                            for (int i = 0; i < userWishlists.size(); i++) {
                                if (product_id.equals(userWishlists.get(i).getProduct_id())) {
                                    //remove_wishlist_button.setVisibility(View.VISIBLE);
                                    //wish_list_image1.setVisibility(View.VISIBLE);
                                    //wish_list_image.setImageResource(R.drawable.heart_fill1);
                                    isLiked = true;
                                    break;
                                }
                            }

                            if (isLiked){
                                wish_list_image.setImageResource(R.drawable.heart_fill1);
                            }else {
                                wish_list_image.setImageResource(R.drawable.heart_s);
                            }

                        }
                        if(!jsonObject2.getString("quantity").isEmpty()){
                            //int qty = Integer.parseInt(jsonObject2.getString("quantity"));
                            quantity = jsonObject2.getString("quantity");
                            /*for(int i=1;i<=qty;i++){
                                if(i<=10){
                                    //qtyList.add(String.valueOf(i));
                                    if(i==10){
                                        break;
                                    }
                                }
                            }*/
                            //System.out.println("qty"+qtyList);
                        }else {
                            quantity = "10";
                        }
                        product_images = jsonObject2.getString("product_images");
                        if(!product_images.isEmpty()){
                            aList= new ArrayList(Arrays.asList(product_images.split(",")));
                            for(int i=0;i<aList.size();i++){
                                NUM_PAGES++;
                            }
                        }

                        JSONObject jsonObject3 = new JSONObject(jsonObject.getString("related_product_list_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject3.getString("product_list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            related_pro_text.setVisibility(View.VISIBLE);
                            JSONObject jsonObject4 = jsonArray.getJSONObject(i);
                            String startRange = "", endRange = "";
                            boolean shouldBuyNow = false;
                            JSONObject powerObject = jsonObject4.optJSONObject("power");
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
                            productList.setId(jsonObject4.getString("id"));
                            productList.setQuantity(jsonObject4.getString("quantity"));
                            productList.setUser_id(jsonObject4.getString("user_id"));
                            productList.setCate_id(jsonObject4.getString("cate_id"));
                            productList.setCate_name(jsonObject4.getString("cate_name"));
                            productList.setStartRange(startRange);
                            productList.setEndRange(endRange);
                            productList.setShouldBuyNow(shouldBuyNow);
                            productList.setTitle(jsonObject4.getString("title"));
                            productList.setDescription(jsonObject4.getString("description"));
                            productList.setProduct_image(API.ProductURL + jsonObject4.getString("product_image"));
                            productList.setProduct_images(jsonObject4.getString("product_images"));
                            productList.setModel_no(jsonObject4.getString("model_no"));
                            productList.setSku_code(jsonObject4.getString("sku_code"));
                            productList.setPrice(jsonObject4.getString("price"));
                            productList.setCurrent_currency(jsonObject4.getString("current_currency"));
                            if(jsonObject4.has("sale_price")){
                                productList.setSale_price(jsonObject4.getString("sale_price"));
                            }
                            productList.setNegotiable(jsonObject4.getString("negotiable"));
                            productList.setBrand_name(jsonObject4.getString("brand_name"));
                            productList.setBrand_id(jsonObject4.getString("brand_id"));
                            productList.setVariation_color(jsonObject4.getString("variation_color"));
                            productList.setTags(jsonObject4.getString("tags"));
                            productList.setIs_hide(jsonObject4.getString("is_hide"));
                            productList.setReviewed(jsonObject4.getString("reviewed"));
                            productList.setFeatured(jsonObject4.getString("featured"));
                            productList.setArchived(jsonObject4.getString("archived"));
                            productList.setDeleted_at(jsonObject4.getString("deleted_at"));
                            productList.setStatus(jsonObject4.getString("status"));
                            productList.setStock_flag(jsonObject4.getString("stock_flag"));
                            productList.setRating(jsonObject4.getString("rating"));
                            productList.setReplacement(jsonObject4.getString("replacement"));
                            productList.setReleted_product(jsonObject4.getString("releted_product"));
                            productList.setOffer_id(jsonObject4.getString("offer_id"));
                            productList.setOffer_name(jsonObject4.getString("offer_name"));
                            productList.setIsSpecialOffer(jsonObject2.getString("is_special_offer"));
                            productList.setSpecialPrice(jsonObject2.getString("special_price"));
                            productList.setSpecialDiscount(jsonObject2.getString("special_discount"));
                            productList.setSpecialDiscountType(jsonObject2.getString("special_discount_type"));
                            productList.setSpecialPriceWithPower(jsonObject2.getString("special_price_with_power"));

                            if(userWishlists.size()>0) {
                                for (int j = 0; j < userWishlists.size(); j++) {
                                    if (userWishlists.get(j).getProduct_id().equals(jsonObject2.getString("id"))) {
                                        productList.setSelected(true);
                                        break;
                                    }
                                }
                            }

                            productListList.add(productList);

                        }
                        JSONObject product_rating_json = new JSONObject(jsonObject.getString("product_rating_list_Array"));
                        rating_count = product_rating_json.getInt("rating_count");
                        avg_rating = product_rating_json.getString("avg_rating");
                        System.out.println("sdfdsf ds "+rating_count +" "+avg_rating);
                        if(rating_count>0){
                            review_text_view.setVisibility(View.VISIBLE);
                            review_text_view.setText(rating_count+" "+getResources().getString(R.string.review_text));
                            ratingBar.setRating(Float.parseFloat(avg_rating));
                        }else{
                            review_text_view.setVisibility(View.VISIBLE);
                        }


                    }else {

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

                productDetailSliderAdapter = new ProductDetailSliderAdapter(aList,ProductDetailsActivity.this);
                slide_viewPager.setAdapter(productDetailSliderAdapter);
                circlePageIndicator.setViewPager(slide_viewPager);
                findViewById(R.id.pager_click).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProductDetailsActivity.this,ImageViewZoomActivity.class);
                        intent.putExtra("image",product_images);
                        startActivity(intent);
                    }
                });

                //adapter = new ArrayAdapter<String>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, qtyList);
                //qty_dropdown_text_view.setAdapter(adapter);
                //GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductDetailsActivity.this,2,RecyclerView.HORIZONTAL, false);
                related_product_recycler_view.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.HORIZONTAL, false));

                productListAdapter = new ProductListAdapter(ProductDetailsActivity.this,productListList, userWishlists, number, ProductDetailsActivity.this);
                related_product_recycler_view.setAdapter(productListAdapter);

                /*scrollView.setOnTouchListener(new OnSwipeTouchListener(ProductDetailsActivity.this) {
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
                params.put("product_id", product_id);
              //  params.put("current_currency", current_currency);
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

    private void getWishList(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"wishlist", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

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
                        getProductDetail(dialog);
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

    private void wishListAdd(final String product_id){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"wishlist_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        getWishList();
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
                params.put("left_eye_power", leftPower);
                params.put("right_eye_power", rightPower);
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
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"usercart", new com.android.volley.Response.Listener<String>() {
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
                        String totalcount = jsonObject1.optString("totalcount", "");
                        number.setText(totalcount);
                        /*number.setVisibility(View.GONE);
                        if(jsonArray.length()> 0) {
                            number.setVisibility(View.VISIBLE);
                            total_value = jsonArray.length();
                            total_count = String.valueOf(total_value);
                            number.setText(total_count);
                        }else {
                            number.setText("");
                        }*/
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

    private void dialogBox(final String product_id,final String getBrand_id,final String getBrand_Name,final String power_selection_type){

        final Dialog dialog = new Dialog(ProductDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.buy_now_dialog);
        TextView continue_text_view = (TextView)dialog.findViewById(R.id.continue_text_view);
        TextView go_to_text_view = (TextView)dialog.findViewById(R.id.go_to_text_view);
        continue_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                //System.out.println("user_id"+product_id+getBrand_id+getBrand_Name);
                //addToCart(product_id,getBrand_id,getBrand_Name,"continue");
                finish();
            }
        });
        go_to_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductDetailsActivity.this, UserCartActivity.class);
                intent.putExtra("power_selection_type",power_selection_type);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
                //addToCart(product_id,getBrand_id,getBrand_Name,"goToCart");
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addToCart(final String product_id,final String getBrand_id,final String getBrand_Name,final String keyValue){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"usercart_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Log.d("responceptype",response);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = object.getString("message");
                    String power_selection_type = object.getJSONObject("response1").getJSONObject("record").getJSONObject("usercart").getString("power_selection_type");
                   // Log.d("power_selection_type",power_selection_type);
                    sessionManager.setPower_select_typ(power_selection_type);

                    if(status.equals("success")){
                        CommanClass.getCartValue(ProductDetailsActivity.this, number);
                        dialogBox(getProduct_id,getBrand_id,getBrand_name,power_selection_type);
                       /* if(keyValue.equals("continue")){

                            if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)){
                                CommanClass.getCartValue(ProductDetailsActivity.this, number);
                            }
                        }else if(keyValue.equals("goToCart")){
                            Intent intent=new Intent(ProductDetailsActivity.this, UserCartActivity.class);
                            startActivity(intent);
                        }*/


                    }else{
                        Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        openalartdialog(message);
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
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                int leftCount = Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString());
                int rightCount = Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString());
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                    params.put("user_id", sessionManager.getUserId());
                    params.put("current_currency", sessionManager.getCurrencyCode());
                }else{
                    params.put("user_id", sessionManager.getRandomValue());
                    params.put("current_currency", sessionManager.getCurrencyCode());
                }
                if(power_selection_type.equals("2")){
                    params.put("right_cyl",rightCylPower);
                    params.put("left_cyl",leftCylPower);
                    params.put("left_axis",leftAxisPower);
                    params.put("right_axis",rightAxisPower);
                    params.put("current_currency", sessionManager.getCurrencyCode());
                    Log.d("params",params.toString());
                }else if(power_selection_type.equals("3")){
                    params.put("right_add",rightAdditionPower);
                    params.put("left_add",leftAdditionPower);
                    params.put("current_currency", sessionManager.getCurrencyCode());
                    Log.d("params",params.toString());
                }
                params.put("power_selection_type",power_selection_type);
                params.put("product_id",product_id);
                params.put("shade", "");
                params.put("current_currency", sessionManager.getCurrencyCode());

                // params.put("quantity",select_qty);
                Log.d("params",params.toString());

                if (select_power_button.getVisibility() == View.GONE){
                    params.put("quantity", String.valueOf(leftCount));
                    params.put("current_currency", sessionManager.getCurrencyCode());
                }else {

                    if ((left_power_text.getVisibility() == View.VISIBLE) && (right_power_text.getVisibility() == View.VISIBLE)) {

                        if (leftPower.equals(rightPower)) {
                            params.put("quantity", String.valueOf(leftCount + rightCount));
                            params.put("current_currency", sessionManager.getCurrencyCode());

                            Log.d("params",params.toString());

                            if (power_selection_type.equals("2")){
                                if(! leftCylPower.equals(rightCylPower))
                                {
                                    params.put("quantity", String.valueOf(leftCount + rightCount));
                                    params.put("current_currency", sessionManager.getCurrencyCode());

                                }
                                else if(!leftAxisPower.equals(rightAxisPower))
                                {
                                    params.put("quantity", String.valueOf(leftCount + rightCount));
                                    params.put("current_currency", sessionManager.getCurrencyCode());

                                }
                            }else if (power_selection_type.equals("3")){
                                if(! leftAdditionPower.equals(rightAdditionPower))
                                {
                                    params.put("quantity", String.valueOf(leftCount + rightCount));
                                    params.put("current_currency", sessionManager.getCurrencyCode());

                                }
                            }

                            params.put("quantity_left", String.valueOf(leftCount));
                            params.put("quantity_right", String.valueOf(rightCount));
                            params.put("current_currency", sessionManager.getCurrencyCode());

                            Log.d("params",params.toString());

                            if (params.get("quantity").equals("1")){
                                params.put("quantity_left", "0");
                                params.put("quantity_right", "0");
                                Log.d("params",params.toString());
                                params.put("current_currency", sessionManager.getCurrencyCode());

                            }else{
                                if (leftPower.equals(rightPower)){
                                    params.put("quantity_left", "0");
                                    params.put("quantity_right", "0");
                                    Log.d("paramskanha",params.toString());
                                    params.put("current_currency", sessionManager.getCurrencyCode());

                                }
                            }

                        } else {
                            params.put("quantity", String.valueOf(leftCount + rightCount));
                            params.put("quantity_left", String.valueOf(leftCount));
                            params.put("quantity_right", String.valueOf(rightCount));
                            params.put("current_currency", sessionManager.getCurrencyCode());

                            Log.d("params",params.toString());
                        }

                       /* params.put("quantity_left", String.valueOf(leftCount));
                        params.put("quantity_right", String.valueOf(rightCount));*/
                        params.put("left_eye_power", leftPower);
                        params.put("right_eye_power", rightPower);
                        params.put("current_currency", sessionManager.getCurrencyCode());

                        Log.d("params",params.toString());
                    } else {
                        if (findViewById(R.id.left_power_text).getVisibility() == View.VISIBLE) {
                            params.put("quantity", String.valueOf(leftCount));
                            params.put("quantity_left", String.valueOf(leftCount));
                            params.put("left_eye_power", leftPower);
                            params.put("current_currency", sessionManager.getCurrencyCode());

                            Log.d("params",params.toString());
                        }

                        if (findViewById(R.id.right_power_text).getVisibility() == View.VISIBLE) {
                            params.put("quantity", String.valueOf(rightCount));
                            params.put("quantity_right", String.valueOf(rightCount));
                            params.put("right_eye_power", rightPower);
                            params.put("current_currency", sessionManager.getCurrencyCode());

                            Log.d("params",params.toString());
                        }
                    }
                }

                //params.put("quantity_left", leftPower);
                //params.put("quantity_right", rightPower);

                Log.d("params",params.toString());

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
        final Dialog dialog = new Dialog(this);
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
                if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)){
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

                /*Intent intent = new Intent(ProductDetailsActivity.this,SearchResultsActivity.class);
                intent.putExtra("array_of_product_lists",array_of_product_lists);
                startActivity(intent);*/
                Intent intent = new Intent(ProductDetailsActivity.this,ProductDetailsActivity.class);
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
                    Intent intent = new Intent(ProductDetailsActivity.this,SearchResultsActivity.class);
                    intent.putExtra("array_of_product_lists",array_of_product_lists);
                    intent.putExtra("search_string", search_text);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    }

    private void searchProduct(String search_text){
       /* final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();*/
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"productlist_of_brand", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //dialog.dismiss();
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
                            productList.setTitle(CommanMethod.getTitle(ProductDetailsActivity.this, jsonObject2));
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
                    //dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }
                searchadapter= new ArrayAdapter<ProductSearchModel>(ProductDetailsActivity.this,android.R.layout.simple_list_item_1, productLists);
                searchView.setAdapter(searchadapter);
                searchView.showDropDown();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
                //dialog.dismiss();
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

    String flagTwo = "1", flagThree = "1";
    private void powerDialogBox(final String from){
        Dialog power_dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        power_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        power_dialog.setCancelable(false);
        power_dialog.setContentView(R.layout.power_dialog_list);
        TextView textView = (TextView)power_dialog.findViewById(R.id.close_dialog);

        TextView sphere_tab = (TextView)power_dialog.findViewById(R.id.sphere_tab);
        TextView cylender_tab = (TextView)power_dialog.findViewById(R.id.cylender_tab);
        TextView axis_tab = (TextView)power_dialog.findViewById(R.id.axis_tab);
        TextView sphere_tab_two = (TextView)power_dialog.findViewById(R.id.sphere_tab_two);
        TextView addition_tab = (TextView)power_dialog.findViewById(R.id.addition_tab);

        left_eye_text = (TextView)power_dialog.findViewById(R.id.left_eye_text);
        right_eye_text = (TextView)power_dialog.findViewById(R.id.right_eye_text);
        RecyclerView leftListView = (RecyclerView) power_dialog.findViewById(R.id.select_list_view);
        RecyclerView rightListView = (RecyclerView)power_dialog.findViewById(R.id.select_list_view1);
        Button btn_done_one = (Button)power_dialog.findViewById(R.id.btn_done_one);
        Button btn_done_two = (Button)power_dialog.findViewById(R.id.btn_done_two);
        Button btn_done_three = (Button)power_dialog.findViewById(R.id.btn_done_three);
        btn_done_one.setVisibility(View.GONE);
        btn_done_two.setVisibility(View.GONE);
        btn_done_three.setVisibility(View.GONE);

        if(power_selection_type.equals("2")){
            power_dialog.findViewById(R.id.tab_lay).setVisibility(View.VISIBLE);
            power_dialog.findViewById(R.id.type_two).setVisibility(View.VISIBLE);
            power_dialog.findViewById(R.id.type_three).setVisibility(View.GONE);
            btn_done_two.setVisibility(View.VISIBLE);

        }else if(power_selection_type.equals("3")){
            power_dialog.findViewById(R.id.tab_lay).setVisibility(View.VISIBLE);
            power_dialog.findViewById(R.id.type_two).setVisibility(View.GONE);
            power_dialog.findViewById(R.id.type_three).setVisibility(View.VISIBLE);
            btn_done_three.setVisibility(View.VISIBLE);

        }else {
            power_dialog.findViewById(R.id.tab_lay).setVisibility(View.GONE);
            power_dialog.findViewById(R.id.type_two).setVisibility(View.GONE);
            power_dialog.findViewById(R.id.type_three).setVisibility(View.GONE);
            btn_done_one.setVisibility(View.VISIBLE);
        }

        sphere_tab.setOnClickListener(v -> {
            if(!flagTwo.equals("1")){
                flagTwo = "1";
                btn_done_two.setText(getString(R.string.next));
                sphere_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_selected));
                cylender_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_unselected));
                axis_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_unselected));

                leftListView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
                leftPowerOneAdapter = new LeftPowerOneAdapter(ProductDetailsActivity.this, leftPowerModelList,null);
                leftListView.setAdapter(leftPowerOneAdapter);

                rightListView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
                rightPowerOneAdapter = new RightPowerOneAdapter(ProductDetailsActivity.this,rightPowerModelList,null);
                rightListView.setAdapter(rightPowerOneAdapter);
            }

        });

        cylender_tab.setOnClickListener(v -> {
            if (!flagTwo.equals("2")){
                flagTwo = "2";
                speriphicalStep();
                btn_done_two.setText(getString(R.string.next));
                sphere_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_unselected));
                cylender_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_selected));
                axis_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_unselected));

                leftListView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
                leftCylAdapter = new LeftPowerAdditionAdapter(ProductDetailsActivity.this, leftCylList, "cyl");
                leftListView.setAdapter(leftCylAdapter);

                rightListView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
                rightCylAdapter = new RightPowerAdditionAdapter(ProductDetailsActivity.this,rightCylList, "cyl");
                rightListView.setAdapter(rightCylAdapter);
            }
        });

        axis_tab.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(leftCylPowerKey) || !TextUtils.isEmpty(rightCylPowerKey)){
                if ((leftCylList != null && rightCylList != null) && (leftCylList.size()>0 && rightCylList.size()>0)) {
                    for (int i = 0; i < leftCylList.size(); i++) {
                        if (leftCylList.get(i).isSelected()) {
                            leftCylPower = leftCylList.get(i).getValue();
                            leftCylPowerKey = rightCylList.get(i).getTag();
                            break;
                        }
                    }
                    for (int i = 0; i < rightCylList.size(); i++) {
                        if (rightCylList.get(i).isSelected()) {
                            rightCylPower = rightCylList.get(i).getValue();
                            rightCylPowerKey = rightCylList.get(i).getTag();
                            break;

                        }
                    }
                }

                if (!flagTwo.equals("3")){
                    flagTwo = "3";
                    btn_done_two.setText(getString(R.string.done));
                    sphere_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_unselected));
                    cylender_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_unselected));
                    axis_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_selected));
                    getAxisPowerData(leftListView, rightListView);

                }
            }else {
                CommanMethod.getCustomOkAlert(ProductDetailsActivity.this, getString(R.string.cyleder_value));
            }

        });

        sphere_tab_two.setOnClickListener(v -> {
            if (!flagThree.equals("1")) {
                flagThree = "1";
                btn_done_three.setText(getString(R.string.next));
                addition_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_unselected));
                sphere_tab_two.setBackground(getResources().getDrawable(R.drawable.power_tab_selected));
                leftListView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
                leftPowerOneAdapter = new LeftPowerOneAdapter(ProductDetailsActivity.this, leftPowerModelList,null);
                leftListView.setAdapter(leftPowerOneAdapter);


                //GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this,1);
                rightListView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
                rightPowerOneAdapter = new RightPowerOneAdapter(ProductDetailsActivity.this, rightPowerModelList,null);
                rightListView.setAdapter(rightPowerOneAdapter);
            }
        });

        addition_tab.setOnClickListener(v -> {
            if (!flagThree.equals("2")){
                flagThree = "2";
                speriphicalStep();
                btn_done_three.setText(getString(R.string.done));
                addition_tab.setBackground(getResources().getDrawable(R.drawable.power_tab_selected));
                sphere_tab_two.setBackground(getResources().getDrawable(R.drawable.power_tab_unselected));

                leftListView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
                leftAdditionAdapter = new LeftPowerAdditionAdapter(ProductDetailsActivity.this, leftAdditionList,"addition");
                leftListView.setAdapter(leftAdditionAdapter);

                rightListView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
                rightAdditionAdapter = new RightPowerAdditionAdapter(ProductDetailsActivity.this,rightAdditionList, "addition");
                rightListView.setAdapter(rightAdditionAdapter);

            }

        });

        textView.setOnClickListener(v -> {
            if (power_selection_type.equals("2")){
                leftCylPower = "";
                rightCylPower = "";
                leftAxisPower = "";
                rightAxisPower = "";
                leftPower = "0.00";
                rightPower = "0.00";
            }else if (power_selection_type.equals("3")){
                leftAdditionPower = "";
                rightAdditionPower = "";
                leftPower = "0.00";
                rightPower = "0.00";
            }
            power_dialog.dismiss();
        });

        btn_done_one.setOnClickListener(v -> {
            // Toast.makeText(getApplicationContext(), "clicking...btn1", Toast.LENGTH_SHORT).show();
            leftPower = "";
            rightPower = "";
            boolean leftOutOfStock = false, rightOutOfStock = false;
            for (int i = 0; i < leftPowerModelList.size(); i++) {

                if (leftPowerModelList.get(i).isSelected()) {
                    if (leftPowerModelList.get(i).isOutOfStock()) {
                        leftOutOfStock = true;
                        break;
                    } else {
                        leftPower = leftPowerModelList.get(i).getValue();
                        sessionManager.setLeftEyePower(leftPower);
                        break;
                    }
                }
            }

            for (int i = 0; i < rightPowerModelList.size(); i++) {
                if (rightPowerModelList.get(i).isSelected()) {
                    if (rightPowerModelList.get(i).isOutOfStock()) {
                        rightOutOfStock = true;
                        break;
                    } else {
                        rightPower = rightPowerModelList.get(i).getValue();
                        sessionManager.setRightEyePower(rightPower);
                        break;
                    }
                }
            }
            if (leftOutOfStock || rightOutOfStock) {
                CommanMethod.getCustomOkAlert(ProductDetailsActivity.this, getResources().getString(R.string.out_of_stock_small));
            } else {
                if (is_special_offer.equals("1")){
                    // powerDone(special_price, special_price_with_power);
                    powerDoneSpecialPrice(special_price, with_power_price);

                }else {
                    powerDone(sale_price, with_power_price);
                }

                ((TextView) findViewById(R.id.left_count_tv)).setText("1");
                ((TextView) findViewById(R.id.right_count_tv)).setText("1");
                singleLeftLensPrice = left_sale_price_tv.getText().toString().trim().split(" ")[0];
                singleRightLensPrice = right_sale_price_tv.getText().toString().trim().split(" ")[0];
                left_power_text.setText(getResources().getString(R.string.left_eye_power) + " " + leftPower);
                right_power_text.setText(getResources().getString(R.string.right_eye_power) + " " + rightPower);

                powerFlag++;
                if (from.equals("add_bag")) {
                    addToCart(product_id, getBrand_id, getBrand_name, "select");
                }
                power_dialog.dismiss();
            }
        });

        btn_done_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "clicking...btn2", Toast.LENGTH_SHORT).show();
                if (flagTwo.equals("1")){
                    leftPower = "";
                    rightPower = "";
                    speriphicalStep();
                    cylender_tab.performClick();
                }else if(flagTwo.equals("2")){
                    boolean leftSelected=false , rightSelected=false;
                    if ((leftCylList != null && rightCylList != null) && (leftCylList.size()>0 && rightCylList.size()>0)) {
                        for (int i = 0; i < leftCylList.size(); i++) {
                            if (leftCylList.get(i).isSelected()) {
                                leftCylPower = leftCylList.get(i).getValue();
                                leftCylPowerKey = rightCylList.get(i).getTag();
                                leftSelected = true;
                                break;
                            }
                        }
                        for (int i = 0; i < rightCylList.size(); i++) {
                            if (rightCylList.get(i).isSelected()) {
                                rightCylPower = rightCylList.get(i).getValue();
                                rightCylPowerKey = rightCylList.get(i).getTag();
                                rightSelected = true;
                                break;

                            }
                        }
                    }else {
                        leftSelected = true;
                        rightSelected = true;
                    }

                    if (leftSelected || rightSelected){
                        axis_tab.performClick();
                    }else {
                        CommanMethod.getCustomOkAlert(ProductDetailsActivity.this, getString(R.string.cyleder_value));
                    }

                }else if(flagTwo.equals("3")){
                    boolean leftSelected=false , rightSelected=false;
                    if (leftAxisList != null && leftAxisList.size()>0) {
                        for (int i = 0; i < leftAxisList.size(); i++) {
                            if (leftAxisList.get(i).isSelected()) {
                                leftAxisPower = leftAxisList.get(i).getValue();
                                leftSelected = true;
                                break;
                            }
                        }
                    }else {
                        leftSelected = true;
                    }
                    if (rightAxisList != null && rightAxisList.size()>0) {
                        for (int i = 0; i < rightAxisList.size(); i++) {
                            if (rightAxisList.get(i).isSelected()) {
                                rightAxisPower = rightAxisList.get(i).getValue();
                                rightSelected = true;
                                break;
                            }
                        }
                    }else {
                        rightSelected = true;
                    }

                    if (leftSelected || rightSelected){
                        powerFlag++;
                        if (from.equals("add_bag")) {
                            addToCart(product_id, getBrand_id, getBrand_name, "select");
                        }
                        flagTwo = "1";

                        if (!TextUtils.isEmpty(leftCylPower) && !TextUtils.isEmpty(rightCylPower)){
                            ((TextView)findViewById(R.id.cyl_power_tv)).setText(getString(R.string.left_cyl_power)+leftCylPower+", "+getString(R.string.right_cyl_power)+rightCylPower);
                        }else if (TextUtils.isEmpty(leftCylPower) && TextUtils.isEmpty(rightCylPower)){
                            ((TextView)findViewById(R.id.cyl_power_tv)).setVisibility(View.GONE);
                        }if (!TextUtils.isEmpty(leftCylPower) && TextUtils.isEmpty(rightCylPower)){
                            ((TextView)findViewById(R.id.cyl_power_tv)).setText(getString(R.string.left_cyl_power)+leftCylPower);
                        }if (TextUtils.isEmpty(leftCylPower) && !TextUtils.isEmpty(rightCylPower)){
                            ((TextView)findViewById(R.id.cyl_power_tv)).setText(getString(R.string.right_cyl_power)+rightCylPower);
                        }

                        if (!TextUtils.isEmpty(leftAxisPower) && !TextUtils.isEmpty(rightAxisPower)){
                            ((TextView)findViewById(R.id.axis_power_tv)).setText(getString(R.string.left_axis_power)+leftAxisPower+", "+getString(R.string.right_axis_power)+rightAxisPower);
                        }else if (TextUtils.isEmpty(leftAxisPower) && TextUtils.isEmpty(rightAxisPower)){
                            ((TextView)findViewById(R.id.axis_power_tv)).setVisibility(View.GONE);
                        }if (!TextUtils.isEmpty(leftAxisPower) && TextUtils.isEmpty(rightAxisPower)){
                            ((TextView)findViewById(R.id.axis_power_tv)).setText(getString(R.string.left_axis_power)+leftAxisPower);
                        }if (TextUtils.isEmpty(leftAxisPower) && !TextUtils.isEmpty(rightAxisPower)){
                            ((TextView)findViewById(R.id.axis_power_tv)).setText(getString(R.string.right_axis_power)+rightAxisPower);
                        }

                        left_power_text.setText(getResources().getString(R.string.left_sph_power) + " " + leftPower);
                        right_power_text.setText(getResources().getString(R.string.right_sph_power) + " " + rightPower);
                        if (power_selection_type.equals("2")){
                            findViewById(R.id.type_two_lay).setVisibility(View.VISIBLE);
                            findViewById(R.id.addition_tv).setVisibility(View.GONE);
                        }else if (power_selection_type.equals("3")){
                            findViewById(R.id.type_two_lay).setVisibility(View.GONE);
                            findViewById(R.id.addition_tv).setVisibility(View.VISIBLE);
                        }else {
                            findViewById(R.id.type_two_lay).setVisibility(View.GONE);
                            findViewById(R.id.addition_tv).setVisibility(View.GONE);
                        }
                        power_dialog.dismiss();

                        //new code powerselectiontype.axis calling mathode
                        //findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
                        //Toast.makeText(ProductDetailsActivity.this, "click..axis..btn", Toast.LENGTH_SHORT).show();
                        powerselectiontypeone(power_selection_type,left_eye_text.toString(),right_eye_text.toString(),leftCylPower,rightCylPower,
                                leftAxisPower,rightAxisPower,leftAdditionPower,rightAdditionPower);

                    }else {
                        CommanMethod.getCustomOkAlert(ProductDetailsActivity.this, getString(R.string.axis_value));
                    }
                }
            }
        });

        btn_done_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "clicking"+is_special_offer+leftPower, Toast.LENGTH_SHORT).show();
                if (flagThree.equals("1")){
                    leftPower = "";
                    rightPower = "";
                    speriphicalStep();
                    addition_tab.performClick();
                }else if(flagThree.equals("2")){
                    boolean leftSelected=false , rightSelected=false;
                    if ((leftAdditionList != null && rightAdditionList != null)
                            && (leftAdditionList.size()>0 && rightAdditionList.size()>0)) {
                        for (int i = 0; i < leftAdditionList.size(); i++) {
                            if (leftAdditionList.get(i).isSelected()) {
                                leftAdditionPower = leftAdditionList.get(i).getValue();
                                leftSelected = true;
                                break;
                            }
                        }
                        for (int i = 0; i < rightAdditionList.size(); i++) {
                            if (rightAdditionList.get(i).isSelected()) {
                                rightAdditionPower = rightAdditionList.get(i).getValue();
                                rightSelected = true;
                                break;

                            }
                        }
                    }else {
                        leftSelected = true;
                        rightSelected = true;
                    }

                    if (leftSelected || rightSelected){
                        powerFlag++;
                        if (from.equals("add_bag")) {
                            addToCart(product_id, getBrand_id, getBrand_name, "select");
                        }
                        if (!TextUtils.isEmpty(leftAdditionPower) && !TextUtils.isEmpty(rightAdditionPower)){
                            ((TextView)findViewById(R.id.addition_tv)).setText(getString(R.string.left_add_power)+leftAdditionPower+", "+getString(R.string.right_add_power)+rightAdditionPower);
                        }else if (TextUtils.isEmpty(leftAdditionPower) && TextUtils.isEmpty(rightAdditionPower)){
                            ((TextView)findViewById(R.id.addition_tv)).setVisibility(View.GONE);
                        }if (!TextUtils.isEmpty(leftAdditionPower) && TextUtils.isEmpty(rightAdditionPower)){
                            ((TextView)findViewById(R.id.addition_tv)).setText(getString(R.string.left_add_power)+leftAdditionPower);
                        }if (TextUtils.isEmpty(leftAdditionPower) && !TextUtils.isEmpty(rightAdditionPower)){
                            ((TextView)findViewById(R.id.addition_tv)).setText(getString(R.string.right_add_power)+rightAdditionPower);
                        }
                        flagThree = "1";

                        if (power_selection_type.equals("2")){
                            findViewById(R.id.type_two_lay).setVisibility(View.VISIBLE);
                            findViewById(R.id.addition_tv).setVisibility(View.GONE);
                        }else if (power_selection_type.equals("3")){
                            findViewById(R.id.type_two_lay).setVisibility(View.GONE);
                            findViewById(R.id.addition_tv).setVisibility(View.VISIBLE);
                        }else {
                            findViewById(R.id.type_two_lay).setVisibility(View.GONE);
                            findViewById(R.id.addition_tv).setVisibility(View.GONE);
                        }
                        power_dialog.dismiss();

                        //new code powerselectiontype.addition calling mathode
                        // Toast.makeText(ProductDetailsActivity.this, "click..addition..btn", Toast.LENGTH_SHORT).show();
                        powerselectiontypeone(power_selection_type,left_eye_text.toString(),right_eye_text.toString(),leftCylPower,rightCylPower,
                                leftAxisPower,rightAxisPower,leftAdditionPower,rightAdditionPower);


                    }else {
                        CommanMethod.getCustomOkAlert(ProductDetailsActivity.this, getString(R.string.addition_value));
                    }
                }
            }
        });

        String[] arrSplit = l_p_n_available.split(",");
        leftPowerList = new ArrayList<>();
        leftPowerList.addAll(Arrays.asList(arrSplit));

        String[] arrSplit1 = r_p_n_available.split(",");
        rightPowerlist = new ArrayList<>();
        rightPowerlist.addAll(Arrays.asList(arrSplit1));


        leftPowerModelList = new ArrayList<>();
        rightPowerModelList = new ArrayList<>();

        if(!start_range.isEmpty() && !end_range.isEmpty()){
           // Float start_number=Float.parseFloat(start_range);
           // Float end_number = Float.parseFloat(end_range);


            for (String s : powerRangeArray) {
                PowerModel powerModel = new PowerModel();
                RightPowerModel rightPowerModel = new RightPowerModel();
                powerModel.setValue(s);
                rightPowerModel.setValue(s);
                for (int j = 0; j < leftPowerList.size(); j++) {
                    if (s.equals(leftPowerList.get(j))) {
                        //leftOutOfStock = true;
                        powerModel.setOutOfStock(true);
                    }
                }

                for (int k = 0; k < rightPowerlist.size(); k++) {
                    if (s.equals(rightPowerlist.get(k))) {
                        //rightOutOfStock = true;
                        rightPowerModel.setOutOfStock(true);
                    }
                }

                if (!sessionManager.getLeftEyePower().isEmpty()) {
                    if (powerModel.getValue().equals(sessionManager.getLeftEyePower())) {
                        left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog) + ": " + sessionManager.getLeftEyePower());
                        powerModel.setSelected(true);
                    } else if (sessionManager.getLeftEyePower().equals("0.00")) {
                        powerModel.setSelected(false);
                    }
                } else {
                    if (powerModel.getValue().equals("0.00")) {
                        powerModel.setSelected(true);
                        left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog) + ": 0.00");
                    }
                }

                if (!sessionManager.getRightEyePower().isEmpty()) {
                    if (rightPowerModel.getValue().equals(sessionManager.getRightEyePower())) {
                        right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog) + ": " + sessionManager.getRightEyePower());
                        rightPowerModel.setSelected(true);
                    } else if (sessionManager.getLeftEyePower().equals("0.00")) {
                        right_power_text.setText(sessionManager.getRightEyePower());
                        rightPowerModel.setSelected(false);
                    }
                } else {
                    if (rightPowerModel.getValue().equals("0.00")) {
                        rightPowerModel.setSelected(true);
                        right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog) + ": 0.00");
                    }
                }

                leftPowerModelList.add(powerModel);
                rightPowerModelList.add(rightPowerModel);
            }


            for (int i = 0; i< leftPowerModelList.size(); i++) {
                PowerModel model = leftPowerModelList.get(i);
                if (!(model.getValue().contains("-") || model.getValue().contains("0.00") || model.getValue().contains("0.0"))) {
                    model.setValue("+" +model.getValue());
                }
                leftPowerModelList.set(i, model);
            }

            for (int j=0; j<rightPowerModelList.size(); j++) {
                RightPowerModel model = rightPowerModelList.get(j);
                if (!(model.getValue().contains("-") || model.getValue().contains("0.00") || model.getValue().contains("0.0"))) {
                    model.setValue("+" +model.getValue());
                }
                rightPowerModelList.set(j, model);
            }


            leftListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            leftPowerOneAdapter = new LeftPowerOneAdapter(this, leftPowerModelList,null);
            leftListView.setAdapter(leftPowerOneAdapter);


            //GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this,1);
            rightListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            rightPowerOneAdapter = new RightPowerOneAdapter(this,rightPowerModelList,null);
            rightListView.setAdapter(rightPowerOneAdapter);

        }
        power_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        power_dialog.show();
    }

    private void powerDone(String sale_price, String with_power_price){
        final boolean b = sale_price.equals("0.00")
                || sale_price.equals("0.000")
                || sale_price.equals("0");
        if (leftPower.equals("0.00") && rightPower.equals("0.00")) {
            if (b) {
                left_sale_price_tv.setText(price + " " + currency);
                left_price_tv.setVisibility(View.GONE);
            } else {
                left_sale_price_tv.setText(sale_price + " " + currency);
                left_price_tv.setText(price + " " + currency);
            }
            right_power_text.setVisibility(View.VISIBLE);
            left_power_text.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
            findViewById(R.id.left_right_divider).setVisibility(View.GONE);

        } else if (leftPower.equals(rightPower)) {
            if (b) {
                left_sale_price_tv.setText(with_power_price + " " + currency);
                left_price_tv.setVisibility(View.GONE);
            } else {
                left_sale_price_tv.setText(sale_price + " " + currency);
                left_price_tv.setText(price + " " + currency);
            }
            right_power_text.setVisibility(View.VISIBLE);
            left_power_text.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
            findViewById(R.id.left_right_divider).setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(leftPower) && rightPower.equals("0.00")) {
            if (b) {
                left_sale_price_tv.setText(price + " " + currency);
                left_price_tv.setVisibility(View.GONE);
            } else {
                left_sale_price_tv.setText(sale_price + " " + currency);
                left_price_tv.setText(price + " " + currency);
            }
            right_power_text.setVisibility(View.VISIBLE);
            left_power_text.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
            findViewById(R.id.left_right_divider).setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(leftPower) && !rightPower.equals("0.00")) {
            if (b) {
                left_sale_price_tv.setText(with_power_price + " " + currency);
                left_price_tv.setVisibility(View.GONE);
            } else {
                left_sale_price_tv.setText(sale_price + " " + currency);
                left_price_tv.setText(price + " " + currency);
            }
            right_power_text.setVisibility(View.VISIBLE);
            left_power_text.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
            findViewById(R.id.left_right_divider).setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(rightPower) && leftPower.equals("0.00")) {
            if (b) {
                left_sale_price_tv.setText(price + " " + currency);
                left_price_tv.setVisibility(View.GONE);
            } else {
                left_sale_price_tv.setText(sale_price + " " + currency);
                left_price_tv.setText(price + " " + currency);
            }
            left_power_text.setVisibility(View.VISIBLE);
            right_power_text.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
            findViewById(R.id.left_right_divider).setVisibility(View.GONE);
        } else if (TextUtils.isEmpty(rightPower) && !leftPower.equals("0.00")) {
            if (b) {
                left_sale_price_tv.setText(with_power_price + " " + currency);
                left_price_tv.setVisibility(View.GONE);
            } else {
                left_sale_price_tv.setText(sale_price + " " + currency);
                left_price_tv.setText(price + " " + currency);
            }
            left_power_text.setVisibility(View.VISIBLE);
            right_power_text.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
            findViewById(R.id.left_right_divider).setVisibility(View.GONE);
        } else if (leftPower.equals("0.00") && !TextUtils.isEmpty(rightPower)) {
            if (b) {
                left_sale_price_tv.setText(price + " " + currency);
                left_price_tv.setVisibility(View.GONE);
                right_sale_price_tv.setText(with_power_price + " " + currency);
                right_price_tv.setVisibility(View.GONE);
            } else {
                left_sale_price_tv.setText(price + " " + currency);
                right_sale_price_tv.setText(with_power_price + " " + currency);
                left_price_tv.setText(price + " " + currency);
                right_price_tv.setText(price + " " + currency);
            }
            left_power_text.setVisibility(View.VISIBLE);
            right_power_text.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
            findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
        } else if (rightPower.equals("0.00") && !TextUtils.isEmpty(leftPower)) {
            if (b) {
                left_sale_price_tv.setText(with_power_price + " " + currency);
                left_price_tv.setVisibility(View.GONE);
                right_sale_price_tv.setText(price + " " + currency);
                right_price_tv.setVisibility(View.GONE);
            } else {
                left_sale_price_tv.setText(sale_price + " " + currency);
                right_sale_price_tv.setText(price + " " + currency);
                left_price_tv.setText(price + " " + currency);
                right_price_tv.setText(price + " " + currency);
            }

            left_power_text.setVisibility(View.VISIBLE);
            right_power_text.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
            findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(leftPower) && !TextUtils.isEmpty(rightPower)) {
            if (b) {
                left_sale_price_tv.setText(with_power_price + " " + currency);
                left_price_tv.setVisibility(View.GONE);
                right_sale_price_tv.setText(with_power_price + " " + currency);
                right_price_tv.setVisibility(View.GONE);
            } else {
                left_sale_price_tv.setText(sale_price + " " + currency);
                right_sale_price_tv.setText(sale_price + " " + currency);
                left_price_tv.setText(price + " " + currency);
                right_price_tv.setText(price + " " + currency);
            }

            left_power_text.setVisibility(View.VISIBLE);
            right_power_text.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
            findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
        }
    }

    private void powerDoneSpecialPrice(String sale_price, String with_power_price) {
        //Toast.makeText(this, "hello" + is_special_offer, Toast.LENGTH_SHORT).show();
        if (is_special_offer.equals("1")) {
            powerDoneSpecialPrice_copy(sale_price, with_power_price);
        } else {

            final boolean b = sale_price.equals("0.00")
                    || sale_price.equals("0.000")
                    || sale_price.equals("0");
            if (leftPower.equals("0.00") && rightPower.equals("0.00")) {
                if (b) {
                    left_sale_price_tv.setText(price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                }
                right_power_text.setVisibility(View.VISIBLE);
                left_power_text.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);

            } else if (leftPower.equals(rightPower)) { //kjkj
                if (b) {
                    left_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                }
                right_power_text.setVisibility(View.VISIBLE);
                left_power_text.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(leftPower) && rightPower.equals("0.00")) {
                if (b) {
                    left_sale_price_tv.setText(price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                }
                right_power_text.setVisibility(View.VISIBLE);
                left_power_text.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(leftPower) && !rightPower.equals("0.00")) {
                if (b) {
                    left_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                }
                right_power_text.setVisibility(View.VISIBLE);
                left_power_text.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(rightPower) && leftPower.equals("0.00")) {
                if (b) {
                    left_sale_price_tv.setText(price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                }
                left_power_text.setVisibility(View.VISIBLE);
                right_power_text.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(rightPower) && !leftPower.equals("0.00")) {
                if (b) {
                    left_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                }
                left_power_text.setVisibility(View.VISIBLE);
                right_power_text.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);
            } else if (leftPower.equals("0.00") && !TextUtils.isEmpty(rightPower)) {
                if (b) {
                    left_sale_price_tv.setText(price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                    right_sale_price_tv.setText(with_power_price + " " + currency);
                    right_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(price + " " + currency);
                    right_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                    right_price_tv.setText(price + " " + currency);
                }
                left_power_text.setVisibility(View.VISIBLE);
                right_power_text.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
            } else if (rightPower.equals("0.00") && !TextUtils.isEmpty(leftPower)) {
                if (b) {
                    left_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                    right_sale_price_tv.setText(price + " " + currency);
                    right_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    right_sale_price_tv.setText(price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                    right_price_tv.setText(price + " " + currency);
                }

                left_power_text.setVisibility(View.VISIBLE);
                right_power_text.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
            } else if (!TextUtils.isEmpty(leftPower) && !TextUtils.isEmpty(rightPower)) {
                if (b) {
                    left_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                    right_sale_price_tv.setText(with_power_price + " " + currency);
                    right_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    right_sale_price_tv.setText(sale_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                    right_price_tv.setText(price + " " + currency);
                }

                left_power_text.setVisibility(View.VISIBLE);
                right_power_text.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
            }
        }
    }

    private void powerDoneSpecialPrice_copy(String sale_price, String with_power_price){
        // Toast.makeText(this, "hello" + with_power_price+special_price_with_power, Toast.LENGTH_SHORT).show();
        //left power
        if (leftPower.equals(rightPower)){
            if(leftPower.equals("0.00"))
            {
                left_sale_price_tv.setText(special_price + " " + currency);
                left_price_tv.setText(price + " " + currency);

                //left price = special_price, price
                // powerDoneSpecialPrice(special_price, price);
            }
            else
            {
                left_sale_price_tv.setText(special_price_with_power + " " + currency);
                left_price_tv.setText(with_power_price + " " + currency);

                // left price = special_price_with_power,  with_power_price
                // powerDoneSpecialPrice(special_price_with_power, with_power_price);

            }

            left_power_text.setVisibility(View.VISIBLE);
            right_power_text.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
            findViewById(R.id.left_right_divider).setVisibility(View.GONE);



        }else {

            if(leftPower.equals("0.00"))
            {
                left_sale_price_tv.setText(special_price + " " + currency);
                left_price_tv.setText(price + " " + currency);
                //left price = special_price, price
                // powerDoneSpecialPrice(special_price, price);
            }
            else
            {
                left_sale_price_tv.setText(special_price_with_power + " " + currency);
                left_price_tv.setText(with_power_price + " " + currency);
                // left price = special_price_with_power,  with_power_price
                // powerDoneSpecialPrice(special_price_with_power, with_power_price);

            }

            //right power
            if(rightPower.equals("0.00"))
            {
                right_sale_price_tv.setText(special_price + " " + currency);
                right_price_tv.setText(price + " " + currency);
                //right price = special_price, price
                //powerDoneSpecialPrice(special_price, price);

            }
            else
            {
                right_sale_price_tv.setText(special_price_with_power + " " + currency);
                right_price_tv.setText(with_power_price + " " + currency);
                // right price = special_price_with_power,  with_power_price
                // powerDoneSpecialPrice(special_price_with_power, with_power_price);

            }

        /*left_sale_price_tv.setText(sale_price + " " + currency);
        right_sale_price_tv.setText(sale_price + " " + currency);
        left_price_tv.setText(price + " " + currency);
        right_price_tv.setText(price + " " + currency);*/

            left_power_text.setVisibility(View.VISIBLE);
            right_power_text.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
            findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
            findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);

        }


    }

    private void speriphicalStep(){
        boolean leftOutOfStock = false, rightOutOfStock = false;
        for (int i = 0; i < leftPowerModelList.size(); i++) {

            if (leftPowerModelList.get(i).isSelected()) {
                if (leftPowerModelList.get(i).isOutOfStock()) {
                    leftOutOfStock = true;
                    break;
                } else {
                    leftPower = leftPowerModelList.get(i).getValue();
                    sessionManager.setLeftEyePower(leftPower);
                    break;
                }
            }
        }

        for (int i = 0; i < rightPowerModelList.size(); i++) {
            if (rightPowerModelList.get(i).isSelected()) {
                if (rightPowerModelList.get(i).isOutOfStock()) {
                    rightOutOfStock = true;
                    break;
                } else {
                    rightPower = rightPowerModelList.get(i).getValue();
                    sessionManager.setRightEyePower(rightPower);
                    break;
                }
            }
        }

        if (leftOutOfStock || rightOutOfStock) {
            CommanMethod.getCustomOkAlert(ProductDetailsActivity.this, getResources().getString(R.string.out_of_stock_small));
        } else {
            if (is_special_offer.equals("1")){
                //powerDone(special_price, special_price_with_power);
                powerDoneSpecialPrice(special_price, with_power_price);
            }else {
                powerDone(sale_price, with_power_price);
            }
           /* //uncomment code
            if (leftPower.equals("0.00") && rightPower.equals("0.00")) {
                if (sale_price.equals("0.00")
                        || sale_price.equals("0.000")
                        || sale_price.equals("0")) {
                    left_sale_price_tv.setText(price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                }
                right_power_text.setVisibility(View.VISIBLE);
                left_power_text.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);

            } else if (leftPower.equals(rightPower)) {
                if (sale_price.equals("0.00")
                        || sale_price.equals("0.000")
                        || sale_price.equals("0")) {
                    left_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                }
                right_power_text.setVisibility(View.VISIBLE);
                left_power_text.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(leftPower) && rightPower.equals("0.00")) {
                if (sale_price.equals("0.00")
                        || sale_price.equals("0.000")
                        || sale_price.equals("0")) {
                    left_sale_price_tv.setText(price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                    right_sale_price_tv.setText(with_power_price +" "+ currency);
                    right_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    right_sale_price_tv.setText(sale_price +" "+ currency);
                    left_price_tv.setText(price + " " + currency);
                    right_price_tv.setText(price +" "+ currency);
                }
                right_power_text.setVisibility(View.VISIBLE);
                left_power_text.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(leftPower) && !rightPower.equals("0.00")) {
                if (sale_price.equals("0.00")
                        || sale_price.equals("0.000")
                        || sale_price.equals("0")) {
                    left_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                    right_sale_price_tv.setText(with_power_price +" "+ currency);
                    right_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    right_sale_price_tv.setText(sale_price +" "+ currency);
                    left_price_tv.setText(price + " " + currency);
                    right_price_tv.setText(price +" "+ currency);
                }
                right_power_text.setVisibility(View.VISIBLE);
                left_power_text.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(rightPower) && leftPower.equals("0.00")) {
                if (sale_price.equals("0.00")
                        || sale_price.equals("0.000")
                        || sale_price.equals("0")) {
                    left_sale_price_tv.setText(price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                    right_sale_price_tv.setText(with_power_price +" "+ currency);
                    right_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    right_sale_price_tv.setText(sale_price +" "+ currency);
                    left_price_tv.setText(price + " " + currency);
                    right_price_tv.setText(price +" "+ currency);
                }
                left_power_text.setVisibility(View.VISIBLE);
                right_power_text.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(rightPower) && !leftPower.equals("0.00")) {
                if (sale_price.equals("0.00")
                        || sale_price.equals("0.000")
                        || sale_price.equals("0")) {
                    left_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                    right_sale_price_tv.setText(with_power_price +" "+ currency);
                    right_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    right_sale_price_tv.setText(sale_price +" "+ currency);
                    left_price_tv.setText(price + " " + currency);
                    right_price_tv.setText(price +" "+ currency);
                }
                left_power_text.setVisibility(View.VISIBLE);
                right_power_text.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                findViewById(R.id.left_right_divider).setVisibility(View.GONE);
            } else if (leftPower.equals("0.00") && !TextUtils.isEmpty(rightPower)) {
                if (sale_price.equals("0.00")
                        || sale_price.equals("0.000")
                        || sale_price.equals("0")) {
                    left_sale_price_tv.setText(price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                    right_sale_price_tv.setText(with_power_price + " " + currency);
                    right_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(price + " " + currency);
                    right_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                    right_price_tv.setText(price + " " + currency);
                }
                //right_power_text.setVisibility(View.GONE);
                //((TextView)findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                left_power_text.setVisibility(View.VISIBLE);
                right_power_text.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
            } else if (rightPower.equals("0.00") && !TextUtils.isEmpty(leftPower)) {
                if (sale_price.equals("0.00")
                        || sale_price.equals("0.000")
                        || sale_price.equals("0")) {
                    left_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                    right_sale_price_tv.setText(price + " " + currency);
                    right_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    right_sale_price_tv.setText(price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                    right_price_tv.setText(price + " " + currency);
                }

                left_power_text.setVisibility(View.VISIBLE);
                right_power_text.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
            } else if (!TextUtils.isEmpty(leftPower) && !TextUtils.isEmpty(rightPower)) {
                if (sale_price.equals("0.00")
                        || sale_price.equals("0.000")
                        || sale_price.equals("0")) {
                    left_sale_price_tv.setText(with_power_price + " " + currency);
                    left_price_tv.setVisibility(View.GONE);
                    right_sale_price_tv.setText(with_power_price + " " + currency);
                    right_price_tv.setVisibility(View.GONE);
                } else {
                    left_sale_price_tv.setText(sale_price + " " + currency);
                    right_sale_price_tv.setText(sale_price + " " + currency);
                    left_price_tv.setText(price + " " + currency);
                    right_price_tv.setText(price + " " + currency);
                }

                left_power_text.setVisibility(View.VISIBLE);
                right_power_text.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
                findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
            } //uncomment code*/

            ((TextView) findViewById(R.id.left_count_tv)).setText("1");
            ((TextView) findViewById(R.id.right_count_tv)).setText("1");
            singleLeftLensPrice = left_sale_price_tv.getText().toString().trim().split(" ")[0];
            singleRightLensPrice = right_sale_price_tv.getText().toString().trim().split(" ")[0];
            left_power_text.setText(getResources().getString(R.string.left_sph_power) + " " + leftPower);
            right_power_text.setText(getResources().getString(R.string.right_sph_power) + " " + rightPower);

        }
    }

    private void powerselectiontypeone(String power_selection_type, String left_eye_text, String right_eye_text, String leftCylPower, String rightCylPower, String leftAxisPower, String rightAxisPower, String leftAdditionPower, String rightAdditionPower) {
        //rightvisible();
        Log.d("powerselect",power_selection_type+left_eye_text+right_eye_text+leftCylPower+
                rightCylPower+leftAxisPower+rightAxisPower+leftAdditionPower+rightAdditionPower);

        if( ! (left_eye_text.equals(right_eye_text) )) {

            if( power_selection_type.equals("2")) {

                if(! leftCylPower.equals(rightCylPower) )
                {
                    //visible right
                    rightvisible();
                }
                else if(! leftAxisPower.equals(rightAxisPower))
                {
                    //visible right
                    rightvisible();
                }
            }

            else if( power_selection_type.equals("3"))
            {
                if(! leftAdditionPower.equals(rightAdditionPower))
                {
                    //visible right
                    rightvisible();
                }
            }
        }

    }

    private void rightvisible() {
        left_power_text.setVisibility(View.VISIBLE);
        right_power_text.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
        findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
        findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
    }

    public void updateAdditionLeft(int position, String from){
        if (from.equals("addition")){
            for (int i = 0; i< leftAdditionList.size(); i++){
                if (i==position){
                    if (leftAdditionList.get(position).isSelected()){
                        boolean rightSelected = false;
                        for (int j=0; j<leftAdditionList.size(); j++){
                            if (leftAdditionList.get(j).isSelected()){
                                rightSelected = true;
                                break;
                            }
                        }
                        if (rightSelected) {
                            leftAdditionList.get(position).setSelected(false);
                            left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog));
                            sessionManager.setLeftAddPower("");
                            leftAdditionPower = leftAxisList.get(position).getValue();
                        }

                    }else {
                        leftAdditionList.get(position).setSelected(true);
                        left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog)+": "+ leftAdditionList.get(position).getValue());
                        sessionManager.setLeftAddPower(leftAdditionList.get(position).getValue());
                        leftAdditionPower = leftAdditionList.get(position).getValue();
                    }
                }else {
                    leftAdditionList.get(i).setSelected(false);
                }

            }
            leftAdditionAdapter.notifyDataSetChanged();
        }else if(from.equals("cyl")){
            for (int i = 0; i< leftCylList.size(); i++){
                if (i==position){
                    if (leftCylList.get(position).isSelected()){
                        boolean rightSelected = false;
                        for (int j=0; j<leftCylList.size(); j++){
                            if (leftCylList.get(j).isSelected()){
                                rightSelected = true;
                                break;
                            }
                        }
                        if (rightSelected) {
                            leftCylList.get(position).setSelected(false);
                            left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog));
                            sessionManager.setLeftCylPower("");
                            leftCylPower = "";
                        }

                    }else {
                        leftCylList.get(position).setSelected(true);
                        left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog)+": "+ leftCylList.get(position).getValue());
                        sessionManager.setLeftCylPower(leftCylList.get(position).getValue());
                        leftCylPower = leftCylList.get(position).getValue();
                    }
                }else {
                    leftCylList.get(i).setSelected(false);
                }

            }
            leftCylAdapter.notifyDataSetChanged();
        }else if(from.equals("axis")){
            for (int i = 0; i< leftAxisList.size(); i++){
                if (i==position){
                    if (leftAxisList.get(position).isSelected()){
                        boolean rightSelected = false;
                        for (int j=0; j<leftAxisList.size(); j++){
                            if (leftAxisList.get(j).isSelected()){
                                rightSelected = true;
                                break;
                            }
                        }
                        if (rightSelected) {
                            leftAxisList.get(position).setSelected(false);
                            left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog));
                            sessionManager.setLeftAxisPower("");
                            leftAxisPower = "";
                        }

                    }else {
                        leftAxisList.get(position).setSelected(true);
                        left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog)+": "+ leftAxisList.get(position).getValue());
                        sessionManager.setLeftAxisPower(leftAxisList.get(position).getValue());
                        leftAxisPower = leftAxisList.get(position).getValue();
                    }
                }else {
                    leftAxisList.get(i).setSelected(false);
                }

            }
            leftAxisAdapter.notifyDataSetChanged();
        }

    }

    public void updateAdditionRight(int position, String from){
        if (from.equals("addition")){
            for (int i=0; i<rightAdditionList.size(); i++){
                if (i==position){
                    if (rightAdditionList.get(position).isSelected()){
                        boolean leftSelected = false;
                        for (int j = 0; j< rightAdditionList.size(); j++){
                            if (rightAdditionList.get(j).isSelected()){
                                leftSelected = true;
                                break;
                            }
                        }
                        if (leftSelected) {
                            rightAdditionList.get(position).setSelected(false);
                            right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog));
                            sessionManager.setRightAddPower("");
                            rightAdditionPower = "";
                        }

                    }else {
                        rightAdditionList.get(position).setSelected(true);
                        right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog)+": "+rightAdditionList.get(position).getValue());
                        sessionManager.setRightAddPower(rightAdditionList.get(position).getValue());
                        rightAdditionPower = rightAdditionList.get(position).getValue();
                    }
                }else {
                    rightAdditionList.get(i).setSelected(false);
                }

            }
            rightAdditionAdapter.notifyDataSetChanged();
        }else if(from.equals("cyl")){
            for (int i=0; i<rightCylList.size(); i++){
                if (i==position){
                    if (rightCylList.get(position).isSelected()){
                        boolean leftSelected = false;
                        for (int j = 0; j< rightCylList.size(); j++){
                            if (rightCylList.get(j).isSelected()){
                                leftSelected = true;
                                break;
                            }
                        }
                        if (leftSelected) {
                            rightCylList.get(position).setSelected(false);
                            right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog));
                            sessionManager.setRightAxisPower("");
                            rightCylPower = "";
                            rightCylPowerKey = "";
                        }

                    }else {
                        rightCylList.get(position).setSelected(true);
                        right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog)+": "+rightCylList.get(position).getValue());
                        sessionManager.setRightCylPower(rightCylList.get(position).getValue());
                        rightCylPower = rightCylList.get(position).getValue();
                        rightCylPowerKey = rightCylList.get(position).getTag();
                    }
                }else {
                    rightCylList.get(i).setSelected(false);
                }

            }
            rightCylAdapter.notifyDataSetChanged();
        }else if(from.equals("axis")){
            for (int i=0; i<rightAxisList.size(); i++){
                if (i==position){
                    if (rightAxisList.get(position).isSelected()){
                        boolean leftSelected = false;
                        for (int j = 0; j< rightAxisList.size(); j++){
                            if (rightAxisList.get(j).isSelected()){
                                leftSelected = true;
                                break;
                            }
                        }
                        if (leftSelected) {
                            rightAxisList.get(position).setSelected(false);
                            right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog));
                            sessionManager.setRightAxisPower("");
                            rightAxisPower = "";
                        }

                    }else {
                        rightAxisList.get(position).setSelected(true);
                        right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog)+": "+rightAxisList.get(position).getValue());
                        sessionManager.setRightAxisPower(rightAxisList.get(position).getValue());
                        rightAxisPower = rightAxisList.get(position).getValue();
                    }
                }else {
                    rightAxisList.get(i).setSelected(false);
                }

            }
            rightAxisAdapter.notifyDataSetChanged();
        }

    }

    public void updatePowerSelectionLeft(int position){

        for (int i = 0; i< leftPowerModelList.size(); i++){
            if (i==position){
                if (leftPowerModelList.get(position).isSelected()){
                    boolean rightSelected = false;
                    for (int j=0; j<rightPowerModelList.size(); j++){
                        if (rightPowerModelList.get(j).isSelected()){
                            rightSelected = true;
                            break;
                        }
                    }
                    if (rightSelected) {
                        leftPowerModelList.get(position).setSelected(false);
                        left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog));
                        sessionManager.setLeftEyePower("");
                    }

                    //System.out.println("get Value1" +powerList.get(position).getValue());
                    //leftPower="0.00";

                }else {
                    leftPowerModelList.get(position).setSelected(true);
                    //System.out.println("get Value2" +powerList.get(position).getValue());
                    //leftPower = powerList.get(position).getValue();
                    left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog)+": "+ leftPowerModelList.get(position).getValue());
                    sessionManager.setLeftEyePower(leftPowerModelList.get(position).getValue());
                }
            }else {
                leftPowerModelList.get(i).setSelected(false);
            }

        }
        leftPowerOneAdapter.notifyDataSetChanged();
    }

    public void updatepowerSelectionRight(int position){

        for (int i=0; i<rightPowerModelList.size(); i++){
            if (i==position){
                if (rightPowerModelList.get(position).isSelected()){
                    boolean leftSelected = false;
                    for (int j = 0; j< leftPowerModelList.size(); j++){
                        if (leftPowerModelList.get(j).isSelected()){
                            leftSelected = true;
                            break;
                        }
                    }
                    if (leftSelected) {
                        rightPowerModelList.get(position).setSelected(false);
                        right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog));
                        sessionManager.setRightEyePower("");
                    }
                    //rightPower="0.00";

                }else {
                    rightPowerModelList.get(position).setSelected(true);
                    //rightPower = rightPowerModelList.get(position).getValue();
                    right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog)+": "+rightPowerModelList.get(position).getValue());
                    sessionManager.setRightEyePower(rightPowerModelList.get(position).getValue());
                }
            }else {
                rightPowerModelList.get(i).setSelected(false);
            }

        }
        rightPowerOneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)){
            CommanClass.getCartValue(this, number);
        }
    }

    @Override
    public void onBackPressed(){
        sessionManager.setLeftEyePower("");
        sessionManager.setRightEyePower("");
        super.onBackPressed();
    }

    @Override
    public void refreshList() {

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / getResources().getDimension(R.dimen._160sdp));
        findViewById(R.id.relativeLayout).setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(findViewById(R.id.relativeLayout), scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void getAxisPowerData(RecyclerView leftRecyclerView, RecyclerView rightRecyclerView){
        Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();

        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"family_axis", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    //{"response":{"axis":{"title":"Axis List","axis_list":{"col1":["30","70","110","150","10"],
                    // "col2":["150"],
                    // "col3":["30","70","110","150","10"],
                    // "col4":["100"],
                    // "col5":["30","40","50","60","70","80","90","100","110","120","130","140","150","160","170",
                    // "180","20"]}}},"status":"success","message":"Record fetched successfully","message_ar":"تم جلب السجل بنجاح"}
                    if(status.equals("success")){
                        leftAxisList = new ArrayList<>();
                        rightAxisList = new ArrayList<>();
                        JSONObject responseObject = jsonObject.getJSONObject("response");
                        JSONObject axisObject1 = responseObject.getJSONObject("axis");
                        JSONObject axisObject = axisObject1.getJSONObject("axis_list");
                        if(!TextUtils.isEmpty(leftCylPowerKey)) {
                            JSONArray leftPowerArray = axisObject.getJSONArray(leftCylPowerKey);
                            for (int i = 0; i < leftPowerArray.length(); i++) {
                                String power = leftPowerArray.getString(i);
                                PowerModel leftModel = new PowerModel();
                                leftModel.setValue(power);
                                leftAxisList.add(leftModel);
                            }
                        }
                        if(!TextUtils.isEmpty(rightCylPowerKey)) {
                            JSONArray rightPowerArray = axisObject.getJSONArray(rightCylPowerKey);
                            for (int i = 0; i < rightPowerArray.length(); i++) {
                                String power = rightPowerArray.getString(i);
                                PowerModel rightModel = new PowerModel();
                                rightModel.setValue(power);
                                rightAxisList.add(rightModel);
                            }
                        }

                        leftRecyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
                        leftAxisAdapter = new LeftPowerAdditionAdapter(ProductDetailsActivity.this, leftAxisList, "axis");
                        leftRecyclerView.setAdapter(leftAxisAdapter);

                        rightRecyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
                        rightAxisAdapter = new RightPowerAdditionAdapter(ProductDetailsActivity.this,rightAxisList, "axis");
                        rightRecyclerView.setAdapter(rightAxisAdapter);

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
                params.put("product_id",product_id);
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
