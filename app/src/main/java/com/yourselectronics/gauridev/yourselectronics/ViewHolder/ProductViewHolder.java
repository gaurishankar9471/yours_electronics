package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private ImageView m_product_img;
    private TextView  m_product_title, m_product_desc,m_product_off_price, m_product_real_price, m_product_discount_percentage;

    public ProductViewHolder(View itemView) {
        super(itemView);
        mView =itemView;

        m_product_img = itemView.findViewById(R.id.product_img);
        m_product_title = itemView.findViewById(R.id.product_title);
        m_product_desc = itemView.findViewById(R.id.product_desc);
        m_product_off_price = itemView.findViewById(R.id.product_off_price);
        m_product_real_price = itemView.findViewById(R.id.product_real_price);
        m_product_real_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        m_product_discount_percentage = itemView.findViewById(R.id.product_discount_percentage);


    }

    public void setProduct_img(String product_img) {
        Picasso.get().load(product_img).fit().into(m_product_img);

    }
    public void setProduct_title(String product_title) {
        m_product_title.setText(product_title);
    }
    public void setProduct_desc(String product_desc) {
        m_product_desc.setText(product_desc);
    }
    public void setProduct_off_price(String product_off_price) {
        m_product_off_price.setText("Rs. "+product_off_price);
    }
    public void setProduct_real_price(String product_real_price) {
        m_product_real_price.setText("Rs. "+product_real_price);
    }
    public void setProduct_discount_percentage(String product_discount_percentage) {
        m_product_discount_percentage.setText(product_discount_percentage);
    }

}
