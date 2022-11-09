package com.lenzzo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.lenzzo.BrandListActivity;
import com.lenzzo.BrandListForFilterActivity;
import com.lenzzo.CollectionsActivity;
import com.lenzzo.ProductListActivity;
import com.lenzzo.R;
import com.lenzzo.customviews.CustomTextViewNormal;
import com.lenzzo.fragment.CollectionFragment;
import com.lenzzo.model.BrandListOfCategory;
import com.lenzzo.utility.CustomVolleyRequest;
import com.lenzzo.utility.SessionManager;

import java.util.List;

public class BrandListForFilterAdapter extends RecyclerView.Adapter<BrandListForFilterAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<BrandListOfCategory> brandListOfCategoryList;
    private Context context;

    public BrandListForFilterAdapter(Context ctx, List<BrandListOfCategory> brandListOfCategoryList){
        inflater = LayoutInflater.from(ctx);
        this.brandListOfCategoryList = brandListOfCategoryList;
        this.context=ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.single_item_brand_name_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.brand_tv.setText(brandListOfCategoryList.get(position).getName());
        if (brandListOfCategoryList.get(position).isSelected()){
            holder.radio_iv.setImageResource(R.drawable.slect_100);
        }else {
            holder.radio_iv.setImageResource(R.drawable.de_select_100);
        }
        holder.brand_lay.setOnClickListener(view ->  {

            ((BrandListForFilterActivity)context).selectBrand(position);


        });

    }

    @Override
    public int getItemCount() {
        return brandListOfCategoryList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView radio_iv;
        TextView brand_tv;
        LinearLayout brand_lay;

        public MyViewHolder(View itemView) {
            super(itemView);
            radio_iv = itemView.findViewById(R.id.radio_iv);
            brand_tv = itemView.findViewById(R.id.brand_tv);
            brand_lay = itemView.findViewById(R.id.brand_lay);

        }
    }



}
