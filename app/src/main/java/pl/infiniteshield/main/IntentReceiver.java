package pl.infiniteshield.main;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

public class IntentReceiver extends IntentService {

    public IntentReceiver() {
        super("IntentReceiver");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!Prefs.getIsDelayingToSend(this)) {
            return;
        }

        scheduleNextIntent();

        if (SystemClock.elapsedRealtime() > Prefs.getResetTime(this)) {
            InternetConnection.reset(this);
            Prefs.setResetTime(this, SystemClock.elapsedRealtime() + RandomDelay.getNextLong());
        }

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
        startActivity(launchIntent);
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
