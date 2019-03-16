package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class SupportMemberViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private ImageView status, profile_img;
    private TextView status_text;
    public TextView recent_message;
    public SupportMemberViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.agent_user_name);
        status = itemView.findViewById(R.id.user_activity_status);
        status_text = itemView.findViewById(R.id.user_status_text);
        recent_message = itemView.findViewById(R.id.user_chat_recent_message);
        profile_img = itemView.findViewById(R.id.agent_user_profile_img);

    }

    public void setUser_name(String user_name) {
        name.setText(user_name);
    }

    public void setUser_status(boolean user_status) {
        if (user_status==true){
            status_text.setText("Online");
            status.setImageResource(R.drawable.chat_online_circle);
        }
        else {
            status_text.setText("Offline");
            status.setImageResource(R.drawable.chat_status_offline_circle);
        }
    }
    public void setUser_profile_img(String user_profile_img) {
        Picasso.get().load(user_profile_img).into(profile_img);
    }

}
