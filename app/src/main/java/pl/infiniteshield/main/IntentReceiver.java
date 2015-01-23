package pl.infiniteshield.main;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class IntentReceiver extends IntentService {

    public IntentReceiver() {
        super("IntentReceiver");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!Prefs.getIsDelayingToSend(this)) {
            return;
        }

        startForeground(1337, createNotification());
        Log.d("coc", "started foreground");

        scheduleNextIntent();

        if (SystemClock.elapsedRealtime() > Prefs.getResetTime(this)) {
            InternetConnection.reset(this);
            Prefs.setResetTime(this, SystemClock.elapsedRealtime() + RandomDelay.getNextLong());
        }

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
        startActivity(launchIntent);
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.shield_small)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.shield_small))
                .setContentTitle("Infinite Shield")
                .setContentText("Restarting Clash of Clans")
                .setProgress(0, 0, true)
                .build();
    }

    /**
     * Send intent after random delay.
     */
    private void scheduleNextIntent() {
        int nextShort = RandomDelay.getNextShort();
        Shield.acquireWakeLock(this, nextShort);
        IntentSender.sendAfterDelay(this, nextShort);
    }

}
