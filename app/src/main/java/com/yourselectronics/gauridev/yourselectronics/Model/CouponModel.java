package com.yourselectronics.gauridev.yourselectronics.Model;

public class CouponModel {
    private String coupon_title, coupon_desc, coupon_code, coupon_validity;

    public CouponModel() {
    }

    public CouponModel(String coupon_title, String coupon_desc, String coupon_code, String coupon_validity) {
        this.coupon_title = coupon_title;
        this.coupon_desc = coupon_desc;
        this.coupon_code = coupon_code;
        this.coupon_validity = coupon_validity;
    }

    public String getCoupon_title() {
        return coupon_title;
    }

    public void setCoupon_title(String coupon_title) {
        this.coupon_title = coupon_title;
    }

    public String getCoupon_desc() {
        return coupon_desc;
    }

    public void setCoupon_desc(String coupon_desc) {
        this.coupon_desc = coupon_desc;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getCoupon_validity() {
        return coupon_validity;
    }

    public void setCoupon_validity(String coupon_validity) {
        this.coupon_validity = coupon_validity;
    }
}

