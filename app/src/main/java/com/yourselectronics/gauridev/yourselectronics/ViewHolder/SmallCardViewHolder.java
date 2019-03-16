package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class SmallCardViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView mprice;
    public SmallCardViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.small_card_img);
        mprice = itemView.findViewById(R.id.small_card_price);
    }
    public void setImg(String img) {

        Picasso.get().load(img).fit().into(imageView);
    }

    public void setPrice(String price) {
        mprice.setText(price);
    }

}
