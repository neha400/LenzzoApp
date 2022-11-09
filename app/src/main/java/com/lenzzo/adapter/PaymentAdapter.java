package com.lenzzo.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.ImageLoader;
import com.lenzzo.interfacelenzzo.PaymentInterface;
import com.lenzzo.R;
import com.lenzzo.model.PaymentModel;
import com.lenzzo.utility.CustomVolleyRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<PaymentModel> paymentModelList;
    private ImageLoader imageLoader;
    private Context context;
    private int lastSelectedPosition = -1;
    private PaymentInterface paymentInterface;

    public PaymentAdapter(Context ctx, List<PaymentModel> paymentModelList){
        inflater = LayoutInflater.from(ctx);
        this.paymentModelList = paymentModelList;
        this.context=ctx;
        this.paymentInterface = ((PaymentInterface)context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.payment_recycler_view_item, parent, false);
        //MyViewHolder holder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

       // imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
       // imageLoader.get(paymentModelList.get(position).getLogo(), ImageLoader.getImageListener(holder.logo, R.drawable.no_img, R.drawable.no_img));

        Log.d("sjhfdjhedf",paymentModelList.get(position).getLogo());

        Picasso.get().load(paymentModelList.get(position).getLogo())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.logo);
        holder.radioButton.setChecked(lastSelectedPosition == position);
        if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            holder.payment_text.setText(paymentModelList.get(position).getName());
        }else{
            holder.payment_text.setText(paymentModelList.get(position).getName_ar());
        }
    }

    @Override
    public int getItemCount() {
        return paymentModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        RadioButton radioButton;
        TextView payment_text;
        ImageView logo;
        LinearLayout pay_option_lay;

        public MyViewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton)itemView.findViewById(R.id.radioButton);
            payment_text = (TextView)itemView.findViewById(R.id.payment_text);
            pay_option_lay = itemView.findViewById(R.id.pay_option_lay);
            logo = (ImageView)itemView.findViewById(R.id.logo);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    paymentInterface.paymentMode(paymentModelList.get(lastSelectedPosition).getId(),paymentModelList.get(lastSelectedPosition).getName());
                    notifyDataSetChanged();
                }
            });

            pay_option_lay.setOnClickListener(view ->{
                lastSelectedPosition = getAdapterPosition();
                paymentInterface.paymentMode(paymentModelList.get(lastSelectedPosition).getId(),paymentModelList.get(lastSelectedPosition).getName());
                notifyDataSetChanged();
            });
        }
    }
}
