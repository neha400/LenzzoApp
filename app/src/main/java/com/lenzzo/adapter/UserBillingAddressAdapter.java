package com.lenzzo.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenzzo.AddNewAddressActivity;
import com.lenzzo.CheckOutActivity;
import com.lenzzo.R;
import com.lenzzo.interfacelenzzo.UserAddressInterface;
import com.lenzzo.api.API;
import com.lenzzo.model.UserBillingAddressModel;
import com.lenzzo.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBillingAddressAdapter extends RecyclerView.Adapter<UserBillingAddressAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<UserBillingAddressModel> userBillingAddressModelList;
    private ImageLoader imageLoader;
    private Context context;
    private int lastSelectedPosition = -1;
    UserAddressInterface userAddressInterface;

    public UserBillingAddressAdapter(Context ctx, List<UserBillingAddressModel> userBillingAddressModelList){
        inflater = LayoutInflater.from(ctx);
        this.userBillingAddressModelList = userBillingAddressModelList;
        this.context=ctx;
        this.userAddressInterface = ((UserAddressInterface)context);
    }


    @Override
    public UserBillingAddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.user_billing_address_recycler_item, parent, false);
        UserBillingAddressAdapter.MyViewHolder holder = new UserBillingAddressAdapter.MyViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final UserBillingAddressAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.name_text_view.setText(userBillingAddressModelList.get(position).getFull_name());
        holder.area_text_view.setText(userBillingAddressModelList.get(position).getArea());
        holder.block_text_view.setText(userBillingAddressModelList.get(position).getBlock());
        holder.street_text_view.setText(userBillingAddressModelList.get(position).getStreet());
        holder.avenue_text_view.setText(userBillingAddressModelList.get(position).getAvenue());
        holder.house_text_view.setText(userBillingAddressModelList.get(position).getHouse_no());
        holder.floor_text_view.setText(userBillingAddressModelList.get(position).getFloor_no());
        holder.flat_text_view.setText(userBillingAddressModelList.get(position).getFlat_no());
        holder.phone_text_view.setText(userBillingAddressModelList.get(position).getPhone_no());
        holder.paci_text_view.setText(userBillingAddressModelList.get(position).getPaci_number());
        holder.comment_text_view.setText(userBillingAddressModelList.get(position).getComments());
        holder.current_city_text_view.setText(userBillingAddressModelList.get(position).getCitynames());

        holder.current_location_text_view.setText(userBillingAddressModelList.get(position).getCurrrent_location());
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getCurrrent_location())){
            holder.cur_location_lay.setVisibility(View.GONE);
        }else {
            holder.cur_location_lay.setVisibility(View.VISIBLE);

        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getFull_name())){
            holder.liner_layout_n.setVisibility(View.GONE);
        }else {
            holder.liner_layout_n.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getArea())){
            holder.liner_layout_a.setVisibility(View.GONE);
        }else {
            holder.liner_layout_a.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getBlock())){
            holder.liner_layout_b.setVisibility(View.GONE);
        }else {
            holder.liner_layout_b.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getStreet())){
            holder.liner_layout_s.setVisibility(View.GONE);
        }else {
            holder.liner_layout_s.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getAvenue())){
            holder.liner_layout_av.setVisibility(View.GONE);
        }else {
            holder.liner_layout_av.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getHouse_no())){
            holder.liner_layout_h.setVisibility(View.GONE);
        }else {
            holder.liner_layout_h.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getFloor_no())){
            holder.liner_layout_fl.setVisibility(View.GONE);
        }else {
            holder.liner_layout_fl.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getFlat_no())){
            holder.liner_layout_fa.setVisibility(View.GONE);
        }else {
            holder.liner_layout_fa.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getPhone_no())){
            holder.liner_layout_ph.setVisibility(View.GONE);
        }else {
            holder.liner_layout_ph.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getPaci_number())){
            holder.liner_layout_pa.setVisibility(View.GONE);
        }else {
            holder.liner_layout_pa.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getComments())){
            holder.liner_layout_c.setVisibility(View.GONE);
        }else {
            holder.liner_layout_c.setVisibility(View.VISIBLE);
        }
        if (userBillingAddressModelList.get(position).isSelected()){  // new code implement
            holder.select_tv.setText(context.getResources().getString(R.string.select));
            holder.delete_layout.setClickable(false);
            //holder.select_tv.setBackground(context.getResources().getDrawable(R.drawable.check_out_button));
            holder.relativ_layour.setBackground(context.getResources().getDrawable(R.drawable.address_unselected_bg));
            holder.select_check_box.setVisibility(View.VISIBLE);
            holder.unselect_check_box.setVisibility(View.GONE);
            holder.select_tv.setTextColor(context.getResources().getColor(R.color.black));
        }else {
            holder.select_tv.setText(context.getResources().getString(R.string.unselected));
            holder.relativ_layour.setBackground(context.getResources().getDrawable(R.drawable.check_out_button));
           // holder.select_tv.setBackground(context.getResources().getDrawable(R.drawable.address_unselected_bg));
            holder.select_check_box.setVisibility(View.GONE);
            holder.unselect_check_box.setVisibility(View.VISIBLE);
            holder.select_tv.setTextColor(context.getResources().getColor(R.color.white));
        }
       /* if (userBillingAddressModelList.get(position).isSelected()){    // Old code coments
            holder.select_tv.setText(context.getResources().getString(R.string.selected));
            holder.select_tv.setBackground(context.getResources().getDrawable(R.drawable.check_out_button));
        }else {
            holder.select_tv.setText(context.getResources().getString(R.string.select));
            holder.select_tv.setBackground(context.getResources().getDrawable(R.drawable.address_unselected_bg));
        }*/

        //holder.redia_button.setChecked(lastSelectedPosition == position);
        holder.delete_layout.setOnClickListener(v -> dialogBox(userBillingAddressModelList.get(position).getId(),position));

        holder.relativ_layour.setOnClickListener(v -> {
            /*if (userBillingAddressModelList.get(lastSelectedPosition).isSelected()){
                userBillingAddressModelList.get(lastSelectedPosition).setSelected(false);
            }else {
                userBillingAddressModelList.get(lastSelectedPosition).setSelected(true);
                userAddressInterface.getAddressId(userBillingAddressModelList.get(lastSelectedPosition).getId(),userBillingAddressModelList.get(lastSelectedPosition).getFull_name(),userBillingAddressModelList.get(lastSelectedPosition).getArea(),userBillingAddressModelList.get(lastSelectedPosition).getBlock(),userBillingAddressModelList.get(lastSelectedPosition).getStreet(),userBillingAddressModelList.get(lastSelectedPosition).getAvenue(),userBillingAddressModelList.get(lastSelectedPosition).getHouse_no(),userBillingAddressModelList.get(lastSelectedPosition).getFloor_no(),userBillingAddressModelList.get(lastSelectedPosition).getFlat_no(),userBillingAddressModelList.get(lastSelectedPosition).getPhone_no(),userBillingAddressModelList.get(lastSelectedPosition).getComments());
            }

            notifyItemChanged(position);*/
            /*if (!userBillingAddressModelList.get(position).isSelected()){
                userBillingAddressModelList.get(position).setSelected(true);
                userAddressInterface.getAddressId(userBillingAddressModelList.get(position).getId(),userBillingAddressModelList.get(position).getFull_name(),userBillingAddressModelList.get(position).getArea(),userBillingAddressModelList.get(position).getBlock(),userBillingAddressModelList.get(position).getStreet(),userBillingAddressModelList.get(position).getAvenue(),userBillingAddressModelList.get(position).getHouse_no(),userBillingAddressModelList.get(position).getFloor_no(),userBillingAddressModelList.get(position).getFlat_no(),userBillingAddressModelList.get(position).getPhone_no(),userBillingAddressModelList.get(position).getComments());
            }*/
            userBillingAddressModelList.get(position).setSelected(true);
            userAddressInterface.getAddressId(userBillingAddressModelList.get(position).getId(),userBillingAddressModelList.get(position).getFull_name(),userBillingAddressModelList.get(position).getArea(),userBillingAddressModelList.get(position).getBlock(),userBillingAddressModelList.get(position).getStreet(),userBillingAddressModelList.get(position).getAvenue(),userBillingAddressModelList.get(position).getHouse_no(),userBillingAddressModelList.get(position).getFloor_no(),userBillingAddressModelList.get(position).getFlat_no(),userBillingAddressModelList.get(position).getPhone_no(),userBillingAddressModelList.get(position).getComments());

            for (int i = 0; i<userBillingAddressModelList.size(); i++) {
                if (i != position) {
                    userBillingAddressModelList.get(i).setSelected(false);
                }
            }
            //notifyItemChanged(position);
            notifyDataSetChanged();
        });

        holder. address_lay.setOnClickListener(v -> {
            //lastSelectedPosition = position;
            //userAddressInterface.getAddressId(userBillingAddressModelList.get(lastSelectedPosition).getId(),userBillingAddressModelList.get(lastSelectedPosition).getFull_name(),userBillingAddressModelList.get(lastSelectedPosition).getArea(),userBillingAddressModelList.get(lastSelectedPosition).getBlock(),userBillingAddressModelList.get(lastSelectedPosition).getStreet(),userBillingAddressModelList.get(lastSelectedPosition).getAvenue(),userBillingAddressModelList.get(lastSelectedPosition).getHouse_no(),userBillingAddressModelList.get(lastSelectedPosition).getFloor_no(),userBillingAddressModelList.get(lastSelectedPosition).getFlat_no(),userBillingAddressModelList.get(lastSelectedPosition).getPhone_no(),userBillingAddressModelList.get(lastSelectedPosition).getComments());
            //notifyDataSetChanged();
            //notifyItemChanged(position);
            /*if (!userBillingAddressModelList.get(position).isSelected()){
                userBillingAddressModelList.get(position).setSelected(true);
                userAddressInterface.getAddressId(userBillingAddressModelList.get(position).getId(),userBillingAddressModelList.get(position).getFull_name(),userBillingAddressModelList.get(position).getArea(),userBillingAddressModelList.get(position).getBlock(),userBillingAddressModelList.get(position).getStreet(),userBillingAddressModelList.get(position).getAvenue(),userBillingAddressModelList.get(position).getHouse_no(),userBillingAddressModelList.get(position).getFloor_no(),userBillingAddressModelList.get(position).getFlat_no(),userBillingAddressModelList.get(position).getPhone_no(),userBillingAddressModelList.get(position).getComments());
            }*/

            userBillingAddressModelList.get(position).setSelected(true);
            userAddressInterface.getAddressId(userBillingAddressModelList.get(position).getId(),userBillingAddressModelList.get(position).getFull_name(),userBillingAddressModelList.get(position).getArea(),userBillingAddressModelList.get(position).getBlock(),userBillingAddressModelList.get(position).getStreet(),userBillingAddressModelList.get(position).getAvenue(),userBillingAddressModelList.get(position).getHouse_no(),userBillingAddressModelList.get(position).getFloor_no(),userBillingAddressModelList.get(position).getFlat_no(),userBillingAddressModelList.get(position).getPhone_no(),userBillingAddressModelList.get(position).getComments());

            for (int i = 0; i<userBillingAddressModelList.size(); i++) {
                if (i != position) {
                    userBillingAddressModelList.get(i).setSelected(false);
                }
            }
            notifyDataSetChanged();
        });

        holder.edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNewAddressActivity.class);
                intent.putExtra("user_billing_address_id",userBillingAddressModelList.get(position).getId());
                intent.putExtra("get_full_name",userBillingAddressModelList.get(position).getFull_name());
                intent.putExtra("get_area_text",userBillingAddressModelList.get(position).getArea());
                intent.putExtra("get_block_text",userBillingAddressModelList.get(position).getBlock());
                intent.putExtra("get_street_text",userBillingAddressModelList.get(position).getStreet());
                intent.putExtra("get_avenue_text",userBillingAddressModelList.get(position).getAvenue());
                intent.putExtra("get_house_text",userBillingAddressModelList.get(position).getHouse_no());
                intent.putExtra("get_floor_text",userBillingAddressModelList.get(position).getFloor_no());
                intent.putExtra("get_flat_text",userBillingAddressModelList.get(position).getFlat_no());
                intent.putExtra("get_phone_text",userBillingAddressModelList.get(position).getPhone_no());
                intent.putExtra("get_paci_text",userBillingAddressModelList.get(position).getPaci_number());
                intent.putExtra("get_comment_text",userBillingAddressModelList.get(position).getComments());
                intent.putExtra("get_current_location_name",userBillingAddressModelList.get(position).getCurrrent_location());
                intent.putExtra("get_current_location_lat",userBillingAddressModelList.get(position).getLatitude());
                intent.putExtra("get_current_location_long",userBillingAddressModelList.get(position).getLongitude());
                intent.putExtra("get_city_name",userBillingAddressModelList.get(position).getCitynames());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userBillingAddressModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout edit_layout;
        LinearLayout delete_layout;
        LinearLayout select_layour;
        TextView name_text_view;
        TextView area_text_view;
        TextView block_text_view;
        TextView street_text_view;
        TextView avenue_text_view;
        TextView house_text_view;
        TextView floor_text_view;
        TextView flat_text_view;
        TextView phone_text_view;
        TextView paci_text_view;
        TextView comment_text_view;
        TextView select_tv;
        TextView current_city_text_view;
        TextView current_location_text_view;
        RadioButton redia_button;
        LinearLayout liner_layout_n, address_lay;
        LinearLayout liner_layout_a;
        LinearLayout liner_layout_b;
        LinearLayout liner_layout_s;
        LinearLayout liner_layout_av;
        LinearLayout liner_layout_h;
        LinearLayout liner_layout_fl;
        LinearLayout liner_layout_fa;
        LinearLayout liner_layout_ph;
        LinearLayout liner_layout_pa;
        LinearLayout liner_layout_c;
        LinearLayout cur_location_lay;
        ImageView unselect_check_box;
        ImageView select_check_box;
        RelativeLayout relativ_layour;

        public MyViewHolder(View itemView) {
            super(itemView);
            edit_layout = (LinearLayout)itemView.findViewById(R.id.edit_layout);
            delete_layout = (LinearLayout)itemView.findViewById(R.id.delete_layout);
            select_layour = (LinearLayout)itemView.findViewById(R.id.select_layour);
            name_text_view = (TextView)itemView.findViewById(R.id.name_text_view);
            area_text_view = (TextView)itemView.findViewById(R.id.area_text_view);
            block_text_view = (TextView)itemView.findViewById(R.id.block_text_view);
            street_text_view = (TextView)itemView.findViewById(R.id.street_text_view);
            avenue_text_view = (TextView)itemView.findViewById(R.id.avenue_text_view);
            house_text_view = (TextView)itemView.findViewById(R.id.house_text_view);
            floor_text_view = (TextView)itemView.findViewById(R.id.floor_text_view);
            flat_text_view = (TextView)itemView.findViewById(R.id.flat_text_view);
            phone_text_view = (TextView)itemView.findViewById(R.id.phone_text_view);
            paci_text_view = (TextView)itemView.findViewById(R.id.paci_text_view);
            current_city_text_view = itemView.findViewById(R.id.current_city_text_view);
            comment_text_view = (TextView)itemView.findViewById(R.id.comment_text_view);
            select_tv = itemView.findViewById(R.id.select_tv);
            unselect_check_box=itemView.findViewById(R.id.unselect_check_box);
            select_check_box=itemView.findViewById(R.id.select_check_box);
            relativ_layour=itemView.findViewById(R.id.relativ_layour);
            current_location_text_view = (TextView)itemView.findViewById(R.id.current_location_text_view);
            cur_location_lay = (LinearLayout)itemView.findViewById(R.id.cur_location_lay);
            liner_layout_n = (LinearLayout)itemView.findViewById(R.id.liner_layout_n);
            liner_layout_a = (LinearLayout)itemView.findViewById(R.id.liner_layout_a);
            liner_layout_b = (LinearLayout)itemView.findViewById(R.id.liner_layout_b);
            liner_layout_s = (LinearLayout)itemView.findViewById(R.id.liner_layout_s);
            liner_layout_av = (LinearLayout)itemView.findViewById(R.id.liner_layout_av);
            liner_layout_h = (LinearLayout)itemView.findViewById(R.id.liner_layout_h);
            liner_layout_fl = (LinearLayout)itemView.findViewById(R.id.liner_layout_fl);
            liner_layout_fa = (LinearLayout)itemView.findViewById(R.id.liner_layout_fa);
            liner_layout_ph = (LinearLayout)itemView.findViewById(R.id.liner_layout_ph);
            liner_layout_pa = (LinearLayout)itemView.findViewById(R.id.liner_layout_pa);
            liner_layout_c = (LinearLayout)itemView.findViewById(R.id.liner_layout_c);
            address_lay = itemView.findViewById(R.id.address_lay);

            redia_button = (RadioButton) itemView.findViewById(R.id.redia_button);
            /*redia_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();

                    System.out.println("check position "+userBillingAddressModelList.get(lastSelectedPosition).getArea()+"fdgfd "+userBillingAddressModelList.get(lastSelectedPosition).getId());
                    userAddressInterface.getAddressId(userBillingAddressModelList.get(lastSelectedPosition).getId(),userBillingAddressModelList.get(lastSelectedPosition).getFull_name(),userBillingAddressModelList.get(lastSelectedPosition).getArea(),userBillingAddressModelList.get(lastSelectedPosition).getBlock(),userBillingAddressModelList.get(lastSelectedPosition).getStreet(),userBillingAddressModelList.get(lastSelectedPosition).getAvenue(),userBillingAddressModelList.get(lastSelectedPosition).getHouse_no(),userBillingAddressModelList.get(lastSelectedPosition).getFloor_no(),userBillingAddressModelList.get(lastSelectedPosition).getFlat_no(),userBillingAddressModelList.get(lastSelectedPosition).getPhone_no(),userBillingAddressModelList.get(lastSelectedPosition).getComments());
                    notifyDataSetChanged();
                }
            });*/

        }
    }

    private void dialogBox(final String user_billing_address_id,final int position){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.address_delete_dialog);
        TextView yes_text_view = (TextView)dialog.findViewById(R.id.yes_text_view);
        TextView no_text_view = (TextView)dialog.findViewById(R.id.no_text_view);
        yes_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckOutActivity)context).getUserShippingAddress();
                dialog.dismiss();
                userAddressDelete(user_billing_address_id,position);
            }
        });
        no_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void userAddressDelete(final String user_billing_address_id,final int position){
        //gifImageView.setVisibility(View.VISIBLE);
        SessionManager sessionManager = new SessionManager(context);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"user_billing_address_delete", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //gifImageView.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        removeAt(position);
                        ((CheckOutActivity)context).getUserShippingAddress();
                        ((CheckOutActivity)context).recreate();
                    }else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // gifImageView.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_billing_address_id",user_billing_address_id);
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

    public void removeAt(int position) {
        userBillingAddressModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userBillingAddressModelList.size());
    }
}
