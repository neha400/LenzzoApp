package com.lenzzo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.adapter.FilterAdapter;
import com.lenzzo.adapter.FilterListAdapter;
import com.lenzzo.api.API;
import com.lenzzo.interfacelenzzo.FilterListChildInterface;
import com.lenzzo.interfacelenzzo.FilterListInterface;
import com.lenzzo.localization.BaseActivity;
import com.lenzzo.model.FilterListModel;
import com.lenzzo.model.FilterModel;
import com.lenzzo.utility.CommanMethod;
import com.lenzzo.utility.SessionManager;
import com.lenzzo.utility.SortFilterSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;

public class FilterActivity extends BaseActivity implements View.OnClickListener, FilterListInterface, FilterListChildInterface {

    private RecyclerView select_list_view_recycler;
    private RecyclerView list_view_recycler;
    private List<FilterModel> filterModelList;
    private FilterAdapter filterAdapter;
    private List<FilterListModel> filterListModelList;
    private FilterListAdapter filterListAdapter;
    private String title="";
    private SortFilterSessionManager sortFilterSessionManager;
    Set<String> set_Brands = new HashSet<String>();
    Set<String> set_Tags = new HashSet<String>();
    Set<String> set_Colors = new HashSet<String>();
    Set<String> set_Replacement = new HashSet<String>();
    Set<String> set_Gender = new HashSet<String>();
    Set<String> set_Rating = new HashSet<String>();
    Set<String> test_atri = new HashSet<String>();
    private ImageView back_image;
    //private GifImageView gifImageView;
    private String rating="";
    private SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        sessionManager = new SessionManager(this);
        sortFilterSessionManager = new SortFilterSessionManager(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.applay_button).setOnClickListener(this);
        findViewById(R.id.clear_all_text).setOnClickListener(this);
        //gifImageView = findViewById(R.id.gifImageView);
        select_list_view_recycler = (RecyclerView)findViewById(R.id.select_list_view_recycler);
        list_view_recycler = (RecyclerView)findViewById(R.id.list_view_recycler);
        back_image = (ImageView)findViewById(R.id.back_image);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
        }
        ((AppCompatRatingBar)findViewById(R.id.ratingbar)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                //int rating = Math.round(v);
                rating = String.valueOf(Math.round(v));
            }
        });

        if (CommanMethod.isInternetConnected(FilterActivity.this)){
            getFilterValue();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.applay_button:

                sortFilterSessionManager.setFilter_Rating( rating);
                String from = getIntent().getStringExtra("from");
                if (from != null){
                    if (from.equals("list_fragment")){
                        sessionManager.setShouldFilterApply(true);
                        finish();
                    }
                }else {
                    Intent intent = new Intent(FilterActivity.this, ProductListActivity.class);
                    intent.putExtra("isCall","isCall");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                break;
            case R.id.clear_all_text:
                sortFilterSessionManager.clearPreferences();
                rating="";
                ((AppCompatRatingBar)findViewById(R.id.ratingbar)).setRating(0.0f);
                getFilterValue();
                break;
        }
    }


    public void getFilterValue(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"filter_list", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    filterModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONArray jsonArray = new JSONArray(object.getString("response"));
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            FilterModel filterModel = new FilterModel();
                            filterModel.setTitle(jsonObject.getString("title"));
                            filterModel.setTitle_ar(jsonObject.getString("title_ar"));
                            JSONArray jsonArray1 = new JSONArray(jsonObject.getString("list"));
                            filterModel.setJsonArray(jsonArray1);
                            filterModelList.add(filterModel);
                        }
                    }else{
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

                //GridLayoutManager gridLayoutManager = new GridLayoutManager(FilterActivity.this,1);
                select_list_view_recycler.setLayoutManager(new LinearLayoutManager(FilterActivity.this, RecyclerView.VERTICAL, false));
                filterAdapter = new FilterAdapter(FilterActivity.this,filterModelList);
                select_list_view_recycler.setAdapter(filterAdapter);

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

    @Override
    public void listOfName(JSONArray jsonArray,String title) {
        this.title=title;
        if (title.equals("Rating")){
            findViewById(R.id.ratingbar).setVisibility(View.VISIBLE);
            findViewById(R.id.list_view_recycler).setVisibility(View.GONE);
        }else {
            findViewById(R.id.ratingbar).setVisibility(View.GONE);
            findViewById(R.id.list_view_recycler).setVisibility(View.VISIBLE);
        }
        filterListModelList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FilterListModel filterListModel = new FilterListModel();
                filterListModel.setId(jsonObject.getString("id"));
                filterListModel.setName(jsonObject.getString("name"));
                filterListModel.setName_ar(jsonObject.getString("name_ar"));
                filterListModel.setSlug(jsonObject.getString("slug"));
                filterListModel.setCategory(title);
                JSONArray jsonArray1 = new JSONArray(jsonObject.getString("brand_list"));
                filterListModel.setJsonArray(jsonArray1);
                filterListModelList.add(filterListModel);
            }
        }catch (JSONException e){

        }

        list_view_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        filterListAdapter = new FilterListAdapter(this,filterListModelList);
        list_view_recycler.setAdapter(filterListAdapter);
    }

    @Override
    public void getFilterChildId(String id, boolean check_uncheck,JSONArray jsonArray,String slug) {
        System.out.println("titel "+title+" id "+id+" check_uncheck "+check_uncheck);
        if(title.equals("Brands")){

            Intent intent =new Intent(this, BrandListForFilterActivity.class);
            intent.putExtra("category_list",jsonArray.toString());
            intent.putExtra("from", "filter");
            intent.putExtra("title", "filter");
            startActivityForResult(intent, 100);
            /*if(slug.equals("glasses") || slug.equals("contact-lenses")){
                Intent intent =new Intent(this, BrandListForFilterActivity.class);
                intent.putExtra("category_list",jsonArray.toString());
                intent.putExtra("from", "filter");
                intent.putExtra("title", "filter");

                startActivityForResult(intent, 100);
            }else{
                if(check_uncheck){
                    set_Brands.add(slug);
                    //sortFilterSessionManager.setFilter_Brands(set_Brands.toString());
                }else{
                    Iterator<String> i=set_Brands.iterator();
                    while(i.hasNext())
                    {
                        String str = i.next();
                        if(str.equals(slug)){
                            i.remove();
                        }
                    }
                    //sortFilterSessionManager.setFilter_Brands(set_Brands.toString());
                    //sortFilterSessionManager.delete(title,id);
                }
            }*/

        }else if(title.equals("Tags")){
            if(check_uncheck){
                //set_Tags.add(id);
                set_Tags.add(slug);
                sortFilterSessionManager.setFilter_Tags(set_Tags.toString());
            }else{
                if(set_Tags.size()==0){
                    String[] split_tags = sortFilterSessionManager.getFilter_Tags().replace("[","").replace("]","").split(",");
                    for(String string_tags:split_tags){
                        set_Tags.add(string_tags);
                    }
                }
                Iterator<String> i=set_Tags.iterator();
                while(i.hasNext())
                {
                    String str = i.next();
                    if(str.equals(slug)){
                        i.remove();
                    }
                }
                sortFilterSessionManager.setFilter_Tags(set_Tags.toString());
                //sortFilterSessionManager.delete(title,id);
            }
        }else if(title.equals("Color")){
            if(check_uncheck){
                //set_Colors.add(id);
                set_Colors.add(slug);
                sortFilterSessionManager.setFilter_Colors(set_Colors.toString());
            }else{
                if(set_Colors.size()==0){
                    String[] split_color = sortFilterSessionManager.getFilter_Colors().replace("[","").replace("]","").split(",");
                    for(String string_color:split_color){
                        set_Colors.add(string_color);
                    }
                }
                Iterator<String> i=set_Colors.iterator();
                while(i.hasNext())
                {
                    String str = i.next();
                    if(str.equals(slug)){
                        i.remove();
                    }
                }
                sortFilterSessionManager.setFilter_Colors(set_Colors.toString());
                //sortFilterSessionManager.delete(title,slug);
            }

        }else if(title.equals("Replacement")){
            if(check_uncheck){
                //set_Replacement.add(id);
                set_Replacement.add(slug);
                sortFilterSessionManager.setFilter_Replacement(set_Replacement.toString());
            }else{
                if(set_Replacement.size()==0){
                    String[] split_replace = sortFilterSessionManager.getFilter_Replacement().replace("[","").replace("]","").split(",");
                    for(String string_replace:split_replace){
                        set_Replacement.add(string_replace);
                    }
                }
                Iterator<String> i=set_Replacement.iterator();
                while(i.hasNext())
                {
                    String str = i.next();
                    if(str.equals(slug)){
                        i.remove();
                    }
                }
                sortFilterSessionManager.setFilter_Replacement(set_Replacement.toString());

                //sortFilterSessionManager.delete(title,id);
            }

        }else if(title.equals("Gender")){
            if(check_uncheck){
                //set_Gender.add(id);
                set_Gender.add(slug);
                sortFilterSessionManager.setFilter_Gender(set_Gender.toString());
            }else{
                if(set_Gender.size()==0){
                    String[] split_gender = sortFilterSessionManager.getFilter_Gender().replace("[","").replace("]","").split(",");
                    for(String string_gender:split_gender){
                        set_Gender.add(string_gender);
                    }
                }
                Iterator<String> i=set_Gender.iterator();
                while(i.hasNext())
                {
                    String str = i.next();
                    if(str.equals(slug)){
                        i.remove();
                    }
                }
                sortFilterSessionManager.setFilter_Gender(set_Gender.toString());
                //sortFilterSessionManager.delete(title,id);
            }

        }else if(title.equals("Rating")){
            if(check_uncheck){
                //set_Rating.add(id);
                set_Rating.add(slug);
                sortFilterSessionManager.setFilter_Rating(set_Rating.toString());
            }else{
                Iterator<String> i=set_Rating.iterator();
                while(i.hasNext())
                {
                    String str = i.next();
                    if(str.equals(slug)){
                        i.remove();
                    }
                }
                sortFilterSessionManager.setFilter_Rating(set_Rating.toString());
                //sortFilterSessionManager.delete(title,id);
            }
        }else if(title.equals("Testing Attribute")){
            if(check_uncheck){
                set_Rating.add(id);
                //sortFilterSessionManager.setFilter_Rating(set_Rating.toString());
            }else{
                Iterator<String> i=test_atri.iterator();
                while(i.hasNext())
                {
                    String str = i.next();
                    if(str.equals(id)){
                        i.remove();
                    }
                }
                //sortFilterSessionManager.setFilter_Rating(set_Rating.toString());
                //sortFilterSessionManager.delete(title,id);
            }
        }
        //filterListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == 100){
                String brand_id = data.getStringExtra("brand_id");
                sortFilterSessionManager.setFilter_Brands(brand_id);
            }
        }
    }
}
