package pl.infiniteshield.main;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;

public class Shield {

    public static final int INFINITE_SCREEN_OFF_TIMEOUT = 2147483647;
    public static final int DEFAULT_SCREEN_OFF_TIMEOUT = 60 * 1000;

    public static boolean toggle(Context context) {
        if (isActive(context)) {
            // turn off
            IntentSender.cancelSendAfterDelay(context);
            Prefs.setIsDelayingToSend(context, false);
            Shield.restoreUserScreenSetting(context);
            enableKeyguard(context, true);
            return false;
        } else {
            // turn on
            enableKeyguard(context, false);
            Shield.setInfiniteScreen(context);
            IntentSender.sendAfterDelay(context, 0);
            Prefs.setResetTime(context, SystemClock.elapsedRealtime() + RandomDelay.getNextLong());
            return true;
        }
    }

    public static boolean isActive(Context context) {
        return Prefs.getIsDelayingToSend(context);
    }

    public static void wakeDevice(Context context, int time) {
        PowerManager.WakeLock screenLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock
                (PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire(time + 3000);
    }

    private static void enableKeyguard(Context context, boolean enable) {
        KeyguardManager.KeyguardLock keyguardLock = ((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE))
                .newKeyguardLock("TAG");
        if (enable) {
            keyguardLock.reenableKeyguard();
        } else {
            keyguardLock.disableKeyguard();
        }
    }

    private static void setInfiniteScreen(Context context) {
        if (!Prefs.getIsUserScreenTimeoutOverridden(context)) {
            int previousSetting = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
                    DEFAULT_SCREEN_OFF_TIMEOUT);
            Prefs.setUserScreenTimeout(context, previousSetting);
        }
        Prefs.setIsUserScreenTimeoutOverridden(context, true);
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, INFINITE_SCREEN_OFF_TIMEOUT);
    }

    private static void restoreUserScreenSetting(Context context) {
        int previousSetting = Prefs.getUserScreenTimeout(context);
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, previousSetting);
        Prefs.setIsUserScreenTimeoutOverridden(context, false);
    }
}
