package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class CatViewHolder extends RecyclerView.ViewHolder {
    private View mView;
    private ImageView mImage;
    private TextView mText;

    public CatViewHolder(View itemView) {
        super(itemView);
        mView =itemView;
        mImage = itemView.findViewById(R.id.cat_img);
        mText = itemView.findViewById(R.id.cat_title);
    }
    public void setCat_name(String cat_name) {

        mText.setText(cat_name);

    }
    public void setCat_img(String cat_img) {
        Picasso.get().load(cat_img).fit().into(mImage);

    }
}
