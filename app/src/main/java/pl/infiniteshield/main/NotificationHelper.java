package pl.infiniteshield.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationHelper {

    public static final int NOTIFICATION_ID = 12423;
    private static final int STOP_ACTION_REQUEST_CODE = 1;

    public static void showShieldNotification(Context context, boolean firstTime) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.
                NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, createNotification(context, firstTime));
    }

    public static void clearNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public static Notification createNotification(Context context, boolean firstTime) {
        final SimpleDateFormat dayTimeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String startTimeText = dayTimeFormatter.format(new Date(System.currentTimeMillis()));

        Intent stopIntent = new Intent(context, NotificationReceiver.class);
        stopIntent.setAction(NotificationReceiver.ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, STOP_ACTION_REQUEST_CODE, stopIntent, 0);

        Intent contentIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(contentIntent);
        PendingIntent contentPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.shield_small)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.shield_small))
                .setContentTitle("Clash of Clans Infinite Shield")
                .setContentText("Started at: " + startTimeText)
                .setContentIntent(contentPendingIntent)
                .addAction(R.drawable.ic_action_cancel, "Stop shield", stopPendingIntent)
                .setProgress(0, 0, true)
                .setUsesChronometer(true);

        if (!firstTime) {
            long shieldStartTime = Prefs.getShieldStartTime(context);
            builder.setWhen(shieldStartTime);
            builder.setContentText("Started at: " + dayTimeFormatter.format(new Date(shieldStartTime)));
        }

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        return notification;
    }

}
