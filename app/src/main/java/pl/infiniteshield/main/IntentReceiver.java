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
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
        startActivity(launchIntent);

        int delay = RandomDelay.getNext();
        Log.d("coc", "onHandleIntent: " + delay);
        Global.app.intentSender.sendAfterDelay(delay); // send next intent after random delay

        PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire(delay + 3000);
    }
}
