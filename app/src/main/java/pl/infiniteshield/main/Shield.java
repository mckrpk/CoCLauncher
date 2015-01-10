package pl.infiniteshield.main;

import android.content.Context;
import android.provider.Settings;

public class Shield {

    public static final int INFINITE_SCREEN_OFF_TIMEOUT = 2147483647;
    public static final int DEFAULT_SCREEN_OFF_TIMEOUT = 60 * 1000;

    public static boolean toggle(Context context) {
        if (isActivated(context)) {
            IntentSender.cancelSendAfterDelay(context);
            Prefs.setIsDelayingToSend(context, false);
            Shield.restoreUserScreenSetting(context);
            return false;
        } else {
            Shield.setInfiniteScreen(context);
            IntentSender.sendAfterDelay(context, 0);
            return true;
        }
    }

    public static boolean isActivated(Context context) {
        return Prefs.getIsDelayingToSend(context);
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
