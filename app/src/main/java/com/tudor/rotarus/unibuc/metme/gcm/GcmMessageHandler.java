package com.tudor.rotarus.unibuc.metme.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.activities.NavigationDrawerActivity;
import com.tudor.rotarus.unibuc.metme.pojos.responses.PushNotificationBody;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Tudor on 16.04.2016.
 */
public class GcmMessageHandler extends GcmListenerService {

    private final String TAG = getClass().getSimpleName();

    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        PushNotificationBody body = new Gson().fromJson(data.getString("data"), PushNotificationBody.class);

        String title = null;
        String message = null;

        if(body != null) {

            title = body.getTitle();
            message = body.getBody();

        } else {
            Log.e(TAG, "Null body");
        }

        createNotification(title, message);
    }

    private void createNotification(String title, String body) {
        Context context = getBaseContext();

        Intent notificationIntent = new Intent(context, NavigationDrawerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrate = {0, 100, 200, 300};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(alarmSound)
                .setVibrate(vibrate)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MESSAGE_NOTIFICATION_ID, builder.build());
    }
}
