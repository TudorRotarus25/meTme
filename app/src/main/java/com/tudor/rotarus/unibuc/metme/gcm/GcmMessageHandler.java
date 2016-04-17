package com.tudor.rotarus.unibuc.metme.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.tudor.rotarus.unibuc.metme.R;

/**
 * Created by Tudor on 16.04.2016.
 */
public class GcmMessageHandler extends GcmListenerService {

    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        String message = data.getString("message");

        createNotification(from, message);
    }

    private void createNotification(String title, String body) {
        Context context = getBaseContext();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MESSAGE_NOTIFICATION_ID, builder.build());
    }
}
