package com.lenzzo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lenzzo.adapter.BannerListAdapter;
import com.lenzzo.adapter.CategoryBrandListAdapter;
import com.lenzzo.adapter.ExpandableListAdapter;
import com.lenzzo.adapter.ImageSliderAdapter;
import com.lenzzo.api.API;
import com.lenzzo.fragment.CategoryFragment;
import com.lenzzo.fragment.ContactUsFragment;
import com.lenzzo.fragment.MyAccountFragment;
import com.lenzzo.fragment.NotificationsFragment;
import com.lenzzo.fragment.OffersFragment;
import com.lenzzo.fragment.ProductListFragment;
import com.lenzzo.fragment.WishListFragment;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.localization.LocaleManager;
import com.lenzzo.model.BannerList;
import com.lenzzo.model.BrandList;
import com.lenzzo.model.CategoryBrandList;
import com.lenzzo.model.ImageSliderList;
import com.lenzzo.model.ProductList;
import com.lenzzo.model.ProductSearchModel;
import com.lenzzo.updatedVersionChecker.GooglePlayStoreAppVersionNameLoader;
import com.lenzzo.updatedVersionChecker.VersionCheckListner;
import com.lenzzo.utility.CommanClass;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.CustomVolleyRequest;
import com.lenzzo.utility.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.lenzzo.utility.SortFilterSessionManager;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.annotation.NonNull;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navView;
    private DrawerLayout drawer;
    //private ActionBarDrawerToggle toggle;
    private static ExpandableListAdapter adapter;
    private static ExpandableListView expandableListView;
    private List<ImageSliderList> imageSliderLists;
    private ImageSliderAdapter imageSliderAdapter;
    private ViewPager slide_viewPager;
    private CirclePageIndicator circlePageIndicator;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private List<BrandList> brandLists;
    private List<CategoryBrandList> categoryBrandLists;
    private RecyclerView categoryBrandLists_recycler;
    private CategoryBrandListAdapter categoryBrandListAdapter;
    private BannerListAdapter bannerListAdapter;
    private List<BannerList> bannerLists;
    private TextView sign_button;
    private TextView user_name_textview;
    private ArrayList<String> header;
    private SessionManager sessionManager;
    int a=1;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView title_text_view;
    private ImageView logo_image;
    private int total_value;
    private String total_count="";
    private ImageView cart_image;
    private TextView number;
    private ImageView filter_image;
    private ImageView search_image;
    private Dialog dialog;
    private List<ProductSearchModel> productLists;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private String search_text="", whatsapp_number = "";
    LinearLayout linearLayout;
    private ImageLoader imageLoader;
    private AutoCompleteTextView searchView;


    public void replaceFragment(Fragment fragment){
        getSupportActionBar().hide();
        fragmentManager = getSupportFragmentManager();

        fragmentManager.popBackStack (fragment.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManager = new SessionManager(this);


        if ("ar".equals(sessionManager.getLanguageSelected())) {
            LocaleManager.setNewLocale(this, "ar");
        } else {
            LocaleManager.setNewLocale(this, "en");
        }


        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportActionBar().show();
                        int count = getSupportFragmentManager().getBackStackEntryCount();
                        if (count!=0) {
                            //logo_image.setVisibility(View.VISIBLE);
                            //title_text_view.setVisibility(View.GONE);
                            //fragmentManager.popBackStackImmediate();
                            if (fragmentManager != null) {
                                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                        }
                        if (CommanMethod.isInternetConnected(HomeActivity.this)){
                            sendAndRequestResponse();
                            CommanClass.getCartValue(HomeActivity.this, number);
                            //searchProduct();
                        }
                        return true;
                    case R.id.navigation_favourite:
                        //Intent intent = new Intent(HomeActivity.this,WishListActivity.class);
                        //startActivity(intent);
                    /*WishListFragment wishListFragment = new WishListFragment();
                    replaceFragment(wishListFragment);*/

                        ProductListFragment wishListFragment = new ProductListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("from","wish");
                        wishListFragment.setArguments(bundle);
                        replaceFragment(wishListFragment);

                        return true;
                    case R.id.navigation_offers:
                        OffersFragment offersFragment = new OffersFragment();
                        replaceFragment(offersFragment);
                        return true;

                    case R.id.navigation_my_account:
                        if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                            MyAccountFragment myAccountFragment = new MyAccountFragment();
                            replaceFragment(myAccountFragment);
                        }else{
                            Intent intent1 = new Intent(HomeActivity.this,LoginActivity.class);
                            intent1.putExtra("from", "home");
                            startActivity(intent1);
                        }
                        return true;

                    case R.id.navigation_contact_us:
                        //logo_image.setVisibility(View.GONE);
                        //title_text_view.setVisibility(View.VISIBLE);
                        ContactUsFragment contactUsFragment = new ContactUsFragment();
                        replaceFragment(contactUsFragment);
                        return true;
                }
                return false;
            }
        };

        user_name_textview = (TextView)findViewById(R.id.user_name_textview);
        user_name_textview.setVisibility(View.GONE);
        title_text_view = (TextView)findViewById(R.id.title_text_view);
        logo_image = (ImageView)findViewById(R.id.logo_image);
        cart_image = (ImageView)findViewById(R.id.cart_image);
        number = (TextView)findViewById(R.id.number);
        filter_image = (ImageView)findViewById(R.id.filter_image);
        search_image = (ImageView)findViewById(R.id.search_image);
        linearLayout=(LinearLayout)findViewById(R.id.liner);

        /*FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, findViewById(R.id.nav_view).getHeight());
        findViewById(R.id.whataapp_iv).setLayoutParams(params);*/

        sign_button = (TextView)findViewById(R.id.sign_button);
        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                intent.putExtra("from", "home");
                startActivity(intent);
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        findViewById(R.id.powered_by_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.kdakw.com"));
                startActivity(browserIntent);
            }
        });

        if(sessionManager.getUserType().equals("Guest")){
            user_name_textview.setVisibility(View.VISIBLE);
            user_name_textview.setText(getResources().getString(R.string.hello)+" "+getResources().getString(R.string.guest_user));
            sign_button.setVisibility(View.GONE);

        }else if(!sessionManager.getUserEmail().isEmpty() &&
                !sessionManager.getUserPassword().isEmpty()){
            user_name_textview.setVisibility(View.VISIBLE);
            user_name_textview.setText(getResources().getString(R.string.hello)+" "+CommanMethod.capitalize(sessionManager.getUserName()));
            sign_button.setVisibility(View.GONE);

        }else{
            user_name_textview.setVisibility(View.GONE);
            sign_button.setVisibility(View.VISIBLE);

            //navView.getMenu().removeItem(R.id.navigation_my_account); // hide for BottomNavigationView items
        }
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().getItem(0).setTitle(R.string.title_home);
        navView.getMenu().getItem(1).setTitle(R.string.navigation_favourite);
        navView.getMenu().getItem(2).setTitle(R.string.navigation_offers);
        navView.getMenu().getItem(3).setTitle(R.string.navigation_my_accout);
        navView.getMenu().getItem(4).setTitle(R.string.navigation_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().getDrawerArrowDrawable().
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        drawer = findViewById(R.id.drawer_layout);
        //toggle = new ActionBarDrawerToggle(this, drawer, toolbar, null, R.drawable.menu_60, R.drawable.menu_60);
        //toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //toggle.setDrawerArrowDrawable(new DrawerArrowDrawable(this));
        //toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        //drawer.addDrawerListener(toggle);

        //toggle.syncState();
        // OS ID: 4e0f80b1-131b-4f05-8610-8bee99b0469c
        //OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String userId = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

        findViewById(R.id.back_image).setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));
        if (sessionManager.getIsFromOfferNotification()){
            sessionManager.setIsFromOfferNotification(false);

            /*if (CommanMethod.isInternetConnected(this)){
                getOfferList(getIntent().getStringExtra("offer_id"));
            }*/

            Bundle bundle = new Bundle();
            bundle.putString("brand_id", sessionManager.getPushBrandId());
            bundle.putString("brand_name", "");
            bundle.putString("family_id", sessionManager.getPushFamilyId());
            bundle.putString("from", "");
            ProductListFragment productListFragment = new ProductListFragment();
            productListFragment.setArguments(bundle);
            replaceFragment(productListFragment);

            //navView.setSelectedItemId(R.id.navigation_offers);

        }

        expandableListView = (ExpandableListView) findViewById(R.id.simple_expandable_listview);
        expandableListView.setGroupIndicator(null);
        setItems();
        setListener();

        slide_viewPager = (ViewPager)findViewById(R.id.slide_viewPager);
        circlePageIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        //circlePageIndicator.setPadding(5,5,5,5);
        if (CommanMethod.isInternetConnected(HomeActivity.this)){
            sendAndRequestResponse();
            CommanClass.getCartValue(this, number);
            //searchProduct();
        }

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    slide_viewPager.setCurrentItem(currentPage++, true);
                }else{
                    if (currentPage == 0) {
                        currentPage = NUM_PAGES;
                        slide_viewPager.setCurrentItem(0, true);
                    }else {
                        slide_viewPager.setCurrentItem(currentPage--, true);
                    }
                }
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 4000);

        slide_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){ // if you want the second page, for example
                    //Your code here
                }
                currentPage = position;
                //Toast.makeText(HomeActivity.this, "Position="+position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        categoryBrandLists_recycler = (RecyclerView)findViewById(R.id.categoryBrandLists_recycler);
        categoryBrandLists_recycler.setNestedScrollingEnabled(false);

        cart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,UserCartActivity.class);
                startActivity(intent);
            }
        });

        filter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(HomeActivity.this,FilterActivity.class);
                startActivity(intent1);
            }
        });

        search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortDialog();
            }
        });
        System.out.println("Home lan "+ Locale.getDefault().getLanguage());
        if(this.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            linearLayout.setGravity(Gravity.END);
            //findViewById(R.id.whataapp_iv).setScaleX(1f);
            //((ImageView)findViewById(R.id.whataapp_iv)).setImageResource(R.drawable.ic_whatsapp);
        }else{
            linearLayout.setGravity(Gravity.START);
            //findViewById(R.id.whataapp_iv).setScaleX(-1f);
            //((ImageView)findViewById(R.id.whataapp_iv)).setImageResource(R.drawable.ic_ar_whatsapp);
            /*int parentHeight = (int)getResources().getDimension(R.dimen._100sdp);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    parentHeight
            );
            int leftMargin = (int)getResources().getDimension(R.dimen._5sdp);
            params.setMargins(leftMargin, 0, 0, 0);
            ((ImageView)findViewById(R.id.whataapp_iv)).setLayoutParams(params);*/
            int leftMargin = (int)getResources().getDimension(R.dimen._5sdp);
            ((FrameLayout)findViewById(R.id.menu_lay)).setPadding(leftMargin, 0,0,0);
        }

        findViewById(R.id.whataapp_iv).setOnClickListener(view -> {
            if (TextUtils.isEmpty(whatsapp_number)){
                if (CommanMethod.isInternetConnected(this)){
                    getSupportDetails();
                }
            }else {
                openWhatsApp();
            }
        });

       // checkForUpdate();

        new GooglePlayStoreAppVersionNameLoader(this, new VersionCheckListner() {
            @Override
            public void onGetResponse(boolean isUpdateAvailable) {
                if (isUpdateAvailable){
                    getCustomOkAlert();
                }
            }
        }).execute();
    }

    private void getSupportDetails(){
        //gifImageView.setVisibility(View.VISIBLE);
        Dialog dialog = CommanMethod.getProgressDialogForFragment(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"contact_us", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("cms_detail_Array"));
                        JSONObject jsonObject12 = new JSONObject(jsonObject1.getString("admin_settings"));

                        whatsapp_number = jsonObject12.getString("whatsup_number");
                        /*order_email.setText(jsonObject12.getString("order_email"));
                        get_order_email = jsonObject12.getString("order_email");
                        support_email.setText(jsonObject12.getString("support_email"));
                        get_support_email = jsonObject12.getString("support_email");
                        support_number_tv.setText(jsonObject12.getString("support_number"));
                        support_number = jsonObject12.getString("support_number");*/
                        openWhatsApp();
                    }else if(status.equals("failed")){
                        // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
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

    public void openWhatsApp(){

        PackageManager packageManager = getPackageManager();

        //Intent i = new Intent(Intent.ACTION_VIEW);
        try {
           /* String url = "https://api.whatsapp.com/send?phone="+ whatsapp_number +"&text=" + URLEncoder.encode("", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }*/

            String url = "https://api.whatsapp.com/send?phone="+whatsapp_number+"&text=" + URLEncoder.encode("", "UTF-8");
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, getResources().getString(R.string.whatsapp_not_found), Toast.LENGTH_LONG).show();
        }
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
                if (CommanMethod.isInternetConnected(HomeActivity.this)){
                    if(search_text.length()>=3){
                        searchProduct(search_text);
                    }
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

                /*Intent intent = new Intent(HomeActivity.this,SearchResultsActivity.class);
                intent.putExtra("array_of_product_lists",array_of_product_lists);
                startActivity(intent);*/
                Intent intent = new Intent(HomeActivity.this,ProductDetailsActivity.class);
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
                    Intent intent = new Intent(HomeActivity.this,SearchResultsActivity.class);
                    intent.putExtra("array_of_product_lists",array_of_product_lists);
                    intent.putExtra("search_string", search_text);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setItems() {

        // Array list for header
        header = new ArrayList<String>();

        // Hash map for both header and child
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
        header.add(this.getString(R.string.home));
        header.add(this.getString(R.string.country));
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            header.add(this.getString(R.string.arabic));
        }else{
            header.add(this.getString(R.string.english));
        }

        header.add(this.getString(R.string.category_text));
        header.add(this.getString(R.string.term_and_condition));
        header.add(this.getString(R.string.about_us));
        header.add(this.getString(R.string.faq));
        header.add(this.getString(R.string.notifications));
        header.add(this.getString(R.string.rate));

        if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
            header.add(this.getString(R.string.sign_out));
            if (CommanMethod.isInternetConnected(this)){
                makeGuest();
            }
        }
        //header.add("Sign Out");
        adapter = new ExpandableListAdapter(HomeActivity.this, header, hashMap);
        expandableListView.setAdapter(adapter);
    }

    public void setListener() {
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(adapter.getGroupId(groupPosition)==0){
                    Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else if(adapter.getGroupId(groupPosition)==9){
                    //sessionManager.clearPreferences();
                    /*sessionManager.removeId("user_email","user_password");
                    Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
                    if (CommanMethod.isInternetConnected(HomeActivity.this)){
                        doLogout();
                    }
                }else if(adapter.getGroupId(groupPosition)==4){
                    Intent intent=new Intent(HomeActivity.this,TermsAndConditionsActivity.class);
                    startActivity(intent);
                }else if(adapter.getGroupId(groupPosition)==5){
                    Intent intent = new Intent(HomeActivity.this,AboutUsActivity.class);
                    startActivity(intent);
                }else if(adapter.getGroupId(groupPosition)==6){
                    Intent intent = new Intent(HomeActivity.this,FAQActivity.class);
                    startActivity(intent);
                }else if(adapter.getGroupId(groupPosition)==3){
                    /*Intent intent = new Intent(HomeActivity.this,CategoryActivity.class);
                    startActivity(intent);*/
                    CategoryFragment categoryFragment = new CategoryFragment();
                    replaceFragment(categoryFragment);
                }else if(adapter.getGroupId(groupPosition)==1){
                    Intent intent = new Intent(HomeActivity.this,CountryActivity.class);
                    startActivity(intent);
                }else if(adapter.getGroupId(groupPosition)==2){
                    //Intent intent = new Intent(HomeActivity.this,LanguageActivity.class);
                    //Intent intent = new Intent(HomeActivity.this,CountryActivity.class);
                    //startActivity(intent);
                    changeLanguageDialog();
                }else if(adapter.getGroupId(groupPosition)==7){
                    NotificationsFragment notificationsFragment = new NotificationsFragment();
                    replaceFragment(notificationsFragment);
                }else if(adapter.getGroupId(groupPosition)==8){
                    System.out.println("rate us");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.lenzzo"));
                    startActivity(intent);
                }

                //Toast.makeText(HomeActivity.this, "You clicked : " + adapter.getGroupId(groupPosition), Toast.LENGTH_SHORT).show();
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  private void changeLanguageDialog(){
          final Dialog dialog = new Dialog(this);
          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
          dialog.setCancelable(false);
          dialog.setContentView(R.layout.change_language_alert);
          LinearLayout liner_layout = (LinearLayout)dialog.findViewById(R.id.liner_layout);
          TextView change_language_yes = (TextView)dialog.findViewById(R.id.change_language_yes);
          TextView change_language_no = (TextView)dialog.findViewById(R.id.change_language_no);
          if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
              //liner_layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

          }else{
              //liner_layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

          }

          change_language_yes.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  dialog.dismiss();
                  if (CommanMethod.isInternetConnected(HomeActivity.this)) {
                      if (Locale.getDefault().getLanguage().equals("en")) {
                          setNewLocale(getLanguageNotation(1));
                      } else {
                          setNewLocale(getLanguageNotation(0));
                      }
                  }
              }
          });
         change_language_no.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  dialog.dismiss();
              }
          });
          dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
          dialog.show();
  }

    private void setNewLocale(String language) {

        LocaleManager.setNewLocale(this, language);
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.setLanguageSelected(language);

       /* finish();
        //startActivity(getIntent());
        *//*if ("ar".equals(sessionManager.getLanguageSelected())) {
            LocaleManager.setNewLocale(this, "ar");
        } else {
            LocaleManager.setNewLocale(this, "en");
        }*//*
        Intent refresh = new Intent(this, HomeActivity.class);
        startActivity(refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));*/

        changeLanguage();

    }

    public void changeLanguage(){
        Dialog dialog = CommanMethod.getProgressDialogForFragment(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"change_language", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        finish();
                        Intent refresh = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                    params.put("user_id", sessionManager.getUserId());
                }else{
                    params.put("user_id", sessionManager.getRandomValue());
                }
                params.put("current_language", Locale.getDefault().getLanguage());
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

    private void sendAndRequestResponse(){
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(this);
        dialog.show();

        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"home", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //gifImageView.setVisibility(View.GONE);
                    dialog.dismiss();
                    imageSliderLists = new ArrayList<>();
                    categoryBrandLists = new ArrayList<>();
                    bannerLists = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("success")){
                        JSONObject response1 =new JSONObject(object.getString("response"));

                        //imageLoader = CustomVolleyRequest.getInstance(HomeActivity.this).getImageLoader();
                        //imageLoader.get(API.BannerSliderURL+response1.getString("logo"),ImageLoader.getImageListener(logo_image, R.drawable.contact_us_icon,R.drawable.contact_us_icon ));

                        Picasso.get()
                                .load(API.BannerSliderURL+response1.getString("logo"))
                                .placeholder(R.drawable.no_img)
                                .error(R.drawable.no_img)
                                .into(logo_image);

                        JSONObject jsonObject = new JSONObject(response1.getString("slider_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("slider_list"));
                        for(int i=0;i<jsonArray.length();i++){
                            NUM_PAGES++;
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            ImageSliderList imageSliderList = new ImageSliderList();
                            imageSliderList.setId(jsonObject1.getString("id"));
                            imageSliderList.setName(jsonObject1.getString("name"));
                            imageSliderList.setPath(API.BannerSliderURL+jsonObject1.getString("path"));
                            imageSliderList.setType(jsonObject1.getString("type"));
                            imageSliderList.setSlug(jsonObject1.getString("slug"));
                            imageSliderList.setProduct_id(jsonObject1.getString("product_id"));//
                            imageSliderList.setProduct_title(jsonObject1.getString("product_title"));
                            imageSliderList.setCategory_id(jsonObject1.getString("category_id"));//
                            imageSliderList.setCategory_name(jsonObject1.getString("category_name"));
                            imageSliderList.setBrand_id(jsonObject1.getString("brand_id"));//
                            imageSliderList.setBrand_name(jsonObject1.getString("brand_name"));
                            imageSliderList.setOffer_id(jsonObject1.getString("offer_id"));//
                            imageSliderList.setOffer_name(jsonObject1.getString("offer_name"));
                            imageSliderList.setStatus(jsonObject1.getString("status"));
                            imageSliderLists.add(imageSliderList);
                        }

                        JSONObject category_brand = new JSONObject(response1.getString("category_brand_Array"));
                        JSONArray category_brand_Array = new JSONArray(category_brand.getString("category_brand_list"));
                        for(int i=0;i<category_brand_Array.length();i++){
                            brandLists = new ArrayList<>();
                            JSONObject category_jsonObject = category_brand_Array.getJSONObject(i);
                            CategoryBrandList categoryBrandList = new CategoryBrandList();
                            categoryBrandList.setCategory_id(category_jsonObject.getString("category_id"));
                            categoryBrandList.setCategoryName(category_jsonObject.getString("categoryName"));
                            categoryBrandList.setCategoryName_ar(category_jsonObject.getString("categoryName_ar"));
                            JSONArray brand_list = new JSONArray(category_jsonObject.getString("brand_list"));
                            for(int j=0;j<brand_list.length();j++){
                                JSONObject brand_jsonObject = brand_list.getJSONObject(j);
                                BrandList brandList = new BrandList();
                                brandList.setId(brand_jsonObject.getString("id"));
                                brandList.setName(brand_jsonObject.getString("name"));
                                brandList.setName_ar(brand_jsonObject.getString("name_ar"));
                                brandList.setSlug(brand_jsonObject.getString("slug"));
                                brandList.setIcon(brand_jsonObject.getString("icon"));
                                brandList.setCreated_at(brand_jsonObject.getString("created_at"));
                                brandList.setModified_at(brand_jsonObject.getString("updated_at"));
                                brandList.setStatus(brand_jsonObject.getString("status"));
                                brandList.setBrand_image(API.BrandURL+brand_jsonObject.getString("brand_image"));
                                brandList.setCategory_id(brand_jsonObject.getString("category_id"));
                                brandList.setUi_order(brand_jsonObject.getString("ui_order"));
                                brandList.setStart_range(brand_jsonObject.optString("start_range"));
                                brandList.setEnd_range(brand_jsonObject.optString("end_range"));
                                brandList.setOffer_id(brand_jsonObject.optString("offer_id"));
                                brandList.setOffer_name(brand_jsonObject.optString("offer_name"));
                                brandList.setOffer_subtitle(brand_jsonObject.optString("offer_subtitle"));
                                JSONArray child_jsonArray = new JSONArray(brand_jsonObject.optString("child"));
                                brandList.setJsonArray(child_jsonArray);
                                brandLists.add(brandList);
                            }
                            categoryBrandList.setCategoryBrandList(brandLists);
                            categoryBrandLists.add(categoryBrandList);
                        }

                        JSONObject banner = new JSONObject(response1.getString("banner_Array"));
                        JSONArray banner_list = new JSONArray(banner.getString("banner_list"));
                        for(int i=0;i<banner_list.length();i++){
                            JSONObject banner_item = banner_list.getJSONObject(i);
                            BannerList bannerList = new BannerList();
                            bannerList.setId(banner_item.getString("id"));
                            bannerList.setName(banner_item.getString("name"));
                            bannerList.setPath(API.BannerSliderURL+banner_item.getString("path"));
                            bannerList.setType(banner_item.getString("type"));
                            bannerList.setSlug(banner_item.getString("slug"));
                            bannerList.setProduct_id(banner_item.getString("product_id"));
                            bannerList.setProduct_title(banner_item.getString("product_title"));
                            bannerList.setCategory_id(banner_item.getString("category_id"));
                            bannerList.setCategory_name(banner_item.getString("category_name"));
                            bannerList.setBrand_id(banner_item.getString("brand_id"));
                            bannerList.setBrand_name(banner_item.getString("brand_name"));
                            bannerList.setOffer_id(banner_item.getString("offer_id"));
                            bannerList.setOffer_id(banner_item.getString("offer_name"));
                            bannerList.setStatus(banner_item.getString("status"));

                            bannerLists.add(bannerList);
                        }

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }
                imageSliderAdapter = new ImageSliderAdapter(imageSliderLists, HomeActivity.this);
                slide_viewPager.setAdapter(imageSliderAdapter);
                circlePageIndicator.setViewPager(slide_viewPager);
                if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                    slide_viewPager.setCurrentItem(0);
                }else{
                    slide_viewPager.setCurrentItem(imageSliderLists.size()-1);
                }

                GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this,1);
                categoryBrandLists_recycler.setLayoutManager(gridLayoutManager);
                categoryBrandListAdapter = new CategoryBrandListAdapter(HomeActivity.this,categoryBrandLists,bannerLists);
                categoryBrandLists_recycler.setAdapter(categoryBrandListAdapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
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

        CustomVolleyRequest.getInstance(this).addToRequestQueue(mStringRequest);
    }

    private String getRendomNumber(){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)));
        for (int i = 0; i < 6; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);
        return sb.toString();
    }

    @Override
    public void onBackPressed() {

        /*int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
                logo_image.setVisibility(View.VISIBLE);
                title_text_view.setVisibility(View.GONE);
                fragmentManager.popBackStackImmediate();
                }*/
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            getSupportActionBar().show();
            CommanClass.getCartValue(this, number);
            super.onBackPressed();
        }
        else {
            super.onBackPressed();
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
                        String totalcount = jsonObject1.optString("totalcount", "");
                        number.setText(totalcount);
                        /*if(jsonArray.length()> 0) {
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
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){
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

    private void searchProduct(String search_text){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"productlist_of_brand", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("searchResponse", response);
                    //{"response":{"product_list_Array":{"title":"Product List","product_count":1,"product_list":[{"id":"808","quantity":"951","sell_quantity":"49","user_id":"0","cate_id":"3","cate_name":"Colored Contact Lenses","title":"Sandy Gray Bella Elite","title_ar":"ساندي جراي بيلا إليت","description":"<p><strong>Brand : Bella<\/strong><\/p>\r\n\r\n<p><strong>Material:<\/strong>  Polymacon   <\/p>\r\n\r\n<p><strong>Diameter:<\/strong> 14.5 mm <\/p>\r\n\r\n<p><strong>Water content:<\/strong> 38% <\/p>\r\n\r\n<p><strong>Base Curve:<\/strong> 8.6<\/p>\r\n\r\n<p><strong>Box contain:<\/strong> 2 lens<\/p>\r\n\r\n<p><strong>Replacement Schedule<\/strong><strong>: <\/strong>Reusable Monthly up to 3 month<\/p>","description_ar":"<p><strong>العلامة التجارية: بيلا<\/strong><\/p>\r\n\r\n<p><strong>المادة<\/strong>:&nbsp; Polymacon&nbsp;<\/p>\r\n\r\n<p><strong>القطر<\/strong>:&nbsp;14.5&nbsp;مم<\/p>\r\n\r\n<p><strong>نسبة الماء:<\/strong>&nbsp;38%&nbsp;<\/p>\r\n\r\n<p><strong>انحناء العدسة:<\/strong>&nbsp;8.6<\/p>\r\n\r\n<p><strong>العلبة<\/strong>:&nbsp;2 عدسة<\/p>\r\n\r\n<p><strong>الإستخدام<\/strong>: شهرية لمدة 3 شهور&nbsp;<\/p>\r\n","product_image":"product_1604990463.jpg","product_images":"pro_gallery_1604990463.jpg","model_no":"5336","sku_code":null,"price":"13.000","current_currency":"KWD","with_power_price":"15.000","negotiable":"Yes","brand_name":"Bella Contact lenses","brand_id":"22","family_name":"Bella Elite","family_id":"24","variations":null,"variation_color":"-gray-","tags":"","is_hide":"Yes","reviewed":"309","featured":"0","archived":"0","deleted_at":"0","created_at":"2019-12-05 19:11:28","updated_at":"2020-12-16 07:56:07","status":"1","stock_flag":"1","rating":"0","replacement":"3-month","releted_product":"803,1116,843","l_p_n_available":"-0.25","r_p_n_available":"-0.25","offer":{"id":"83","name":"2+1 Free","offer_subtitle":"2+1 Free","name_ar":"2+1 Free","offer_subtitle_ar":"2+1 Free","buy":"2","get_type":"0","free":"1","discount_type":"","discount":"0","offer_type_id":"1","category_id":null,"brand_id":"22","family_id":"1,2,3,23,24,26,49,108","product_id":null,"start_date":"2020-11-15","end_date":"2021-07-21","status":"1","created_at":"2020-04-29 23:18:24","updated_at":"2021-01-07 08:14:20","image":"brand1605414477.jpg"},"offer_id":"83","offer_name":"2+1 Free","power":{"id":"22","name":"Bella Contact lenses","name_ar":"عدسات لاصقة بيلا","slug":"bella-contact-lenses","ui_order":"1","icon":"","created_at":"2019-08-23 05:32:25","updated_at":"2020-07-26 06:36:04","status":"1","brand_image":"brand0.171604001595734564.jpg","category_id":"3","start_range":"-10","end_range":"+0","diff_range":"0.25","range_above":"6","diff_range_above":"0.50","brand_slider_images":"pro_gallery_1576073868_0.jpg,pro_gallery_1576074117_0.jpg,pro_gallery_1576074117_1.jpg","brand_slider_images_order":"2,3","offer_collection":null}}]}},"status":"success","message":"Record fetched successfully","message_ar":"تم جلب السجل بنجاح"}
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
                            productList.setTitle(CommanMethod.getTitle(HomeActivity.this, jsonObject2));

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

                }
                searchadapter= new ArrayAdapter<ProductSearchModel>(HomeActivity.this,android.R.layout.simple_list_item_1, productLists);
                searchView.setAdapter(searchadapter);
                searchView.showDropDown();

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


    public String getLanguageNotation(int position){
        switch (position){
            case 0:
                return LocaleManager.LANGUAGE_ENGLISH;
            case 1:
                return LocaleManager.LANGUAGE_ARABIC;
            default:
                return LocaleManager.LANGUAGE_ENGLISH;

        }
    }

    private void makeGuest(){
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"user_logs",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //gifImageView.setVisibility(View.GONE);
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if(status.equals("success")){
                                String message = CommanMethod.getMessage(HomeActivity.this, object);
                                //Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                                //CommanMethod.getCustomOkAlert(HomeActivity.this, message);
                            }else if(status.equals("failed")){
                                String message = CommanMethod.getMessage(HomeActivity.this, object);
                                //Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                                //CommanMethod.getCustomOkAlert(HomeActivity.this, message);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            //gifImageView.setVisibility(View.GONE);
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams(){

                Map<String, String>  params = new HashMap<String, String>();
                /*if(FirebaseInstanceId.getInstance().getToken() != null) {
                    params.put("device_id", FirebaseInstanceId.getInstance().getToken());
                }*/
                params.put("guestid", sessionManager.getRandomValue());
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

    private void doLogout(){
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if(status.equals("success")){
                                SortFilterSessionManager sortFilterSessionManager = new SortFilterSessionManager(HomeActivity.this);
                                //JSONObject jsonObject=new JSONObject(object.getString("response"));
                                //sessionManager.removeId("user_email","user_password");
                                sessionManager.clearSelectedPreferences();
                                sortFilterSessionManager.clearSelectedFilter();
                                if (TextUtils.isEmpty(sessionManager.getRandomValue())){
                                    sessionManager.setRandomValue(CommanMethod.getRandomNumber());
                                }
                                Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else if(status.equals("failed")){
                                String message = CommanMethod.getMessage(HomeActivity.this, object);
                                CommanMethod.getCustomOkAlert(HomeActivity.this, message);
                                //Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams(){

                Map<String, String>  params = new HashMap<String, String>();
                /*if(FirebaseInstanceId.getInstance().getToken() != null) {
                    params.put("device_id", FirebaseInstanceId.getInstance().getToken());
                }*/
                params.put("user_id", sessionManager.getUserId());
                params.put("email", sessionManager.getUserEmail());
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
        if (CommanMethod.isInternetConnected(HomeActivity.this)){
            CommanClass.getCartValue(this, number);
        }


        boolean isSignOut = true;

        if(sessionManager.getUserType().equals("Guest")){
            user_name_textview.setVisibility(View.VISIBLE);
            user_name_textview.setText(getResources().getString(R.string.hello)+" "+getResources().getString(R.string.guest_user));
            sign_button.setVisibility(View.GONE);

            for (int i=0; i<header.size(); i++) {
                if (header.get(i).equals(getResources().getString(R.string.sign_out))){
                    isSignOut = false;
                    break;
                }

            }

            if (isSignOut) {
                header.add(this.getString(R.string.sign_out));
                adapter.notifyDataSetChanged();
            }

        }else if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
            user_name_textview.setVisibility(View.VISIBLE);
            user_name_textview.setText(getResources().getString(R.string.hello)+" "+CommanMethod.capitalize(sessionManager.getUserName()));
            sign_button.setVisibility(View.GONE);
            for (int i=0; i<header.size(); i++) {
                if (header.get(i).equals(getResources().getString(R.string.sign_out))){
                    isSignOut = false;
                    break;
                }
            }

            if (isSignOut) {
                header.add(this.getString(R.string.sign_out));
                adapter.notifyDataSetChanged();
            }

        }else{
            user_name_textview.setVisibility(View.GONE);
            sign_button.setVisibility(View.VISIBLE);
            //navView.getMenu().removeItem(R.id.navigation_my_account);//hide for BottomNavigationView items
        }
    }

    private void getOfferList(final String offerId){
       /* final Dialog dialog = CommanMethod.getProgressDialogForFragment(this);
        dialog.show();*/
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"offer_brandlist_of_category", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //dialog.dismiss();
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
                                replaceFragment(productListFragment);
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
                //dialog.dismiss();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(sessionManager.getUserType().equals("Guest")){
            user_name_textview.setVisibility(View.VISIBLE);
            user_name_textview.setText(getResources().getString(R.string.hello)+" "+getResources().getString(R.string.guest_user));
            sign_button.setVisibility(View.GONE);

        }else if(!sessionManager.getUserEmail().isEmpty() &&
                !sessionManager.getUserPassword().isEmpty()){
            user_name_textview.setVisibility(View.VISIBLE);
            user_name_textview.setText(getResources().getString(R.string.hello)+" "+CommanMethod.capitalize(sessionManager.getUserName()));
            sign_button.setVisibility(View.GONE);

        }else{
            user_name_textview.setVisibility(View.GONE);
            sign_button.setVisibility(View.VISIBLE);

            //navView.getMenu().removeItem(R.id.navigation_my_account);//hide for BottomNavigationView items
        }
    }

    void checkForUpdate(){
        // Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks whether the platform allows the specified type of update,
        // and checks the update priority.
        /*Update Priority: 0 to 5*/
        int LOW_PRIORITY_UPDATE = 0;
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            //AppUpdateInfo{packageName=com.lenzzo, availableVersionCode=16, updateAvailability=1, installStatus=0, clientVersionStalenessDays=null, updatePriority=0, bytesDownloaded=0, totalBytesToDownload=0, immediateUpdateIntent=null, flexibleUpdateIntent=null}
            /*if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.updatePriority() >= LOW_PRIORITY_UPDATE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                getCustomOkAlert();
            }*/

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                getCustomOkAlert();
            }
            /*if (appUpdateInfo.updatePriority() >= LOW_PRIORITY_UPDATE) {
                getCustomOkAlert();

            }*/
        });
    }


    public void getCustomOkAlert(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.play_store_update_info_layout);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);

      /*  TextView not_now_tv = dialog.findViewById(R.id.not_now_tv);

        not_now_tv.setOnClickListener(view ->  {

            dialog.dismiss();
        });*/

        ok_tv.setOnClickListener(view ->  {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
            dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }


}
