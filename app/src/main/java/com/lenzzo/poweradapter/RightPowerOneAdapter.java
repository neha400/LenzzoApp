package com.lenzzo.poweradapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.lenzzo.ProductDetailsActivity;
import com.lenzzo.R;
import com.lenzzo.model.RightPowerModel;

import java.util.List;

public class RightPowerOneAdapter extends RecyclerView.Adapter<RightPowerOneAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<RightPowerModel> rightPowerModelList;
    private Context context;
    LeftPowerOneAdapter.OnItemClick onItemClick;

    public RightPowerOneAdapter(Context ctx, List<RightPowerModel> rightPowerModelList, LeftPowerOneAdapter.OnItemClick itemClick){
        inflater = LayoutInflater.from(ctx);
        this.rightPowerModelList = rightPowerModelList;
        this.context=ctx;
        onItemClick=itemClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.power_list_with_radio_button1, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RightPowerOneAdapter.MyViewHolder holder, final int position) {
        holder.selected_text.setText(rightPowerModelList.get(position).getValue());

        if(rightPowerModelList.get(position).isOutOfStock()){
            holder.selected_text.setText(rightPowerModelList.get(position).getValue()+" "+context.getResources().getString(R.string.out_of_stock_small));
            holder.selected_text.setTextColor(Color.RED);
            holder.redio_button.setEnabled(false);
            holder.power_layout.setEnabled(false);
        }else{
            holder.selected_text.setText(rightPowerModelList.get(position).getValue());
            holder.selected_text.setTextColor(Color.BLACK);
            holder.redio_button.setEnabled(true);
            holder.power_layout.setEnabled(true);
        }

        if (rightPowerModelList.get(position).isSelected()){
            holder.redio_button.setImageResource(R.drawable.radio_fill_50);
        }else {
            holder.redio_button.setImageResource(R.drawable.radio_50);
        }
        holder.power_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof  ProductDetailsActivity) {
                    ((ProductDetailsActivity) context).updatepowerSelectionRight(position);
                }else {
                    if (onItemClick!=null){
                        onItemClick.itemClick(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rightPowerModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView redio_button;
        TextView selected_text;
        LinearLayout power_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            redio_button = (ImageView)itemView.findViewById(R.id.redio_button);
            selected_text=(TextView)itemView.findViewById(R.id.selected_text);
            power_layout = itemView.findViewById(R.id.power_layout);
        }
    }
}
