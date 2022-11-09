package com.lenzzo;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.model.CityPojoModel.CityList;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.GPSTracker;
import com.lenzzo.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddNewAddressActivity extends BaseActivity implements View.OnClickListener {

    private EditText full_name;
    private EditText current_location_name;
    private EditText current_location_lat;
    private EditText current_location_long;
    private TextView area;
    private EditText area_text;
    private TextView block;
    private EditText block_text;
    private TextView street;
    private EditText street_text;
    private EditText avenue_text;
    private TextView house;
    private EditText house_text;
    private EditText floor_text;
    private EditText flat_text;
    private EditText phone_text;
    private EditText paci_text;
    private EditText comment_text;
    private LinearLayout linerLayout2;
    private SessionManager sessionManager;
    private static final int LOCATION_REQUEST_CODE = 100;
    private String Latitude,Longitude;
    private String get_full_name;
    private String get_current_location_name;
    private String get_current_location_lat;
    private String get_current_location_long;
    private String get_area_text;
    private String get_block_text;
    private String get_street_text;
    private String get_avenue_text;
    private String get_house_text;
    private String get_floor_text;
    private String get_flat_text;
    private String get_phone_text;
    private String get_paci_text;
    private String get_city_name;
    private String get_comment_text;
    private TextView button;
    private boolean isNewAddress=true;
    private String user_billing_address_id;
    private ImageView back_image;
    private Spinner spinner_current_city;

    ArrayList<String> zipp_City_ID;
    ArrayList<String> city_name;
    String zip_id;
    TextView citytext;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        sessionManager = new SessionManager(this);
        sessionManager.clearzippcode();
        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.linerLayout).setOnClickListener(this);
        findViewById(R.id.close_text).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);
        full_name = (EditText)findViewById(R.id.full_name);
        current_location_name = (EditText)findViewById(R.id.current_location_name);
        current_location_lat = (EditText)findViewById(R.id.current_location_lat);
        current_location_long = (EditText)findViewById(R.id.current_location_long);
        area = (TextView)findViewById(R.id.area);
        area_text = (EditText)findViewById(R.id.area_text);
        block = (TextView)findViewById(R.id.block);
        block_text = (EditText)findViewById(R.id.block_text);
        street = (TextView)findViewById(R.id.street);
        street_text = (EditText)findViewById(R.id.street_text);
        avenue_text = (EditText)findViewById(R.id.avenue_text);
        house = (TextView)findViewById(R.id.house);
        house_text = (EditText)findViewById(R.id.house_text);
        floor_text = (EditText)findViewById(R.id.floor_text);
        flat_text = (EditText)findViewById(R.id.flat_text);
        phone_text = (EditText)findViewById(R.id.phone_text);
        paci_text = (EditText)findViewById(R.id.paci_text);
        comment_text = (EditText)findViewById(R.id.comment_text);
        linerLayout2 = (LinearLayout)findViewById(R.id.linerLayout2);
        spinner_current_city=findViewById(R.id.spinner_current_city);
        button = (TextView)findViewById(R.id.button);
        linerLayout2.setVisibility(View.GONE);
        findViewById(R.id.linerLayout).setVisibility(View.GONE);
        back_image = (ImageView)findViewById(R.id.back_image);

        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            findViewById(R.id.ballon_iv).setScaleX(1f);

        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            findViewById(R.id.ballon_iv).setScaleX(-1f);
        }

        if(sessionManager.getCurrencyCode().equals("KWD")){
            findViewById(R.id.linerLayout).setVisibility(View.VISIBLE);
        }
        checkAndRequestPermissions();

        citytext=findViewById(R.id.city);
        getCityList();

        zipp_City_ID = new ArrayList<>();
        city_name = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            isNewAddress=false;
            user_billing_address_id = bundle.getString("user_billing_address_id");
            get_full_name = bundle.getString("get_full_name");
            full_name.setText(get_full_name);
            get_current_location_name = bundle.getString("get_current_location_name");
            get_current_location_lat = bundle.getString("get_current_location_lat");
            get_current_location_long = bundle.getString("get_current_location_long");
            if(TextUtils.isEmpty(get_current_location_name)){
                linerLayout2.setVisibility(View.GONE);
                area.setText(this.getString(R.string.area));
                block.setText(this.getString(R.string.block));
                street.setText(this.getString(R.string.street));
                house.setText(this.getString(R.string.house));
            }else{
                area.setText(this.getString(R.string.area_hint));
                block.setText(this.getString(R.string.block_hint));
                street.setText(this.getString(R.string.street_hint));
                linerLayout2.setVisibility(View.VISIBLE);
                current_location_name.setText(get_current_location_name);
                current_location_lat.setText(get_current_location_lat);
                current_location_long.setText(get_current_location_long);
            }
            get_area_text = bundle.getString("get_area_text");
            area_text.setText(get_area_text);
            get_block_text = bundle.getString("get_block_text");
            block_text.setText(get_block_text);
            get_street_text = bundle.getString("get_street_text");
            street_text.setText(get_street_text);
            get_avenue_text = bundle.getString("get_avenue_text");
            avenue_text.setText(get_avenue_text);
            get_house_text = bundle.getString("get_house_text");
            house_text.setText(get_house_text);
            get_floor_text = bundle.getString("get_floor_text");
            floor_text.setText(get_floor_text);
            get_flat_text = bundle.getString("get_flat_text");
            flat_text.setText(get_flat_text);
            get_phone_text = bundle.getString("get_phone_text");
            phone_text.setText(get_phone_text);
            get_paci_text = bundle.getString("get_paci_text");
            paci_text.setText(get_paci_text);
            get_comment_text = bundle.getString("get_comment_text");
            comment_text.setText(get_comment_text);
            button.setText(this.getString(R.string.update_address));
        }
    }


    private void getCityList(){
        final Dialog dialogs = CommanMethod.getCustomProgressDialog(this);
        dialogs.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"city_list",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onResponse(String response) {
                        dialogs.dismiss();
                        Log.d("cityresponse", response);
                        try {
                            JSONObject object=new JSONObject(response);
                            JSONObject jsonObject = object.getJSONObject("response");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("city_Array");
                            String title=jsonObject1.getString("title");

                            JSONArray jsonArray = jsonObject1.getJSONArray("citylist");

                            citytext.setText(title);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject_posts = jsonArray.getJSONObject(i);

                                zip_id=jsonObject_posts.getString("zip_id");
                               // String region_id=jsonObject_posts.getString("region_id");
                                String city_code=jsonObject_posts.getString("code");
                              //  String default_name=jsonObject_posts.getString("default_name");
                               // String created=jsonObject_posts.getString("created");
                              //  String is_active=jsonObject_posts.getString("is_active");
                               // String delivery_charge=jsonObject_posts.getString("delivery_charge");
                               // String country_id=jsonObject_posts.getString("country_id");

                               // CityList cityList = new Gson().fromJson(jsonObject_posts.toString(),CityList.class);

                                zipp_City_ID.add(zip_id);
                                city_name.add(city_code);
                                Log.d("zip_id",zip_id);
                            }
                            zipp_City_ID.add(0, "");
                            city_name.add(0, "Select City");
                            ArrayAdapter<String> category_list_dropdown = new ArrayAdapter(AddNewAddressActivity.this, android.R.layout.simple_spinner_dropdown_item, city_name);
                            category_list_dropdown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinner_current_city.setAdapter(category_list_dropdown);
                            spinner_current_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedItemId = zipp_City_ID.get(position);
                                    String selected_city_name = city_name.get(position);

                                    sessionManager.setCity_Zipp_code(selectedItemId);

                                    try {
                                        ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
                                        ((TextView)parent.getChildAt(0)).setTextSize(14);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    //search_btn.setEnabled(false);
                                }
                            });




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialogs.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> arguments = new HashMap<String, String>();

                arguments.put("user_id", sessionManager.getUserId());
                arguments.put("current_currency", sessionManager.getCurrencyCode());

                return arguments;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        mRequestQueue.add(stringRequest);

    }


    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.back_image:
               super.onBackPressed();
               break;
           case R.id.linerLayout:
               linerLayout2.setVisibility(View.VISIBLE);
              // findViewById(R.id.linerLayout).setVisibility(View.GONE);
               findViewById(R.id.linerLayout);
               area.setText(this.getString(R.string.area_hint));
               block.setText(this.getString(R.string.block_hint));
               street.setText(this.getString(R.string.street_hint));
               house.setText(this.getString(R.string.house_hint));
               locationFind();
               break;
           case R.id.close_text:
               linerLayout2.setVisibility(View.GONE);
               //findViewById(R.id.linerLayout).setVisibility(View.VISIBLE);
               current_location_name.setText("");
               current_location_lat.setText("");
               current_location_long.setText("");
               area.setText(this.getString(R.string.area));
               block.setText(this.getString(R.string.block));
               street.setText(this.getString(R.string.street));
               house.setText(this.getString(R.string.house));
               break;
           case R.id.save_button:
               validation();
               break;
       }
    }

    private void validation(){
        get_full_name = full_name.getText().toString();
        get_current_location_name = current_location_name.getText().toString();
        get_current_location_lat = current_location_lat.getText().toString();
        get_current_location_long = current_location_long.getText().toString();
        get_area_text = area_text.getText().toString();
        get_block_text = block_text.getText().toString();
        get_street_text = street_text.getText().toString();
        get_avenue_text = avenue_text.getText().toString();
        get_house_text = house_text.getText().toString();
        get_floor_text = floor_text.getText().toString();
        get_flat_text = flat_text.getText().toString();
        get_phone_text = phone_text.getText().toString();
        get_paci_text = paci_text.getText().toString();
        get_comment_text = comment_text.getText().toString();
        if(get_full_name.equals("") || get_full_name.length()==0){
            full_name.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_name),Toast.LENGTH_SHORT).show();
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_name));
        }else if(get_phone_text.equals("") || get_phone_text.length()==0){
            phone_text.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_massage_mobile),Toast.LENGTH_SHORT).show();
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_massage_mobile));
        }else if(TextUtils.isEmpty(get_current_location_name)){
                if(get_area_text.equals("") || get_area_text.length()==0){
                    area_text.requestFocus();
                    ///Toast.makeText(this,this.getString(R.string.toast_message_area),Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_area));
                }else if(get_block_text.equals("") || get_block_text.length()==0){
                    block_text.requestFocus();
                    //Toast.makeText(this,this.getString(R.string.toast_message_block),Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_block));
                }else if(get_street_text.equals("") || get_street_text.length()==0){
                    street_text.requestFocus();
                    //Toast.makeText(this,this.getString(R.string.toast_message_street),Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_street));
                }else if(get_house_text.equals("") || get_house_text.length()==0){
                    house_text.requestFocus();
                    //Toast.makeText(this,this.getString(R.string.toast_message_house),Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_house));
                }else if(TextUtils.isEmpty(get_paci_text)){
                    if(get_house_text.equals("") || get_house_text.length()==0){
                        house_text.requestFocus();
                        //Toast.makeText(this,this.getString(R.string.toast_message_house),Toast.LENGTH_SHORT).show();
                        CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_house));
                    }else if(get_floor_text.equals("")|| get_floor_text.length()==0){
                        floor_text.requestFocus();
                        //Toast.makeText(this,this.getString(R.string.toast_floor),Toast.LENGTH_SHORT).show();
                        CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_floor));
                    }else if(get_flat_text.equals("") || get_flat_text.length()==0){
                        flat_text.requestFocus();
                        //Toast.makeText(this,this.getString(R.string.toast_flat),Toast.LENGTH_SHORT).show();
                        CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_flat));
                    }if(isNewAddress){
                        if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)){
                            addNewAddress();
                        }
                    }else{
                        if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)){
                            updateAddress(user_billing_address_id);
                        }
                    }

                }else{
                    if(isNewAddress){
                        if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)){
                            addNewAddress();
                        }
                    }else{
                        if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)){
                            updateAddress(user_billing_address_id);
                        }
                    }
                }
        }else if(TextUtils.isEmpty(get_paci_text)){
                if(get_house_text.equals("") || get_house_text.length()==0){
                    house_text.requestFocus();
                    CommanMethod.getCustomOkAlert(this,this.getString(R.string.toast_message_house));
                    //Toast.makeText(this,this.getString(R.string.toast_message_house),Toast.LENGTH_SHORT).show();
                }else if(get_floor_text.equals("")|| get_floor_text.length()==0){
                    floor_text.requestFocus();
                    CommanMethod.getCustomOkAlert(this,this.getString(R.string.toast_floor));
                    //Toast.makeText(this,this.getString(R.string.toast_floor),Toast.LENGTH_SHORT).show();
                }else if(get_flat_text.equals("") || get_flat_text.length()==0){
                    flat_text.requestFocus();
                    CommanMethod.getCustomOkAlert(this,this.getString(R.string.toast_flat));
                    //Toast.makeText(this,this.getString(R.string.toast_flat),Toast.LENGTH_SHORT).show();
                }else{
                    if(isNewAddress){
                        if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)){
                            addNewAddress();
                        }
                    }else{
                        if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)){
                            updateAddress(user_billing_address_id);
                        }
                    }
                }

        }else{
            if(isNewAddress){
                if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)){
                    addNewAddress();
                }
            }else{
                if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)){
                    updateAddress(user_billing_address_id);
                }
            }
        }
    }

    private void locationFind() {
        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            Latitude = String.valueOf(gps.getLatitude());
            Longitude = String.valueOf(gps.getLongitude());
            try {
                String address = "", city = "", postalcode = "";
                List<Address> addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 5);

                for (int i = 0; i< addresses.size(); i++){
                    if (!TextUtils.isEmpty(addresses.get(i).getAddressLine(0))){
                        address = addresses.get(i).getAddressLine(0);
                        city = addresses.get(i).getLocality();
                        postalcode = addresses.get(i).getPostalCode();
                        break;
                    }
                }
                current_location_name.setText(address);
                current_location_lat.setText(Latitude);
                current_location_long.setText(Longitude);

            } catch (IOException e) {
                e.printStackTrace();
            }catch (RuntimeException e) {
                e.printStackTrace();
            }

        } else {

            gps.showSettingsAlert();

        }
    }

    private  boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(AddNewAddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(AddNewAddressActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),LOCATION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    private void addNewAddress(){
        //gifImageView.setVisibility(View.VISIBLE);
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"user_billing_address_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                   // gifImageView.setVisibility(View.GONE);
                    Log.d("dfasdfscsdfcdsfgv",response);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        Intent intent = new Intent(AddNewAddressActivity.this,CheckOutActivity.class);
                        startActivity(intent);

                    }else {
                       // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    dialog.dismiss();
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
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
                params.put("full_name", get_full_name);
                params.put("area", get_area_text);
                params.put("block", get_block_text);
                params.put("street", get_street_text);
                params.put("avenue", get_avenue_text);
                params.put("house_no", get_house_text);
                params.put("floor_no", get_floor_text);
                params.put("flat_no", get_flat_text);
                params.put("phone_no", get_phone_text);
                params.put("comments", get_comment_text);
                params.put("latitude", get_current_location_lat);
                params.put("longitude", get_current_location_long);
                params.put("user_id", sessionManager.getUserId());
                params.put("currrent_location", get_current_location_name);
                params.put("paci_number", get_paci_text);
                params.put("current_currency", sessionManager.getCurrencyCode());
                params.put("city_id",sessionManager.getCity_Zipp_code());
                params.put("current_language",sessionManager.getLanguageSelected());
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

    private void updateAddress(final String user_billing_address_id){
        //gifImageView.setVisibility(View.VISIBLE);
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"user_billing_address_update", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    Log.d("dfasdf",response);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        Intent intent = new Intent(AddNewAddressActivity.this,CheckOutActivity.class);
                        startActivity(intent);

                    }else {
                        String message = CommanMethod.getMessage(AddNewAddressActivity.this, object);
                        CommanMethod.getCustomOkAlert(AddNewAddressActivity.this, message);
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    dialog.dismiss();
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
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
                params.put("full_name", get_full_name);
                params.put("area", get_area_text);
                params.put("block", get_block_text);
                params.put("street", get_street_text);
                params.put("avenue", get_avenue_text);
                params.put("house_no", get_house_text);
                params.put("floor_no", get_floor_text);
                params.put("flat_no", get_flat_text);
                params.put("phone_no", get_phone_text);
                params.put("comments", get_comment_text);
                params.put("latitude", get_current_location_lat);
                params.put("longitude", get_current_location_long);
                params.put("user_id", sessionManager.getUserId());
                params.put("currrent_location", get_current_location_name);
                params.put("paci_number", get_paci_text);
                params.put("user_billing_address_id",user_billing_address_id);
                params.put("current_currency", sessionManager.getCurrencyCode());
                params.put("city_id",sessionManager.getCity_Zipp_code());
                params.put("current_language",sessionManager.getLanguageSelected());
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
