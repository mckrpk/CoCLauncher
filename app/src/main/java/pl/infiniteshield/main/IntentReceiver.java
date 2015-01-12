package pl.infiniteshield.main;

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

        if (SystemClock.elapsedRealtime() > Prefs.getResetTime(this)) {
            InternetConnection.reset(this);
            Prefs.setResetTime(this, SystemClock.elapsedRealtime() + RandomDelay.getNextLong());
        }

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
        startActivity(launchIntent);

		int nextShort = RandomDelay.getNextShort();
		Shield.wakeDevice(this, nextShort);
		IntentSender.sendAfterDelay(this, nextShort); // send next intent after random delay
    }

}
