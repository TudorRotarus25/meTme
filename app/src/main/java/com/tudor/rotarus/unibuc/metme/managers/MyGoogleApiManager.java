package com.tudor.rotarus.unibuc.metme.managers;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.tudor.rotarus.unibuc.metme.MyApplication;

/**
 * Created by Tudor on 16.05.2016.
 */
public class MyGoogleApiManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = getClass().getSimpleName();

    private static MyGoogleApiManager instance;

    private GoogleApiClient googleApiClient;
    private Location mLastLocation;

    private MyGoogleApiManager() {

    }

    public static MyGoogleApiManager getInstance() {
        if (instance == null) {
            instance = new MyGoogleApiManager();
            GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(MyApplication.getContext())
                    .addConnectionCallbacks(instance)
                    .addOnConnectionFailedListener(instance)
                    .addApi(LocationServices.API)
                    .build();
            instance.googleApiClient = mGoogleApiClient;
            instance.googleApiClient.connect();

        }

        return instance;
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
