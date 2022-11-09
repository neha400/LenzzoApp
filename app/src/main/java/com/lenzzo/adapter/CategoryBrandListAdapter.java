package com.lenzzo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.lenzzo.BrandListActivity;
import com.lenzzo.R;
import com.lenzzo.fragment.BrandListFragment;
import com.lenzzo.model.BannerList;
import com.lenzzo.model.CategoryBrandList;
import com.lenzzo.utility.CustomVolleyRequest;

import java.util.List;
import java.util.Locale;

public class CategoryBrandListAdapter extends RecyclerView.Adapter<CategoryBrandListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<CategoryBrandList> categoryBrandLists;
    //private ImageLoader imageLoader;
    private Context context;
    private List<BannerList> bannerLists;
    int j=1;
    int startIndex=0;
    int endIndex=1;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public CategoryBrandListAdapter(Context ctx, List<CategoryBrandList> categoryBrandLists,List<BannerList> bannerLists){
        inflater = LayoutInflater.from(ctx);
        this.categoryBrandLists = categoryBrandLists;
        this.context=ctx;
        this.bannerLists=bannerLists;
    }

    @Override
    public CategoryBrandListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.category_randlists_recycler_view_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryBrandListAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        if(Locale.getDefault().getLanguage().equals("en")){
            holder.categoryName.setText(categoryBrandLists.get(position).getCategoryName());
        }else{
            holder.categoryName.setText(categoryBrandLists.get(position).getCategoryName_ar());
        }
        holder.view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("cat id" + categoryBrandLists.get(position).getCategory_id());
                /*Intent intent =new Intent(context, BrandListActivity.class);
                intent.putExtra("category_id",categoryBrandLists.get(position).getCategory_id());
                context.startActivity(intent);*/
                BrandListFragment collectionFragment = new BrandListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("category_id",categoryBrandLists.get(position).getCategory_id());
                collectionFragment.setArguments(bundle);
                replaceFragment(collectionFragment);
            }
        });
        BrandListAdapter brandListAdapter = new BrandListAdapter(context,categoryBrandLists.get(position).getCategoryBrandList());
        holder.brand_recycler.setAdapter(brandListAdapter);
        holder.brand_recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            if (position == j) {
                j=j+2;
                if(bannerLists.size()>=startIndex && bannerLists.size()>=endIndex) {
                    List<BannerList> sublist = bannerLists.subList(startIndex, endIndex);
                    holder.banner_recycler.setVisibility(View.VISIBLE);
                    GridLayoutManager gridLayoutManager1 = new GridLayoutManager(context, 1);
                    holder.banner_recycler.setLayoutManager(gridLayoutManager1);
                    BannerListAdapter bannerListAdapter = new BannerListAdapter(context, sublist);
                    holder.banner_recycler.setAdapter(bannerListAdapter);
                    startIndex = startIndex + 1;
                    endIndex = endIndex + 1;
                }
             }
       }

    @Override
    public int getItemCount() {
        return categoryBrandLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView categoryName;
        TextView view_all;
        RecyclerView brand_recycler;
        RecyclerView banner_recycler;

        public MyViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
            view_all = (TextView)itemView.findViewById(R.id.view_all);
            brand_recycler = (RecyclerView)itemView.findViewById(R.id.brand_recycler);
            banner_recycler = (RecyclerView)itemView.findViewById(R.id.banner_recycler);
            banner_recycler.setVisibility(View.GONE);
        }
    }

    private void replaceFragment(Fragment fragment){
        ((AppCompatActivity)context).getSupportActionBar().hide();
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
