package com.lenzzo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenzzo.R;
import com.lenzzo.model.NotificationsPojoModel.NotificationsResponse;

import java.util.ArrayList;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.Holder> {

    ArrayList<NotificationsResponse> notificationsModels;
    Context context;

    public NotificationListAdapter(Context context, ArrayList<NotificationsResponse> notificationsModels) {
        this.context = context;
        this.notificationsModels = notificationsModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_recycler_view_item, parent, false);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.notification_title.setText(notificationsModels.get(position).getTitle());
        holder.notification_description.setText(notificationsModels.get(position).getMessage());
        holder.notification_create_date.setText(notificationsModels.get(position).getCreatedAt());

    }


    @Override
    public int getItemCount() {
        return notificationsModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView notification_title,notification_description,notification_create_date;
        ImageView notify_image;

        public Holder(@NonNull View itemView) {
            super(itemView);

            notification_title=itemView.findViewById(R.id.notification_title);
            notification_description=itemView.findViewById(R.id.notification_description);
            notification_create_date=itemView.findViewById(R.id.notification_create_date);
            notify_image=itemView.findViewById(R.id.notify_image);

        }
    }
}
