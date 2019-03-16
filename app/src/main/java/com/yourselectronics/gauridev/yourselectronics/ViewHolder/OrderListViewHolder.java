package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class OrderListViewHolder extends RecyclerView.ViewHolder {
    private TextView title, desc , status;
    private ImageView img;
    public OrderListViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.order_list_title);
        desc = itemView.findViewById(R.id.order_list_desc);
        img = itemView.findViewById(R.id.order_list_img);
        status = itemView.findViewById(R.id.order_list_status);

    }

    public void setItem_name(String item_name) {
        title.setText(item_name);
    }
    public void setItem_desc(String item_desc) {
        desc.setText(item_desc);
    }
    public void setItem_img(String item_img) {
        Picasso.get().load(item_img).fit().into(img);
    }


}
