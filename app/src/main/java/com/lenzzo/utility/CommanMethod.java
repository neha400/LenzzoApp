package com.lenzzo.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.exifinterface.media.ExifInterface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.HomeActivity;
import com.lenzzo.PaymentActivity;
import com.lenzzo.R;
import com.lenzzo.api.API;
import com.lenzzo.localization.LocaleManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommanMethod {

    public static final int LOGIN_REQ_CODE = 100;
    public static final int REGISTER_REQ_CODE = 200;

    public static String getTwoPlaceDecimalValue(float value){
        //NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        try{
            /*Number number = format.parse(String.format ("%.2f", value));
            return number.floatValue();*/
            //DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
            DecimalFormat formatter = new DecimalFormat("0.00");
            //DecimalFormat formatter = new DecimalFormat("#,###.00");
            return formatter.format(value);

        }catch (Exception ex){
            ex.printStackTrace();
            return "0.00";
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;
        try {
            netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CommanMethod.getCustomOkAlert(context, context.getResources().getString(R.string.internet_error));
        //Toast.makeText(context, context.getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
        return false;
    }

    public static Bitmap  rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap getRotatedBitmap(Bitmap bitmap,String photoPath) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap = null;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }


    public static String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }


    public static void getCustomOkAlert(Context context, String message){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        info_tv.setText(message);
        ok_tv.setOnClickListener(view ->  {

            dialog.dismiss();

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public static Dialog getCustomProgressDialog(Context context){
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.progress_dialog_alert_layout);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.show();

        return dialog;
    }

    public static Dialog getProgressDialogForFragment(Context context){
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.progress_dialog_for_fragment_layout);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.show();


        return dialog;
    }

    public static void getCustomGOHome(Context context, String message){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        info_tv.setText(message);
        ok_tv.setOnClickListener(view ->  {
            Intent intent = new Intent(context,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static String getStringByLocal(Activity context, int id, String locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(id);
    }

    public static String getRandomNumber(){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)));
        for (int i = 0; i < 6; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);
        return sb.toString();
    }

    public static BigDecimal getCountryWiseDecimalNumber(Context context, String textNumber) {
        try {
            SessionManager sessionManager = new SessionManager(context);
            String dhjsdh = sessionManager.getCurrencyCode();
            if (sessionManager.getCurrencyCode().equalsIgnoreCase("KWD")) {
                return new BigDecimal(textNumber).setScale(3, RoundingMode.HALF_UP);
            } else {
                return new BigDecimal(textNumber).setScale(2, RoundingMode.HALF_UP);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static BigDecimal getTwoDecimalNumber(Context context, String textNumber){
        /*SessionManager sessionManager = new SessionManager(context);
        String dhjsdh = sessionManager.getCurrencyCode();
        if (sessionManager.getCurrencyCode().equalsIgnoreCase("KWD")){
            return new BigDecimal(textNumber).setScale(3, RoundingMode.HALF_UP);
        }else {
            return new BigDecimal(textNumber).setScale(2, RoundingMode.HALF_UP);
        }*/
        return new BigDecimal(textNumber).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal getCountryWiseDecimalNumberFloatInput(Context context, float floatNumber){
        try {
            SessionManager sessionManager = new SessionManager(context);
            if (sessionManager.getCurrencyCode().equalsIgnoreCase("KWD")){
                return new BigDecimal(floatNumber).setScale(3, RoundingMode.HALF_UP);
            }else {
                return new BigDecimal(floatNumber).setScale(2, RoundingMode.HALF_UP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMessage(Context context, JSONObject jsonObject){
        try {
            String message = jsonObject.getString("message");
            String message_ar = jsonObject.optString("message_ar", "");
            if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                return message;
            }else{
                if (TextUtils.isEmpty(message_ar)) {
                    return message;
                }else {
                    return message_ar;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    public static String getTitle(Context context, JSONObject jsonObject){
        try {
            String message = jsonObject.getString("title");
            String message_ar = jsonObject.optString("title_ar", "");
            if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                return message;
            }else{
                if (TextUtils.isEmpty(message_ar)) {
                    return message;
                }else {
                    return message_ar;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    public static boolean isOutOfStock(String stockFlag, String quantity){

        if(stockFlag.equals("0")){

            return true;
        }else{
            if (quantity.equals("null") || Integer.parseInt(quantity)<1){
                return true;
            }else {
                return false;
            }
        }
    }

    public static String getOfferName(Context context, String offerData) {

        try {
            Object json = new JSONTokener(offerData.trim()).nextValue();
            if (json instanceof JSONObject) {
                JSONObject jsonObject = new JSONObject(offerData.trim());
                String offerName = "";
                if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                    offerName = jsonObject.optString("name","");
                }else{
                    offerName = jsonObject.optString("name_ar","");
                }

                return offerName;
            }else if (json instanceof JSONArray){
                return "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void rateAlert(Context context, String message,String orderId,String order_num){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rating_alert_dialog_box);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView info_tvs = dialog.findViewById(R.id.info_tvs);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        TextView rate_tv = dialog.findViewById(R.id.rate_tv);
        //info_tv.setText(getString(R.string.only_cod_success));
        ImageView rate_iv = dialog.findViewById(R.id.rate_iv);
        SessionManager sessionManager = new SessionManager(context.getApplicationContext());

        //info_orderid.setText("ID -"+" "+orderId);
        //info_tv.getText(R.string.rate_app_info+" "+"ID -"+" "+orderId+R.string.rate_app_infoend);

        if ("ar".equals(sessionManager.getLanguageSelected())) {
            //LocaleManager.setNewLocale(context, "ar");
            info_tv.setText(" بنجاح"+"("+ orderId+")"+"تم استلام طلبك ");

        } else {
            //LocaleManager.setNewLocale(context, "en");
            info_tv.setText("Your order  "+"(ID - "+ orderId+")"+" has been received successfully. ");
        }


        if (order_num.equals("1")){
            info_tvs.setVisibility(View.VISIBLE);
        }else {
            info_tvs.setVisibility(View.GONE);
        }

        ok_tv.setOnClickListener(view ->  {
            dialog.dismiss();
            Intent intent = new Intent(context,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

        });
        rate_tv.setOnClickListener(view -> {

            if(isInternetConnected(context)){
                rateThisApp(context);
            }else {
                Intent intent = new Intent(context,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                openAppOnPlayStore(context);
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        rate_iv.setOnClickListener(view -> {

            if(isInternetConnected(context)){
                rateThisApp(context);
            }else {
                Intent intent = new Intent(context,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                openAppOnPlayStore(context);
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static void openAppOnPlayStore(Context context){
        Uri uri = Uri.parse("market://details?id="+context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException ex) {

            context.startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("http://play.google.com/store/apps/details?id="+context.getPackageName())));
        }
    }

    private static void rateThisApp(Context context){
        final Dialog dialog  = CommanMethod.getCustomProgressDialog(context);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"rate_this_app", response -> {
            try {
                dialog.dismiss();
                Log.d("rateResponse", response);
                JSONObject object = new JSONObject(response);
                String status = object.getString("status");
                if(status.equals("success")){
                    /*Intent intent = new Intent(context,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    dialog.dismiss();
                    openAppOnPlayStore(context);*/
                }else {
                    // String message = object.getString("message");
                    //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(context,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                openAppOnPlayStore(context);
            }catch (JSONException e){
                dialog.dismiss();
                e.printStackTrace();
                //gifImageView.setVisibility(View.GONE);
            }

        }, error -> {
            dialog.dismiss();
            try {
                String responseBody = new String(error.networkResponse.data, "utf-8");
                JSONObject data = new JSONObject(responseBody);
                String message = CommanMethod.getMessage(context, data);
                //CommanMethod.getCustomOkAlert(ChangePasswordActivity.this, message);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException errorr) {
                error.printStackTrace();
            }
            // gifImageView.setVisibility(View.GONE);
        })
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(context);
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
}
