package com.yourselectronics.gauridev.yourselectronics.Model;

public class CatModel {

    private String cat_img, cat_name, category_id;



    public CatModel(String cat_img, String cat_name, String category_id) {
        this.cat_img = cat_img;
        this.cat_name = cat_name;
        this.category_id = category_id;
    }

    public CatModel() {
    }

    public String getCat_img() {
        return cat_img;
    }

    public void setCat_img(String cat_img) {
        this.cat_img = cat_img;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
