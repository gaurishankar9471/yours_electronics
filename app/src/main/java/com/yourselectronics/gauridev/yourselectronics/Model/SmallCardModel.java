package com.yourselectronics.gauridev.yourselectronics.Model;

public class SmallCardModel {
    private String img, price, product_id;
    public SmallCardModel() {
    }

    public SmallCardModel(String img, String price, String product_id) {
        this.img = img;
        this.price = price;
        this.product_id = product_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
