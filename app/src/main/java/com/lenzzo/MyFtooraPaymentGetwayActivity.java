package com.lenzzo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.api.API;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFtooraPaymentGetwayActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PaymentCommantActivity.class.getSimpleName();
    private SessionManager sessionManager;
    //private GifImageView gifImageView;
    private WebView webView;
    private String sourceType;
    private String user_id;
    private String address_id;
    private String cart_id;
    private String gift_id;
    private String payment_mode_id;
    private String delivery_charge;
    private String delivery_charge_city;
    private String sub_total;
    private String total_order_price;
    private String current_currency;
    private String order_id;
    private String transaction_url ="";
    private String coupon_id="";
    private String coupon_code="";
    private String coupon_price="";
    private ImageView back_image;

    String fatooraweburl = "";
    private Dialog dialog;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ftoora_payment_getway);
        sessionManager = new SessionManager(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        dialog = CommanMethod.getCustomProgressDialog(this);
        back_image = (ImageView)findViewById(R.id.back_image);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
        }
        webView = (WebView)findViewById(R.id.webView);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            sourceType = bundle.getString("sourceType");
            user_id = bundle.getString("user_id");
            address_id = bundle.getString("address_id");
            cart_id = bundle.getString("cart_id");
            gift_id = bundle.getString("gift_id");
            payment_mode_id = bundle.getString("payment_mode_id");
            coupon_code = bundle.getString("coupon_code");
            delivery_charge = bundle.getString("delivery_charge");
            delivery_charge_city = bundle.getString("delivery_charge_city");
            sub_total = bundle.getString("sub_total");
            total_order_price = bundle.getString("total_order_price");
            current_currency = bundle.getString("current_currency");
            order_id = bundle.getString("order_id");
            coupon_id = bundle.getString("coupon_id");
            coupon_code = bundle.getString("coupon_code");
            coupon_price = bundle.getString("coupon_price");
        }

      //  Toast.makeText(getApplicationContext(), ""+order_id, Toast.LENGTH_SHORT).show();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Win64; x64; rv:46.0) Gecko/20100101 Firefox/68.0");
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);


        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);

        webView.setWebChromeClient(new ChromeClient());
        webView.setInitialScale(0);

        final Activity activity = this;
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 1000);
            }
        });

        if (ContextCompat.checkSelfPermission(MyFtooraPaymentGetwayActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        }

        webView.setWebViewClient(new Browser_Home());
        webView.setWebChromeClient(new ChromeClient());
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

       // fatooraweburl = API.DOMAIN+"mobile/payment_by_myfatoora_test/myfatoorah?order_id="+order_id+"&currency="+current_currency;
        fatooraweburl = API.DOMAIN+"mobile/payment_by_myfatoora/myfatoorah?order_id="+order_id+"&currency="+sessionManager.getCurrencyCode();
        loadWebSite();

        webView.setWebChromeClient(new WebChromeClient() {
            @SuppressLint("NewApi")
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }

            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                webView.setVisibility(View.GONE);
            }

            public void onHideCustomView() {
                super.onHideCustomView();
                webView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadWebSite () {
        webView.loadUrl(fatooraweburl);
    }

    private class Browser_Home extends WebViewClient {
        Browser_Home() {
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //super.onPageFinished(view, url);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Log.d("urlsss",url);  //  "payment_by_myfatoora_test/success?paymentId=" ,.. for test
                if (url.contains("/success?paymentId=" )) {
                    System.out.println("payment_by_tap shouldOverrideUrlLoading" + url + "sub string " + url.substring(url.indexOf("=")) + " last index " + url.substring(url.lastIndexOf("=")));
                  //  Toast.makeText(getApplicationContext()," "+url,Toast.LENGTH_SHORT).show();
                    if (CommanMethod.isInternetConnected(MyFtooraPaymentGetwayActivity.this)){
                        String paymentstatus = "success";
                        userPayment(url.split("=")[1],paymentstatus);
                    }

               /* } else if (url.contains("cancelpaypalorder?insid=" + order_id)) {
                    System.out.println("payment_by_tap shouldOverrideUrlLoading" + url + "sub string " + url.substring(url.indexOf("=")) + " last index " + url.substring(url.lastIndexOf("=")));
                    //Toast.makeText(getApplicationContext()," "+url,Toast.LENGTH_SHORT).show();
                    //backtousercard();*/

                }else if (url.contains("/error?paymentId=")){ // "payment_by_myfatoora_test/error?paymentId=" , .. for test
                    System.out.println("payment_by_tap shouldOverrideUrlLoading" + url + "sub string " + url.substring(url.indexOf("=")) + " last index " + url.substring(url.lastIndexOf("=")));
                    //  Toast.makeText(getApplicationContext()," "+url,Toast.LENGTH_SHORT).show();
                    if (CommanMethod.isInternetConnected(MyFtooraPaymentGetwayActivity.this)){
                        String paymentstatus = "fail";
                        userPayment(url.split("=")[1],paymentstatus);
                    }
                }else {
                    //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void userPayment(final String paymentId,String paymentstatus) {
        Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"updateOrderFatoora", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responce",response);
                //{"response":{"record":{"title":"Order","usercart":"5320"}},"status":"failed","message":"Order placed failed","message_ar":"عملية الطلب فاشلة"}
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String order_num = object.getString("order_num");
                    if(status.equals("success")){
                        String usercart = object.getJSONObject("response").getJSONObject("record").getString("usercart");
                       /* Intent intent =new Intent(PaymentCommantActivity.this,HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                        //CommanMethod.getCustomGOHome(PaymentCommantActivity.this, CommanMethod.getMessage(PaymentCommantActivity.this, object));
                        CommanMethod.rateAlert(MyFtooraPaymentGetwayActivity.this, getString(R.string.rate_app_info),usercart,order_num);
                    }else {
                        CommanMethod.getCustomGOHome(MyFtooraPaymentGetwayActivity.this, CommanMethod.getMessage(MyFtooraPaymentGetwayActivity.this, object));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                System.out.println("hello error "+error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                //params.put("user_id", user_id);
                params.put("order_id",getIntent().getStringExtra("order_id"));
                params.put("tap_id",paymentId);
                params.put("status",paymentstatus);
                params.put("current_currency", sessionManager.getCurrencyCode());
                params.put("delivery_charge_city",delivery_charge_city);

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

    @Override
    public void onRequestPermissionsResult ( int requestCode, String permissions[],
                                             int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Start your camera handling here
                } else {
                    // AppUtils.showUserMessage("You declined to allow the app to access your camera", this);
                }
        }
    }

    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
        }
    }
}