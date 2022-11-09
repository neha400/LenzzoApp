package com.lenzzo;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParser;
import com.lenzzo.api.API;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.localization.LocaleManager;
import com.lenzzo.updatedVersionChecker.GooglePlayStoreAppVersionNameLoader;
import com.lenzzo.updatedVersionChecker.VersionCheckListner;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.GPSTracker;
import com.lenzzo.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends BaseActivity {

     private Animation animation ,zoom;
     private ImageView slide_image,slide_images;
     VideoView videoView;
     ImageView addbannerimageview;
     Button next_button;
     FrameLayout main_logo_lay;
     //private TextView app_name_textview;``

    public SessionManager sessionManager;
    ProgressDialog dialogss;
    Dialog dialog;

    String currentVersion, latestVersion;
    String CurrentcountryCode = "";

    private static final int LOCATION_REQUEST_CODE = 100;
    private String Latitude,Longitude;

    /*private TextView current_location_name;
    private TextView current_location_lat;
    private TextView current_location_long;
    private TextView current_country_name;
    private TextView current_currency_code;
    private TextView current_country_code;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        dialogss = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);
        if (TextUtils.isEmpty(sessionManager.getRandomValue())){
            sessionManager.setRandomValue(CommanMethod.getRandomNumber());
        }

        /*current_location_name=findViewById(R.id.current_location_name);
        current_location_lat=findViewById(R.id.current_location_lat);
        current_location_long=findViewById(R.id.current_location_long);
        current_country_name=findViewById(R.id.current_country_name);
        current_currency_code=findViewById(R.id.current_currency_code);
        current_country_code=findViewById(R.id.current_country_code);*/


//        new GooglePlayStoreAppVersionNameLoader(this, new VersionCheckListner() {
//            @Override
//            public void onGetResponse(boolean isUpdateAvailable) {
//                Log.e("tag","isUpdateAvailable:"+isUpdateAvailable);
//                if (isUpdateAvailable){
//                    getCustomOkAlert();
//                }else {
                    final Handler handlers = new Handler();
                    handlers.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            next_button.setVisibility(View.VISIBLE);
                        }
                    }, 3000);
