package com.yourselectronics.gauridev.yourselectronics.Model;

public class PinCodeModel {
    private String pin_code, location, state, estimated_delivery_time, delivery_charge;
    private boolean cod_status;

    public PinCodeModel() {
    }

    public PinCodeModel(String pin_code, String location, String state, String estimated_delivery_time, String delivery_charge, boolean cod_status) {
        this.pin_code = pin_code;
        this.location = location;
        this.state = state;
        this.estimated_delivery_time = estimated_delivery_time;
        this.delivery_charge = delivery_charge;
        this.cod_status = cod_status;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEstimated_delivery_time() {
        return estimated_delivery_time;
    }

    public void setEstimated_delivery_time(String estimated_delivery_time) {
        this.estimated_delivery_time = estimated_delivery_time;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }


    public boolean isCod_status() {
        return cod_status;
    }

    public void setCod_status(boolean cod_status) {
        this.cod_status = cod_status;
    }
}
