package com.yourselectronics.gauridev.yourselectronics.Model;

public class ProductModel {

    private String product_img, product_title, product_desc,product_off_price, product_real_price, product_discount_percenatge, product_delivery_price, product_category_id;

    public ProductModel() {

    }

    public ProductModel(String product_img, String product_title, String product_desc, String product_off_price, String product_real_price, String product_discount_percentge, String product_discount_percenatge, String product_delivery_price, String product_category_id) {
        this.product_img = product_img;
        this.product_title = product_title;
        this.product_desc = product_desc;
        this.product_off_price = product_off_price;
        this.product_real_price = product_real_price;
        this.product_discount_percenatge = product_discount_percenatge;
        this.product_delivery_price = product_delivery_price;
        this.product_category_id = product_category_id;
    }



    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
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

    public String getProduct_delivery_price() {
        return product_delivery_price;
    }

    public void setProduct_delivery_price(String product_delivery_price) {
        this.product_delivery_price = product_delivery_price;
    }

    public String getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(String product_category_id) {
        this.product_category_id = product_category_id;
    }


    public String getProduct_discount_percenatge() {
        return product_discount_percenatge;
    }

    public void setProduct_discount_percenatge(String product_discount_percenatge) {
        this.product_discount_percenatge = product_discount_percenatge;
    }
}
