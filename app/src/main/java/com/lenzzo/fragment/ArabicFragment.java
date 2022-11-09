package com.lenzzo.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.lenzzo.HomeActivity;
import com.lenzzo.LoginActivity;
import com.lenzzo.MainActivity;
import com.lenzzo.R;
import com.lenzzo.adapter.CountryListAdapter;
import com.lenzzo.api.API;
import com.lenzzo.localization.LocaleManager;
import com.lenzzo.model.CountryList;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.SessionManager;
import com.lenzzo.utility.SortFilterSessionManager;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

import static com.lenzzo.utility.CommanMethod.getCustomOkAlert;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArabicFragment extends Fragment {

    private ListView listView;
    //private GifImageView gifImageView;
    private ArrayList<CountryList> countryLists;
    private CountryListAdapter countryListAdapter;
    private Button applay_button;
    SessionManager sessionManager;
    private String country_code="";
    private String currency_code="";
    int check_positon=-1;


    private String getemail;
    private String getpassword;


    public ArabicFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_arabic, container, false);

        sessionManager = new SessionManager(getActivity());
        listView = (ListView)view.findViewById(R.id.listView);
        //gifImageView = (GifImageView)view.findViewById(R.id.gifImageView);
        applay_button = (Button)view.findViewById(R.id.applay_button);
        if (CommanMethod.isInternetConnected(getContext())){
            getCountryList();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                countryListAdapter.getselectedPosition = position;
                countryListAdapter.notifyDataSetChanged();
                CountryList countryList = countryLists.get(position);
                country_code = countryList.getCode();
                currency_code = countryList.getCurrency_code();
            }
        });

        applay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommanMethod.isInternetConnected(getActivity())) {
                    sessionManager.setCountryCode(country_code);
                    sessionManager.setCurrencyCode(currency_code);
                    setNewLocale(getLanguageNotation(1));
                   // SessionLogOut();
                   // sessionManager.clearSelectedPreferences();
                    userLogin();

                }
                //Intent intent = new Intent(getActivity(), HomeActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
            }
        });
        return view;
    }

    private void userLogin(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(getContext());
        dialog.show();
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"login", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Log.d("sfhjdfgv",response);
                    //{"status":"success","response":{"id":"106","username":"ajay@gmail.com","name":"ajay",
                    // "password":"fcea920f7412b5da7be0cf42b8c93759","email":"ajay@gmail.com",
                    // "country_code":"+965","phone":"8800985790","gender":"Male",
                    // "activation_code":"715417","created_at":null,"updated_at":"2019-12-09 09:46:19",
                    // "active":"1","status":"1","permission":null,"useracessid":null,"last_login":null,
                    // "forgotten_password_code":null,"parent_id":"0","added_by":null,
                    // "profilephoto":"res_1576151280.jpg","user_type":"1",
                    // "device_id":"cBs23iskTiQ:APA91bFsJzqLEIZcejmOJj870c58oaZeNRaOMf1dOcHJmNf4pyp0QRA2x16OzyLf4a4ZIm0hg3GrsFuWNsNwDNcr_e3M1qpH0faY23ILm-kCPLDvbUF8CZEjsb5gmP6ZLyDY66oAs-1X",
                    // "dob":null,"loyality_point":"300","signup_type":null,
                    // "image_path":"http:\/\/www.lenzzo.com\/uploads\/users\/res_1576151280.jpg"}}
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        sessionManager.setUserId(jsonObject.getString("id"));
                        sessionManager.setUserName(jsonObject.getString("name"));
                        sessionManager.setUserEmail(jsonObject.getString("email"));
                        sessionManager.setUserPassword(sessionManager.getUserSavePassword());
                        sessionManager.setUserType("user");
                        /*Intent intent = new Intent();
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();*/
                       // Toast.makeText(getActivity(), "Login Again", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                    }else if(status.equals("false") || status.equals("failed")){
                        String message = CommanMethod.getMessage(getContext(), object);
                        //CommanMethod.getCustomOkAlert(LoginActivity.this, getResources().getString(R.string.login_error));
                        CommanMethod.getCustomOkAlert(getContext(), message);
                        //{"status":"failed","message":"Your account is not verified. So please check your email and click verification link"}
                        /*if(Locale.getDefault().getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)){
                            CommanMethod.getCustomOkAlert(LoginActivity.this, getResources().getString(R.string.login_error));
                        }else{
                            CommanMethod.getCustomOkAlert(LoginActivity.this, object.getString("message_ar"));
                        }*/
                       // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                dialog.dismiss();
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
                if(OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId() != null) {
                    params.put("device_id", OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
                }

                params.put("email", sessionManager.getUserEmail());
                params.put("password", sessionManager.getUserSavePassword());
                params.put("guestid", sessionManager.getRandomValue());
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


    private void SessionLogOut(){
        //gifImageView.setVisibility(View.VISIBLE);
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            //gifImageView.setVisibility(View.GONE);
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if(status.equals("success")){
                                //JSONObject jsonObject=new JSONObject(object.getString("response"));
                                //sessionManager.removeId("user_email","user_password");

                                SortFilterSessionManager sortFilterSessionManager = new SortFilterSessionManager(getActivity());
                                //JSONObject jsonObject=new JSONObject(object.getString("response"));
                                //sessionManager.removeId("user_email","user_password");
                                sessionManager.clearSelectedPreferences();
                                sortFilterSessionManager.clearSelectedFilter();
                                if (TextUtils.isEmpty(sessionManager.getRandomValue())){
                                    sessionManager.setRandomValue(CommanMethod.getRandomNumber());
                                }
                                /*Intent intent4 = new Intent(getActivity(),HomeActivity.class);
                                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent4);*/
                                Toast.makeText(getActivity(), "log out", Toast.LENGTH_SHORT).show();
                            }else if(status.equals("failed")){
                                String message = CommanMethod.getMessage(getActivity(), object);
                                CommanMethod.getCustomOkAlert(getActivity(), message);
                                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            dialog.dismiss();
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
            protected Map<String, String> getParams(){

                Map<String, String>  params = new HashMap<String, String>();
                /*if(FirebaseInstanceId.getInstance().getToken() != null) {
                    params.put("device_id", FirebaseInstanceId.getInstance().getToken());
                }*/
                if(OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId() != null) {
                    params.put("device_id", OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
                    params.put("current_currency", sessionManager.getCurrencyCode());
                }
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


    private void getCountryList(){
        SessionManager sessionManager = new SessionManager(getActivity());
        final Dialog dialog = CommanMethod.getCustomProgressDialog(getActivity());
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"country_list", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    countryLists = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONArray jsonArray=new JSONArray(object.getString("result"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CountryList countryList = new CountryList();
                            countryList.setId(jsonObject.getString("id"));
                            countryList.setCode(jsonObject.getString("code"));
                            countryList.setIso3(jsonObject.getString("iso3"));
                            countryList.setIso_numeric(jsonObject.getString("iso_numeric"));
                            countryList.setFips(jsonObject.getString("fips"));
                            countryList.setName(jsonObject.getString("name"));
                            countryList.setAsciiname(jsonObject.getString("asciiname_ar"));
                            countryList.setAsciiname_ar(jsonObject.getString("asciiname_ar"));
                            countryList.setCapital(jsonObject.getString("capital"));
                            countryList.setArea(jsonObject.getString("area"));
                            countryList.setPopulation(jsonObject.getString("population"));
                            countryList.setContinent_code(jsonObject.getString("continent_code"));
                            countryList.setTld(jsonObject.getString("tld"));
                            countryList.setCurrency_code(jsonObject.getString("currency_code"));
                            countryList.setPhone(jsonObject.getString("phone"));
                            countryList.setPostal_code_format(jsonObject.getString("postal_code_format"));
                            countryList.setPostal_code_regex(jsonObject.getString("postal_code_regex"));
                            countryList.setLanguages(jsonObject.getString("languages"));
                            countryList.setNeighbours(jsonObject.getString("neighbours"));
                            countryList.setEquivalent_fips_code(jsonObject.getString("equivalent_fips_code"));
                            countryList.setFlag(API.FlagURL+jsonObject.getString("flag"));
                            countryList.setAdmin_type(jsonObject.getString("admin_type"));
                            countryList.setAdmin_field_active(jsonObject.getString("admin_field_active"));
                            countryList.setActive(jsonObject.getString("active"));
                            countryList.setDelivery_charge(jsonObject.getString("delivery_charge"));

                            countryLists.add(countryList);
                        }

                        for(int i=0;i<countryLists.size();i++){
                            if(sessionManager.getCountryCode().equals(countryLists.get(i).getCode())){
                                check_positon=i;
                            }
                        }

                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

                countryListAdapter = new CountryListAdapter(countryLists,getContext());
                listView.setAdapter(countryListAdapter);
                countryListAdapter.getselectedPosition = check_positon;
                countryListAdapter.notifyDataSetChanged();
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

    /*private void setNewLocale(String language) {
        LocaleManager.setNewLocale(getContext(), language);
        SessionManager sessionManager = new SessionManager(getContext());
        sessionManager.setLanguageSelected(language);
        Intent refresh = new Intent(getContext(), MainActivity.class);
        startActivity(refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().finish();

    }*/

    private void setNewLocale(String language) {
        SessionManager sessionManager = new SessionManager(getContext());
        if(TextUtils.isEmpty(sessionManager.getCountryCode()) && TextUtils.isEmpty(sessionManager.getCurrencyCode())){
            getCustomOkAlert(getActivity(), "يرجى تحديد البلد.");
        }else {
            LocaleManager.setNewLocale(getContext(), language);
            sessionManager.setLanguageSelected(language);

            changeLanguage();

        }
    }

    public static void getCustomOkAlert(Context context, String message){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView title_tv = dialog.findViewById(R.id.title_tv);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        title_tv.setText("محزر");
        info_tv.setText("يرجى تحديد البلد.");
        ok_tv.setText("نعم");
        ok_tv.setOnClickListener(view ->  {

            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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

    private void changeLanguage(){
        Dialog dialog = CommanMethod.getCustomProgressDialog(getActivity());
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1+"change_language", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        Intent refresh = new Intent(getContext(), HomeActivity.class);
                        startActivity(refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        getActivity().finish();


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
