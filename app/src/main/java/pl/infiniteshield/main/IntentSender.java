package pl.infiniteshield.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class IntentSender {

    private static final int REQUEST_CODE = 666;

    private final AlarmManager alarmManager;
    private final Context context;
    private PendingIntent alarmIntent;

    public IntentSender(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void sendAfterDelay(final int delay) {
        Log.d("coc", "sendAfterDelay: " + delay);

        Intent receiverIntent = createReceiverIntent();
        alarmIntent = PendingIntent.getService(context, REQUEST_CODE, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delay, alarmIntent);
        Prefs.setIsDelayingToSend(context, true);
    }

    private Intent createReceiverIntent() {
        Intent receiverIntent = new Intent("pl.infiniteshield.LAUNCH_ACTION");
        receiverIntent.setClass(context, IntentReceiver.class);
        return receiverIntent;
    }

    public void cancelSendAfterDelay() {
        Log.d("coc", "cancelSendAfterDelay");

        alarmManager.cancel(alarmIntent);
        Prefs.setIsDelayingToSend(context, false);
    }

    public boolean isDelayingToSend() {
        Log.d("coc", "isDelayingToSend: " + Prefs.getIsDelayingToSend(context));

        return Prefs.getIsDelayingToSend(context);
    }
}
