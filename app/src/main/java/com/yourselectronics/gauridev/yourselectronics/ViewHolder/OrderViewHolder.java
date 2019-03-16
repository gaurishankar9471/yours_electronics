package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class OrderViewHolder  extends RecyclerView.ViewHolder {
    private ImageView img;
    private TextView morder_status,o_title,o_desc,o_price,o_method,o_address;
    public TextView order_id;
    public OrderViewHolder(View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.o_img);
        order_id = itemView.findViewById(R.id.order_id);
        morder_status = itemView.findViewById(R.id.order_status);
        o_title = itemView.findViewById(R.id.o_title);
        o_desc = itemView.findViewById(R.id.o_desc);
        o_price = itemView.findViewById(R.id.o_price);
        o_method = itemView.findViewById(R.id.o_method);


    }
    public void setItem_name(String item_name) {
        o_title.setText(item_name);
    }
    public void setItem_desc(String item_desc) {
        o_desc.setText(item_desc);
    }
    public void setItem_quantity(String item_quantity) {

    }
    public void setItem_img(String item_img) {
        Picasso.get().load(item_img).fit().into(img);
    }
    public void setPayment_method(String payment_method) {
        o_method.setText(payment_method);
    }
    public void setPrice(String price) {
        o_price.setText(price);
    }
    public void setOrder_status(String order_status) {
        morder_status.setText(order_status);
    }

}
