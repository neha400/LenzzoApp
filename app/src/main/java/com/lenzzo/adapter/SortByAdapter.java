package com.lenzzo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.lenzzo.R;
import com.lenzzo.interfacelenzzo.SortByInterface;
import com.lenzzo.model.SortByModel;

import java.util.List;
import java.util.Locale;

public class SortByAdapter extends RecyclerView.Adapter<SortByAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<SortByModel> sortByModelList;
    private ImageLoader imageLoader;
    private Context context;
    private int lastSelectedPosition = -1;
    private SortByInterface sortByInterface;

    public SortByAdapter(Context ctx, List<SortByModel> sortByModelList){
        inflater = LayoutInflater.from(ctx);
        this.sortByModelList = sortByModelList;
        this.context=ctx;
        this.sortByInterface = ((SortByInterface)context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.sort_by_recycler_view_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            holder.text_sort.setText(sortByModelList.get(position).getTitle());
        }else{
            holder.text_sort.setText(sortByModelList.get(position).getTitleAr());
        }
        /*if(Locale.getDefault().getLanguage().equals("en")){
            holder.text_sort.setText(sortByModelList.get(position).getTitle());
        }else{
            holder.text_sort.setText(sortByModelList.get(position).getTitleAr());
        }*/
        holder.redia_button.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return sortByModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        RadioButton redia_button;
        TextView text_sort;
        LinearLayout sort_lay;

        public MyViewHolder(View itemView) {
            super(itemView);
            sort_lay = itemView.findViewById(R.id.sort_lay);
            redia_button = (RadioButton) itemView.findViewById(R.id.redia_button);
            text_sort = (TextView)itemView.findViewById(R.id.text_sort);
            sort_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();

                    sortByInterface.sortByPrice(sortByModelList.get(lastSelectedPosition).getKey(),sortByModelList.get(lastSelectedPosition).getValue());
                    notifyDataSetChanged();
                }
            });

            redia_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();

                    sortByInterface.sortByPrice(sortByModelList.get(lastSelectedPosition).getKey(),sortByModelList.get(lastSelectedPosition).getValue());
                    notifyDataSetChanged();
                }
            });


        }
    }
}
