package com.lenzzo.adapter;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.ImageLoader;
import com.lenzzo.interfacelenzzo.FilterListChildInterface;
import com.lenzzo.R;
import com.lenzzo.model.FilterListModel;
import com.lenzzo.utility.SortFilterSessionManager;

import java.util.List;

public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<FilterListModel> filterModelList;
    private ImageLoader imageLoader;
    private Context context;
    private int lastSelectedPosition = -1;
    private FilterListChildInterface filterListChildInterface;
    private SortFilterSessionManager sortFilterSessionManager;


    public FilterListAdapter(Context ctx, List<FilterListModel> filterModelList){
        inflater = LayoutInflater.from(ctx);
        this.filterModelList = filterModelList;
        this.context=ctx;
        this.filterListChildInterface = ((FilterListChildInterface)context);
        this.sortFilterSessionManager = new SortFilterSessionManager(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public FilterListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.filter_list_recycler_view_item, parent, false);
        FilterListAdapter.MyViewHolder holder = new FilterListAdapter.MyViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            holder.check_iv.setImageResource(R.drawable.ic_brand_ltr);
        }else{
            holder.check_iv.setImageResource(R.drawable.ic_brand_rtl);
        }
        if (filterModelList.get(position).getCategory().equals("Brands")){
            holder.check_iv.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
        }else {
            holder.check_iv.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.VISIBLE);
        }

        if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            holder.list_name.setText(filterModelList.get(position).getName());
        }else{
            holder.list_name.setText(filterModelList.get(position).getName_ar());
        }

        String[] split_filter_brands = sortFilterSessionManager.getFilter_Brands().replace("[","").replace("]","").split(", ");
        for (int i=0; i < split_filter_brands.length; i++)
        {
            if(filterModelList.get(position).getSlug().equals(split_filter_brands[i])){
                holder.checkbox.setChecked(true);
            }
        }

        String[] split_filter_tags = sortFilterSessionManager.getFilter_Tags().replace("[","").replace("]","").split(", ");
        for (int i=0; i < split_filter_tags.length; i++)
        {
            if(filterModelList.get(position).getSlug().equals(split_filter_tags[i])){
                holder.checkbox.setChecked(true);
            }
        }

        String[] split_filter_color = sortFilterSessionManager.getFilter_Colors().replace("[","").replace("]","").split(", ");
        for (int i=0; i < split_filter_color.length; i++)
        {
            if(filterModelList.get(position).getSlug().toLowerCase().equals(split_filter_color[i])){
                holder.checkbox.setChecked(true);
            }
        }

        String[] split_filter_replacement = sortFilterSessionManager.getFilter_Replacement().replace("[","").replace("]","").split(", ");
        for (int i=0; i < split_filter_replacement.length; i++)
        {
            if(filterModelList.get(position).getSlug().equals(split_filter_replacement[i])){
                holder.checkbox.setChecked(true);
            }
        }

        String[] split_filter_gender = sortFilterSessionManager.getFilter_Gender().replace("[","").replace("]","").split(", ");
        for (int i=0; i < split_filter_gender.length; i++)
        {
            if(filterModelList.get(position).getSlug().equals(split_filter_gender[i])){
                holder.checkbox.setChecked(true);
            }
        }

        String[] split_filter_rating = sortFilterSessionManager.getFilter_Rating().replace("[","").replace("]","").split(", ");
        for (int i=0; i < split_filter_rating.length; i++)
        {
            if(filterModelList.get(position).getSlug().equals(split_filter_rating[i])){
                holder.checkbox.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return filterModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView list_name;
        CheckBox checkbox;
        ImageView check_iv;
        RatingBar ratingBar;
        LinearLayout liner;
        LinearLayout relativeLayout;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public MyViewHolder(View itemView) {
            super(itemView);
            list_name = (TextView)itemView.findViewById(R.id.list_name);
            checkbox = (CheckBox)itemView.findViewById(R.id.checkbox);
            check_iv = itemView.findViewById(R.id.check_iv);
            liner = (LinearLayout)itemView.findViewById(R.id.liner);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

            if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                liner.setGravity(Gravity.END);

            }else{
                liner.setGravity(Gravity.START);
            }

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    if(!checkbox.isChecked()){
                        System.out.println("clicked"+filterModelList.get(lastSelectedPosition).getId()+"array "+filterModelList.get(lastSelectedPosition).getJsonArray()+" sulag  "+filterModelList.get(lastSelectedPosition).getSlug());
                        checkbox.setChecked(true);
                        filterListChildInterface.getFilterChildId(filterModelList.get(lastSelectedPosition).getId(),true,filterModelList.get(lastSelectedPosition).getJsonArray(),filterModelList.get(lastSelectedPosition).getSlug());
                    }else{
                        System.out.println("clicked"+filterModelList.get(lastSelectedPosition).getId()+"array "+filterModelList.get(lastSelectedPosition).getJsonArray()+" sulag  "+filterModelList.get(lastSelectedPosition).getSlug());
                        checkbox.setChecked(false);
                        filterListChildInterface.getFilterChildId(filterModelList.get(lastSelectedPosition).getId(),false,filterModelList.get(lastSelectedPosition).getJsonArray(),filterModelList.get(lastSelectedPosition).getSlug());
                    }
                }
            });

            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    if(checkbox.isChecked()){
                        //System.out.println(lastSelectedPosition);
                        //checkbox.setChecked(true);
                        filterListChildInterface.getFilterChildId(filterModelList.get(lastSelectedPosition).getId(),true,filterModelList.get(lastSelectedPosition).getJsonArray(),filterModelList.get(lastSelectedPosition).getSlug());
                    }else {
                        //System.out.println(lastSelectedPosition);
                        filterListChildInterface.getFilterChildId(filterModelList.get(lastSelectedPosition).getId(),false,filterModelList.get(lastSelectedPosition).getJsonArray(),filterModelList.get(lastSelectedPosition).getSlug());
                    }
                    //notifyDataSetChanged();
                    //notifyItemChanged(lastSelectedPosition);
                }
            });
        }
    }
}
