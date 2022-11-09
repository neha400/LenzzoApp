package com.lenzzo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.ProductDetailsActivity;
import com.lenzzo.R;
import com.lenzzo.UserCartActivity;
import com.lenzzo.api.API;
import com.lenzzo.interfacelenzzo.UserCartInterface;
import com.lenzzo.model.CartFamilyModel;
import com.lenzzo.model.UserCartModel;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.CustomVolleyRequest;
import com.lenzzo.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartParentAdapter extends RecyclerView.Adapter<CartParentAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<CartFamilyModel> cartFamilyModels;
    private ImageLoader imageLoader;
    private Context context;
    private UserCartInterface userCartInterface;
    private SessionManager sessionManager;

    public CartParentAdapter(Context ctx, List<CartFamilyModel> cartFamilyModels){
        inflater = LayoutInflater.from(ctx);
        this.cartFamilyModels = cartFamilyModels;
        this.context=ctx;
        this.userCartInterface = ((UserCartInterface)context);
        sessionManager = new SessionManager(context);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_item_cart_parent_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (cartFamilyModels.get(position).getOfferFlag().equals("1")){
            holder.offer_layout.setVisibility(View.VISIBLE);
            holder.offer_name.setText(cartFamilyModels.get(position).getOfferName());
        }else {
            holder.offer_layout.setVisibility(View.GONE);
        }

        if (Integer.parseInt(cartFamilyModels.get(position).getDiscountType())>0 || Integer.parseInt(cartFamilyModels.get(position).getFreeQuantity())>0){
            holder.offer_tv.setText(context.getResources().getString(R.string.offer)+":"+cartFamilyModels.get(position).getOfferName());
        }else {
            holder.offer_tv.setText("");
        }

        holder.family_name_tv.setText(cartFamilyModels.get(position).getFamilyName());
        holder.total_tv.setText(context.getResources().getString(R.string.total)+": "+cartFamilyModels.get(position).getTotal()+" "+sessionManager.getCurrencyCode());
        holder.product_rv.setAdapter(new UserCartAdapter(context,cartFamilyModels.get(position).getUserCartModelList()) );
    }

    @Override
    public int getItemCount() {
        return cartFamilyModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView family_name_tv, offer_tv, total_tv, offer_name;
        RecyclerView product_rv;
        LinearLayout offer_layout;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public MyViewHolder(View itemView) {
            super(itemView);
            family_name_tv = (TextView)itemView.findViewById(R.id.family_name_tv);
            offer_tv = (TextView)itemView.findViewById(R.id.offer_tv);
            total_tv = itemView.findViewById(R.id.total_tv);
            offer_name = itemView.findViewById(R.id.offer_name);

            product_rv = itemView.findViewById(R.id.product_rv);
            offer_layout = itemView.findViewById(R.id.offer_layout);

        }
    }

    
}
