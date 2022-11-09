package com.lenzzo;

import static com.lenzzo.utility.CommanMethod.capitalize;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.adapter.PaymentAdapter;
import com.lenzzo.api.API;
import com.lenzzo.interfacelenzzo.PaymentInterface;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.model.CheckUnchekModel;
import com.lenzzo.model.CityPojoModel.CityList;
import com.lenzzo.model.PaymentModel;
import com.lenzzo.model.UserCartModel;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import pl.droidsonroids.gif.GifImageView;

public class PaymentActivity extends BaseActivity implements View.OnClickListener, PaymentInterface {

    private SessionManager sessionManager;
    private TextView total_price_text_view,total_price_text_view2,vat_persentage_text_view2,vat_price_text_view2,delivery_price_text_view2,fast_delivery_price_text_view2,grand_price_text_view2;
    private TextView express_charge2,express_charge1,fast_delivery_price_text_view,delivery_price_text_view,vat_persentage_text_view,vat_price_text_view;
    private TextView grand_price_text_view;
    public  CheckBox Fastdelivery_CheckBox,Fastdelivery_CheckBox2;
    private LinearLayout pay_liner_layout,checkbox_layout1,checkbox_layout2,Fastdelivery_liner_layout,Fastdelivery_liner_layout2;
    private LinearLayout shipping_liner_layout,liner_layout1,liner_layout2;
    private RecyclerView payment_recycler_view;
    private PaymentAdapter paymentAdapter;
    private List<PaymentModel> paymentModelList;
    private String paymentModeid="";
    private String shippingCharge="";
    private String Express_delivery_chrg;
    private String vat_value = "";
    private String address_id;
    private String cart_id;
    private String giftId;
    private String sub_total;
    private String name;
    private String area;
    private String block;
    private String street;
    private String avenue;
    private String house;
    private String floor;
    private String flat;
    private String phone;
    private String comments;
    private Dialog dialog;
    private float grand_total;
    private String vat_pricess;
   // private String Express_Delivery_Chrg;
    //private GifImageView gifImageView;
    private String sourceType="";
    private String payment_type="";
    private String coupon_id="";
    private String coupon_code="";
    private String coupon_price="";
    private String onlyCod="";
    private ImageView back_image;
    private BigDecimal bd;
    public String cartNEWTotal = "";
    String CurrentVersionCode = null;
    String Device_id,Device_Name,Device_Model;
    public boolean SetSelected ;
    String expVlaue="0.0";


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        sessionManager = new SessionManager(this);

        // Log.d("powertyp",sessionManager.getPower_select_typ());
        Log.d("redeempointstatus",sessionManager.getRedeemPointStatus());
        Log.d("redeempointflagstatus",sessionManager.getRedeemPointFlagStatus());

        try {
            CurrentVersionCode = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("CurrentVersionCode",CurrentVersionCode);

        Device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("Device_id",Device_id);
        getDeviceName();


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            address_id = bundle.getString("address_id");
            cart_id = bundle.getString("cart_id");
            giftId = bundle.getString("giftId");
            sub_total = bundle.getString("sub_total");
            name = bundle.getString("name");
            area = bundle.getString("area");
            block = bundle.getString("block");
            street = bundle.getString("street");
            avenue = bundle.getString("avenue");
            house = bundle.getString("house");
            floor = bundle.getString("floor");
            flat = bundle.getString("flat");
            phone = bundle.getString("phone");
            comments = bundle.getString("comments");
            coupon_id = bundle.getString("coupon_id");
            coupon_code = bundle.getString("coupon_code");
            coupon_price = bundle.getString("coupon_price");
            onlyCod = bundle.getString("onlyCod");
            Express_delivery_chrg = bundle.getString("Express_delivery_chrg");
        }

