package com.yourselectronics.gauridev.yourselectronics.Model;

public class SupportMemberModel  {

    private String user_name, user_profile_img;
    private boolean user_status;
    public SupportMemberModel() {
    }

    public SupportMemberModel(String user_name, String user_profile_img, boolean user_status) {
        this.user_name = user_name;
        this.user_profile_img = user_profile_img;
        this.user_status = user_status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public boolean isUser_status() {
        return user_status;
    }

    public void setUser_status(boolean user_status) {
        this.user_status = user_status;
    }

    public String getUser_profile_img() {
        return user_profile_img;
    }

    public void setUser_profile_img(String user_profile_img) {
        this.user_profile_img = user_profile_img;
    }
}
