package com.lenzzo;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.lenzzo.adapter.GiftAdapter;
import com.lenzzo.adapter.UserBillingAddressAdapter;
import com.lenzzo.api.API;
import com.lenzzo.interfacelenzzo.GiftInterface;
import com.lenzzo.interfacelenzzo.UserAddressInterface;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.model.GiftModel;
import com.lenzzo.model.UserBillingAddressModel;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOutActivity extends BaseActivity implements View.OnClickListener, UserAddressInterface, GiftInterface {
    private UserBillingAddressModel userBillingAddressModel;
    private List<UserBillingAddressModel> userBillingAddressModelList;
    private UserBillingAddressAdapter userBillingAddressAdapter;
    private RecyclerView address_recycler_view;
    private SessionManager sessionManager;
    private String sub_total;
    private String current_currency;
    private TextView total_price_text_view;
    private String addressId="";
    private List<GiftModel> giftModelList;
    private GiftAdapter giftAdapter;
    private Dialog dialog;
    private RecyclerView gift_recycler_view;
    private TextView close_dialog;
    private Button btn_done;
    private Button btn_skip;
    private String giftId="";
    private String cart_id;
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
    private String coupon_id="";
    private String coupon_code="";
    private String coupon_price="";
    private String loyality_point="";
    private String Express_delivery_chrg="";
    private ImageView back_image;
    private LinearLayout liner;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        sessionManager = new SessionManager(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.home_back).setOnClickListener(this);
        findViewById(R.id.relativeLayout1).setOnClickListener(this);
        findViewById(R.id.liner_layout2).setOnClickListener(this);
        address_recycler_view = (RecyclerView)findViewById(R.id.address_recycler_view);
        total_price_text_view = (TextView)findViewById(R.id.total_price_text_view);
        back_image = (ImageView)findViewById(R.id.back_image);

        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
        } else
        {
            back_image.setImageResource(R.drawable.arrow_right_30);
        }
        /*if (CommanMethod.isInternetConnected(CheckOutActivity.this)){
            getUserShippingAddress();
        }*/

       // sub_total=sessionManager.getNewSubTotal();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            sub_total = bundle.getString("sub_total");
            current_currency = bundle.getString("current_currency");
            cart_id = bundle.getString("cart_id");
            coupon_id = bundle.getString("coupon_id");
            coupon_code = bundle.getString("coupon_code");
            coupon_price = bundle.getString("coupon_price");

            loyality_point = bundle.getString("loyality_point");

            Log.d("loyality_point",loyality_point.toString());

            // intent.putExtra("loyality_point", getIntent().getStringExtra("loyality_point"));


        }

        total_price_text_view.setText(sessionManager.getNewSubTotal()+" "+current_currency);


      //  Toast.makeText(this, sessionManager.getNewSubTotal()+"  -  "+sub_total, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                 super.onBackPressed();
                break;
            case R.id.home_back:
                Intent intent = new Intent(CheckOutActivity.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.relativeLayout1:
                Intent intent1 = new Intent(CheckOutActivity.this,AddNewAddressActivity.class);
                startActivity(intent1);
                break;
            case R.id.liner_layout2:
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                        if(addressId.equals("") || addressId.length()==0) {
                            CommanMethod.getCustomOkAlert(this, this.getString(R.string.select_any_one_address));
                        }else {
                            if(CommanMethod.isInternetConnected(this)){
                                getGiftList();
                               // Toast.makeText(this, ""+addressId +" , " +name, Toast.LENGTH_LONG).show();
                                expressdeliveryCharge();
                            }

                        }
                    }else {
                        Intent intent2 = new Intent(CheckOutActivity.this,LoginActivity.class);
                        intent2.putExtra("from", "checkout");
                        startActivity(intent2);

                    }
                    break;
                }
        }

    private void giftDialogBox(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.gift_dialog_screen);
        gift_recycler_view = (RecyclerView)dialog.findViewById(R.id.gift_recycler_view);
        close_dialog = (TextView)dialog.findViewById(R.id.close_dialog);
        btn_done = (Button)dialog.findViewById(R.id.btn_done);
        btn_skip = (Button)dialog.findViewById(R.id.btn_skip);
        TextView text_dialog = dialog.findViewById(R.id.text_dialog);
        TextView step_text = dialog.findViewById(R.id.step_text);
        if(this.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            text_dialog.setGravity(Gravity.START);
            step_text.setGravity(Gravity.END);
        }else{
            text_dialog.setGravity(Gravity.END);
            step_text.setGravity(Gravity.START);
        }

        if (giftModelList.size()>0){
            dialog.findViewById(R.id.empty_gift_tv).setVisibility(View.GONE);
            gift_recycler_view.setVisibility(View.VISIBLE);
        }else {
            dialog.findViewById(R.id.empty_gift_tv).setVisibility(View.VISIBLE);
            gift_recycler_view.setVisibility(View.GONE);
        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(CheckOutActivity.this,1);
        gift_recycler_view.setLayoutManager(gridLayoutManager);
        giftAdapter = new GiftAdapter(CheckOutActivity.this,giftModelList);
        gift_recycler_view.setAdapter(giftAdapter);


        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(CheckOutActivity.this,PaymentActivity.class);
                intent.putExtra("cart_id",cart_id);
                intent.putExtra("giftId",giftId);
                intent.putExtra("sub_total",sub_total);
                intent.putExtra("address_id",addressId);
                intent.putExtra("name",name);
                intent.putExtra("area",area);
                intent.putExtra("block",block);
                intent.putExtra("street",street);
                intent.putExtra("avenue",avenue);
                intent.putExtra("house",house);
                intent.putExtra("floor",floor);
                intent.putExtra("flat",flat);
                intent.putExtra("phone",phone);
                intent.putExtra("comments",comments);
                intent.putExtra("coupon_id",coupon_id);
                intent.putExtra("coupon_code",coupon_code);
                intent.putExtra("coupon_price",coupon_price);
                intent.putExtra("onlyCod",getIntent().getBooleanExtra("onlyCod", false));
                intent.putExtra("total", getIntent().getStringExtra("total"));
                intent.putExtra("loyality_point", getIntent().getStringExtra("loyality_point"));
                intent.putExtra("Express_delivery_chrg",Express_delivery_chrg);
                startActivity(intent);

            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(CheckOutActivity.this, ""+addressId +" , "+name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CheckOutActivity.this,PaymentActivity.class);
                intent.putExtra("cart_id",cart_id);
                intent.putExtra("giftId",giftId);
                intent.putExtra("sub_total",sub_total);
                intent.putExtra("address_id",addressId);
                intent.putExtra("name",name);
                intent.putExtra("area",area);
                intent.putExtra("block",block);
                intent.putExtra("street",street);
                intent.putExtra("avenue",avenue);
                intent.putExtra("house",house);
                intent.putExtra("floor",floor);
                intent.putExtra("flat",flat);
                intent.putExtra("phone",phone);
                intent.putExtra("comments",comments);
                intent.putExtra("coupon_id",coupon_id);
                intent.putExtra("coupon_code",coupon_code);
                intent.putExtra("coupon_price",coupon_price);
                intent.putExtra("total", getIntent().getStringExtra("total"));
                intent.putExtra("onlyCod",getIntent().getBooleanExtra("onlyCod", false));
                intent.putExtra("loyality_point", getIntent().getStringExtra("loyality_point"));
                intent.putExtra("Express_delivery_chrg",Express_delivery_chrg);
                startActivity(intent);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void getUserShippingAddress(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"user_billing_address", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Log.d("getaddresslistsff",response);
                    userBillingAddressModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("user_billing_address_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("wishlist"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            UserBillingAddressModel userBillingAddressModel = new UserBillingAddressModel();
                            userBillingAddressModel.setId(jsonObject2.getString("id"));
                            userBillingAddressModel.setFull_name(jsonObject2.getString("full_name"));
                            userBillingAddressModel.setArea(jsonObject2.getString("area"));
                            userBillingAddressModel.setBlock(jsonObject2.getString("block"));
                            userBillingAddressModel.setStreet(jsonObject2.getString("street"));
                            userBillingAddressModel.setAvenue(jsonObject2.getString("avenue"));
                            userBillingAddressModel.setHouse_no(jsonObject2.getString("house_no"));
                            userBillingAddressModel.setFloor_no(jsonObject2.getString("floor_no"));
                            userBillingAddressModel.setFlat_no(jsonObject2.getString("flat_no"));
                            userBillingAddressModel.setPhone_no(jsonObject2.getString("phone_no"));
                            userBillingAddressModel.setComments(jsonObject2.getString("comments"));
                            userBillingAddressModel.setCurrrent_location(jsonObject2.getString("currrent_location"));
                            userBillingAddressModel.setLatitude(jsonObject2.getString("latitude"));
                            userBillingAddressModel.setLongitude(jsonObject2.getString("longitude"));
                            userBillingAddressModel.setUser_id(jsonObject2.getString("user_id"));
                            userBillingAddressModel.setPaci_number(jsonObject2.getString("paci_number"));
                            userBillingAddressModel.setCitynames(jsonObject2.getString("city_name"));
                            userBillingAddressModelList.add(userBillingAddressModel);
                        }

                    }else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

                //GridLayoutManager gridLayoutManager = new GridLayoutManager(CheckOutActivity.this,1);
                address_recycler_view.setLayoutManager(new LinearLayoutManager(CheckOutActivity.this, RecyclerView.VERTICAL, false));
                userBillingAddressAdapter = new UserBillingAddressAdapter(CheckOutActivity.this,userBillingAddressModelList);
                address_recycler_view.setAdapter(userBillingAddressAdapter);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
            }
        })
        {
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

    private void getGiftList(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"giftlist", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    giftModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONArray jsonArray = new JSONArray(object.getString("result"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            GiftModel giftModel = new GiftModel();
                            giftModel.setId(jsonObject.getString("id"));
                            //giftModel.setTitle(jsonObject.getString("title"));
                            giftModel.setImage(API.GiftsURL+jsonObject.getString("image"));
                            giftModel.setStatus(jsonObject.getString("status"));

                            if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                                giftModel.setTitle(jsonObject.getString("title"));
                                giftModel.setDescription(jsonObject.getString("description"));
                            }else{
                                giftModel.setTitle(jsonObject.getString("title_ar"));
                                giftModel.setDescription(jsonObject.getString("description_ar"));
                            }

                            giftModelList.add(giftModel);
                        }
                    }
                    giftDialogBox();
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
                params.put("current_currency", sessionManager.getCurrencyCode());
                params.put("total_price",sub_total);

                Log.d("sdjhsdjksd",params.toString());

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
        if (CommanMethod.isInternetConnected(CheckOutActivity.this)){
            getUserShippingAddress();
        }
    }

    @Override
    public void getAddressId(String addressId,String name,String area,String block,String street,String avenue,String house,String floor,String flat,String phone,String comments) {
        this.addressId=addressId;
        this.name=name;
        this.area=area;
        this.block=block;
        this.street=street;
        this.avenue=avenue;
        this.house=house;
        this.floor=floor;
        this.flat=flat;
        this.phone=phone;
        this.comments=comments;
    }


    @Override
    public void getGiftId(String giftId) {
        this.giftId = giftId;
    }


    private void expressdeliveryCharge(){
        final Dialog dialogs = CommanMethod.getCustomProgressDialog(this);
        dialogs.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"city_charge",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onResponse(String response) {
                        dialogs.dismiss();
                        Log.d("cityresponse", response);
                        try {
                            JSONObject object=new JSONObject(response);

                            String status=object.getString("status");

                            String delivery_charge_city=object.getString("delivery_charge_city");

                           // BigDecimal number = new BigDecimal(delivery_charge_city);

                           // String formattedBalance = number.stripTrailingZeros().toPlainString();

                            sessionManager.setExpress_Delivery_Charges(delivery_charge_city);
                            Express_delivery_chrg = delivery_charge_city;

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
                arguments.put("address_id", addressId);

                Log.d("arguments", arguments.toString());


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
}
