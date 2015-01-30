package com.shield.main;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
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

        Log.d("coc", (Prefs.getShieldEndTime(this) - SystemClock.elapsedRealtime()) + " to end");
        if (AppType.isFree(this) && SystemClock.elapsedRealtime() > Prefs.getShieldEndTime(this)) {
            Log.d("coc", "shield end time");
            Shield.toggle(this);
            return;
        }

        startForeground(NotificationHelper.NOTIFICATION_ID, NotificationHelper.createNotification(this, false));
        Log.d("coc", "started foreground");

        scheduleNextIntent();

        if (SystemClock.elapsedRealtime() > Prefs.getResetTime(this)) {
            InternetConnection.reset(this);
            Prefs.setResetTime(this, SystemClock.elapsedRealtime() + RandomDelay.getNextLong());
        }

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
        startActivity(launchIntent);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        // During service life notification was associated with it so it would disappear after service is dead.
        // Hence we need to show notification once again.
        if (Shield.isActive(this)) {
            NotificationHelper.showShieldNotification(this, false);
        }
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
