package com.yourselectronics.gauridev.yourselectronics.Model;

public class ListViewModel  {
    private String list_text;

    public ListViewModel() {
    }

    public ListViewModel(String list_text) {
        this.list_text = list_text;
    }

    public String getList_text() {
        return list_text;
    }

    public void setList_text(String list_text) {
        this.list_text = list_text;
    }
}
