package com.tudor.rotarus.unibuc.metme.pojos.responses;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Tudor on 24.04.2016.
 */
public class PushNotificationBody {

    private String title;
    private String body;

    public PushNotificationBody() {
    }

    public PushNotificationBody(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
