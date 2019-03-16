package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class SingleImageViewHolder extends RecyclerView.ViewHolder{
    private ImageView img;

    public SingleImageViewHolder(View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.single_product_img_rc_view);
    }
    public void setProduct_img(String product_img) {
        Picasso.get().load(product_img).fit().into(img);
    }
}
