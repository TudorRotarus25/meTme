package com.tudor.rotarus.unibuc.metme;

import android.app.Application;
import android.content.SharedPreferences;

import com.tudor.rotarus.unibuc.metme.rest.RestClient;

/**
 * Created by Tudor on 29.02.2016.
 */
public class MyApplication extends Application {

    public static final String METME_SHARED_PREFERENCES = "METME_SHARED_PREFERENCES_FIRST_NAME";

    private static final String ID = "_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String TOKEN = "token";

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences(METME_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = getSharedPreferences(METME_SHARED_PREFERENCES, MODE_PRIVATE).edit();

    }

    public void writeUser(int id, String firstName, String lastName, String phoneNumber) {
        editor.putInt(ID, id);
        editor.putString(FIRST_NAME, firstName);
        editor.putString(LAST_NAME, lastName);
        editor.putString(PHONE_NUMBER, phoneNumber);
        editor.commit();
    }

    public void writeToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public int readId() {
        if(preferences.contains(ID)) {
            return preferences.getInt(ID, -1);
        } else {
            return -2;
        }
    }

    public String readFirstName() {
        if(preferences.contains(FIRST_NAME)) {
            return preferences.getString(FIRST_NAME, "");
        } else {
            return null;
        }
    }

    public String readLastName() {
        if(preferences.contains(LAST_NAME)) {
            return preferences.getString(LAST_NAME, "");
        } else {
            return null;
        }
    }

    public String readPhoneNumber() {
        if(preferences.contains(PHONE_NUMBER)) {
            return preferences.getString(PHONE_NUMBER, "");
        } else {
            return null;
        }
    }

    public String readToken() {
        if(preferences.contains(TOKEN)) {
            return preferences.getString(TOKEN, "");
        } else {
            return null;
        }
    }
}
