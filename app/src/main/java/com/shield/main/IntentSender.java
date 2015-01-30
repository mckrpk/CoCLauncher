package com.shield.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class IntentSender {

    public static final int INTENT_REQUEST_CODE = 666;

    public static void sendAfterDelay(Context context, final int delay) {
        Log.d("coc", "sendAfterDelay: " + delay);

        getAlarmManager(context).set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay,
                getAlarmIntent(context));
        Prefs.setIsDelayingToSend(context, true);
    }

    public static void cancelSendAfterDelay(Context context) {
        Log.d("coc", "cancelSendAfterDelay");

        getAlarmManager(context).cancel(getAlarmIntent(context));
    }

    public static void schedule(Context context, long milis) {
        Prefs.setBoolean(context, Prefs.IS_SCHEDULED, true);
        Prefs.setLong(context, Prefs.SCHEDULER_DUE_TIME, System.currentTimeMillis() + milis);
        getAlarmManager(context).set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + milis, getSchedulerIntent(context));
    }

    public static void cancelScheduler(Context context) {
        getAlarmManager(context).cancel(getSchedulerIntent(context));
        Prefs.setBoolean(context, Prefs.IS_SCHEDULED, false);
    }

    private static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private static PendingIntent getAlarmIntent(Context context) {
        return PendingIntent.getService(context, INTENT_REQUEST_CODE, createReceiverIntent(context), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getSchedulerIntent(Context context) {
        return PendingIntent.getBroadcast(context, INTENT_REQUEST_CODE, createSchedulerIntent(context), PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public static Intent createReceiverIntent(Context context) {
        return new Intent(context, IntentReceiver.class);
    }

    public static Intent createSchedulerIntent(Context context) {
        return new Intent(context, SchedulerReceiver.class);
    }

}
