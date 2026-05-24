package com.ipi.garageplus.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.ipi.garageplus.receiver.RegistrationAlarmReceiver;

public class AlarmScheduler {

    public static void scheduleRegistrationReminder(
            Context context,
            long triggerAtMillis,
            String vehicleName,
            String reminderDateText
    ) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Intent settingsIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(settingsIntent);
            return;
        }

        Intent intent = new Intent(context, RegistrationAlarmReceiver.class);
        intent.putExtra(RegistrationAlarmReceiver.EXTRA_TITLE, "Obnova registracije");
        intent.putExtra(
                RegistrationAlarmReceiver.EXTRA_TEXT,
                "Podsjetnik za obnovu registracije vozila " + vehicleName +
                        " bit će zakazan za " + reminderDateText + "."
        );
        intent.putExtra(RegistrationAlarmReceiver.EXTRA_NOTIFICATION_ID, (int) System.currentTimeMillis());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long testTriggerAtMillis = System.currentTimeMillis() + 3_000L;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        testTriggerAtMillis,
                        pendingIntent
                );
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        testTriggerAtMillis,
                        pendingIntent
                );
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}