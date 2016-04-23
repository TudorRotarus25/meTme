package com.tudor.rotarus.unibuc.metme.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tudor on 23.04.2016.
 */
public class SharedPreferencesManager {

    private final String TAG = getClass().getSimpleName();

    public static final String METME_SHARED_PREFERENCES = "METME_SHARED_PREFERENCES_FIRST_NAME";

    private static final String ID = "_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String PHONE_PREFIX = "phone_prefix";
    private static final String TOKEN = "token";
    private static final String GCM_TOKEN = "gcm_token";
    private static final String SENT_GCM_TOKEN_TO_SERVER = "sent_gcm_token_to_server";

    private static SharedPreferencesManager instance;

    private SharedPreferencesManager() {
    }

    public static SharedPreferencesManager getInstance() {
        if(instance == null) {
            instance = new SharedPreferencesManager();
        }
        return instance;
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(METME_SHARED_PREFERENCES, context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor(Context context) {
        return context.getSharedPreferences(METME_SHARED_PREFERENCES, context.MODE_PRIVATE).edit();
    }

    public void writeUser(Context context, int id, String firstName, String lastName, String phoneNumber) {

        SharedPreferences.Editor editor = getEditor(context);

        editor.putInt(ID, id);
        editor.putString(FIRST_NAME, firstName);
        editor.putString(LAST_NAME, lastName);
        editor.putString(PHONE_NUMBER, phoneNumber);
        editor.commit();
    }

    public void writePrefix(Context context, String prefix) {

        SharedPreferences.Editor editor = getEditor(context);

        editor.putString(PHONE_PREFIX, prefix);
        editor.commit();
    }

    public void writeToken(Context context, String token) {

        SharedPreferences.Editor editor = getEditor(context);

        editor.putString(TOKEN, token);
        editor.putBoolean(SENT_GCM_TOKEN_TO_SERVER, true);
        editor.commit();
    }

    public void writeFailedToSendGcmToken(Context context) {

        SharedPreferences.Editor editor = getEditor(context);

        editor.putBoolean(SENT_GCM_TOKEN_TO_SERVER, false);
        editor.commit();
    }

    public void writeGcmToken(Context context, String token) {

        SharedPreferences.Editor editor = getEditor(context);

        editor.putString(GCM_TOKEN, token);
        editor.commit();
    }

    public int readId(Context context) {

        SharedPreferences preferences = getSharedPreferences(context);

        if(preferences.contains(ID)) {
            return preferences.getInt(ID, -1);
        } else {
            return -2;
        }
    }

    public String readFirstName(Context context) {

        SharedPreferences preferences = getSharedPreferences(context);

        if(preferences.contains(FIRST_NAME)) {
            return preferences.getString(FIRST_NAME, "");
        } else {
            return null;
        }
    }

    public String readLastName(Context context) {

        SharedPreferences preferences = getSharedPreferences(context);

        if(preferences.contains(LAST_NAME)) {
            return preferences.getString(LAST_NAME, "");
        } else {
            return null;
        }
    }

    public String readPhoneNumber(Context context) {

        SharedPreferences preferences = getSharedPreferences(context);

        if(preferences.contains(PHONE_NUMBER)) {
            return preferences.getString(PHONE_NUMBER, "");
        } else {
            return null;
        }
    }

    public String readToken(Context context) {

        SharedPreferences preferences = getSharedPreferences(context);

        if(preferences.contains(TOKEN)) {
            return preferences.getString(TOKEN, "");
        } else {
            return null;
        }
    }

    public String readGcmToken(Context context) {

        SharedPreferences preferences = getSharedPreferences(context);

        if (preferences.contains(GCM_TOKEN)) {
            return preferences.getString(GCM_TOKEN, "");
        }
        return null;
    }

    public boolean readSentGcmToken(Context context) {

        SharedPreferences preferences = getSharedPreferences(context);

        if(preferences.contains(SENT_GCM_TOKEN_TO_SERVER)) {
            return preferences.getBoolean(SENT_GCM_TOKEN_TO_SERVER, false);
        }
        return false;
    }

}
