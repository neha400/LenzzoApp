package com.lenzzo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.api.API;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.model.CountryList;
import com.lenzzo.model.ProductList;
import com.lenzzo.model.ProductSearchModel;
import com.lenzzo.utility.CommanClass;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.CustomVolleyRequest;
import com.lenzzo.utility.SessionManager;
import com.lenzzo.utility.VolleyMultipartRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {

    private TextView number;
    private CircleImageView profilePic;
    private CircleImageView iv_camera;
    private EditText name_edittext;
    private TextView email_edittext;
    private AutoCompleteTextView number_code_edittext;
    private EditText phone_edittext;
    private Button update_button;
    private ImageView female_icon;
    private ImageView male_icon;
    //private GifImageView gifImageView;
    private ArrayList<CountryList> countryLists;
    private ArrayAdapter<CountryList> countryAdapter;
    private SessionManager sessionManager;
    private Dialog dialog;
    private List<ProductSearchModel> productLists;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private String search_text="";
    private ImageView search_image;
    private String total_count="";
    private int total_value;
    private static final String IMAGE_DIRECTORY = "/profile picture";
    private ImageLoader imageLoader;
    private String getName;
    private String getDateofBirth;
    private String getCountryCode;
    private String getNumber;
    private String selectGender="";
    private RequestQueue rQueue;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private ImageView back_image;
    private LinearLayout liner;
    public static int count = 0;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String mCurrentPhotoPath;
    private AutoCompleteTextView searchView;

    Calendar myCalendar;

    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sessionManager = new SessionManager(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.cart_image).setOnClickListener(this);
        findViewById(R.id.search_image).setOnClickListener(this);
        findViewById(R.id.filter_image).setOnClickListener(this);
        findViewById(R.id.iv_camera).setOnClickListener(this);
        findViewById(R.id.update_button).setOnClickListener(this);
        number = (TextView)findViewById(R.id.number);
        profilePic = (CircleImageView)findViewById(R.id.profilePic);
        name_edittext = (EditText)findViewById(R.id.name_edittext);
        email_edittext = (TextView) findViewById(R.id.email_edittext);
        //email_edittext.setEnabled(false);
        number_code_edittext = (AutoCompleteTextView)findViewById(R.id.number_code_edittext);
        phone_edittext = (EditText)findViewById(R.id.phone_edittext);

        female_icon = (ImageView)findViewById(R.id.female_icon);
        female_icon.setVisibility(View.GONE);

        male_icon = (ImageView)findViewById(R.id.male_icon);

        findViewById(R.id.female_lay).setOnClickListener(this);
        findViewById(R.id.male_lay).setOnClickListener(this);

        back_image = (ImageView)findViewById(R.id.back_image);
        liner = (LinearLayout)findViewById(R.id.liner);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }
        if (CommanMethod.isInternetConnected(EditProfileActivity.this)){
            getCountryList();
            getProfileDetails();
            //searchProduct();
            CommanClass.getCartValue(this, number);
        }

        email_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendatepicker();
            }
        });

        number_code_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_code_edittext.showDropDown();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditProfileActivity.this);
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
                countryAdapter = new ArrayAdapter<CountryList>(EditProfileActivity.this, android.R.layout.simple_list_item_1, countryLists);
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
    }

    private void opendatepicker() {
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        email_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditProfileActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    private void updateLabel() {
        //String myFormat = "dd/MM/yyyy";
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        email_edittext.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.cart_image:
                Intent intent = new Intent(EditProfileActivity.this, UserCartActivity.class);
                startActivity(intent);
                break;
            case R.id.search_image:
                sortDialog();
                break;
            case R.id.filter_image:
                Intent intent1 = new Intent(EditProfileActivity.this, FilterActivity.class);
                startActivity(intent1);
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
            case R.id.iv_camera:
                /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 10);
                    return;
                }else{
                    try{
                        selectImage();
                    }
                    catch (android.content.ActivityNotFoundException ex){
                    }
                }*/
                if(checkPermissions()){
                    selectImage();
                }
                break;
            case R.id.update_button:
                validation();
                break;
        }
    }

    private void validation(){
        getName = name_edittext.getText().toString();
        getDateofBirth = email_edittext.getText().toString();
        getCountryCode = number_code_edittext.getText().toString();
        getNumber = phone_edittext.getText().toString();
        if(getName.equals("") || getName.length()==0) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_profile_name));
            //Toast.makeText(this, this.getString(R.string.toast_message_profile_name), Toast.LENGTH_SHORT).show();
        }else if (getDateofBirth.equals("")||getDateofBirth.length()==0){
           // CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_dob));
            //Toast.makeText(this, "Please enter date of birth", Toast.LENGTH_SHORT).show();
        }else if(getCountryCode.equals("") || getCountryCode.length()==0){
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_country_code));
            //Toast.makeText(this, this.getString(R.string.toast_message_country_code), Toast.LENGTH_SHORT).show();
        }else if(getNumber.equals("") || getNumber.length()==0) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_number));
            //Toast.makeText(this, this.getString(R.string.toast_message_number), Toast.LENGTH_SHORT).show();
        /*}else if(selectGender.equals("")){
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_register_gender));*/
        }else{
            profileUpdate();
        }
    }

    private void profileUpdate(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"my_profile_update", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        sessionManager.setUserName(getName);
                        Intent intent4 = new Intent(EditProfileActivity.this,HomeActivity.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                    }else {
                        // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", sessionManager.getUserId());
                params.put("name",getName);
                params.put("dob",getDateofBirth);
                params.put("country_code", getCountryCode);
                params.put("phone", getNumber);
                params.put("gender",selectGender);
                params.put("current_currency", sessionManager.getCurrencyCode());
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }


    private void uploadImage(final Bitmap bitmap){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, API.BASE_URL1+"my_profile_update",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        dialog.dismiss();
                        Log.d("ressssssoo",new String(response.data));
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
                            Picasso.get().load(API.PROFILE_IMAGE+jsonObject1.getString("profilephoto")).placeholder(R.drawable.no_img).into(profilePic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", sessionManager.getUserId());
                params.put("current_currency", sessionManager.getCurrencyCode());

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("profilephoto", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void getCountryList(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(EditProfileActivity.this);
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

    private void getProfileDetails(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"my_profile", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Log.d("responcedob",response);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        imageLoader = CustomVolleyRequest.getInstance(EditProfileActivity.this).getImageLoader();
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        name_edittext.setText(jsonObject.getString("name"));
                        email_edittext.setText(jsonObject.getString("dob"));
                        number_code_edittext.setText(jsonObject.getString("country_code"));
                        phone_edittext.setText(jsonObject.getString("phone"));
                        //imageLoader.get(API.PROFILE_IMAGE+jsonObject.getString("profilephoto"), ImageLoader.getImageListener(profilePic, R.drawable.no_img, android.R.drawable.ic_dialog_alert));
                        Picasso.get().load(API.PROFILE_IMAGE+jsonObject.getString("profilephoto")).placeholder(R.drawable.no_img).into(profilePic);

                        String gender = jsonObject.getString("gender");

                        if (gender.equals("Female")){
                            //Toast.makeText(EditProfileActivity.this, "Female", Toast.LENGTH_SHORT).show();
                            female_icon.setVisibility(View.VISIBLE);
                            male_icon.setVisibility(View.GONE);
                        }else if (gender.equals("Male")){
                            //Toast.makeText(EditProfileActivity.this, "Male", Toast.LENGTH_SHORT).show();
                            female_icon.setVisibility(View.GONE);
                            male_icon.setVisibility(View.VISIBLE);
                        }else {

                        }

                    }else {
                        // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", sessionManager.getUserId());
                params.put("current_currency", sessionManager.getCurrencyCode());
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
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
                if (CommanMethod.isInternetConnected(EditProfileActivity.this)){
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

               /* Intent intent = new Intent(EditProfileActivity.this,SearchResultsActivity.class);
                intent.putExtra("array_of_product_lists",array_of_product_lists);
                startActivity(intent);*/

                Intent intent = new Intent(EditProfileActivity.this,ProductDetailsActivity.class);
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
                    Intent intent = new Intent(EditProfileActivity.this,SearchResultsActivity.class);
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
                                productList.setTitle(CommanMethod.getTitle(EditProfileActivity.this, jsonObject2));
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
                        //
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                searchadapter= new ArrayAdapter<ProductSearchModel>(EditProfileActivity.this,android.R.layout.simple_list_item_1, productLists);
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

    private void selectImage() {

        final CharSequence[] options = { getResources().getString(R.string.take_photo), getResources().getString(R.string.from_gallery),getResources().getString(R.string.cancle) };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle(getResources().getString(R.string.add_photo));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0)  {
                    dispatchTakePictureIntent();
                }else if (item == 1) {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (item == 2) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri imageFileUri = FileProvider.getUriForFile(this, "com.lenzzo.provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createImageFile() throws IOException {

        String imageFileName = "IMG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmpFactoryOptions);
                try {
                    uploadImage(CommanMethod.getRotatedBitmap(bitmap,mCurrentPhotoPath)); //upload profile pic
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(bitmap.getWidth()+" "+bitmap.getHeight());
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                uploadImage(thumbnail); //upload profile pic
                profilePic.setImageBitmap(thumbnail);
                }
            }
        }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(EditProfileActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {
//                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
//                            .show();
                }
                // permissions list of don't granted permission
            }
            return;
        }
    }
}
