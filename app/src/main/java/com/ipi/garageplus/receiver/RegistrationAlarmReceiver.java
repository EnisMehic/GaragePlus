package com.ipi.garageplus.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ipi.garageplus.R;

public class RegistrationAlarmReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "registration_reminder_channel";
    public static final String CHANNEL_NAME = "Podsjetnici za registraciju";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_TEXT = "extra_text";
    public static final String EXTRA_NOTIFICATION_ID = "extra_notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        String title = intent.getStringExtra(EXTRA_TITLE);
        String text = intent.getStringExtra(EXTRA_TEXT);
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 1001);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title != null ? title : "Podsjetnik za registraciju")
                .setContentText(text != null ? text : "Registracija uskoro ističe.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text != null ? text : "Registracija uskoro ističe."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(notificationId, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Podsjetnik za obnovu registracije vozila");
                manager.createNotificationChannel(channel);
            }
        }
    }
}