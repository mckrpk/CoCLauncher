package pl.infiniteshield.main;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class ScreenOffTimeout {

    public static final int INFINITE_SCREEN_OFF_TIMEOUT = 2147483647;

    public static void setInfinite(Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, INFINITE_SCREEN_OFF_TIMEOUT);
    }

}
