package com.lenzzo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.adapter.CartParentAdapter;
import com.lenzzo.api.API;
import com.lenzzo.interfacelenzzo.UserCartInterface;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.model.CartFamilyModel;
import com.lenzzo.model.UserCartModel;
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

public class UserCartActivity extends BaseActivity implements View.OnClickListener, UserCartInterface {

    private RecyclerView bag_recycler_view;
    private List<CartFamilyModel> cartFamilyModels;
    private List<UserCartModel> userCartModelList;
    //private UserCartAdapter userCartAdapter;
    private CartParentAdapter cartParentAdapter;
    private SessionManager sessionManager;
    private TextView total_price_text_view;
    private float total;
    private String current_currency;
    private RelativeLayout no_pro_relative_layout;
    private LinearLayout relativeLayout1;
    private String sub_total;
    private String cart_id = "";
    private String out_of_stock = "";
    //private LinearLayout offer_layout;
    private TextView offer_name;
    private StringBuilder stringBuilder;
    private List<String> product_offer_name;
    private TextView cart_total_price_text_view;
    private LinearLayout liner_layout_discount;
    private TextView discount_price_text_view;
    private EditText promo_code_edittext;
    private String get_promo_code;
    private String coupon_id = "";
    private String coupon_code = "";
    private String coupon_price = "";
    //private String coupon_total="";
    //private float coupon_sub_total;
    private Button remove_button;
    private Button apply_button;
    private ImageView back_image;
    private LinearLayout liner;
    private LinearLayout redeem_layout;
    private LinearLayout redeemed_layout;
    private TextView redeem_point_text;
    private TextView redeemed_point_text;
    private Button redeem_button;
    private Button redeem_remove_button;
    private String st_redeem_point;
    private String red_point_price = "";
    private TextView loyalty_price_text_view;
    private LinearLayout liner_layout_loyalty_point;
    private TextView loyalty_point_text_view;
    private LinearLayout liner_layout_loyalty_point_add;
    private boolean onlyCod = false;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        sessionManager = new SessionManager(this);
        sessionManager.clearSelectedredeempoint();
        sessionManager.clearNewSubTotal();

        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.home_back).setOnClickListener(this);
        bag_recycler_view = findViewById(R.id.bag_recycler_view);
        findViewById(R.id.liner_layout).setOnClickListener(this);
        findViewById(R.id.liner_layout2).setOnClickListener(this);
        total_price_text_view = findViewById(R.id.total_price_text_view);
        no_pro_relative_layout = findViewById(R.id.no_pro_relative_layout);
        relativeLayout1 = findViewById(R.id.relativeLayout1);
        //offer_layout = (LinearLayout)findViewById(R.id.offer_layout);
        offer_name = findViewById(R.id.offer_name);
        cart_total_price_text_view = findViewById(R.id.cart_total_price_text_view);
        liner_layout_discount = findViewById(R.id.liner_layout_discount);
        liner_layout_discount.setVisibility(View.GONE);
        discount_price_text_view = findViewById(R.id.discount_price_text_view);
        promo_code_edittext = findViewById(R.id.promo_code_edittext);
        apply_button = findViewById(R.id.apply_button);
        findViewById(R.id.apply_button).setOnClickListener(this);
        remove_button = findViewById(R.id.remove_button);
        findViewById(R.id.remove_button).setOnClickListener(this);
        redeem_layout = findViewById(R.id.redeem_layout);
        redeemed_layout = findViewById(R.id.redeemed_layout);
        redeem_point_text = findViewById(R.id.redeem_point_text);
        redeemed_point_text = findViewById(R.id.redeemed_point_text);
        redeem_button = findViewById(R.id.redeem_button);
        findViewById(R.id.redeem_button).setOnClickListener(this);
        redeem_remove_button = findViewById(R.id.redeem_remove_button);
        findViewById(R.id.redeem_remove_button).setOnClickListener(this);
        loyalty_price_text_view = findViewById(R.id.loyalty_price_text_view);
        liner_layout_loyalty_point = findViewById(R.id.liner_layout_loyalty_point);
        liner_layout_loyalty_point_add = findViewById(R.id.liner_layout_loyalty_point_add);
        loyalty_point_text_view = findViewById(R.id.loyalty_point_text_view);

        sessionManager.clearSelectedredeempoint();
        sessionManager.setRedeemPointFlagStatus("1");

        back_image = findViewById(R.id.back_image);
        //liner = (LinearLayout)findViewById(R.id.liner);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            back_image.setImageResource(R.drawable.arrow_30);
            //liner.setGravity(Gravity.END);
            ((LinearLayout) findViewById(R.id.liner_layout_cart_total)).setGravity(Gravity.END);
            ((LinearLayout) findViewById(R.id.liner_layout_discount)).setGravity(Gravity.END);
            ((LinearLayout) findViewById(R.id.liner_layout_loyalty_point)).setGravity(Gravity.END);
            ((LinearLayout) findViewById(R.id.liner_layout1)).setGravity(Gravity.END);

        } else {
            back_image.setImageResource(R.drawable.arrow_right_30);
            //liner.setGravity(Gravity.START);
            ((LinearLayout) findViewById(R.id.liner_layout_cart_total)).setGravity(Gravity.START);
            ((LinearLayout) findViewById(R.id.liner_layout_discount)).setGravity(Gravity.START);
            ((LinearLayout) findViewById(R.id.liner_layout_loyalty_point)).setGravity(Gravity.START);
            ((LinearLayout) findViewById(R.id.liner_layout1)).setGravity(Gravity.START);
        }
        if (CommanMethod.isInternetConnected(UserCartActivity.this)) {
            final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
            dialog.setCancelable(true);
            dialog.show();
            getCartValue(dialog);
        }

        if (sessionManager.getUserType().equals("Guest")) {
            findViewById(R.id.loyalty_for_reg_tv).setVisibility(View.VISIBLE);
        } else if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
            findViewById(R.id.loyalty_for_reg_tv).setVisibility(View.GONE);
        } else {
            findViewById(R.id.loyalty_for_reg_tv).setVisibility(View.VISIBLE);
            //navView.getMenu().removeItem(R.id.navigation_my_account);//hide for BottomNavigationView items
        }

        findViewById(R.id.loyalty_for_reg_tv).setOnClickListener(this);
        //  Toast.makeText(this, sessionManager.getNewSubTotal(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.home_back:
                Intent intent = new Intent(UserCartActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.liner_layout:
                Intent intent1 = new Intent(UserCartActivity.this, WishListActivity.class);
                startActivity(intent1);
                break;
            case R.id.liner_layout2:
                toCheckCartValue();
                /*if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                                if(out_of_stock.equals("0")){
                                    dialogBox();
                                }else{
                                    Intent intent2 = new Intent(UserCartActivity.this,CheckOutActivity.class);
                                    //BigDecimal item_su_total = new BigDecimal(sub_total).setScale(2, RoundingMode.HALF_UP);
                                    BigDecimal item_su_total = CommanMethod.getCountryWiseDecimalNumber(UserCartActivity.this, sub_total);
                                    intent2.putExtra("sub_total",""+item_su_total);
                                    intent2.putExtra("current_currency",current_currency);
                                    intent2.putExtra("cart_id",cart_id);
                                    intent2.putExtra("coupon_id",coupon_id);
                                    intent2.putExtra("coupon_code",coupon_code);
                                    intent2.putExtra("coupon_price",coupon_price);
                                    intent2.putExtra("coupon_price",coupon_price);
                                    intent2.putExtra("loyality_point",redeemed_point_text.getText().toString());
                                    startActivity(intent2);
                                }
                            }else{
                                Intent intent2 = new Intent(UserCartActivity.this, LoginActivity.class);
                                intent2.putExtra("from", "cart");
                                startActivityForResult(intent2, CommanMethod.LOGIN_REQ_CODE);

                            }*/
                break;
            case R.id.apply_button:
                validation();
                break;
            case R.id.remove_button:
                sub_total = String.valueOf(Float.parseFloat(sub_total) + Float.parseFloat(coupon_price));
                coupon_id = "";
                coupon_code = "";
                coupon_price = "";
                promo_code_edittext.setEnabled(true);
                // coupon_total="";
                //BigDecimal su_total = new BigDecimal(sub_total).setScale(2, RoundingMode.HALF_UP);
                BigDecimal su_total = CommanMethod.getCountryWiseDecimalNumber(UserCartActivity.this, sub_total);
                total_price_text_view.setText(su_total + " " + current_currency);
                earnRedeemPoint("" + su_total);
                liner_layout_discount.setVisibility(View.GONE);
                apply_button.setVisibility(View.VISIBLE);
                remove_button.setVisibility(View.GONE);
                promo_code_edittext.setText("");
                break;
            case R.id.redeem_button:
                if (CommanMethod.isInternetConnected(UserCartActivity.this)) {
                    getRedeemPoint();
                }
                break;
            case R.id.redeem_remove_button:
                sessionManager.clearSelectedredeempoint();
                sessionManager.setRedeemPointFlagStatus("0");
                sub_total = String.valueOf(Float.parseFloat(sub_total) + Float.parseFloat(red_point_price));
                //BigDecimal red_su_total = new BigDecimal(sub_total).setScale(2, RoundingMode.HALF_UP);
                BigDecimal red_su_total = CommanMethod.getCountryWiseDecimalNumber(this, sub_total);
                total_price_text_view.setText(red_su_total + " " + current_currency);
                redeem_point_text.setText(st_redeem_point);

                redeemed_point_text.setText("0.00");

                redeemed_layout.setVisibility(View.GONE);
                redeem_remove_button.setVisibility(View.GONE);
                liner_layout_loyalty_point.setVisibility(View.GONE);
                liner_layout_loyalty_point_add.setVisibility(View.VISIBLE);
                break;

            case R.id.loyalty_for_reg_tv:
                Intent regIntent = new Intent(UserCartActivity.this, RegisterActivity.class);
                regIntent.putExtra("from", "cart");
                //startActivity(regIntent);
                startActivityForResult(regIntent, CommanMethod.LOGIN_REQ_CODE);
                break;

        }
    }

    private void validation() {
        get_promo_code = promo_code_edittext.getText().toString();
        if (get_promo_code.equals("") || get_promo_code.length() == 0) {
            //Toast.makeText(this, this.getString(R.string.toast_message_promo_code_hint), Toast.LENGTH_SHORT).show();
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_promo_code_hint));
        } else {
            if (!sub_total.equals("0.0")) {
                if (CommanMethod.isInternetConnected(UserCartActivity.this)) {
                    getPromoCode();
                }
            } else {
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.sub_total_price) + " " + sub_total);
                //Toast.makeText(this, this.getString(R.string.sub_total_price)+" "+sub_total, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dialogBox() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.success_dialog_box);
        //TextView alert_text = (TextView)dialog.findViewById(R.id.item_text_view);
        //alert_text.setText(this.getString(R.string.user_cart_alert));
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        //info_tv.setText(getResources().getString(R.string.dialog_product_not_availabel));
        //TextView item_text_view1 = (TextView)dialog.findViewById(R.id.item_text_view1);
        info_tv.setText(this.getString(R.string.user_cart_out_of_stock));
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        ok_tv.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    public void getCartValue(Dialog dialog) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1 + "usercart", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(String response) {
                try {
                    //dialog.dismiss();
                    cartFamilyModels = new ArrayList<>();
                    product_offer_name = new ArrayList<>();
                    stringBuilder = new StringBuilder();
                    StringBuilder stringBuilder = new StringBuilder();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));
                        relativeLayout1.setVisibility(View.GONE);
                        no_pro_relative_layout.setVisibility(View.VISIBLE);
                        String cartGrandTotal = jsonObject1.getString("cartGrandTotal");
                        sessionManager.setOldTotal(cartGrandTotal);

                        if (jsonArray.length() > 0) {
                            relativeLayout1.setVisibility(View.VISIBLE);
                            no_pro_relative_layout.setVisibility(View.GONE);
                            for (int j = 0; j < jsonArray.length(); j++) {
                                List<UserCartModel> userCartModelList = new ArrayList<>();
                                CartFamilyModel cartFamilyModel = new CartFamilyModel();

                                JSONObject childObject = jsonArray.getJSONObject(j);

                                String family_id = childObject.optString("family_id");
                                String family_name = childObject.optString("family_name");
                                String family_name_ar = childObject.optString("family_name_ar");
                                String price = childObject.optString("price");
                                String totalValue = childObject.optString("total");
                                String quantity = childObject.optString("quantity");
                                String quantity_left = childObject.optString("quantity_left");
                                String quantity_right = childObject.optString("quantity_right");
                                String free_quantity = childObject.optString("free_quantity");
                                String get_type = childObject.optString("get_type");
                                String discount_type = childObject.optString("discount_type");
                                current_currency = childObject.optString("current_currency");
                                String offer_flag = childObject.optString("offer_flag");
                                String offer_id = childObject.optString("offer_id");
                                String offer_name = childObject.optString("offer_name");
                                String offer_name_ar = childObject.optString("offer_name_ar");
                                cartFamilyModel.setFamilyId(family_id);
                                cartFamilyModel.setFamilyNameAr(family_name_ar);
                                cartFamilyModel.setPrice(price);
                                cartFamilyModel.setTotal(totalValue);
                                cartFamilyModel.setQuantity(quantity);
                                cartFamilyModel.setQuantityLeft(quantity_left);
                                cartFamilyModel.setQuantityRight(quantity_right);
                                cartFamilyModel.setFreeQuantity(free_quantity);
                                cartFamilyModel.setGetType(get_type);
                                cartFamilyModel.setDiscountType(discount_type);
                                cartFamilyModel.setCurrentCurrency(current_currency);
                                cartFamilyModel.setOfferFlag(offer_flag);
                                cartFamilyModel.setOfferId(offer_id);
                                cartFamilyModel.setOfferNameAr(offer_name_ar);

                                if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                                    cartFamilyModel.setFamilyName(family_name);
                                    cartFamilyModel.setOfferName(offer_name);
                                } else {
                                    cartFamilyModel.setFamilyName(family_name_ar);
                                    cartFamilyModel.setOfferName(offer_name_ar);
                                }
                                JSONArray childArray = childObject.getJSONArray("child");
                                for (int i = 0; i < childArray.length(); i++) {
                                    JSONObject jsonObject2 = childArray.getJSONObject(i);
                                    UserCartModel userCartModel = new UserCartModel();
                                    userCartModel.setId(jsonObject2.getString("id"));
                                    userCartModel.setUser_id(jsonObject2.getString("user_id"));
                                    userCartModel.setStock_flag(jsonObject2.getString("stock_flag"));
                                    if (jsonObject2.getString("stock_flag").equals("0")) {
                                        out_of_stock = jsonObject2.getString("stock_flag");
                                    }
                                    if (jsonObject2.getString("offer_flag").equals("1")) {
                                        product_offer_name.add("● " + jsonObject2.getString("product_name").trim());
                                    }
                                    userCartModel.setProduct_quantity(jsonObject2.getString("product_quantity"));
                                    userCartModel.setProduct_id(jsonObject2.getString("product_id"));
                                    userCartModel.setProduct_name(jsonObject2.getString("product_name"));
                                    userCartModel.setProduct_image(API.ProductURL + jsonObject2.getString("product_image"));
                                    userCartModel.setCategory_id(jsonObject2.getString("category_id"));
                                    userCartModel.setCoupon_id(jsonObject2.getString("coupon_id"));
                                    userCartModel.setShip_id(jsonObject2.getString("ship_id"));
                                    userCartModel.setPrice(jsonObject2.getString("price"));
                                    userCartModel.setPrice_main(jsonObject2.getString("price_main"));
                                    userCartModel.setCurrent_currency(jsonObject2.getString("current_currency"));
                                    userCartModel.setSale_price(jsonObject2.getString("sale_price"));
                                    userCartModel.setQuantity(jsonObject2.getString("quantity"));
                                    userCartModel.setLeft_eye_power(jsonObject2.getString("left_eye_power"));
                                    userCartModel.setRight_eye_power(jsonObject2.getString("right_eye_power"));
                                    userCartModel.setTotal(jsonObject2.getString("total"));
                                    userCartModel.setTax_cost(jsonObject2.getString("tax_cost"));
                                    userCartModel.setCoupon_code(jsonObject2.getString("coupon_code"));
                                    userCartModel.setAttribute_values(jsonObject2.getString("attribute_values"));
                                    userCartModel.setOffer_discount_price(jsonObject2.getString("offer_discount_price"));
                                    userCartModel.setOffer_date(jsonObject2.getString("offer_date"));
                                    userCartModel.setShip_type(jsonObject2.getString("ship_type"));
                                    userCartModel.setShip_price(jsonObject2.getString("ship_price"));
                                    userCartModel.setShip_price_main(jsonObject2.getString("ship_price_main"));
                                    userCartModel.setShip_price_other(jsonObject2.getString("ship_price_other"));
                                    userCartModel.setShip_api_service_name(jsonObject2.getString("ship_api_service_name"));
                                    userCartModel.setShip_handle_price(jsonObject2.getString("ship_handle_price"));
                                    userCartModel.setShipp_api_service_type(jsonObject2.getString("shipp_api_service_type"));
                                    userCartModel.setSeller_pay_type(jsonObject2.getString("seller_pay_type"));
                                    userCartModel.setNote(jsonObject2.getString("note"));
                                    userCartModel.setBasePriceLeft(jsonObject2.getString("base_price_left"));
                                    userCartModel.setBasePriceRight(jsonObject2.getString("base_price_right"));
                                    userCartModel.setQuantityLeft(jsonObject2.getString("quantity_left"));
                                    userCartModel.setQuantityRight(jsonObject2.getString("quantity_right"));

                                    userCartModel.setLeftAddition(jsonObject2.getString("left_add"));
                                    userCartModel.setRightAddition(jsonObject2.getString("right_add"));
                                    userCartModel.setLeftCyl(jsonObject2.getString("left_cyl"));
                                    userCartModel.setRightCyl(jsonObject2.getString("right_cyl"));
                                    userCartModel.setLeftAxis(jsonObject2.getString("left_axis"));
                                    userCartModel.setRightAxis(jsonObject2.getString("right_axis"));
                                    userCartModel.setPowerSelectionType(jsonObject2.getString("power_selection_type"));
                                    if (jsonObject2.getString("power_selection_type").equals("2")
                                            || jsonObject2.getString("power_selection_type").equals("3")) {
                                        onlyCod = true;
                                    }

                                    userCartModel.setIsSpecialOffer(jsonObject2.getString("is_special_offer"));
                                    userCartModel.setSpecialPrice(jsonObject2.getString("special_price"));
                                    userCartModel.setSpecialDiscount(jsonObject2.getString("special_discount"));
                                    userCartModel.setSpecialDiscountType(jsonObject2.getString("special_discount_type"));
                                    userCartModel.setSpecialPriceWithPower(jsonObject2.getString("special_price_with_power"));
                                    //total = total+Float.parseFloat(jsonObject2.getString("price"))*Float.parseFloat(jsonObject2.getString("quantity"));

                                    stringBuilder.append(jsonObject2.getString("id")).append(",");

                                    userCartModelList.add(userCartModel);

                                }

                                total = total + Float.parseFloat(totalValue.replace(",", ""));
                                //current_currency = jsonObject2.getString("current_currency");

                                cartFamilyModel.setUserCartModelList(userCartModelList);
                                cartFamilyModels.add(cartFamilyModel);
                            }

                            cart_id = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
                        }
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }

                //System.out.println("cart list id"+cart_id);
                if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                    //sub_total = String.format("%.2f",total);
                    BigDecimal bd = CommanMethod.getCountryWiseDecimalNumberFloatInput(UserCartActivity.this, total);
                    sub_total = String.valueOf(bd);
                    cart_total_price_text_view.setText(bd + " " + current_currency);
                    total_price_text_view.setText(bd + " " + current_currency);
                    earnRedeemPoint(sub_total);
                } else {
                    //BigDecimal bd = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal bd = CommanMethod.getCountryWiseDecimalNumberFloatInput(UserCartActivity.this, total);
                    sub_total = String.valueOf(bd);
                    cart_total_price_text_view.setText(bd + " " + current_currency);
                    total_price_text_view.setText(bd + " " + current_currency);
                    earnRedeemPoint(sub_total);
                }


                if (!product_offer_name.isEmpty()) {
                    //offer_layout.setVisibility(View.VISIBLE);
                    //String details = "";
                    for (int i = 0; i < product_offer_name.size(); i++) {
                        if (i == (product_offer_name.size() - 1)) {
                            stringBuilder.append(product_offer_name.get(i));
                        } else {
                            stringBuilder.append(product_offer_name.get(i) + "\n");
                        }
                    }

                    offer_name.setText(stringBuilder.toString());
                } else {
                    //offer_layout.setVisibility(View.GONE);
                }

                /*bag_recycler_view.setLayoutManager(new LinearLayoutManager(UserCartActivity.this, RecyclerView.VERTICAL, false));
                userCartAdapter = new UserCartAdapter(UserCartActivity.this, userCartModelList);
                bag_recycler_view.setAdapter(userCartAdapter);*/

                bag_recycler_view.setLayoutManager(new LinearLayoutManager(UserCartActivity.this, RecyclerView.VERTICAL, false));
                cartParentAdapter = new CartParentAdapter(UserCartActivity.this, cartFamilyModels);
                bag_recycler_view.setAdapter(cartParentAdapter);

                getTotalRedeemPoint(dialog);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    String jsonError1 = new String(networkResponse.data);
                    Log.e("Cart", jsonError1);
                    // Print Error!
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
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

    private void getPromoCode() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.setCancelable(true);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1 + "couponApply", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    Log.d("", response);
                    String message = CommanMethod.getMessage(UserCartActivity.this, object);
                    if (status.equals("success")) {
                        promo_code_edittext.setEnabled(false);
                        remove_button.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = new JSONObject(object.getString("result"));
                        coupon_id = jsonObject.getString("id");
                        coupon_code = jsonObject.getString("coupon_code");
                        coupon_price = jsonObject.getString("coupon_price");
                        sub_total = String.valueOf(Float.parseFloat(sub_total) - Float.parseFloat(coupon_price));
                        //coupon_total = String.format("%.2f",coupon_sub_total);
                        //BigDecimal cp_price = new BigDecimal(sub_total).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal cp_price = CommanMethod.getCountryWiseDecimalNumber(UserCartActivity.this, sub_total);
                        discount_price_text_view.setText(coupon_price + " " + current_currency);

                        if (Double.parseDouble(coupon_price) >= total) { //Double.parseDouble(sub_total)
                            total_price_text_view.setText("0" + " " + current_currency);
                        } else {
                            total_price_text_view.setText(cp_price + " " + current_currency);
                        }

                        liner_layout_discount.setVisibility(View.VISIBLE);
                        earnRedeemPoint("" + cp_price);
                        CommanMethod.getCustomOkAlert(UserCartActivity.this, getString(R.string.toast_message_success));
                        // Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_success), Toast.LENGTH_SHORT).show();
                    } else {
                        remove_button.setVisibility(View.GONE);
                        total_price_text_view.setText(sub_total + " " + current_currency);
                        liner_layout_discount.setVisibility(View.GONE);
                        CommanMethod.getCustomOkAlert(UserCartActivity.this, message);
                        //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_fail)+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("coupon_code", get_promo_code);
                params.put("price", sub_total);
                //params.put("user_id",sessionManager.getUserId());
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
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

    private void getTotalRedeemPoint(Dialog dialog) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1 + "totalRedeemPoint", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {//{"status":"failed","message":"Something went wrong."}
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        if (Integer.parseInt(object.getString("point")) > 0) {
                            redeem_layout.setVisibility(View.VISIBLE);
                            //findViewById(R.id.loyalty_for_reg_tv).setVisibility(View.GONE);
                            st_redeem_point = object.getString("point");
                            redeem_point_text.setText(st_redeem_point);
                        } else {
                            redeem_layout.setVisibility(View.GONE);
                            //findViewById(R.id.loyalty_for_reg_tv).setVisibility(View.VISIBLE);
                        }
                        //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_success), Toast.LENGTH_SHORT).show();
                    } else {
                        redeem_layout.setVisibility(View.GONE);
                        //findViewById(R.id.loyalty_for_reg_tv).setVisibility(View.VISIBLE);
                        //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_fail)+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("user_id",sessionManager.getUserId());
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
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

    private void getRedeemPoint() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.setCancelable(true);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1 + "redeemPoint", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    Log.d("sdjdfdfdf", response);
                    JSONObject object = new JSONObject(response);
                    System.out.println("redeem Point " + response);
                    String status = object.getString("status");
                    String message = CommanMethod.getMessage(UserCartActivity.this, object);
                    sessionManager.setRedeemPointStatus(status);

                    if (status.equals("pending_payment") || status.equals("success")) {
                        // sessionManager.setRedeemPointStatus(status);
                        sessionManager.setRedeemPointFlagStatus("3");
                        redeemed_layout.setVisibility(View.VISIBLE);
                        redeem_remove_button.setVisibility(View.VISIBLE);
                        liner_layout_loyalty_point.setVisibility(View.VISIBLE);
                        int rem_point = Integer.parseInt(st_redeem_point) - Integer.parseInt(object.getString("point"));
                        redeem_point_text.setText(String.valueOf(rem_point));
                        redeemed_point_text.setText(object.getString("point"));
                        red_point_price = object.getString("price");
                        sub_total = String.valueOf(Float.parseFloat(sub_total) - Float.parseFloat(red_point_price));
                        //BigDecimal red_po = new BigDecimal(red_point_price).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal red_po = CommanMethod.getCountryWiseDecimalNumber(UserCartActivity.this, red_point_price);
                        loyalty_price_text_view.setText(red_po + " " + current_currency);
                        ///BigDecimal bd = new BigDecimal(sub_total).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal bd = CommanMethod.getCountryWiseDecimalNumber(UserCartActivity.this, sub_total);
                        total_price_text_view.setText(bd + " " + current_currency);
                        //red_point_price = String.valueOf(bd);
                        if (sub_total.equals("0.0")) {
                            // sessionManager.setRedeemPointFlagStatus("2");
                            liner_layout_loyalty_point_add.setVisibility(View.GONE);
                            //findViewById(R.id.loyalty_for_reg_tv).setVisibility(View.GONE);
                        } else {
                            //sessionManager.setRedeemPointFlagStatus("3");

                            /*if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                                findViewById(R.id.loyalty_for_reg_tv).setVisibility(View.GONE);
                            }else{
                                findViewById(R.id.loyalty_for_reg_tv).setVisibility(View.VISIBLE);
                            }*/

                        }
                        //System.out.println("red_point_price"+red_point_price);
                        //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_success), Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(UserCartActivity.this, message, Toast.LENGTH_SHORT).show();
                        //sessionManager.setRedeemPointFlagStatus("1");
                        CommanMethod.getCustomOkAlert(UserCartActivity.this, message);
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("user_id",sessionManager.getUserId());
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
                params.put("price", sub_total);
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

    private void earnRedeemPoint(final String sub_total_price) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1 + "earn_redeemPoint", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        liner_layout_loyalty_point_add.setVisibility(View.VISIBLE);
                        loyalty_point_text_view.setText(object.getString("point") + " ");
                        //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_success), Toast.LENGTH_SHORT).show();
                    } else {

                        liner_layout_loyalty_point_add.setVisibility(View.GONE);
                        //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_fail)+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("price", sub_total_price);
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

    @Override
    public void getUserCartValue(Dialog dialog) {
        total = 0;
        out_of_stock = "";
        sub_total = "";
        redeem_point_text.setText(st_redeem_point);
        redeemed_layout.setVisibility(View.GONE);
        redeem_remove_button.setVisibility(View.GONE);
        remove_button.setVisibility(View.GONE);
        apply_button.setVisibility(View.VISIBLE);
        liner_layout_discount.setVisibility(View.GONE);
        promo_code_edittext.setText("");
        promo_code_edittext.setEnabled(true);
        liner_layout_loyalty_point.setVisibility(View.GONE);
        if (CommanMethod.isInternetConnected(UserCartActivity.this)) {
            getCartValue(dialog);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        /*if (resultCode == Activity.RESULT_OK){

            if (requestCode == CommanMethod.LOGIN_REQ_CODE){
                *//*if (CommanMethod.isInternetConnected(UserCartActivity.this)){
                    getCartValue();
                    getTotalRedeemPoint();
                }*//*
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }*/
    }

    public void toCheckCartValue() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.setCancelable(true);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL1 + "usercart", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    boolean shouldProceed = true;
                    float totalToCheck = 0f;
                    StringBuilder stringBuilder = new StringBuilder();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));
                        String cartGrandTotal = jsonObject1.getString("cartGrandTotal");
                        sessionManager.setNewTotal(cartGrandTotal);

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject childObject = jsonArray.getJSONObject(j);
                            JSONArray childArray = childObject.getJSONArray("child");
                            for (int i = 0; i < childArray.length(); i++) {
                                JSONObject jsonObject2 = childArray.getJSONObject(i);
                                UserCartModel userCartModel = new UserCartModel();
                                userCartModel.setId(jsonObject2.getString("id"));
                                userCartModel.setUser_id(jsonObject2.getString("user_id"));
                                userCartModel.setStock_flag(jsonObject2.getString("stock_flag"));
                                String stock_flag = jsonObject2.getString("stock_flag");
                                String product_quantity = jsonObject2.getString("product_quantity");
                                String quantity = jsonObject2.getString("quantity");

                                String total = jsonObject2.getString("total");


                                if (CommanMethod.isOutOfStock(stock_flag, quantity)) {
                                    shouldProceed = false;
                                    break;
                                }
                                totalToCheck = totalToCheck + Float.parseFloat(total.replace(",", ""));
                                stringBuilder.append(jsonObject2.getString("id")).append(",");
                            }

                        }

                        String cart_id = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();

                        if (!shouldProceed) {
                            dialogBox();
                        } else if (!cart_id.equals(UserCartActivity.this.cart_id) && total != totalToCheck) {
                            CommanMethod.getCustomGOHome(UserCartActivity.this, getResources().getString(R.string.something_wrong));

                        } else if (!sessionManager.getOldTotal().equals(sessionManager.getNewTotal())) {
                            dialogBoxtwo();
                        } else {
                            if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                                Intent intent2 = new Intent(UserCartActivity.this, CheckOutActivity.class);
                                //BigDecimal item_su_total = new BigDecimal(sub_total).setScale(2, RoundingMode.HALF_UP);
                                BigDecimal item_su_total = CommanMethod.getCountryWiseDecimalNumber(UserCartActivity.this, sub_total);
                                try {
                                    if (coupon_price.isEmpty()) {
                                        sessionManager.setNewSubTotal(String.valueOf(item_su_total));
                                        intent2.putExtra("sub_total", "" + item_su_total);
                                        intent2.putExtra("current_currency", current_currency);
                                        intent2.putExtra("cart_id", cart_id);
                                        intent2.putExtra("coupon_id", coupon_id);
                                        intent2.putExtra("coupon_code", coupon_code);
                                        intent2.putExtra("coupon_price", coupon_price);
                                        intent2.putExtra("coupon_price", coupon_price);
                                        intent2.putExtra("total", "" + total);
                                        intent2.putExtra("onlyCod", onlyCod);
                                        if (!sessionManager.getRedeemPointFlagStatus().equals("0")) {
                                            intent2.putExtra("loyality_point", redeemed_point_text.getText().toString());
                                        } else {
                                            intent2.putExtra("loyality_point", "0");
                                        }
                                        startActivity(intent2);
                                    } else if (Double.parseDouble(coupon_price) >= Double.parseDouble(String.valueOf(item_su_total))) {
                                        intent2.putExtra("sub_total", "0");
                                        sessionManager.setNewSubTotal("0");
                                        intent2.putExtra("current_currency", current_currency);
                                        intent2.putExtra("cart_id", cart_id);
                                        intent2.putExtra("coupon_id", coupon_id);
                                        intent2.putExtra("coupon_code", coupon_code);
                                        intent2.putExtra("coupon_price", coupon_price);
                                        intent2.putExtra("coupon_price", coupon_price);
                                        intent2.putExtra("total", "" + total);
                                        intent2.putExtra("onlyCod", onlyCod);
                                        if (!sessionManager.getRedeemPointFlagStatus().equals("0")) {
                                            intent2.putExtra("loyality_point", redeemed_point_text.getText().toString());
                                        } else {
                                            intent2.putExtra("loyality_point", "0");
                                        }
                                        startActivity(intent2);
                                    } else {
                                        sessionManager.setNewSubTotal(String.valueOf(item_su_total));
                                        intent2.putExtra("sub_total", "" + item_su_total);
                                        intent2.putExtra("current_currency", current_currency);
                                        intent2.putExtra("cart_id", cart_id);
                                        intent2.putExtra("coupon_id", coupon_id);
                                        intent2.putExtra("coupon_code", coupon_code);
                                        intent2.putExtra("coupon_price", coupon_price);
                                        intent2.putExtra("coupon_price", coupon_price);
                                        intent2.putExtra("total", "" + total);
                                        intent2.putExtra("onlyCod", onlyCod);
                                        if (!sessionManager.getRedeemPointFlagStatus().equals("0")) {
                                            intent2.putExtra("loyality_point", redeemed_point_text.getText().toString());
                                        } else {
                                            intent2.putExtra("loyality_point", "0");
                                        }
                                        startActivity(intent2);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                               /* intent2.putExtra("sub_total",""+item_su_total);
                                intent2.putExtra("current_currency",current_currency);
                                intent2.putExtra("cart_id",cart_id);
                                intent2.putExtra("coupon_id",coupon_id);
                                intent2.putExtra("coupon_code",coupon_code);
                                intent2.putExtra("coupon_price",coupon_price);
                                intent2.putExtra("coupon_price",coupon_price);
                                intent2.putExtra("total", ""+total);
                                intent2.putExtra("onlyCod", onlyCod);
                                intent2.putExtra("loyality_point",redeemed_point_text.getText().toString());
                                startActivity(intent2); */

                            } else {
                                Intent intent2 = new Intent(UserCartActivity.this, LoginActivity.class);
                                intent2.putExtra("from", "cart");
                                startActivityForResult(intent2, CommanMethod.LOGIN_REQ_CODE);

                            }
                        }

                    } else {

                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
                params.put("current_currency", sessionManager.getCurrencyCode());
                Log.d("user_id", sessionManager.getUserId());
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

    private void dialogBoxtwo() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.offer_expire_dialog_box);
        TextView info_tv = dialog.findViewById(R.id.info_tv);

        //info_tv.setText(this.getString(R.string.user_cart_out_of_stock));
        info_tv.setText("Offer Expired. Please choose another one.");

        TextView ok_tv = dialog.findViewById(R.id.ok_tv);

        ok_tv.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(UserCartActivity.this, UserCartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finishAffinity();

            /*Intent intent11 = new Intent(UserCartActivity.this,UserCartActivity.class);
            startActivity(intent11);
            finish();*/
        });
        dialog.show();

    }


}
