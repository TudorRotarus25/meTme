package com.tudor.rotarus.unibuc.metme.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.login.LoginNameActivity;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.managers.MySharedPreferencesManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.RefreshGcmTokenListener;

import java.io.IOException;

/**
 * Created by Tudor on 16.04.2016.
 */
public class RegistrationIntentService extends IntentService implements RefreshGcmTokenListener {

    private final String TAG = getClass().getSimpleName();

    public RegistrationIntentService(String name) {
        super(name);
    }

    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        MySharedPreferencesManager sharedPreferencesManager = MySharedPreferencesManager.getInstance();

        InstanceID instanceID = InstanceID.getInstance(this);
        String senderId = getResources().getString(R.string.gcm_sender_id);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.d(TAG, "GCM Registration Token: " + token);

            sharedPreferencesManager.writeGcmToken(getApplicationContext(), token);
            sendRegistrationToServer(token);

        } catch (IOException e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferencesManager.writeFailedToSendGcmToken(getApplicationContext());
        }

    }

    private void sendRegistrationToServer(String token) {

        MySharedPreferencesManager sharedPreferencesManager = MySharedPreferencesManager.getInstance();

        int userId = sharedPreferencesManager.readId(getApplicationContext());
        if(userId >= 0) {
            NetworkManager networkManager = NetworkManager.getInstance();
            networkManager.refreshGcmToken(userId, token, this);
        } else {
            Log.e(TAG, "Missing user id");
            Intent intent = new Intent(getBaseContext(), LoginNameActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onTokenRefreshSuccess() {
        Log.i(TAG, "Token refreshed successfully");
    }

    @Override
    public void onTokenRefreshFailed() {

        MySharedPreferencesManager sharedPreferencesManager = MySharedPreferencesManager.getInstance();

        Log.d(TAG, "Failed to complete token refresh");
        sharedPreferencesManager.writeFailedToSendGcmToken(getApplicationContext());
    }
}
