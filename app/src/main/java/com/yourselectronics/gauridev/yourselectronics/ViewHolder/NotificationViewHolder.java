package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.GetTimeAgo;
import com.yourselectronics.gauridev.yourselectronics.R;


public class NotificationViewHolder extends RecyclerView.ViewHolder {
    private TextView title, desc, time;
    private ImageView image;
    public ImageView close_icon;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.notification_img);
        title = itemView.findViewById(R.id.notification_title);
        desc = itemView.findViewById(R.id.notification_desc);
        time = itemView.findViewById(R.id.notification_time);
        close_icon = itemView.findViewById(R.id.notification_close_icon);

    }
    public void setNotification_title(String notification_title) {
        title.setText(notification_title);
    }
    public void setNotification_desc(String notification_desc) {
        desc.setText(notification_desc);

    }
    public void setNotification_img(String notification_img) {
        Picasso.get().load(notification_img).fit().into(image);

    }
    public void setNotification_time(Long notification_time) {
        String time_value = GetTimeAgo.getTimeAgo(notification_time,itemView.getContext());
        time.setText(time_value);

    }
}
