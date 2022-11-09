package com.lenzzo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.adapter.MyOrderAdapter;
import com.lenzzo.api.API;
import com.lenzzo.interfacelenzzo.OrderListener;
import com.lenzzo.model.MyOrderModel;
import com.lenzzo.utility.CommanClass;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener, OrderListener {

   private SessionManager sessionManager;
   private LinearLayout liner;
   private ImageView cart_image;
   private TextView number;
   private TextView order_count_text;
   private RecyclerView my_order_recycler;
   private GifImageView gifImageView;
   private TextView product_not_av;
   private ImageView back_image;
   private List<MyOrderModel> myOrderModelList;
   private MyOrderAdapter myOrderAdapter;
   private String total_count="";
   private int total_value;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        sessionManager = new SessionManager(this);
        liner = findViewById(R.id.liner);
        back_image = findViewById(R.id.back_image);
        findViewById(R.id.cart_image).setOnClickListener(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        number = findViewById(R.id.number);
        order_count_text = findViewById(R.id.order_count_text);
        my_order_recycler = findViewById(R.id.my_order_recycler);
        gifImageView = findViewById(R.id.gifImageView);
        product_not_av = findViewById(R.id.product_not_av);

        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }
        if (CommanMethod.isInternetConnected(this)){
            getOrderHistory();
            CommanClass.getCartValue(this, number);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.cart_image:
                Intent intent = new Intent(this,UserCartActivity.class);
                startActivity(intent);
                break;

        }
    }


  private void getOrderHistory(){
      final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
      dialog.setCancelable(true);
      dialog.show();
      //gifImageView.setVisibility(View.VISIBLE);
      RequestQueue mRequestQueue = Volley.newRequestQueue(this);
      StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"orderlist", new com.android.volley.Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              try {
                  dialog.dismiss();
                  Log.d("dfjhdfjhdfjh",response);
                  //gifImageView.setVisibility(View.GONE);
                  myOrderModelList = new ArrayList<>();
                  JSONObject object = new JSONObject(response);
                  String status = object.getString("status");
                  if(status.equals("success")){
                      JSONObject jsonObject = new JSONObject(object.getString("response"));
                      JSONObject jsonObject1 = new JSONObject(jsonObject.getString("orderlist_Array"));
                      JSONArray jsonArray = new JSONArray(jsonObject1.getString("orderlist"));
                      product_not_av.setVisibility(View.VISIBLE);
                      order_count_text.setText(getResources().getString(R.string.my_orders)+" ("+jsonArray.length()+")");
                      for(int i=0;i<jsonArray.length();i++){
                          product_not_av.setVisibility(View.GONE);
                          JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                          MyOrderModel myOrderModel = new MyOrderModel();
                          myOrderModel.setId(jsonObject2.getString("id"));
                          myOrderModel.setInvoice_id(jsonObject2.getString("invoice_id"));
                          myOrderModel.setSerial_number(getResources().getString(R.string.order_id)+": "+jsonObject2.getString("serial_number")+" ");
                          myOrderModel.setUser_id(jsonObject2.getString("user_id"));
                          myOrderModel.setPrice(jsonObject2.getString("price"));
                          myOrderModel.setDisplay_price(jsonObject2.getString("display_price"));
                          myOrderModel.setQuantity(jsonObject2.getString("quantity"));
                          myOrderModel.setQuantityLeft(jsonObject2.optString("quantity_left", "0"));
                          myOrderModel.setQuantityRight(jsonObject2.optString("quantity_right", "0"));
                          myOrderModel.setFree_quantity(jsonObject2.getString("free_quantity"));
                          myOrderModel.setGet_type(jsonObject2.getString("get_type"));
                          myOrderModel.setDiscount_type(jsonObject2.getString("discount_type"));
                          myOrderModel.setDiscount(jsonObject2.getString("discount"));
                          myOrderModel.setDisplay_discount(jsonObject2.getString("display_discount"));
                          myOrderModel.setOffer_id(jsonObject2.getString("offer_id"));
                          myOrderModel.setOffer_name(jsonObject2.getString("offer_name"));
                          myOrderModel.setOffer_name_ar(jsonObject2.getString("offer_name_ar"));
                          myOrderModel.setTotal(jsonObject2.getString("total"));
                          myOrderModel.setDisplay_total(jsonObject2.getString("display_total"));
                          myOrderModel.setShade(jsonObject2.getString("shade"));
                          myOrderModel.setLeft_eye_power(jsonObject2.getString("left_eye_power"));
                          myOrderModel.setRight_eye_power(jsonObject2.getString("right_eye_power"));
                          myOrderModel.setCoupon_code(jsonObject2.getString("coupon_code"));
                          myOrderModel.setCoupon_price(jsonObject2.getString("coupon_price"));
                          myOrderModel.setCoupon_id(jsonObject2.getString("coupon_id"));
                          myOrderModel.setTax_cost(jsonObject2.getString("tax_cost"));
                          myOrderModel.setAttribute_values(jsonObject2.getString("attribute_values"));
                          myOrderModel.setIs_email_send_to_seller(jsonObject2.getString("is_email_send_to_seller"));
                          myOrderModel.setEmail_sent_to_seller_count(jsonObject2.getString("email_sent_to_seller_count"));
                          myOrderModel.setEmail_sent_to_shopper_count(jsonObject2.getString("email_sent_to_shopper_count"));
                          myOrderModel.setOrder_shipping_date(jsonObject2.getString("order_shipping_date"));
                          myOrderModel.setEmail_sent_to_shopper_date(jsonObject2.getString("email_sent_to_shopper_date"));
                          myOrderModel.setOffer_discount_price(jsonObject2.getString("offer_discount_price"));
                          myOrderModel.setOffer_date(jsonObject2.getString("offer_date"));
                          myOrderModel.setIs_offer_email_send(jsonObject2.getString("is_offer_email_send"));
                          myOrderModel.setProductstatus(jsonObject2.getString("productstatus"));
                          myOrderModel.setShip_type(jsonObject2.getString("ship_type"));
                          myOrderModel.setShip_price(jsonObject2.getString("ship_price"));
                          myOrderModel.setShip_price_other(jsonObject2.getString("ship_price_other"));
                          myOrderModel.setShip_api_service_name(jsonObject2.getString("ship_api_service_name"));
                          myOrderModel.setShip_handle_price(jsonObject2.getString("ship_handle_price"));
                          myOrderModel.setShipp_api_service_type(jsonObject2.getString("shipp_api_service_type"));
                          myOrderModel.setSeller_pay_type(jsonObject2.getString("seller_pay_type"));
                          myOrderModel.setNote(jsonObject2.getString("note"));
                          myOrderModel.setCreated_at(getResources().getString(R.string.date)+": "+jsonObject2.getString("created_at"));
                          myOrderModel.setUpdated_at(jsonObject2.getString("updated_at"));
                          myOrderModel.setProduct_id(jsonObject2.getString("product_id"));
                          myOrderModel.setCategory_id(jsonObject2.getString("category_id"));
                          myOrderModel.setProduct_name(jsonObject2.getString("product_name"));
                          myOrderModel.setProduct_name_ar(jsonObject2.getString("product_name_ar"));
                          myOrderModel.setOrder_status_code(jsonObject2.getString("order_status_code"));
                          myOrderModel.setShip_id(jsonObject2.getString("ship_id"));
                          myOrderModel.setProduct_image(API.ProductURL+jsonObject2.getString("product_image"));
                          myOrderModel.setCurrent_currency(jsonObject2.getString("current_currency"));
                          myOrderModel.setCurrent_currency_price(jsonObject2.getString("current_currency_price"));
                          myOrderModel.setLoyality_point(jsonObject2.getString("loyality_point"));
                          myOrderModel.setLoyality_point_price(jsonObject2.getString("loyality_point_price"));
                          myOrderModel.setEarn_loyality_point(jsonObject2.getString("earn_loyality_point"));
                          myOrderModel.setEarn_loyality_point_price(jsonObject2.getString("earn_loyality_point_price"));
                          myOrderModel.setGift_id(jsonObject2.getString("gift_id"));
                          myOrderModel.setGift_name(jsonObject2.getString("gift_name"));
                          myOrderModel.setGift_name_ar(jsonObject2.getString("gift_name_ar"));
                          myOrderModel.setEarn_loyality_point_status(jsonObject2.getString("earn_loyality_point_status"));
                          myOrderModel.setPayment_status(jsonObject2.getString("payment_status"));
                          myOrderModel.setOrder_status(jsonObject2.getString("order_status"));
                          myOrderModel.setPayment_mode_id(jsonObject2.getString("payment_mode_id"));


                          myOrderModel.setPayment_mode_name(jsonObject2.getString("payment_mode_name"));
                          myOrderModel.setPayment_mode_name_ar(jsonObject2.getString("payment_mode_name_ar"));


                          myOrderModel.setFull_name(jsonObject2.getString("full_name"));
                          myOrderModel.setArea(jsonObject2.getString("area"));
                          myOrderModel.setBlock(jsonObject2.getString("block"));
                          myOrderModel.setStreet(jsonObject2.getString("street"));
                          myOrderModel.setAvenue(jsonObject2.getString("avenue"));
                          myOrderModel.setHouse_no(jsonObject2.getString("house_no"));
                          myOrderModel.setFloor_no(jsonObject2.getString("floor_no"));
                          myOrderModel.setFlat_no(jsonObject2.getString("flat_no"));
                          myOrderModel.setPhone_no(jsonObject2.getString("phone_no"));
                          myOrderModel.setComments(jsonObject2.getString("comments"));
                          myOrderModel.setLatitude(jsonObject2.getString("latitude"));
                          myOrderModel.setLongitude(jsonObject2.getString("longitude"));
                          myOrderModel.setVat_price(jsonObject2.getString("vat_value"));
                          myOrderModel.setVat_charge(jsonObject2.getString("vat_charge"));
                          myOrderModel.setCurrrent_location(jsonObject2.getString("currrent_location"));
                          myOrderModel.setTotal_order_price(jsonObject2.getString("total_order_price"));
                          myOrderModel.setDelivery_charge(jsonObject2.getString("delivery_charge"));
                          myOrderModel.setFeedbackStatus(jsonObject2.getString("feedback_status"));
                          myOrderModel.setOrderTrackingId(jsonObject2.optString("order_tracking_id", ""));

                          myOrderModel.setLeftAddition(jsonObject2.getString("left_add"));
                          myOrderModel.setRightAddition(jsonObject2.getString("right_add"));
                          myOrderModel.setLeftCyl(jsonObject2.getString("left_cyl"));
                          myOrderModel.setRightCyl(jsonObject2.getString("right_cyl"));
                          myOrderModel.setLeftAxis(jsonObject2.getString("left_axis"));
                          myOrderModel.setRightAxis(jsonObject2.getString("right_axis"));
                          myOrderModel.setPowerSelectionType(jsonObject2.getString("power_selection_type"));
                          myOrderModel.setIsSpecialOffer(jsonObject2.getString("is_special_offer"));
                          myOrderModel.setSpecialPrice(jsonObject2.getString("special_price"));
                          myOrderModel.setSpecialDiscount(jsonObject2.getString("special_discount"));
                          myOrderModel.setSpecialDiscountType(jsonObject2.getString("special_discount_type"));
                          myOrderModel.setSpecialPriceWithPower(jsonObject2.getString("special_price_with_power"));
                          myOrderModelList.add(myOrderModel);
                      }
                      //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_success), Toast.LENGTH_SHORT).show();
                  }else{

                      //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_fail)+message, Toast.LENGTH_SHORT).show();
                  }
              }catch (JSONException e){
                  e.printStackTrace();
                  dialog.dismiss();
                  //gifImageView.setVisibility(View.GONE);
              }

              //GridLayoutManager gridLayoutManager = new GridLayoutManager(MyOrderActivity.this,1);
              my_order_recycler.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this, RecyclerView.VERTICAL, false));
              myOrderAdapter = new MyOrderAdapter(MyOrderActivity.this,myOrderModelList);
              my_order_recycler.setAdapter(myOrderAdapter);


          }
      }, new com.android.volley.Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              //gifImageView.setVisibility(View.GONE);
              dialog.dismiss();
          }
      }){
          @Override
          protected Map<String, String> getParams()
          {
              Map<String, String>  params = new HashMap<String, String>();
              params.put("user_id",sessionManager.getUserId());
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

    public void getCartValue(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"usercart", new com.android.volley.Response.Listener<String>() {
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

    private void addToCart(MyOrderModel myOrder){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"usercart_add", response -> {
            try {
                dialog.dismiss();
                JSONObject object = new JSONObject(response);
                String status = object.getString("status");
                if(status.equals("success")){
                    CommanClass.getCartValue(MyOrderActivity.this, number);
                    successAlert();

                }else{
                    Toast.makeText(MyOrderActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                dialog.dismiss();
                e.printStackTrace();
            }
        }, error -> dialog.dismiss()){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id", myOrder.getUser_id());
                params.put("right_eye_power",myOrder.getRight_eye_power());
                params.put("quantity",myOrder.getQuantity());
                params.put("left_eye_power",myOrder.getLeft_eye_power());
                params.put("power_selection_type",myOrder.getPowerSelectionType());
                params.put("product_id",myOrder.getProduct_id());
                params.put("quantity_right",myOrder.getQuantityRight());
                params.put("quantity_left",myOrder.getQuantityLeft());
                params.put("left_cyl",myOrder.getLeftCyl());
                params.put("right_cyl",myOrder.getRightCyl());
                params.put("left_axis",myOrder.getLeftAxis());
                params.put("right_axis",myOrder.getRightAxis());
                params.put("right_add",myOrder.getRightAddition());
                params.put("left_add",myOrder.getLeftAddition());
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

    private void successAlert(){
        final Dialog dialog = new Dialog(this);
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
                //finish();
            }
        });
        go_to_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyOrderActivity.this, UserCartActivity.class);
                startActivity(intent);
                dialog.dismiss();
                //addToCart(product_id,getBrand_id,getBrand_Name,"goToCart");
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onReorder(MyOrderModel orderModel) {
        addToCart(orderModel);
    }
}
