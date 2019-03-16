package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class CartViewHolder extends RecyclerView.ViewHolder {
    private TextView mTitle, mDesc,mproduct_off_price,mproduct_real_price,mproduct_discount_percentage, item_price,total_item_price;
    private ImageView mImage;
    public Spinner spinner;
    public CardView mRemoveBtn;
    public View mView;
    public TextView quantity, quantity_text;
    public  ArrayAdapter<String> adapter;
    public ImageView overflowIcon;



    public CartViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

      /*  spinner = itemView.findViewById(R.id.spinner);
        String[] items = new String[]{"one", "two", "three","four","five",};
        adapter = new ArrayAdapter<String>(mView.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
       // spinner.setOnItemSelectedListener();

       */

      quantity_text = itemView.findViewById(R.id.item_quantity);

        mImage = itemView.findViewById(R.id.d_img);
        mTitle = itemView.findViewById(R.id.d_title);
        mDesc = itemView.findViewById(R.id.d_desc);

        mproduct_off_price = itemView.findViewById(R.id.product_off_price);
        mproduct_real_price = itemView.findViewById(R.id.product_real_price);
        mproduct_real_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mproduct_discount_percentage = itemView.findViewById(R.id.product_discount_percentage);
        mRemoveBtn = itemView.findViewById(R.id.cart_item_remove_btn);
        overflowIcon = itemView.findViewById(R.id.overflow_more_icon);


    }
    public void setProduct_img(String product_img) {

        if (!product_img.isEmpty()) {

            Picasso.get().load(product_img).fit().into(mImage);
        }
    }

    public void setProduct_title(String product_title) {
        mTitle.setText(product_title);
    }

    public void setProduct_desc(String product_desc) {
        mDesc.setText(product_desc);
    }

    public void setProduct_off_price(String product_off_price) {

        mproduct_off_price.setText(product_off_price);
    }

    public void setProduct_real_price(String product_real_price) {
        mproduct_real_price.setText(product_real_price);
    }

    public void setProduct_discount_percentage(String product_discount_percentage) {
        mproduct_discount_percentage.setText(product_discount_percentage);
    }
    public void setProduct_quantity(String product_quantity) {
        quantity_text.setText(product_quantity);
    }



}
