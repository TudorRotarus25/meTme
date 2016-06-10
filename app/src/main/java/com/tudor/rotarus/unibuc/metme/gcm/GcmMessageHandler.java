package com.tudor.rotarus.unibuc.metme.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.AcceptMeetingActivity;
import com.tudor.rotarus.unibuc.metme.activities.NavigationDrawerActivity;
import com.tudor.rotarus.unibuc.metme.managers.MySharedPreferencesManager;
import com.tudor.rotarus.unibuc.metme.managers.NetworkManager;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.RefreshLocationListener;
import com.tudor.rotarus.unibuc.metme.pojos.responses.PushNotificationBody;

/**
 * Created by Tudor on 16.04.2016.
 */
public class GcmMessageHandler extends GcmListenerService implements RefreshLocationListener{

    private final String TAG = getClass().getSimpleName();

    private final int NOTIFICATION_TYPE_MEETING_COMING_UP = 1;
    private final int NOTIFICATION_TYPE_NEW_MEETING = 2;
    private final int NOTIFICATION_TYPE_REFRESH_ETA = 3;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        Log.i(TAG, data.getString("data"));

        PushNotificationBody body = new Gson().fromJson(data.getString("data"), PushNotificationBody.class);

        int type;

        if(body != null) {
            type = body.getType();

        } else {
            Log.e(TAG, "Null body");
            return;
        }

        switch (type) {
            case NOTIFICATION_TYPE_MEETING_COMING_UP:
                Intent comingUpNotificationIntent = new Intent(getBaseContext(), NavigationDrawerActivity.class);
                createNotification(body, comingUpNotificationIntent);
                break;
            case NOTIFICATION_TYPE_NEW_MEETING:
                Intent newMeetingNotificationIntent = new Intent(getBaseContext(), AcceptMeetingActivity.class);
                Log.i(TAG, body.getMeetingId() + "");
                newMeetingNotificationIntent.putExtra(AcceptMeetingActivity.MEETING_ID_BUNDLE_KEY, body.getMeetingId());
                newMeetingNotificationIntent.putExtra(AcceptMeetingActivity.MEETING_MESSAGE_BUNDLE_KEY, body.getBody());
                createNotification(body, newMeetingNotificationIntent);
                break;
            case NOTIFICATION_TYPE_REFRESH_ETA:
                refreshETA(body.getMeetingId());
                break;
            default:
                Log.e(TAG, "Notification type not supported");
                break;
        }
    }

    private void createNotification(PushNotificationBody body, Intent notificationIntent) {
        Context context = getBaseContext();

        PendingIntent contentIntent = PendingIntent.getActivity(context, body.getNotificationId(), notificationIntent, Intent.FILL_IN_DATA);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrate = {0, 100, 200, 300};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(body.getTitle())
                .setContentText(body.getBody())
                .setSound(alarmSound)
                .setVibrate(vibrate)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(body.getNotificationId(), builder.build());
    }

    private void refreshETA(final int meetingId) {
        if(meetingId == -1) {
            Log.e(TAG, "null task id");
            return;
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if(mLastLocation != null) {

                                Double latitude = mLastLocation.getLatitude();
                                Double longitude = mLastLocation.getLongitude();

                                MySharedPreferencesManager mySharedPreferencesManager = MySharedPreferencesManager.getInstance();
                                int userId = mySharedPreferencesManager.readId(getBaseContext());

                                NetworkManager networkManager = NetworkManager.getInstance();
                                networkManager.refreshLocation(userId, meetingId, latitude, longitude, GcmMessageHandler.this);

                                Log.i(TAG, userId + " " + meetingId + " " + latitude + " " + longitude);

                            } else {
                                Log.e(TAG, "null location");
                            }
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.e(TAG, "GoogleApiClient connection failed");
                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    @Override
    public void onRefreshLocationSuccess() {
        mGoogleApiClient.disconnect();
        Log.i(TAG, "Refreshed location successfully");
    }

    @Override
    public void onRefreshLocationFailed() {
        mGoogleApiClient.disconnect();
        Log.e(TAG, "Failed to refresh location");
    }

//    public int getNotificationCount() {
//
//        MySharedPreferencesManager preferencesManager = MySharedPreferencesManager.getInstance();
//
//        int notificationCount = preferencesManager.readNotificationCount(getBaseContext());
//        preferencesManager.writeNotificationCount(getBaseContext(), notificationCount + 1);
//
//        return notificationCount;
//    }
}
