package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class HozViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private ImageView imageView;
    private TextView mTitle, mOff;

    public HozViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        imageView = itemView.findViewById(R.id.hoz_img);
        mTitle = itemView.findViewById(R.id.hoz_title);
        mOff = itemView.findViewById(R.id.hoz_off);
    }
    public void setImg(String img) {
        Picasso.get().load(img).fit().into(imageView);
    }

    public void setName(String name) {
        mTitle.setText(name);
    }
    public void setOff(String off) {
        mOff.setText(off);

    }
}
