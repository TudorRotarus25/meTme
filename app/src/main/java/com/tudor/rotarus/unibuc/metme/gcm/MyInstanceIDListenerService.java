package com.tudor.rotarus.unibuc.metme.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Tudor on 16.04.2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
