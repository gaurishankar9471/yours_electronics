package com.yourselectronics.gauridev.yourselectronics.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yourselectronics.gauridev.yourselectronics.R;

public class CouponViewHolder extends RecyclerView.ViewHolder {
    private TextView title, desc, validity,code;
    //public TextView ;
    public CouponViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.coupon_title);
        desc = itemView.findViewById(R.id.coupon_desc);
        code = itemView.findViewById(R.id.coupon_code);
        validity = itemView.findViewById(R.id.coupon_validity);

    }
    public void setCoupon_title(String coupon_title) {
        title.setText(coupon_title);
    }
    public void setCoupon_desc(String coupon_desc) {
        desc.setText(coupon_desc);
    }
    public void setCoupon_code(String coupon_code) {
        code.setText(coupon_code);
    }
    public void setCoupon_validity(String coupon_validity) {
        validity.setText(coupon_validity);
    }
}
