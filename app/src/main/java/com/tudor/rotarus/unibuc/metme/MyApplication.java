package com.tudor.rotarus.unibuc.metme;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.tudor.rotarus.unibuc.metme.db.DatabaseHelper;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.FriendsListListener;
import com.tudor.rotarus.unibuc.metme.pojos.requests.FriendsBody;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;

import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by Tudor on 29.02.2016.
 */
public class MyApplication extends Application implements LocationListener {

    public final String TAG = this.getClass().getSimpleName();

    private static Context context;

    private LocationManager locationManager;
    private String provider;

    private Double latitude;
    private Double longitude;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(provider);
        }
        if (location != null) {
            Log.i(TAG, "Provider " + provider + " selected");
            onLocationChanged(location);
        } else {
            Log.e(TAG, "Location is null");
        }
    }



    public static Context getContext() {
        return context;
    }

    public void refreshFriendList() {
        new ContactsTask().execute();
    }

    private FriendsListListener callback = new FriendsListListener() {
        @Override
        public void onFriendsListSuccess(FriendsPostBody response) {
            //TODO: write response in local DB
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            db.insertAllContacts(response);
            Log.i(TAG, "Friends inserted successfully");
        }

        @Override
        public void onFriendsListFailed() {
            Log.e(TAG, "Friends refresh did not work");
        }
    };

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    private class ContactsTask extends AsyncTask<Void, Void, FriendsBody> {

        @Override
        protected FriendsBody doInBackground(Void... params) {

            return fetchContacts();

        }

        @Override
        protected void onPostExecute(FriendsBody friendsBodies) {
            super.onPostExecute(friendsBodies);

            NetworkManager networkManager = NetworkManager.getInstance();
            networkManager.listFriends(friendsBodies, callback);
        }
    }

    private FriendsBody fetchContacts() {

        final Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        final String _ID = ContactsContract.Contacts._ID;
        final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        final Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        final String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        final String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ArrayList<FriendsBody.FriendBody> result = new ArrayList<>();

        Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contactId = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    FriendsBody.FriendBody newFriend = new FriendsBody().new FriendBody(contactId, name);
                    Cursor phoneCursor = getContentResolver().query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contactId}, null);
                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        if (phoneNumber != null) {
                            newFriend.addPhoneNumber(phoneNumber);
                        }
                    }
                    phoneCursor.close();
                    result.add(newFriend);
                }
            }
            cursor.close();
        }
        return new FriendsBody(result);
    }
}