//                }
//            }
//        }).execute();


        main_logo_lay=findViewById(R.id.main_logo_lay);
        main_logo_lay.setVisibility(View.GONE);
        videoView = findViewById(R.id.video_view);
        addbannerimageview=findViewById(R.id.addbannerimageview);
        slide_image=findViewById(R.id.slide_image);
        next_button=findViewById(R.id.next_button);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getsplashvideourl(videoView,addbannerimageview);
            }
        }, 3000);


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAnimation();
            }
        });

        animateMainLogo();
        getCurrentVersion();
        locationFind();
        checkAndRequestPermissions();


        TelephonyManager tm = (TelephonyManager)this.getSystemService(MainActivity.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        CurrentcountryCode=countryCodeValue;

        Log.d("CurrentcountryCode", CurrentcountryCode);

        if (CurrentcountryCode.equals("KW"))
        {
            sessionManager.setCurrencyCode("KWD");
        }
        else if (CurrentcountryCode.equals("QA"))
        {
            sessionManager.setCurrencyCode("QAR");
        }
        else if (CurrentcountryCode.equals("BH"))
        {
            sessionManager.setCurrencyCode("BHD");
        }
        else if (CurrentcountryCode.equals("OM"))
        {
            sessionManager.setCurrencyCode("OMR");
        }
        else if (CurrentcountryCode.equals("SA"))
        {
            sessionManager.setCurrencyCode("SAR");
        }
        else if (CurrentcountryCode.equals("AE"))
        {
            sessionManager.setCurrencyCode("AED");
        }
        else {
           // sessionManager.setCurrencyCode("KWD");
        }





       /* try {
            VideoView videoHolder = findViewById(R.id.video_view);
            //setContentView(videoHolder);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video_bg);

            videoHolder.setVideoURI(video);
            videoHolder.setOnPreparedListener(mp -> mp.setOnInfoListener((mp1, what, extra) -> {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started; hide the placeholder.
                    View placeholder_view = findViewById(R.id.placeholder_view);
                    placeholder_view.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }));

            videoHolder.setOnCompletionListener(mp -> finishAnimation());

            videoHolder.start();

        } catch (Exception ex) {
            //finishAnimation();
        }*/

        //animateAllCompaniesLogo();
        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishAnimation();
                *//*findViewById(R.id.logos_lay).setVisibility(View.GONE);
                findViewById(R.id.main_logo_lay).setVisibility(View.VISIBLE);

                animateMainLogo();
                animateSecurityLogo();*//*
            }
        }, 5000);*/
    }

    public void getCustomOkAlert(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
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
            //dialog.dismiss();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }


    private void locationFind() {
        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            Latitude = String.valueOf(gps.getLatitude());
            Longitude = String.valueOf(gps.getLongitude());

            try {
                String address = "", city = "", postalcode = "",CountryName = "",CountryCode = "",currencySymbol = "";
                List<Address> addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 5);

                for (int i = 0; i< addresses.size(); i++){
                    if (!TextUtils.isEmpty(addresses.get(i).getAddressLine(0))){
                        address = addresses.get(i).getAddressLine(0);
                        city = addresses.get(i).getLocality();
                        postalcode = addresses.get(i).getPostalCode();
                        CountryName = addresses.get(i).getCountryName();
                        CountryCode = addresses.get(i).getCountryCode();
                        break;
                    }
                }

                CurrentcountryCode = CountryCode;

               /* current_country_code.setText(CountryCode);
                current_country_name.setText(CountryName);
                current_location_name.setText(address);
                current_location_lat.setText(Latitude);
                current_location_long.setText(Longitude);*/

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
        int locationPermission = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),LOCATION_REQUEST_CODE);
            return false;
        }
        return true;
    }




    private void getsplashvideourl(VideoView videoView, ImageView addbannerimageview) {
        dialogss = new ProgressDialog(this);
        dialogss.setMessage("Please wait...");
        dialogss.setCancelable(false);
        dialogss.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"splash_screen",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialogss.dismiss();
                        try {
                            Log.e("Loginresponse", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsn = jsonObject.getJSONObject("response");
                            JSONObject jsnobs = jsn.getJSONObject("splash");
                            String id = jsnobs.getString("id");
                            String screen_type=jsnobs.getString("screen_type");
                            String video_url = jsnobs.getString("video_url");
                            String image_url = jsnobs.getString("image_url");

                            /*JSONArray jsonArray=jsn.getJSONArray("advertise");
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            String ids = jsonObject1.getString("id");
                            String image_url = jsonObject1.getString("image_url");*/

                            Log.d("screen_type",screen_type);

                            if (screen_type.equals("1"))
                            {
                                main_logo_lay.setVisibility(View.GONE);
                                videoView.setVisibility(View.VISIBLE);
                                addbannerimageview.setVisibility(View.GONE);
                                try {
                                    //VideoView videoHolder = findViewById(R.id.video_view);
                                    //setContentView(videoHolder);
                                    // Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video_bg);

                                    Uri video = Uri.parse( video_url);

                                    MainActivity.this.videoView.setVideoURI(video);
                                    MainActivity.this.videoView.setOnPreparedListener(mp -> mp.setOnInfoListener((mp1, what, extra) -> {
                                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                            // video started; hide the placeholder.
                                            View placeholder_view = findViewById(R.id.placeholder_view);
                                            placeholder_view.setVisibility(View.GONE);
                                            return true;
                                        }
                                        return false;
                                    }));

                                    MainActivity.this.videoView.setOnCompletionListener(mp -> finishAnimation());

                                    MainActivity.this.videoView.start();

                                } catch (Exception ex) {
                                    //finishAnimation();
                                }
                                //Toast.makeText(MainActivity.this, "video_url", Toast.LENGTH_SHORT).show();
                            }
                            else if (screen_type.equals("0"))
                            {
                                //Toast.makeText(MainActivity.this, "image_url", Toast.LENGTH_SHORT).show();
                                main_logo_lay.setVisibility(View.GONE);
                                videoView.setVisibility(View.GONE);
                                addbannerimageview.setVisibility(View.VISIBLE);
                                Picasso.get().load(API.DOMAIN+image_url).into(MainActivity.this.addbannerimageview);
                            }else {

                            }

                        } catch (JSONException e) {
                            Log.d("loginexp",e.getMessage());
                            Toast.makeText(MainActivity.this, "response error...", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialogss.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> arguments = new HashMap<String, String>();

                arguments.put("current_currency", sessionManager.getCurrencyCode());

                return arguments;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void finishAnimation(){
        if(!sessionManager.getCountryCode().isEmpty() && !sessionManager.getCurrencyCode().isEmpty()){
                    /*Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();*/
            System.out.println("lan "+Locale.getDefault().getLanguage());

            /*if(Locale.getDefault().getLanguage().equals("en")){
                setNewLocale(getLanguageNotation(0));
            }else{
                setNewLocale(getLanguageNotation(1));
            }*/
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            System.out.println("lan "+Locale.getDefault().getLanguage());
            Intent intent = new Intent(MainActivity.this,CountryActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setNewLocale(String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
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

    public void animateMainLogo(){
        final Animation zoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        slide_image = (ImageView)findViewById(R.id.slide_image);
        slide_image.startAnimation(zoom);
       /* final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //slide_image.clearAnimation();
                //finishAnimation();
                final Animation zoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
                ImageView slide_image = (ImageView)findViewById(R.id.slide_image);
                slide_image.startAnimation(zoom);
                //finishAnimation();
            }
        }, 2000);*/

    }

    public void animateSecurityLogo(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //slide_image.clearAnimation();
                //finishAnimation();
                findViewById(R.id.security_iv).setVisibility(View.VISIBLE);
                final Animation zoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_short);
                ImageView slide_image = (ImageView)findViewById(R.id.security_iv);
                slide_image.startAnimation(zoom);
            }
        }, 1000);

    }

    public void animateAllCompaniesLogo() {
        findViewById(R.id.logos_lay).setVisibility(View.VISIBLE);
        findViewById(R.id.main_logo_lay).setVisibility(View.GONE);
        View[] views = new View[]{findViewById(R.id.freshlook_iv),
                findViewById(R.id.bella_iv),
                findViewById(R.id.john_iv),
                findViewById(R.id.alcon_iv),
                findViewById(R.id.rayban_iv),
                findViewById(R.id.gucci_iv)};

// 100ms delay between Animations
        int delayBetweenAnimations = 500;

        for (int i = 0; i < views.length; i++) {
            final View view = views[i];

            // We calculate the delay for this Animation, each animation starts 100ms
            // after the previous one
            int delay = i * delayBetweenAnimations;

            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in_short);
                    view.startAnimation(animation);
                }
            }, delay);
        }
    }





    private void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        new GetLatestVersion().execute();

    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                 Document doc = Jsoup.connect("lenzzo").get();
                 latestVersion = doc.getElementsByClass("htlgb").get(5).text();

            } catch (Exception e) {
                e.printStackTrace();

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) { //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                        showUpdateDialog();
                    }
                }
            } else
               // background.start();
                super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_update_info1);
        builder.setPositiveButton(R.string.update_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=lenzzo")));
                dialog.dismiss();
            }
        });

       /* builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // background.start();
            }
        });*/

        builder.setCancelable(false);
        dialog = builder.show();
    }

}
