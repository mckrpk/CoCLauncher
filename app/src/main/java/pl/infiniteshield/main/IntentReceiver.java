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
        if (!Prefs.getIsDelayingToSend(this)) {
            return;
        }

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
        startActivity(launchIntent);

        int delay = RandomDelay.getNext();
        Log.d("coc", "onHandleIntent: " + delay);
        IntentSender intentSender = new IntentSender(this);
        intentSender.sendAfterDelay(delay); // send next intent after random delay
    }
}
