package com.tudor.rotarus.unibuc.metme.pojos.interfaces.network;

/**
 * Created by Tudor on 16.04.2016.
 */
public interface RefreshGcmTokenListener {
    void onTokenRefreshSuccess();
    void onTokenRefreshFailed();
}
