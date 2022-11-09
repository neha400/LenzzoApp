package com.lenzzo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.api.API;
import com.lenzzo.api.APIService;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.localization.LocaleManager;
import com.lenzzo.model.CountryList;
import com.lenzzo.model.ProductList;
import com.lenzzo.model.ProductSearchModel;
import com.lenzzo.model.UserLogin;
import com.lenzzo.utility.CommanClass;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.SessionManager;
import com.lenzzo.utility.Utils;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back_image;
    private Button login_account;
    private EditText name_edittext;
    private EditText email_edittext;
    private AutoCompleteTextView number_code_edittext;
    private EditText number_edittext;
    private EditText password_edittext;
    private EditText confirm_password_edittext;
    private ImageView female_icon;
    //private TextView female_textview;
    //private TextView male_textview;
    private ImageView male_icon;
    private Button submit_button;
    private Button reset_button;
    private String getName;
    private String getEmail;
    private String getCountryCode;
    private String getNumber;
    private String getPassword;
    private String getConfirmPassword;
    private String getFemaleGender;
    private String getMaleGender;
    private String selectGender="";
    private ArrayList<CountryList> countryLists;
    private ArrayAdapter<CountryList> countryAdapter;
    private GifImageView gifImageView;
    private LinearLayout liner;
    private ImageView cart_image;
    private TextView number;
    private int total_value;
    private String total_count="";
    private Dialog dialog;
    private List<ProductSearchModel> productLists;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private String search_text="";
    private ImageView search_image;
    private AutoCompleteTextView searchView;
    private SessionManager sessionManager;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sessionManager = new SessionManager(this);
        back_image = (ImageView)findViewById(R.id.back_image);
        findViewById(R.id.back_image).setOnClickListener(this);
        login_account = (Button) findViewById(R.id.login_account);
        findViewById(R.id.login_account).setOnClickListener(this);
        name_edittext = (EditText)findViewById(R.id.name_edittext);
        email_edittext = (EditText)findViewById(R.id.email_edittext);
        number_code_edittext = (AutoCompleteTextView)findViewById(R.id.number_code_edittext);
        number_edittext = (EditText)findViewById(R.id.number_edittext);
        password_edittext = (EditText)findViewById(R.id.password_edittext);
        confirm_password_edittext = (EditText)findViewById(R.id.confirm_password_edittext);
        female_icon = (ImageView)findViewById(R.id.female_icon);
        female_icon.setVisibility(View.GONE);

        if(sessionManager.getUserType().equals("Guest")){
            email_edittext.setText(sessionManager.getUserEmail());
        }
        //female_textview = (TextView)findViewById(R.id.female_textview);
        findViewById(R.id.female_lay).setOnClickListener(this);
        //male_textview = (TextView)findViewById(R.id.male_textview);
        findViewById(R.id.male_lay).setOnClickListener(this);
        male_icon = (ImageView)findViewById(R.id.male_icon);
        male_icon.setVisibility(View.GONE);
        submit_button = (Button)findViewById(R.id.submit_button);
        findViewById(R.id.submit_button).setOnClickListener(this);
        reset_button = (Button)findViewById(R.id.reset_button);
        findViewById(R.id.reset_button).setOnClickListener(this);
        cart_image = (ImageView)findViewById(R.id.cart_image);
        findViewById(R.id.cart_image).setOnClickListener(this);
        number = (TextView)findViewById(R.id.number);
        findViewById(R.id.search_image).setOnClickListener(this);
        findViewById(R.id.filter_image).setOnClickListener(this);

        liner = (LinearLayout)findViewById(R.id.liner);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }
        if (CommanMethod.isInternetConnected(RegisterActivity.this)){
            getCountryList();
            CommanClass.getCartValue(this, number);
        }

        number_code_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_code_edittext.showDropDown();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom_countr_list, null);
                alertDialog.setNegativeButton(getString(R.string.cancle),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.listView);
                final AlertDialog alert = alertDialog.create();
                alert.setTitle(getString(R.string.dialog_select_country));
                countryAdapter = new ArrayAdapter<CountryList>(RegisterActivity.this, android.R.layout.simple_list_item_1, countryLists);
               // countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lv.setAdapter(countryAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        CountryList countryList = (CountryList) arg0.getItemAtPosition(position);
                        number_code_edittext.setText(countryList.getCode());
                        alert.cancel();
                    }
                });
                alert.show();
            }
        });

        gifImageView = (GifImageView)findViewById(R.id.gifImageView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.login_account:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                intent.putExtra("from", "register");
                startActivity(intent);
                break;
            case R.id.submit_button:
                validation();
                break;
            case R.id.reset_button:
                name_edittext.setText("");
                email_edittext.setText("");
                password_edittext.setText("");
                confirm_password_edittext.setText("");
                number_code_edittext.setText("");
                number_edittext.setText("");
                female_icon.setVisibility(View.GONE);
                male_icon.setVisibility(View.GONE);

                break;
            case R.id.female_lay:
                selectGender="Female";
                male_icon.setVisibility(View.GONE);
                female_icon.setVisibility(View.VISIBLE);
                break;
            case R.id.male_lay:
                selectGender="Male";
                female_icon.setVisibility(View.GONE);
                male_icon.setVisibility(View.VISIBLE);
                break;
            case R.id.cart_image:
                Intent intent11 = new Intent(RegisterActivity.this,UserCartActivity.class);
                startActivity(intent11);
                break;
            case R.id.search_image:
                sortDialog();
                break;
            case R.id.filter_image:
                Intent intent12 = new Intent(RegisterActivity.this,FilterActivity.class);
                startActivity(intent12);
                break;
        }
    }

    private void validation(){
        getName = name_edittext.getText().toString();
        getEmail = email_edittext.getText().toString();
        getCountryCode = number_code_edittext.getText().toString();
        getNumber = number_edittext.getText().toString();
        getPassword = password_edittext.getText().toString();
        getConfirmPassword = confirm_password_edittext.getText().toString();

        //Pattern p = Pattern.compile(Utils.regEx);
        //Matcher m = p.matcher(getEmail);

        if(TextUtils.isEmpty(getName)){
            CommanMethod.getCustomOkAlert(RegisterActivity.this, this.getString(R.string.toast_message_register_name));
            //name_edittext.setError(this.getString(R.string.toast_message_register_name));
            name_edittext.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_register_name),Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(getEmail)){
            CommanMethod.getCustomOkAlert(RegisterActivity.this, this.getString(R.string.toast_message_register_email));
            //email_edittext.setError(this.getString(R.string.toast_message_register_email));
            email_edittext.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_register_invalid),Toast.LENGTH_SHORT).show();
        }else if(!CommanMethod.isValidEmail(getEmail)){
            CommanMethod.getCustomOkAlert(RegisterActivity.this, this.getString(R.string.toast_message_register_invalid));
            //email_edittext.setError(this.getString(R.string.toast_message_register_invalid));
            email_edittext.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_register_email),Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(getCountryCode)){
            CommanMethod.getCustomOkAlert(RegisterActivity.this, this.getString(R.string.toast_message_register_country_code));
            number_code_edittext.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_register_country_code),Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(getNumber)){
            CommanMethod.getCustomOkAlert(RegisterActivity.this, this.getString(R.string.toast_message_register_number));
            //number_edittext.setError(this.getString(R.string.toast_message_register_number));
            number_edittext.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_register_number),Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(getPassword)){
            CommanMethod.getCustomOkAlert(RegisterActivity.this, this.getString(R.string.toast_message_register_pass));
            //password_edittext.setError(getResources().getString(R.string.toast_message_register_pass));
            password_edittext.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_register_pass),Toast.LENGTH_SHORT).show();
        }else if(getConfirmPassword.equals("") || getConfirmPassword.length()==0){
            CommanMethod.getCustomOkAlert(RegisterActivity.this, this.getString(R.string.toast_message_register_con_pass));
            //confirm_password_edittext.setError(getResources().getString(R.string.toast_message_register_con_pass));
            confirm_password_edittext.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_register_con_pass),Toast.LENGTH_SHORT).show();
        }else if(!getPassword.equals(getConfirmPassword)){
            CommanMethod.getCustomOkAlert(RegisterActivity.this, this.getString(R.string.toast_message_register_not_match));
            //confirm_password_edittext.setError(getResources().getString(R.string.toast_message_register_not_match));
            confirm_password_edittext.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_register_not_match),Toast.LENGTH_SHORT).show();
        }else if(selectGender.isEmpty()){
            CommanMethod.getCustomOkAlert(RegisterActivity.this, this.getString(R.string.toast_message_register_gender));
            //Toast.makeText(this,this.getString(R.string.toast_message_register_gender),Toast.LENGTH_SHORT).show();
        } else {
            if (CommanMethod.isInternetConnected(RegisterActivity.this)){
                userSignUp();
            }

            //Toast.makeText(this,selectGender,Toast.LENGTH_SHORT).show();
        }

    }

    private void getCountryList(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"country_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    countryLists = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONArray jsonArray = new JSONArray(object.getString("result"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CountryList countryList = new CountryList();
                            countryList.setId(jsonObject.getString("id"));
                            countryList.setCode(jsonObject.getString("code"));
                            countryList.setAsciiname(jsonObject.getString("asciiname"));
                            countryList.setCurrency_code(jsonObject.getString("currency_code"));
                            countryList.setFlag(jsonObject.getString("flag"));
                            countryLists.add(countryList);
                        }

                    } else if (status.equals("0")) {
                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("current_currency", sessionManager.getCurrencyCode());

                return params;
            }
        };


        mRequestQueue.add(mStringRequest);
    }

    private void userSignUp(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(RegisterActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"signup", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    /*{"id":"131","result":{"id":"131","username":"gleamingllptest@gmail.com",
                    "name":"Gleaming","password":"827ccb0eea8a706c4c34a16891f84e7b",
                    "email":"gleamingllptest@gmail.com","country_code":"+965",
                    "phone":"9999999999","gender":"Male","activation_code":null,
                    "created_at":null,"updated_at":null,"active":"0","status":"1","permission":null,
                    "useracessid":null,"last_login":null,"forgotten_password_code":null,"parent_id":"0",
                    "added_by":null,"profilephoto":null,"user_type":"1","device_id":null,"dob":null,
                    "loyality_point":"0","signup_type":null},
                    "status":"success","message":"User added successfully"}*/
                    //gifImageView.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = CommanMethod.getMessage(RegisterActivity.this, object);
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("result"));
                        sessionManager.setUserId(jsonObject.getString("id"));
                        sessionManager.setUserName(jsonObject.getString("name"));
                        sessionManager.setUserEmail(jsonObject.getString("email"));
                        sessionManager.setUserPassword(jsonObject.getString("password"));
                        sessionManager.setUserType("user");
                        /*Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();*/

                        /*if (getIntent().getStringExtra("from").equals("login")){
                            Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            *//*Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            intent.putExtra("from", "register");
                            startActivityForResult(intent, CommanMethod.LOGIN_REQ_CODE);
                            dialog.dismiss();
                            finish();*//*
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }*/

                        getCustomOkToHomeAlert(RegisterActivity.this, message);

                    }else{
                        CommanMethod.getCustomOkAlert(RegisterActivity.this, message);

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                parseVolleyError(error);
                dialog.dismiss();
                //gifImageView.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", getName);
                params.put("email", getEmail);
                params.put("password", getPassword);
                params.put("gender", selectGender);
                params.put("country_code", getCountryCode);
                params.put("phone", getNumber);
                if(OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId() != null) {
                    params.put("device_id", OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
                }
                params.put("guestid", sessionManager.getRandomValue());
                params.put("current_language", Locale.getDefault().getLanguage());
                Log.d("argumentfdhfdgs",params.toString());

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

    public void getCustomOkToHomeAlert(Context context, String message){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        info_tv.setText(message);
        ok_tv.setOnClickListener(view ->  {
            /*Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();*/
            if (getIntent().getStringExtra("from").equals("login")){
                Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String status = data.getString("status");
            String message = CommanMethod.getMessage(RegisterActivity.this, data);
            //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            CommanMethod.getCustomOkAlert(this, message);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException errorr) {
            error.printStackTrace();
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
                        if(jsonArray.length()> 0) {
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
                if (CommanMethod.isInternetConnected(RegisterActivity.this)){
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

                /*Intent intent = new Intent(RegisterActivity.this,SearchResultsActivity.class);
                intent.putExtra("array_of_product_lists",array_of_product_lists);
                startActivity(intent);*/
                Intent intent = new Intent(RegisterActivity.this,ProductDetailsActivity.class);
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
                    Intent intent = new Intent(RegisterActivity.this,SearchResultsActivity.class);
                    intent.putExtra("array_of_product_lists",array_of_product_lists);
                    intent.putExtra("search_string", search_text);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    }
    private void searchProduct(String search_text){

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"productlist_of_brand", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        productLists.clear();
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("product_list_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("product_list"));
                        if(jsonArray!=null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                ProductSearchModel productList = new ProductSearchModel();
                                productList.setId(jsonObject2.getString("id"));
                                productList.setQuantity(jsonObject2.getString("quantity"));
                                productList.setUser_id(jsonObject2.getString("user_id"));
                                productList.setCate_id(jsonObject2.getString("cate_id"));
                                productList.setCate_name(jsonObject2.getString("cate_name"));
                                productList.setTitle(CommanMethod.getTitle(RegisterActivity.this, jsonObject2));
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
                        }
                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    gifImageView.setVisibility(View.GONE);
                }
                searchadapter= new ArrayAdapter<ProductSearchModel>(RegisterActivity.this,android.R.layout.simple_list_item_1, productLists);
                searchView.setAdapter(searchadapter);
                searchView.showDropDown();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                gifImageView.setVisibility(View.GONE);
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
        if (CommanMethod.isInternetConnected(RegisterActivity.this)){
            CommanClass.getCartValue(this, number);
        }
    }
}
