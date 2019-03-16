package com.yourselectronics.gauridev.yourselectronics.Model;

public class NotificationModel  {
    private String notification_title, notification_desc, notification_img;
    private Long notification_time;

    public NotificationModel() {
    }

    public NotificationModel(String notification_title, String notification_desc, String notification_img, Long notification_time) {
        this.notification_title = notification_title;
        this.notification_desc = notification_desc;
        this.notification_img = notification_img;
        this.notification_time = notification_time;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getNotification_desc() {
        return notification_desc;
    }

    public void setNotification_desc(String notification_desc) {
        this.notification_desc = notification_desc;
    }

    public String getNotification_img() {
        return notification_img;
    }

    public void setNotification_img(String notification_img) {
        this.notification_img = notification_img;
    }


    public Long getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(Long notification_time) {
        this.notification_time = notification_time;
    }
}

