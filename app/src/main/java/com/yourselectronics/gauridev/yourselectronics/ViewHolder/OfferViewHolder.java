package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yourselectronics.gauridev.yourselectronics.R;

public class OfferViewHolder extends RecyclerView.ViewHolder {
    private TextView title, desc , off_price, real_price, off_percentage ;
    private TextView deal_timeout;
    private ImageView img;
    public OfferViewHolder(View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.offer_card_img);
        title= itemView.findViewById(R.id.offer_card_title);
        desc = itemView.findViewById(R.id.offer_card_desc);
        off_price = itemView.findViewById(R.id.product_off_price);
        real_price = itemView.findViewById(R.id.product_real_price);
        off_percentage = itemView.findViewById(R.id.product_discount_percentage);
        deal_timeout = itemView.findViewById(R.id.offer_card_timeout);
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

    public void setProduct_deal_timeout(final long product_deal_timeout) {
        //deal_timeout.setText(String.valueOf(product_deal_timeout));

      CountDownTimer countDownTimer = new CountDownTimer( product_deal_timeout-System.currentTimeMillis(),1000){
            @Override
            public void onTick(long l) {
                deal_timeout.setText(covertMilliSecondToHMS(l/1000));
            }

            @Override
            public void onFinish() {
               deal_timeout.setText("Expired");

            }
        };
      countDownTimer.start();



    }
    private String covertMilliSecondToHMS(long time) {
        long s = time%60;
        long m = (time/60)%60;
        long h = (time/(60*60))%24;
        int days = (int) time/(60*60*24);


        if (time>86399 && time<172799){
            return String.valueOf(days)+" day";
        }
        else if (time>172799){
            return String.valueOf(days)+" days";

        }
        else {
            return String.format("%d:%02d:%02d",h,m,s);

        }



    }

}
