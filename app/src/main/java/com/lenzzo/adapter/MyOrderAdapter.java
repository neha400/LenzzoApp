package com.lenzzo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.R;
import com.lenzzo.api.API;
import com.lenzzo.interfacelenzzo.OrderListener;
import com.lenzzo.model.MyOrderModel;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<MyOrderModel> myOrderModelList;
    private ImageLoader imageLoader;
    private Context context;
    private Float rating;
    private String comment;
    private String getName;
    private SessionManager sessionManager;
    private OrderListener orderListener;

    public MyOrderAdapter(Context ctx, List<MyOrderModel> myOrderModelList){
        inflater = LayoutInflater.from(ctx);
        this.myOrderModelList = myOrderModelList;
        this.context=ctx;
        this.orderListener = (OrderListener)ctx;
        this.sessionManager = new SessionManager(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @NonNull
    @Override
    public MyOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_order_recycler_view_item, parent, false);
        MyOrderAdapter.MyViewHolder holder = new MyOrderAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.MyViewHolder holder, int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(myOrderModelList.get(position).getProduct_image(), ImageLoader.getImageListener(holder.image, R.drawable.no_img, R.drawable.no_img));

        Picasso.get()
                .load(myOrderModelList.get(position).getProduct_image())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.image);

        if(Locale.getDefault().getLanguage().equals("ar")){
            holder.name.setText(myOrderModelList.get(position).getProduct_name_ar());
        }else if (Locale.getDefault().getLanguage().equals("en")){
            holder.name.setText(myOrderModelList.get(position).getProduct_name());
        }

        holder.order_id.setText(myOrderModelList.get(position).getSerial_number());
        holder.order_qty.setText(myOrderModelList.get(position).getQuantity());
        holder.date_text.setText(myOrderModelList.get(position).getCreated_at());
        holder.status_text.setText(context.getResources().getString(R.string.order_status)+": "+myOrderModelList.get(position).getOrder_status());
        if(myOrderModelList.get(position).getOrder_status().equalsIgnoreCase("failed")
                || myOrderModelList.get(position).getOrder_status().equalsIgnoreCase("canceled")
                || myOrderModelList.get(position).getOrder_status().equalsIgnoreCase("refunded")){
            holder.status_text.setTextColor(Color.parseColor("#F0F8FF"));
            holder.track_button.setVisibility(View.GONE);
            holder.feedback_button.setVisibility(View.GONE);
        }else{
            holder.status_text.setTextColor(Color.parseColor("#00C48F"));
            holder.track_button.setVisibility(View.VISIBLE);
            holder.feedback_button.setVisibility(View.VISIBLE);
        }

        if(myOrderModelList.get(position).getFeedbackStatus().equals("1")){
            holder.feedback_button.setVisibility(View.GONE);
        }else{

            holder.feedback_button.setVisibility(View.VISIBLE);
        }

        if(myOrderModelList.get(position).getOrder_status().equalsIgnoreCase("completed")){
            holder.track_button.setVisibility(View.GONE);

        }else {
            holder.track_button.setVisibility(View.VISIBLE);
        }

        holder.feedback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedBackDialog(myOrderModelList.get(position).getId(),myOrderModelList.get(position).getUser_id(),myOrderModelList.get(position).getFull_name(),myOrderModelList.get(position).getProduct_id(),myOrderModelList.get(position).getProduct_name(),myOrderModelList.get(position).getSerial_number(), position);
            }
        });

        holder.track_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOrderModelList.get(position).getOrderTrackingId().equals("null") || myOrderModelList.get(position).getOrderTrackingId().isEmpty()) {

                    successdialogBox(context.getResources().getString(R.string.alert_message), context.getResources().getString(R.string.not_order_alert));
                }else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.postaplus.com/?trackid="+myOrderModelList.get(position).getOrderTrackingId()));
                    context.startActivity(browserIntent);
                }
            }
        });

      holder.view_details.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              viewDetailsDialog(myOrderModelList.get(position));
          }
      });

      holder.reorder_tv.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (CommanMethod.isInternetConnected(context)) {
                  orderListener.onReorder(myOrderModelList.get(position));
              }
          }
      });

    }

    @Override
    public int getItemCount() {
        return myOrderModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;
        TextView order_id;
        TextView order_qty;
        TextView view_details;
        TextView date_text;
        TextView status_text, reorder_tv;
        Button feedback_button;
        Button track_button;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            order_id = itemView.findViewById(R.id.order_id);
            order_qty = itemView.findViewById(R.id.order_qty);
            view_details = itemView.findViewById(R.id.view_details);
            date_text = itemView.findViewById(R.id.date_text);
            status_text = itemView.findViewById(R.id.status_text);
            feedback_button = itemView.findViewById(R.id.feedback_button);
            track_button = itemView.findViewById(R.id.track_button);
            reorder_tv = itemView.findViewById(R.id.reorder_tv);
        }
    }

    private void feedBackDialog(String id,String user_id,String name,String product_id,String product_name,String order_id, int position){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.feedback_dialog);
        EditText edit_text_name = dialog.findViewById(R.id.edit_text_name);
        edit_text_name.setText(name);
        AppCompatRatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        EditText comments_edittext = dialog.findViewById(R.id.comments_edittext);
        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        Button btn_close = dialog.findViewById(R.id.btn_close);
        ratingBar.setOnRatingBarChangeListener(new AppCompatRatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = ratingBar.getRating();
                //Toast.makeText(context, "Rating changed, current rating "+ ratingBar.getRating(), Toast.LENGTH_SHORT).show();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                comment = comments_edittext.getText().toString();
                getName = edit_text_name.getText().toString();
                if(getName.equals("") || getName.length()==0){
                    //Toast.makeText(context, context.getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(context, context.getResources().getString(R.string.enter_name));
                }else if(comment.equals("") || comment.length()==0){
                    //Toast.makeText(context, context.getResources().getString(R.string.comment_message), Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(context, context.getResources().getString(R.string.comment_message));
                }else{
                    userRating(id, user_id,getName,comment,product_id,product_name,rating,order_id, position);
                }
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void userRating(String id, String userId,String user_name,String comment,String product_id,String product_name,float rating,String order_id, int position){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(context);
        dialog.setCancelable(true);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"review_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = CommanMethod.getMessage(context, object);
                    if(status.equals("success")){
                        myOrderModelList.get(position).setFeedbackStatus("1");
                        notifyItemChanged(position);
                        successdialogBox(context.getResources().getString(R.string.alert_message),message);
                    }else{
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", userId);
                params.put("user_name",user_name);
                params.put("comment",comment);
                params.put("product_id",product_id);
                params.put("product_name",product_name);
                params.put("rating",""+(int)rating);
                params.put("order_id",order_id.substring(order_id.indexOf(":")+1).trim());
                params.put("order_detail_id", id);
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

    private void successdialogBox(String title,String info){
        final Dialog dialog = new Dialog(context);
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

    private void viewDetailsDialog(MyOrderModel myOrderModel) {
        String nameAndGift = "";
        String address = "";
        String grand_total_with_coupon = "";
        String offer_free_qty = "";
        String loyality_point = "";
        String quantity = "";

        ArrayList<String> strings = new ArrayList<>();
        if (!myOrderModel.getLoyality_point().equals("null") && !myOrderModel.getLoyality_point().equals("")) {
            loyality_point = context.getResources().getString(R.string.paid) + ": " + myOrderModel.getLoyality_point() + "\n";
        }
        if (!myOrderModel.getProduct_name().equals("null") && !myOrderModel.getProduct_name().equals("")) {
            nameAndGift = context.getResources().getString(R.string.product_name) + ": " + myOrderModel.getProduct_name();
        }
        if (!myOrderModel.getGift_name().equals("null") && !myOrderModel.getGift_name().equals("")) {
            nameAndGift += "\n" + context.getResources().getString(R.string.gift_name) + ": " + myOrderModel.getGift_name();
        }
        strings.add(nameAndGift);
        strings.add(myOrderModel.getSerial_number());
        strings.add(context.getResources().getString(R.string.price) + ": " + myOrderModel.getDisplay_total() + " " + myOrderModel.getCurrent_currency());
        //BigDecimal delivery_ch = new BigDecimal(myOrderModel.getDelivery_charge()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal delivery_ch = CommanMethod.getCountryWiseDecimalNumber(context, myOrderModel.getDelivery_charge());
        BigDecimal vat_value = CommanMethod.getCountryWiseDecimalNumber(context, myOrderModel.getVat_price());
        BigDecimal vat_charge = CommanMethod.getCountryWiseDecimalNumber(context, myOrderModel.getVat_charge());

        if (vat_value.equals("null")){
            strings.add((context.getResources().getString(R.string.vat)+"("+"0"+"%)"+" :"+" "+vat_charge+" "+myOrderModel.getCurrent_currency()+"\n"+grand_total_with_coupon).trim());
        }else {
            strings.add((context.getResources().getString(R.string.vat)+"("+vat_value+"%)"+" :"+" "+vat_charge+" "+myOrderModel.getCurrent_currency()+"\n"+grand_total_with_coupon).trim());
        }

        strings.add(context.getResources().getString(R.string.delivery_charge) + ": " + delivery_ch + " " + myOrderModel.getCurrent_currency());
        //BigDecimal grand_tot = new BigDecimal(myOrderModel.getTotal_order_price()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal grand_tot = CommanMethod.getCountryWiseDecimalNumber(context, myOrderModel.getTotal_order_price());
        if (!myOrderModel.getCoupon_code().equals("null") && !myOrderModel.getCoupon_code().equals("")) {
            grand_total_with_coupon += context.getResources().getString(R.string.coupon_code) + ": " + myOrderModel.getCoupon_code() + "\n" + context.getResources().getString(R.string.coupon_price) + ": " + myOrderModel.getCoupon_price() + " " + myOrderModel.getCurrent_currency();
        }
        strings.add((context.getResources().getString(R.string.grand_total)+": "+grand_tot+" "+myOrderModel.getCurrent_currency()+"\n"+grand_total_with_coupon).trim());
        if(!myOrderModel.getFree_quantity().equals("0") && !myOrderModel.getFree_quantity().equals("")){
            offer_free_qty+=context.getResources().getString(R.string.free_quantity)+": "+myOrderModel.getFree_quantity()+"\n";
        }
        if(!myOrderModel.getOffer_name().equals("null") && !myOrderModel.getOffer_name().equals("")){
            offer_free_qty+=context.getResources().getString(R.string.offer_name)+": "+myOrderModel.getOffer_name();
        }
        quantity = (context.getResources().getString(R.string.quantity)+": "+myOrderModel.getQuantity());

        if (!myOrderModel.getQuantityRight().equals("0") && !myOrderModel.getQuantityRight().equals("null")){
            quantity = quantity +"\n"+ context.getResources().getString(R.string.right_qty)+": "+myOrderModel.getQuantityRight();
        }
        if (!myOrderModel.getQuantityLeft().equals("0") && !myOrderModel.getQuantityLeft().equals("null")){
            quantity = quantity +"\n"+ context.getResources().getString(R.string.left_qty)+": "+myOrderModel.getQuantityLeft();
        }
        String specialOffer = "";
        if (myOrderModel.getIsSpecialOffer().equals("1")){

            if (myOrderModel.getSpecialDiscountType().equals("1") && myOrderModel.getSpecialDiscount().length()>0){
                specialOffer = context.getString(R.string.special_discount)+" "+ CommanMethod.getTwoDecimalNumber(context, myOrderModel.getSpecialDiscount())+"%";
            }else if (myOrderModel.getSpecialDiscountType().equals("0") && myOrderModel.getSpecialDiscount().length()>0){
                specialOffer = context.getString(R.string.special_discount)+" "+ CommanMethod.getCountryWiseDecimalNumber(context, myOrderModel.getSpecialDiscount())+myOrderModel.getCurrent_currency();
            }
        }
        if (TextUtils.isEmpty(offer_free_qty)) {
            strings.add((quantity + "\n" + specialOffer).trim());
        }else{
            strings.add((quantity + "\n" + offer_free_qty.trim() + "\n" + specialOffer).trim());
        }
        
        String power_text = "";
        if (myOrderModel.getPowerSelectionType().equals("2") || myOrderModel.getPowerSelectionType().equals("3")){
            if ((!TextUtils.isEmpty(myOrderModel.getLeft_eye_power())
                    && !myOrderModel.getLeft_eye_power().equals("null")) && (!TextUtils.isEmpty(myOrderModel.getRight_eye_power())
                    && !myOrderModel.getRight_eye_power().equals("null"))){
                power_text = context.getResources().getString(R.string.right_sph_power) + " " + myOrderModel.getRight_eye_power() + ", " + context.getResources().getString(R.string.left_sph_power) + " " + myOrderModel.getLeft_eye_power();
            }else if (!TextUtils.isEmpty(myOrderModel.getLeft_eye_power())
                    && !myOrderModel.getLeft_eye_power().equals("null")){
                power_text = context.getResources().getString(R.string.left_sph_power) + " " + myOrderModel.getLeft_eye_power();
            }else if(!TextUtils.isEmpty(myOrderModel.getRight_eye_power())
                    && !myOrderModel.getRight_eye_power().equals("null")){
                power_text = context.getResources().getString(R.string.right_sph_power) + " " + myOrderModel.getRight_eye_power();
            }
        }else {
            if ((!TextUtils.isEmpty(myOrderModel.getLeft_eye_power())
                    && !myOrderModel.getLeft_eye_power().equals("null")) && (!TextUtils.isEmpty(myOrderModel.getRight_eye_power())
                    && !myOrderModel.getRight_eye_power().equals("null"))){
                power_text = context.getResources().getString(R.string.right_eye_power) + " " + myOrderModel.getRight_eye_power() + ", " + context.getResources().getString(R.string.left_eye_power) + " " + myOrderModel.getLeft_eye_power();
            }else if (!TextUtils.isEmpty(myOrderModel.getLeft_eye_power())
                    && !myOrderModel.getLeft_eye_power().equals("null")){
                power_text = context.getResources().getString(R.string.left_eye_power) + " " + myOrderModel.getLeft_eye_power();
            }else if(!TextUtils.isEmpty(myOrderModel.getRight_eye_power())
                    && !myOrderModel.getRight_eye_power().equals("null")){
                power_text = context.getResources().getString(R.string.right_eye_power) + " " + myOrderModel.getRight_eye_power();
            }
        }
        if (myOrderModel.getPowerSelectionType().equals("2")){
            if ((!TextUtils.isEmpty(myOrderModel.getLeftCyl())
                    && !myOrderModel.getLeftCyl().equals("null")) && (!TextUtils.isEmpty(myOrderModel.getRightCyl())
                    && !myOrderModel.getRightCyl().equals("null"))){
                power_text = power_text+"\n"+context.getResources().getString(R.string.right_cyl_power) + " " + myOrderModel.getRightCyl() + ", " + context.getResources().getString(R.string.left_cyl_power) + " " + myOrderModel.getLeftCyl();
            }else if (!TextUtils.isEmpty(myOrderModel.getLeftCyl())
                    && !myOrderModel.getLeftCyl().equals("null")){
                power_text =  power_text+"\n"+context.getResources().getString(R.string.left_cyl_power) + " " + myOrderModel.getLeftCyl();
            }else if(!TextUtils.isEmpty(myOrderModel.getRightCyl())
                    && !myOrderModel.getRightCyl().equals("null")){
                power_text = power_text+"\n"+context.getResources().getString(R.string.right_cyl_power) + " " + myOrderModel.getRightCyl();
            }

            if ((!TextUtils.isEmpty(myOrderModel.getLeftAxis())
                    && !myOrderModel.getLeftAxis().equals("null")) && (!TextUtils.isEmpty(myOrderModel.getRightAxis())
                    && !myOrderModel.getRightAxis().equals("null"))){
                power_text =  power_text+"\n"+context.getResources().getString(R.string.right_axis_power) + " " + myOrderModel.getRightAxis() + ", " + context.getResources().getString(R.string.left_axis_power) + " " + myOrderModel.getLeftAxis();
            }else if (!TextUtils.isEmpty(myOrderModel.getLeftAxis())
                    && !myOrderModel.getLeftAxis().equals("null")){
                power_text = power_text+"\n"+context.getResources().getString(R.string.left_axis_power) + " " + myOrderModel.getLeftAxis();
            }else if(!TextUtils.isEmpty(myOrderModel.getRightAxis())
                    && !myOrderModel.getRightAxis().equals("null")){
                power_text = power_text+"\n"+context.getResources().getString(R.string.right_axis_power) + " " + myOrderModel.getRightAxis();
            }
        }else if (myOrderModel.getPowerSelectionType().equals("3")){
            if ((!TextUtils.isEmpty(myOrderModel.getLeftAddition())
                    && !myOrderModel.getLeftAddition().equals("null")) && (!TextUtils.isEmpty(myOrderModel.getRightAddition())
                    && !myOrderModel.getRightAddition().equals("null"))){
                power_text = power_text+"\n"+context.getResources().getString(R.string.right_add_power) + " " + myOrderModel.getRightAddition() + ", " + context.getResources().getString(R.string.left_add_power) + " " + myOrderModel.getLeftAddition();
            }else if (!TextUtils.isEmpty(myOrderModel.getLeftAddition())
                    && !myOrderModel.getLeftAxis().equals("null")){
                power_text = power_text+"\n"+ context.getResources().getString(R.string.left_add_power) + " " + myOrderModel.getLeftAddition();
            }else if(!TextUtils.isEmpty(myOrderModel.getRightAddition())
                    && !myOrderModel.getRightAddition().equals("null")){
                power_text = power_text+"\n"+ context.getResources().getString(R.string.right_add_power) + " " + myOrderModel.getRightAddition();
            }
        }
        strings.add(power_text);
        /*if(!myOrderModel.getLeft_eye_power().equals("null") && !myOrderModel.getLeft_eye_power().equals("")){
            strings.add(context.getResources().getString(R.string.left_eye)+": "+myOrderModel.getLeft_eye_power()+"\n"+context.getResources().getString(R.string.right_eye)+": "+myOrderModel.getRight_eye_power());
        }*/
       /* if(!myOrderModel.getLoyality_point().equals("null") && !myOrderModel.getLoyality_point().equals("")){
            loyality_point = context.getResources().getString(R.string.paid)+": "+myOrderModel.getLoyality_point()+"\n";
        }*/
        if(!myOrderModel.getEarn_loyality_point().equals("0") && !myOrderModel.getEarn_loyality_point().equals("null") && !myOrderModel.getEarn_loyality_point().equals("")){
            if(myOrderModel.getEarn_loyality_point_status().equals("0") ){
                loyality_point+= context.getResources().getString(R.string.get)+" "+myOrderModel.getEarn_loyality_point()+" "+context.getResources().getString(R.string.get_point);
            }else{
                loyality_point+= context.getResources().getString(R.string.loyal_point)+": "+myOrderModel.getEarn_loyality_point();
            }
        }
        strings.add((context.getResources().getString(R.string.payment_mode)+": "+myOrderModel.getPayment_mode_name()+"\n"+loyality_point).trim());
        if(!myOrderModel.getFull_name().equals("null") && !myOrderModel.getFull_name().equals("")){
            address=context.getResources().getString(R.string.di_full_name)+": "+myOrderModel.getFull_name()+"\n";
        }
        if(!myOrderModel.getArea().equals("null") && !myOrderModel.getArea().equals("")){
            address+=context.getResources().getString(R.string.di_area)+": "+myOrderModel.getArea()+"\n";
        }
        if(!myOrderModel.getBlock().equals("null") && !myOrderModel.getBlock().equals("")){
            address+=context.getResources().getString(R.string.di_block)+": "+myOrderModel.getBlock()+"\n";
        }
        if(!myOrderModel.getStreet().equals("null") && !myOrderModel.getStreet().equals("")){
            address+=context.getResources().getString(R.string.di_street)+": "+myOrderModel.getStreet()+"\n";
        }
        if(!myOrderModel.getHouse_no().equals("null") && !myOrderModel.getHouse_no().equals("")){
            address+=context.getResources().getString(R.string.di_house)+": "+myOrderModel.getHouse_no()+"\n";
        }
        if(!myOrderModel.getFlat_no().equals("null") && !myOrderModel.getFlat_no().equals("")){
        address+=context.getResources().getString(R.string.di_flat)+": "+myOrderModel.getFlat_no()+"\n";
        }
        if(!myOrderModel.getPhone_no().equals("null") && !myOrderModel.getPhone_no().equals("")){
         address+=context.getResources().getString(R.string.di_phone)+":"+myOrderModel.getPhone_no()+"\n";
        }
        if(!myOrderModel.getCurrrent_location().equals("null") && !myOrderModel.getCurrrent_location().equals("")){
            address+=context.getResources().getString(R.string.current_location)+":"+myOrderModel.getCurrrent_location()+"\n";
        }
        if(!myOrderModel.getComments().equals("null") && !myOrderModel.getComments().equals("")){
            address+=context.getResources().getString(R.string.di_comment)+":"+myOrderModel.getComments();
        }

        strings.add(address.trim());
        strings.add(context.getResources().getString(R.string.order_date)+": "+myOrderModel.getCreated_at());
        strings.add(context.getResources().getString(R.string.di_order_status)+": "+myOrderModel.getOrder_status());
        if(myOrderModel.getPayment_status().equals("success") || myOrderModel.getPayment_status().equals("captured") || myOrderModel.getPayment_status().equals("Approved")){
            strings.add(context.getResources().getString(R.string.payment_status)+": "+context.getResources().getString(R.string.pty_success));
        }else {
            strings.add(context.getResources().getString(R.string.payment_status)+": "+myOrderModel.getPayment_status());
        }

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.view_details_dialog);
        ListView mListView = dialog.findViewById(R.id.list);
        TextView product_name = dialog.findViewById(R.id.product_name);
        TextView close_dialog = dialog.findViewById(R.id.close_dialog);
        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        Button reorder_submit = dialog.findViewById(R.id.reorder_submit);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        reorder_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommanMethod.isInternetConnected(context)) {
                    //addToCart(myOrderModel);
                    orderListener.onReorder(myOrderModel);
                }
            }
        });

        ArrayAdapter aAdapter = new ArrayAdapter(context, R.layout.single_item_list_text_layout, strings);
        mListView.setAdapter(aAdapter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /*private void addToCart(MyOrderModel myOrder){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(context);
        dialog.show();

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"usercart_add", response -> {
            try {
                dialog.dismiss();
                JSONObject object = new JSONObject(response);
                String status = object.getString("status");
                if(status.equals("success")){

                    successAlert();

                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
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

        final Dialog dialog = new Dialog(context);
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
                ((MyOrderActivity)context).finish();
            }
        });
        go_to_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UserCartActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
                //addToCart(product_id,getBrand_id,getBrand_Name,"goToCart");
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }*/



}
