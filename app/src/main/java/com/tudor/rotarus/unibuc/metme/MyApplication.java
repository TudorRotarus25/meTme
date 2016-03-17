package com.tudor.rotarus.unibuc.metme;

import android.app.Application;

import com.tudor.rotarus.unibuc.metme.rest.RestClient;

/**
 * Created by Tudor on 29.02.2016.
 */
public class MyApplication extends Application {

    public static final String METME_SHARED_PREFERENCES = "METME_SHARED_PREFERENCES_FIRST_NAME";

    private RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();

        restClient = new RestClient();
    }

    public RestClient getRestClient() {
        return restClient;
    }
}
