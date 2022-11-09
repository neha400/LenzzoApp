package com.lenzzo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.lenzzo.interfacelenzzo.FilterListInterface;
import com.lenzzo.R;
import com.lenzzo.model.FilterModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<FilterModel> filterModelList;
    private ImageLoader imageLoader;
    private Context context;
    private int lastSelectedPosition = -1;
    private FilterListInterface filterListInterface;
    private boolean isSelected = true;

    public FilterAdapter(Context ctx, List<FilterModel> filterModelList){
        inflater = LayoutInflater.from(ctx);
        this.filterModelList = filterModelList;
        this.context=ctx;
        this.filterListInterface = ((FilterListInterface)context);
    }

    @Override
    public FilterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.filter_recycler_view_item, parent, false);
        FilterAdapter.MyViewHolder holder = new FilterAdapter.MyViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final FilterAdapter.MyViewHolder holder, final int position) {
        if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            holder.text_name.setText(filterModelList.get(position).getTitle());
        }else{
            holder.text_name.setText(filterModelList.get(position).getTitle_ar());
        }
        if(filterModelList.get(position).getTitle().equals("Brands")){
            holder.image.setImageResource(R.drawable.brand);
        }else if(filterModelList.get(position).getTitle().equals("Tags")){
            holder.image.setImageResource(R.drawable.tag_90x90);
        }else if(filterModelList.get(position).getTitle().equals("Color")){
            holder.image.setImageResource(R.drawable.color);
        }else if(filterModelList.get(position).getTitle().equals("Replacement")){
            holder.image.setImageResource(R.drawable.replacement_90x90);
        }else if(filterModelList.get(position).getTitle().equals("Gender")){
            holder.image.setImageResource(R.drawable.users_30);
        }else if(filterModelList.get(position).getTitle().equals("Rating")){
            holder.image.setImageResource(R.drawable.star1);
        }
        if(lastSelectedPosition==position){
            holder.linerLayout.setBackgroundColor(Color.parseColor("#ceb03e"));
            holder.text_name.setTextColor(Color.parseColor("#FFFFFF"));
            //holder.liner_layout.setBackgroundColor(Color.parseColor("#000000"));
            holder.liner_layout.setBackgroundResource(R.drawable.circle_black_background);
        }else
        {
            holder.linerLayout.setBackgroundColor(Color.parseColor("#ededed"));
            holder.text_name.setTextColor(Color.parseColor("#000000"));
            //holder.liner_layout.setBackgroundColor(Color.parseColor("#aaaaaa"));
            holder.liner_layout.setBackgroundResource(R.drawable.circle_gry_background);
        }
        if(isSelected && position==0){
            holder.linerLayout.setBackgroundColor(Color.parseColor("#ceb03e"));
            holder.text_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.liner_layout.setBackgroundResource(R.drawable.circle_black_background);
            filterListInterface.listOfName(filterModelList.get(position).getJsonArray(),filterModelList.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return filterModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView text_name;
        LinearLayout linerLayout;
        LinearLayout liner_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (CircleImageView)itemView.findViewById(R.id.image);
            text_name = (TextView)itemView.findViewById(R.id.text_name);
            linerLayout = (LinearLayout)itemView.findViewById(R.id.linerLayout);
            liner_layout = (LinearLayout)itemView.findViewById(R.id.liner_layout);

            linerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelected = false;
                    lastSelectedPosition = getAdapterPosition();
                    filterListInterface.listOfName(filterModelList.get(lastSelectedPosition).getJsonArray(),filterModelList.get(lastSelectedPosition).getTitle());
                    notifyDataSetChanged();
                }
            });
        }
    }

}
