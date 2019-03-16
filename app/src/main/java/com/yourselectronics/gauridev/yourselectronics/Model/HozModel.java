package com.yourselectronics.gauridev.yourselectronics.Model;

public class HozModel {
    private String img, name, off,product_id;
    public HozModel() {
    }

    public HozModel(String img, String name, String off, String product_id) {
        this.img = img;
        this.name = name;
        this.off = off;
        this.product_id = product_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
