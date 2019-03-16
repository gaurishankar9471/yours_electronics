package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class OfferHorizontalViewHolder extends RecyclerView.ViewHolder {
    private TextView title, desc, off_price, real_price, off_percentage;
    private ImageView img;
    public OfferHorizontalViewHolder(View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.offer_hoz_card_img);
        title = itemView.findViewById(R.id.offer_hoz_card_title);
        desc = itemView.findViewById(R.id.offer_hoz_card_desc);
        off_price = itemView.findViewById(R.id.product_off_price);
        real_price = itemView.findViewById(R.id.product_real_price);
        off_percentage = itemView.findViewById(R.id.product_discount_percentage);
        real_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
    public void setProduct_title(String product_title) {
        title.setText(product_title);
    }
    public void setProduct_img(String product_img) {
        Picasso.get().load(product_img).fit().into(img);

    }

    public void setProduct_desc(String product_desc) {
        desc.setText(product_desc);
    }
    public void setProduct_off_price(String product_off_price) {
        off_price.setText(product_off_price);
    }
    public void setProduct_real_price(String product_real_price) {
        real_price.setText(product_real_price);
    }
    public void setProduct_off_percentage(String product_off_percentage) {
        off_percentage.setText(product_off_percentage);
    }

}