        if (getIntent().getBooleanExtra("onlyCod", false)){
            findViewById(R.id.only_cod_alert_tv).setVisibility(View.VISIBLE);
            findViewById(R.id.alert_tv).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.only_cod_alert_tv).setVisibility(View.GONE);
            findViewById(R.id.alert_tv).setVisibility(View.GONE);
        }

        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.pay_liner_layout).setOnClickListener(this);
        findViewById(R.id.shipping_liner_layout).setOnClickListener(this);

        Fastdelivery_liner_layout=findViewById(R.id.Fastdelivery_liner_layout);
        Fastdelivery_liner_layout2=findViewById(R.id.Fastdelivery_liner_layout2);
        liner_layout1=findViewById(R.id.liner_layout1);
        liner_layout2=findViewById(R.id.liner_layout2);
        checkbox_layout1=findViewById(R.id.checkbox_layout1);
        checkbox_layout2=findViewById(R.id.checkbox_layout2);
        express_charge2=findViewById(R.id.express_charge2);
        express_charge1=findViewById(R.id.express_charge1);

        Fastdelivery_CheckBox=findViewById(R.id.Fastdelivery_CheckBox);
        Fastdelivery_CheckBox2=findViewById(R.id.Fastdelivery_CheckBox2);
        total_price_text_view2=findViewById(R.id.total_price_text_view2);
        vat_persentage_text_view2=findViewById(R.id.vat_persentage_text_view2);
        vat_price_text_view2=findViewById(R.id.vat_price_text_view2);
        delivery_price_text_view2=findViewById(R.id.delivery_price_text_view2);
        fast_delivery_price_text_view=findViewById(R.id.fast_delivery_price_text_view);
        fast_delivery_price_text_view2=findViewById(R.id.fast_delivery_price_text_view2);
        grand_price_text_view2=findViewById(R.id.grand_price_text_view2);
        //gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        total_price_text_view = (TextView)findViewById(R.id.total_price_text_view);
        delivery_price_text_view = (TextView)findViewById(R.id.delivery_price_text_view);
        vat_persentage_text_view = findViewById(R.id.vat_persentage_text_view);
        vat_price_text_view = findViewById(R.id.vat_price_text_view);
        grand_price_text_view = (TextView)findViewById(R.id.grand_price_text_view);
        payment_recycler_view = (RecyclerView)findViewById(R.id.payment_recycler_view);
        back_image = (ImageView)findViewById(R.id.back_image);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
        }

        if (CommanMethod.isInternetConnected(PaymentActivity.this)){
            payNow();
            shippingChange();
        }

        if ("ar".equals(sessionManager.getLanguageSelected())) {
            //LocaleManager.setNewLocale(context, "ar");
            liner_layout1.setVisibility(View.GONE);
            liner_layout2.setVisibility(View.VISIBLE);
            checkbox_layout1.setVisibility(View.GONE);
            checkbox_layout2.setVisibility(View.VISIBLE);

        } else {
            //LocaleManager.setNewLocale(context, "en");
            liner_layout1.setVisibility(View.VISIBLE);
            liner_layout2.setVisibility(View.GONE);
            checkbox_layout1.setVisibility(View.VISIBLE);
            checkbox_layout2.setVisibility(View.GONE);

        }

        sub_total=sessionManager.getNewSubTotal();

        total_price_text_view.setText(sub_total+" "+sessionManager.getCurrencyCode());
        total_price_text_view2.setText(sub_total+" "+sessionManager.getCurrencyCode());

      //  Toast.makeText(this, sessionManager.getNewSubTotal()+"  -  "+sub_total, Toast.LENGTH_LONG).show();

        Fastdelivery_CheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               // SetSelected=isChecked;

                if (isChecked){
                    expVlaue=Express_delivery_chrg;
                    shippingChange();
                }else {
                    expVlaue="0.0";
                    shippingChange();
                }
            }
        });

        Fastdelivery_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               // SetSelected=isChecked;

                if (isChecked){
                    expVlaue=Express_delivery_chrg;
                    shippingChange();
                }else {
                    expVlaue="0.0";
                    shippingChange();
                }
            }
        });


        express_charge1.setText("( "+sessionManager.getExpress_Delivery_Charges()+" "+sessionManager.getCurrencyCode()+" )");
        express_charge2.setText("( "+sessionManager.getExpress_Delivery_Charges()+" "+sessionManager.getCurrencyCode()+" )");

    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        Device_Name=manufacturer;
        Device_Model=model;
        Log.d("Device_Name",Device_Name);
        Log.d("Device_Model",Device_Model);
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.pay_liner_layout:
                toCheckCartValue();
                break;
            case R.id.shipping_liner_layout:
                addressDialogBox();
                break;
        }
    }

    private void dialogBox(String title,String info){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.comman_alert_dialog_box);
        TextView alerte_title= (TextView)dialog.findViewById(R.id.title_box);
        alerte_title.setText(title);
        TextView message = (TextView)dialog.findViewById(R.id.info_tv);
        message.setText(info);
        TextView ok_tv = (TextView)dialog.findViewById(R.id.ok_tv);

        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void payNow(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"paymentmode", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Log.d("sdsdsdsd",response);
                   // sessionManager.clearSelectedredeempoint();
                    // Log.e("payment", response);
                    paymentModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONArray jsonArray = new JSONArray(object.getString("result"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            PaymentModel paymentModel = new PaymentModel();
                            paymentModel.setId(jsonObject.getString("id"));
                            paymentModel.setName(jsonObject.getString("name"));
                            paymentModel.setName_ar(jsonObject.getString("name_ar"));
                            paymentModel.setLogo(API.PAYMENT_MODE_LOGO+jsonObject.getString("logo"));
                            paymentModel.setStatus(jsonObject.getString("status"));
                            if (getIntent().getBooleanExtra("onlyCod", false)){
                                if (jsonObject.getString("id").equals("1")){
                                    paymentModelList.add(paymentModel);
                                }
                            }else{
                                paymentModelList.add(paymentModel);
                            }
                        }
                    }
                }catch (JSONException e){
                    dialog.dismiss();
                    e.printStackTrace();
                }

                //GridLayoutManager gridLayoutManager = new GridLayoutManager(PaymentActivity.this,1);
                payment_recycler_view.setLayoutManager(new LinearLayoutManager(PaymentActivity.this, RecyclerView.VERTICAL, false));
                paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentModelList);
                payment_recycler_view.setAdapter(paymentAdapter);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id", sessionManager.getUserId());
                params.put("flag_status", sessionManager.getRedeemPointFlagStatus());
                params.put("current_currency", sessionManager.getCurrencyCode());

                /*if (sessionManager.getRedeemPointStatus().equals("pending_payment")){
                    params.put("flag_status", "3");
                }else if (sessionManager.getRedeemPointStatus().equals("success")){
                    params.put("flag_status", "2");
                    params.put("user_id", sessionManager.getUserId());
                }else {
                    params.put("flag_status", sessionManager.getRedeemPointFlagStatus());
                    params.put("user_id", sessionManager.getUserId());
                }*/

                Log.d("sdujked",params.toString());

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

    private void shippingChange(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"country_list", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(String response) {
                Log.d("sdjhdfjhd",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONArray jsonArray = new JSONArray(object.getString("result"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getString("currency_code").equals(sessionManager.getCurrencyCode())){
                                shippingCharge = jsonObject.getString("delivery_charge");
                                vat_value = jsonObject.getString("vat_value");
                                /*if(shippingCharge.equals("0")){
                                    shippingCharge = "0.00";
                                }*/

                                /*if ("ar".equals(sessionManager.getLanguageSelected())) {
                                    //LocaleManager.setNewLocale(context, "ar");
                                    vat_persentage_text_view.setText("ضريبة القيمة المضافة"+"("+vat_value+"%)"+" :");
                                    vat_persentage_text_view2.setText("ضريبة القيمة المضافة"+"("+vat_value+"%)"+" :");
                                } else {
                                    //LocaleManager.setNewLocale(context, "en");
                                    vat_persentage_text_view.setText("("+vat_value+"%)"+" :");
                                    vat_persentage_text_view2.setText("("+vat_value+"%)"+" :");
                                }*/

                                vat_persentage_text_view.setText("("+vat_value+"%)"+" :");
                                vat_persentage_text_view2.setText("("+vat_value+"%)"+" :");

                                float sub_total_price= Float.parseFloat(sub_total);
                                float vat_value_price= Float.parseFloat(vat_value);

                                vat_pricess= String.valueOf((sub_total_price*vat_value_price)/100);

                                //Log.e("jdfhjkfhk",vat_pricess+"");

                                vat_price_text_view.setText(String.valueOf(vat_pricess)+" "+sessionManager.getCurrencyCode());
                                vat_price_text_view2.setText(String.valueOf(vat_pricess)+" "+sessionManager.getCurrencyCode());

                                delivery_price_text_view.setText(CommanMethod.getCountryWiseDecimalNumber(PaymentActivity.this, shippingCharge)+" "+sessionManager.getCurrencyCode());
                                delivery_price_text_view2.setText(CommanMethod.getCountryWiseDecimalNumber(PaymentActivity.this, shippingCharge)+" "+sessionManager.getCurrencyCode());

                                fast_delivery_price_text_view.setText(expVlaue+" "+sessionManager.getCurrencyCode());
                                fast_delivery_price_text_view2.setText(expVlaue+" "+sessionManager.getCurrencyCode());

                                grand_total = Float.parseFloat(expVlaue)+Float.parseFloat(vat_pricess)+Float.parseFloat(shippingCharge)+Float.parseFloat(sub_total);


                                /*delivery_price_text_view.setText(CommanMethod.getCountryWiseDecimalNumber(PaymentActivity.this, "0.00")+" "+sessionManager.getCurrencyCode());
                                grand_total = Float.parseFloat("0.00")+Float.parseFloat(sub_total);*/

                                /*if(grand_total>=0.0){
                                    payment_recycler_view.setVisibility(View.VISIBLE);
                                }else{
                                    payment_recycler_view.setVisibility(View.GONE);
                                }*////String.format("%.2f",grand_total)

                                if(grand_total==0.0){
                                    payment_recycler_view.setVisibility(View.GONE);
                                }else{
                                    payment_recycler_view.setVisibility(View.VISIBLE);
                                }

                                if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){

                                    grand_price_text_view.setText(CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total)+" "+sessionManager.getCurrencyCode());
                                    grand_price_text_view2.setText(CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total)+" "+sessionManager.getCurrencyCode());
                                }else{
                                    //bd = new BigDecimal(grand_total).setScale(2, RoundingMode.HALF_UP);
                                    bd = CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total);
                                    //double newInput = bd.doubleValue();
                                    grand_price_text_view.setText(bd+" "+sessionManager.getCurrencyCode());
                                    grand_price_text_view2.setText(bd+" "+sessionManager.getCurrencyCode());
                                }
                            }
                        }

                    }else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void addressDialogBox(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.address_dialog);
        TextView close_dialog = (TextView)dialog.findViewById(R.id.close_dialog);
        TextView name = (TextView)dialog.findViewById(R.id.name);
        TextView area = (TextView)dialog.findViewById(R.id.area);
        TextView block = (TextView)dialog.findViewById(R.id.block);
        TextView street = (TextView)dialog.findViewById(R.id.street);
        TextView avenue = (TextView)dialog.findViewById(R.id.avenue);
        TextView house = (TextView)dialog.findViewById(R.id.house);
        TextView floor = (TextView)dialog.findViewById(R.id.floor);
        TextView flat = (TextView)dialog.findViewById(R.id.flat);
        TextView phone = (TextView)dialog.findViewById(R.id.phone);
        TextView comments = (TextView)dialog.findViewById(R.id.comments);
        if (!TextUtils.isEmpty(this.name)){
            name.setVisibility(View.VISIBLE);
        }else {
            name.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.area)){
            area.setVisibility(View.VISIBLE);
        }else {
            area.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.block)){
            block.setVisibility(View.VISIBLE);
        }else {
            block.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.street)){
            street.setVisibility(View.VISIBLE);
        }else {
            street.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.avenue)){
            avenue.setVisibility(View.VISIBLE);
        }else {
            avenue.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.house)){
            house.setVisibility(View.VISIBLE);
        }else {
            house.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.floor)){
            floor.setVisibility(View.VISIBLE);
        }else {
            floor.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.flat)){
            flat.setVisibility(View.VISIBLE);
        }else {
            flat.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.phone)){
            phone.setVisibility(View.VISIBLE);
        }else {
            phone.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.comments)){
            comments.setVisibility(View.VISIBLE);
        }else {
            comments.setVisibility(View.GONE);
        }


        name.setText(getResources().getString(R.string.user_billing_full_name)+" : "+this.name);
        area.setText(getResources().getString(R.string.user_billing_area)+" : "+this.area);
        block.setText(getResources().getString(R.string.user_billing_block)+" : "+this.block);
        street.setText(getResources().getString(R.string.user_billing_street)+" : "+this.street);
        avenue.setText(getResources().getString(R.string.user_billing_avenue)+" : "+this.avenue);
        house.setText(getResources().getString(R.string.user_billing_house)+" : "+this.house);
        floor.setText(getResources().getString(R.string.user_billing_floor)+" : "+this.floor);
        flat.setText(getResources().getString(R.string.user_billing_flat)+" : "+this.flat);
        phone.setText(getResources().getString(R.string.user_billing_phone)+" : "+this.phone);
        comments.setText(getResources().getString(R.string.user_billing_comment)+" : "+this.comments);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void userPayment(final Dialog dialog){
       /* final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();*/
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"payment_by_cod", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseddsd",response);
                //{"response":{"record":{"title":"Order Add","usercart":5339}},"status":"success","message":"تم إضافة السجل بنجاح"}
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String order_num = object.getString("order_num");

                    if(status.equals("success")){
                        String usercart = object.getJSONObject("response").getJSONObject("record").getString("usercart");
                        /*Intent intent = new Intent(PaymentActivity.this,HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/

                        if (sessionManager.getPower_select_typ().equals("2")||sessionManager.getPower_select_typ().equals("3")){

                            getCustomOkAlert(PaymentActivity.this, getResources().getString(R.string.only_cod_success),usercart,order_num);

                            //Toast.makeText(PaymentActivity.this, sessionManager.getPower_select_typ()+"toast1", Toast.LENGTH_SHORT).show();

                        }else {
                            if (order_num.equals("1")){
                                // Toast.makeText(PaymentActivity.this, "toast1"+order_num, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(PaymentActivity.this, sessionManager.getPower_select_typ()+"toast2", Toast.LENGTH_SHORT).show();
                                CommanMethod.rateAlert(PaymentActivity.this, getString(R.string.first_order),usercart,order_num);

                            }else {
                                // Toast.makeText(PaymentActivity.this, "toast2"+order_num, Toast.LENGTH_SHORT).show();

                                //Toast.makeText(PaymentActivity.this, sessionManager.getPower_select_typ()+"toast2", Toast.LENGTH_SHORT).show();
                                CommanMethod.rateAlert(PaymentActivity.this, getString(R.string.rate_app_info),usercart,order_num);

                            }
                        }

                    }else {
                        String message = object.getString("message");
                        Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_SHORT).show();
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
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    String message = CommanMethod.getMessage(PaymentActivity.this, data);
                    //CommanMethod.getCustomOkAlert(ChangePasswordActivity.this, message);
                    Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException errorr) {
                    error.printStackTrace();
                }
                // gifImageView.setVisibility(View.GONE);
            }
        })
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id", sessionManager.getUserId());
                params.put("address_id",address_id);
                params.put("cart_id",cart_id);
                params.put("gift_id",giftId);
                params.put("payment_mode_id",paymentModeid);
                params.put("delivery_charge",shippingCharge);
                params.put("delivery_charge_city",sessionManager.getExpress_Delivery_Charges());
                params.put("sub_total",sub_total);
                params.put("vat_charge",vat_pricess);
                params.put("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                params.put("current_currency",sessionManager.getCurrencyCode());
                if(!coupon_id.isEmpty() && !coupon_code.isEmpty() && !coupon_price.isEmpty()){
                    params.put("coupon_price",coupon_price);
                    params.put("coupon_id",coupon_id);
                    params.put("coupon_code",coupon_code);
                }
                if(grand_total==0.0){
                    params.put("payment_status","free");
                    params.put("payment_mode_id","1001");
                }
                params.put("payment_status","pending");
                params.put("loyality_point", getIntent().getStringExtra("loyality_point"));
                params.put("device_mode","Android"+" - "+Device_Name+" ("+Device_Model+")");
                params.put("api_version","Update Version - "+CurrentVersionCode);
                params.put("current_currency", sessionManager.getCurrencyCode());

                Log.d("jksdjhsd",params.toString());

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

    public void getCustomOkAlert(Context context, String message,String usercart,String order_num){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        ok_tv.setText(getString(R.string.next));
        //info_tv.setText(getString(R.string.only_cod_success));
        info_tv.setText(message);
        ok_tv.setOnClickListener(view ->  {
            /*Intent intent = new Intent(PaymentActivity.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
            dialog.dismiss();
            CommanMethod.rateAlert(PaymentActivity.this, getString(R.string.rate_app_info),usercart,order_num);
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void paymentMode(String paymentModeid,String payment_name) {
        this.paymentModeid = paymentModeid;
        this.payment_type = payment_name;
        sessionManager.clearSelectedredeempoint();
    }

    public void toCheckCartValue(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.setCancelable(true);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"usercart", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(String response) {
                try {
                    //dialog.dismiss();
                    Log.d("responce",response);
                    boolean shouldProceed = true;
                    float totalToCheck = 0f;
                    StringBuilder stringBuilder = new StringBuilder();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));

//                        cartNEWTotal=jsonObject1.getString("cartGrandTotal");

                        for(int j=0;j<jsonArray.length();j++){
                            JSONObject childObject = jsonArray.getJSONObject(j);
                            JSONArray childArray = childObject.getJSONArray("child");
                            for(int i=0;i<childArray.length();i++) {
                                JSONObject jsonObject2 = childArray.getJSONObject(i);
                                UserCartModel userCartModel = new UserCartModel();
                                userCartModel.setId(jsonObject2.getString("id"));
                                userCartModel.setUser_id(jsonObject2.getString("user_id"));
                                userCartModel.setStock_flag(jsonObject2.getString("stock_flag"));
                                String stock_flag = jsonObject2.getString("stock_flag");
                                String product_quantity = jsonObject2.getString("product_quantity");
                                String quantity = jsonObject2.getString("quantity");

                                String total = jsonObject2.getString("total");

                                if (CommanMethod.isOutOfStock(stock_flag, quantity)){
                                    shouldProceed = false;
                                    break;
                                }
                                totalToCheck = totalToCheck+Float.parseFloat(total.replace(",",""));
                                stringBuilder.append(jsonObject2.getString("id")).append(",");
                            }
                        }
                        String cart_id = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();

                        if (!shouldProceed){
                            dialog.dismiss();
                            CommanMethod.getCustomGOHome(PaymentActivity.this, getResources().getString(R.string.user_cart_out_of_stock));
                        }else if (!cart_id.equals(PaymentActivity.this.cart_id) && CommanMethod.getCountryWiseDecimalNumber(PaymentActivity.this, getIntent().getStringExtra("total")).equals(""+totalToCheck)){
                            dialog.dismiss();
                            CommanMethod.getCustomGOHome(PaymentActivity.this, getResources().getString(R.string.something_wrong));
                        }else {
                            if(paymentModeid.equals("") || paymentModeid.length()==0 ) {
                                if (grand_total > 0.0) {
                                    dialog.dismiss();
                                    dialogBox(getResources().getString(R.string.payment_dialog_alert), getResources().getString(R.string.payment_dialog_option));
                                } else {
                                    if (CommanMethod.isInternetConnected(PaymentActivity.this)) {
                                        userPayment(dialog);
                                    } else {
                                        dialog.dismiss();
                                    }
                                }
                            }else if (!sessionManager.getOldTotal().equals(cartNEWTotal)){
                               // Toast.makeText(PaymentActivity.this,  sessionManager.getOldTotal() + "-"+sessionManager.getNewTotal() + " - " +cartNEWTotal , Toast.LENGTH_LONG).show();
                                   dialogBoxtwo();
                            }else{
                               // if(payment_type.equals("1")||payment_type.equals("1002")||payment_type.equals(1001)){
                                if(payment_type.equals("CASH ON DELIVERY")||payment_type.equals("Cash on Delivery")||payment_type.equals("FREE")){
                                    if (CommanMethod.isInternetConnected(PaymentActivity.this)) {
                                        userPayment(dialog);
                                    }else {
                                        dialog.dismiss();
                                    }
                                }else if(payment_type.equals("VISA/MASTER")){
                                    sourceType = "src_card";
                                    createOrder(dialog);
                                    //System.out.println("grand_total" +String.format("%.2f",grand_total));
                                    /*Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                                    intent.putExtra("sourceType",sourceType);
                                    intent.putExtra("user_id", sessionManager.getUserId());
                                    intent.putExtra("address_id",address_id);
                                    intent.putExtra("cart_id",cart_id);
                                    intent.putExtra("gift_id",giftId);
                                    intent.putExtra("payment_mode_id",paymentModeid);
                                    intent.putExtra("delivery_charge",shippingCharge);
                                    intent.putExtra("sub_total",sub_total);
                                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                                    intent.putExtra("coupon_id",coupon_id);
                                    intent.putExtra("coupon_code",coupon_code);
                                    intent.putExtra("coupon_price",coupon_price);
                                    startActivity(intent);*/
                                }else if(payment_type.equals("KNET")){
                                    sourceType = "src_kw.knet";
                                    createOrder(dialog);
                                    /*Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                                    intent.putExtra("sourceType",sourceType);
                                    intent.putExtra("user_id", sessionManager.getUserId());
                                    intent.putExtra("address_id",address_id);
                                    intent.putExtra("cart_id",cart_id);
                                    intent.putExtra("gift_id",giftId);
                                    intent.putExtra("payment_mode_id",paymentModeid);
                                    intent.putExtra("delivery_charge",shippingCharge);
                                    intent.putExtra("sub_total",sub_total);
                                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                                    intent.putExtra("coupon_id",coupon_id);
                                    intent.putExtra("coupon_code",coupon_code);
                                    intent.putExtra("coupon_price",coupon_price);
                                    startActivity(intent);*/
                                }else if(payment_type.equals("MY FATOORA")){
                                    sourceType = "src_myfatoora";
                                    createOrder(dialog);
                                    /*Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                                    intent.putExtra("sourceType",sourceType);
                                    intent.putExtra("user_id", sessionManager.getUserId());
                                    intent.putExtra("address_id",address_id);
                                    intent.putExtra("cart_id",cart_id);
                                    intent.putExtra("gift_id",giftId);
                                    intent.putExtra("payment_mode_id",paymentModeid);
                                    intent.putExtra("delivery_charge",shippingCharge);
                                    intent.putExtra("sub_total",sub_total);
                                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                                    intent.putExtra("coupon_id",coupon_id);
                                    intent.putExtra("coupon_code",coupon_code);
                                    intent.putExtra("coupon_price",coupon_price);
                                    startActivity(intent);*/
                                }else if(payment_type.equals("ONLINE PAYMENT")){
                                    sourceType = "src_online";
                                    createOrder(dialog);
                                    /*Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                                    intent.putExtra("sourceType",sourceType);
                                    intent.putExtra("user_id", sessionManager.getUserId());
                                    intent.putExtra("address_id",address_id);
                                    intent.putExtra("cart_id",cart_id);
                                    intent.putExtra("gift_id",giftId);
                                    intent.putExtra("payment_mode_id",paymentModeid);
                                    intent.putExtra("delivery_charge",shippingCharge);
                                    intent.putExtra("sub_total",sub_total);
                                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                                    intent.putExtra("coupon_id",coupon_id);
                                    intent.putExtra("coupon_code",coupon_code);
                                    intent.putExtra("coupon_price",coupon_price);
                                    startActivity(intent);*/
                                }else if(payment_type.equals("Online Payment")) {
                                    sourceType = "src_loyalty point + online payment";
                                    createOrder(dialog);
                                    /*Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                                    intent.putExtra("sourceType",sourceType);
                                    intent.putExtra("user_id", sessionManager.getUserId());
                                    intent.putExtra("address_id",address_id);
                                    intent.putExtra("cart_id",cart_id);
                                    intent.putExtra("gift_id",giftId);
                                    intent.putExtra("payment_mode_id",paymentModeid);
                                    intent.putExtra("delivery_charge",shippingCharge);
                                    intent.putExtra("sub_total",sub_total);
                                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                                    intent.putExtra("coupon_id",coupon_id);
                                    intent.putExtra("coupon_code",coupon_code);
                                    intent.putExtra("coupon_price",coupon_price);
                                    startActivity(intent);*/
                                }else {
                                    dialog.dismiss();
                                }
                            }
                        }

                    }else{
                        dialog.dismiss();
                        CommanMethod.getCustomGOHome(PaymentActivity.this, getResources().getString(R.string.something_wrong));
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
                params.put("current_currency",sessionManager.getCurrencyCode());

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

    private void dialogBoxtwo(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.offer_expire_dialog_box);
        TextView info_tv = (TextView)dialog.findViewById(R.id.info_tv);

        //info_tv.setText(this.getString(R.string.user_cart_out_of_stock));
        info_tv.setText("Offer Expired. Please choose another one.");

        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(PaymentActivity.this,UserCartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finishAffinity();
            }
        });
        dialog.show();

    }

    private void createOrder(final Dialog dialog){
        /*final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();*/
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"payment_by_live_transaction", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Log.d("fdhfhfddf", response);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String order_id = object.getJSONObject("response").getJSONObject("record").optString("order_id");
                    Log.e("PaymentActivity", response);
                    if(status.equals("success")){
                        if(payment_type.equals("VISA/MASTER")){
                            sourceType = "src_card";
                            //System.out.println("grand_total" +String.format("%.2f",grand_total));
                            Intent intent =new Intent(PaymentActivity.this,MyFtooraPaymentGetwayActivity.class);
                            intent.putExtra("sourceType",sourceType);
                            intent.putExtra("user_id", sessionManager.getUserId());
                            intent.putExtra("address_id",address_id);
                            intent.putExtra("cart_id",cart_id);
                            intent.putExtra("gift_id",giftId);
                            intent.putExtra("payment_mode_id",paymentModeid);
                            intent.putExtra("delivery_charge",shippingCharge);
                            intent.putExtra("delivery_charge_city",sessionManager.getExpress_Delivery_Charges());
                            intent.putExtra("sub_total",sub_total);
                            intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                            intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                            intent.putExtra("coupon_id",coupon_id);
                            intent.putExtra("coupon_code",coupon_code);
                            intent.putExtra("coupon_price",coupon_price);
                            intent.putExtra("order_id",order_id);
                            startActivity(intent);
                        }else if(payment_type.equals("KNET")){
                            sourceType = "src_kw.knet";
                            Intent intent =new Intent(PaymentActivity.this,MyFtooraPaymentGetwayActivity.class);
                            intent.putExtra("sourceType",sourceType);
                            intent.putExtra("user_id", sessionManager.getUserId());
                            intent.putExtra("address_id",address_id);
                            intent.putExtra("cart_id",cart_id);
                            intent.putExtra("gift_id",giftId);
                            intent.putExtra("payment_mode_id",paymentModeid);
                            intent.putExtra("delivery_charge",shippingCharge);
                            intent.putExtra("delivery_charge_city",sessionManager.getExpress_Delivery_Charges());
                            intent.putExtra("sub_total",sub_total);
                            intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                            intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                            intent.putExtra("coupon_id",coupon_id);
                            intent.putExtra("coupon_code",coupon_code);
                            intent.putExtra("coupon_price",coupon_price);
                            intent.putExtra("order_id",order_id);
                            startActivity(intent);
                        }else if(payment_type.equals("MY FATOORA")){
                            sourceType = "src_myfatoora";
                            Intent intent =new Intent(PaymentActivity.this,MyFtooraPaymentGetwayActivity.class);
                            intent.putExtra("sourceType",sourceType);
                            intent.putExtra("user_id", sessionManager.getUserId());
                            intent.putExtra("address_id",address_id);
                            intent.putExtra("cart_id",cart_id);
                            intent.putExtra("gift_id",giftId);
                            intent.putExtra("payment_mode_id",paymentModeid);
                            intent.putExtra("delivery_charge",shippingCharge);
                            intent.putExtra("delivery_charge_city",sessionManager.getExpress_Delivery_Charges());
                            intent.putExtra("sub_total",sub_total);
                            intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                            intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                            intent.putExtra("coupon_id",coupon_id);
                            intent.putExtra("coupon_code",coupon_code);
                            intent.putExtra("coupon_price",coupon_price);
                            intent.putExtra("order_id",order_id);
                            startActivity(intent);
                        }else if(payment_type.equals("ONLINE PAYMENT")){
                            sourceType = "src_online";
                            Intent intent =new Intent(PaymentActivity.this,MyFtooraPaymentGetwayActivity.class);
                            intent.putExtra("sourceType",sourceType);
                            intent.putExtra("user_id", sessionManager.getUserId());
                            intent.putExtra("address_id",address_id);
                            intent.putExtra("cart_id",cart_id);
                            intent.putExtra("gift_id",giftId);
                            intent.putExtra("payment_mode_id",paymentModeid);
                            intent.putExtra("delivery_charge",shippingCharge);
                            intent.putExtra("delivery_charge_city",sessionManager.getExpress_Delivery_Charges());
                            intent.putExtra("sub_total",sub_total);
                            intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                            intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                            intent.putExtra("coupon_id",coupon_id);
                            intent.putExtra("coupon_code",coupon_code);
                            intent.putExtra("coupon_price",coupon_price);
                            intent.putExtra("order_id",order_id);
                            startActivity(intent);
                        }else if(payment_type.equals("Online Payment")) {
                            sourceType = "src_online";
                            Intent intent = new Intent(PaymentActivity.this, MyFtooraPaymentGetwayActivity.class);
                            intent.putExtra("sourceType", sourceType);
                            intent.putExtra("user_id", sessionManager.getUserId());
                            intent.putExtra("address_id", address_id);
                            intent.putExtra("cart_id", cart_id);
                            intent.putExtra("gift_id", giftId);
                            intent.putExtra("payment_mode_id", paymentModeid);
                            intent.putExtra("delivery_charge", shippingCharge);
                            intent.putExtra("delivery_charge_city",sessionManager.getExpress_Delivery_Charges());
                            intent.putExtra("sub_total", sub_total);
                            intent.putExtra("total_order_price", "" + CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                            intent.putExtra("current_currency", sessionManager.getCurrencyCode());
                            intent.putExtra("coupon_id", coupon_id);
                            intent.putExtra("coupon_code", coupon_code);
                            intent.putExtra("coupon_price", coupon_price);
                            intent.putExtra("order_id", order_id);
                            startActivity(intent);
                        }

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
                params.put("user_id", sessionManager.getUserId());
                params.put("address_id",address_id);
                params.put("cart_id",cart_id);
                params.put("gift_id",giftId);
                params.put("payment_mode_id",paymentModeid);
                params.put("coupon_code","");
                params.put("delivery_charge",shippingCharge);
                params.put("delivery_charge_city",sessionManager.getExpress_Delivery_Charges());
                params.put("sub_total",sub_total);
                params.put("vat_charge",vat_pricess);
                params.put("total_order_price", ""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                params.put("current_currency",sessionManager.getCurrencyCode());
                params.put("loyality_point", getIntent().getStringExtra("loyality_point"));
                //params.put("tap_id",tap_id);
                if(!coupon_id.isEmpty() && !coupon_code.isEmpty() && !coupon_price.isEmpty()){
                    params.put("coupon_price",coupon_price);
                    params.put("coupon_id",coupon_id);
                    params.put("coupon_code",coupon_code);
                }
                /*params.put("device_mode","android");
                params.put("api_version","V_2.44");*/
                params.put("device_mode","Android"+" - "+Device_Name+" ("+Device_Model+")");
                params.put("api_version","Update Version - "+CurrentVersionCode);

                Log.d("loyalitypointamers2",params.toString());

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
}
