package pl.infiniteshield.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class IntentSender {

    public static final String LAUNCH_DELAY_KEY = "LAUNCH_DELAY";

    private static final int REQUEST_CODE = 666;

    private final AlarmManager alarmManager = (AlarmManager) Global.app.getSystemService(Context.ALARM_SERVICE);
    private PendingIntent alarmIntent;

    public void sendAfterDelay(final int delay) {
        Log.d("coc", delay + " sendAfterDelay");

        Intent receiverIntent = createReceiverIntent();
        receiverIntent.putExtra(LAUNCH_DELAY_KEY, delay);
        alarmIntent = PendingIntent.getService(Global.app, REQUEST_CODE, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, alarmIntent);
        Prefs.setIsDelayingToSend(true);
    }

    private Intent createReceiverIntent() {
        Intent receiverIntent = new Intent("pl.infiniteshield.LAUNCH_ACTION");
        receiverIntent.setClass(Global.app, IntentReceiver.class);
        return receiverIntent;
    }

    public void cancelSendAfterDelay() {
        Log.d("coc", "cancelSendAfterDelay");

        alarmManager.cancel(alarmIntent);
        Prefs.setIsDelayingToSend(false);
    }

    public boolean isDelayingToSend() {
        Log.d("coc", "isDelayingToSend: " + Prefs.getIsDelayingToSend());

        return Prefs.getIsDelayingToSend();
    }
}
