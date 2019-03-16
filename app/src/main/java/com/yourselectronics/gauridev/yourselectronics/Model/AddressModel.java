package com.yourselectronics.gauridev.yourselectronics.Model;

public class AddressModel {
    private String first_name, last_name, full_address, pin_code, state_name, mobile_no;

    private boolean last_selected_address;

    public AddressModel(){

    }


    public AddressModel(String first_name, String last_name, String full_address, String pin_code, String state_name, String mobile_no, boolean last_selected_address){
        this.first_name = first_name;
        this.last_name = last_name;
        this.full_address = full_address;
        this.pin_code = pin_code;
        this.state_name = state_name;
        this.mobile_no = mobile_no;
        this.last_selected_address = last_selected_address;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public boolean isLast_selected_address() {
        return last_selected_address;
    }

    public void setLast_selected_address(boolean last_selected_address) {
        this.last_selected_address = last_selected_address;
    }


}
