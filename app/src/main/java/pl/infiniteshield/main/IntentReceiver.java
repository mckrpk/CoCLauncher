package pl.infiniteshield.main;

import android.app.IntentService;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class IntentReceiver extends IntentService {

    public IntentReceiver() {
        super("IntentReceiver");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int delay = intent.getIntExtra(IntentSender.LAUNCH_DELAY_KEY, MainActivity.DELAY_AFTER_START_SHIELD_PRESS);

        PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire(delay + 3000);
        Log.d("coc", delay + " onHandleIntent");

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
        startActivity(launchIntent);

        Global.app.intentSender.sendAfterDelay(RandomDelay.getNext()); // send next intent after random delay
    }
}
