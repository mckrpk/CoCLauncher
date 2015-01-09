package pl.infiniteshield.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class IntentSender {

    private static final int REQUEST_CODE = 666;

    private final Context context;

    public IntentSender(Context context) {
        this.context = context;
    }

    public void sendAfterDelay(final int delay) {
        Log.d("coc", "sendAfterDelay: " + delay);

        getAlarmManager().set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, getAlarmIntent());
        Prefs.setIsDelayingToSend(context, true);
    }

    public void cancelSendAfterDelay() {
        Log.d("coc", "cancelSendAfterDelay");

        getAlarmManager().cancel(getAlarmIntent());
        Prefs.setIsDelayingToSend(context, false);
    }

    public boolean isDelayingToSend() {
        Log.d("coc", "isDelayingToSend: " + Prefs.getIsDelayingToSend(context));

        return Prefs.getIsDelayingToSend(context);
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private PendingIntent getAlarmIntent() {
        return PendingIntent.getService(context, REQUEST_CODE, createReceiverIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent createReceiverIntent() {
        Intent receiverIntent = new Intent("pl.infiniteshield.LAUNCH_ACTION");
        receiverIntent.setClass(context, IntentReceiver.class);
        return receiverIntent;
    }
}
