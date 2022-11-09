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

import com.lenzzo.ProductDetailsActivity;
import com.lenzzo.R;
import com.lenzzo.model.PowerModel;

import java.util.List;

public class LeftPowerOneAdapter extends RecyclerView.Adapter<LeftPowerOneAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<PowerModel> powerModelList;
    private Context context;
    OnItemClick itemClick;
    public interface OnItemClick{
        void itemClick(int position);
    }
    public LeftPowerOneAdapter(Context ctx, List<PowerModel> powerModelList,OnItemClick itemClick){
        inflater = LayoutInflater.from(ctx);
        this.powerModelList = powerModelList;
        this.context=ctx;
        this.itemClick=itemClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.power_list_with_radio_button, parent, false);
        //PowerAdapter.MyViewHolder holder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        PowerModel model = powerModelList.get(position);
        holder.selected_text.setText(model.getValue());

        if(powerModelList.get(position).isOutOfStock()){

            holder.selected_text.setText(model.getValue()+" "+context.getResources().getString(R.string.out_of_stock_small));
            holder.selected_text.setTextColor(Color.RED);
            holder.redio_button.setEnabled(false);
            holder.power_layout.setEnabled(false);
        }else{
            holder.selected_text.setText(model.getValue());
            holder.selected_text.setTextColor(Color.BLACK);
            holder.redio_button.setEnabled(true);
            holder.power_layout.setEnabled(true);
        }


        if (powerModelList.get(position).isSelected()){
            holder.redio_button.setImageResource(R.drawable.radio_fill_50);
        }else {
            holder.redio_button.setImageResource(R.drawable.radio_50);
        }

        holder.power_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ProductDetailsActivity) {

                    ((ProductDetailsActivity) context).updatePowerSelectionLeft(position);
                }else {
                    if (itemClick!=null){
                        itemClick.itemClick(position);
                    }

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return powerModelList.size();
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
