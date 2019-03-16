package com.yourselectronics.gauridev.yourselectronics.Model;

public class OrderModel {
    private String item_name,item_desc,item_category_id, item_quantity, item_img, item_off_price,item_real_price ,item_delivery_price, payment_method,price,order_status,order_date,total_price,order_shipping_address,user_mobile, item_single_price;

    public OrderModel() {
    }

    public OrderModel(String item_name, String item_desc, String item_category_id, String item_quantity, String item_img, String item_off_price, String item_real_price, String item_delivery_price, String payment_method, String price, String order_status, String order_date, String total_price, String order_shipping_address, String user_mobile, String item_single_price) {
        this.item_name = item_name;
        this.item_desc = item_desc;
        this.item_category_id = item_category_id;
        this.item_quantity = item_quantity;
        this.item_img = item_img;
        this.item_off_price = item_off_price;
        this.item_real_price = item_real_price;
        this.item_delivery_price = item_delivery_price;
        this.payment_method = payment_method;
        this.price = price;
        this.order_status = order_status;
        this.order_date = order_date;
        this.total_price = total_price;
        this.order_shipping_address = order_shipping_address;
        this.user_mobile = user_mobile;
        this.item_single_price = item_single_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }


    public String getOrder_shipping_address() {
        return order_shipping_address;
    }

    public void setOrder_shipping_address(String order_shipping_address) {
        this.order_shipping_address = order_shipping_address;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getItem_off_price() {
        return item_off_price;
    }

    public void setItem_off_price(String item_off_price) {
        this.item_off_price = item_off_price;
    }

    public String getItem_real_price() {
        return item_real_price;
    }

    public void setItem_real_price(String item_real_price) {
        this.item_real_price = item_real_price;
    }

    public String getItem_delivery_price() {
        return item_delivery_price;
    }

    public void setItem_delivery_price(String item_delivery_price) {
        this.item_delivery_price = item_delivery_price;
    }

    public String getItem_single_price() {
        return item_single_price;
    }

    public void setItem_single_price(String item_single_price) {
        this.item_single_price = item_single_price;
    }

    public String getItem_category_id() {
        return item_category_id;
    }

    public void setItem_category_id(String item_category_id) {
        this.item_category_id = item_category_id;
    }
}
