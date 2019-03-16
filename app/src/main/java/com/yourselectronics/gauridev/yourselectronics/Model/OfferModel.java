package com.yourselectronics.gauridev.yourselectronics.Model;

public class OfferModel {
    private String product_title, product_img, product_desc, product_off_price, product_real_price,product_off_percentage,product_id;
    private long product_deal_timeout;

    public OfferModel() {
    }

    public OfferModel(String product_title, String product_img, String product_desc, String product_off_price, String product_real_price, String product_off_percentage, String product_id, long product_deal_timeout) {
        this.product_title = product_title;
        this.product_img = product_img;
        this.product_desc = product_desc;
        this.product_off_price = product_off_price;
        this.product_real_price = product_real_price;
        this.product_off_percentage = product_off_percentage;
        this.product_id = product_id;
        this.product_deal_timeout = product_deal_timeout;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_off_price() {
        return product_off_price;
    }

    public void setProduct_off_price(String product_off_price) {
        this.product_off_price = product_off_price;
    }

    public String getProduct_real_price() {
        return product_real_price;
    }

    public void setProduct_real_price(String product_real_price) {
        this.product_real_price = product_real_price;
    }

    public String getProduct_off_percentage() {
        return product_off_percentage;
    }

    public void setProduct_off_percentage(String product_off_percentage) {
        this.product_off_percentage = product_off_percentage;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public long getProduct_deal_timeout() {
        return product_deal_timeout;
    }

    public void setProduct_deal_timeout(long product_deal_timeout) {
        this.product_deal_timeout = product_deal_timeout;
    }
}
