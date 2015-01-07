package pl.infiniteshield.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

public class Prefs {

    private static final String PREFS_FILENAME = "ShieldPrefs";
    private static final String IS_DELAYING_TO_SEND_KEY = "IS_DELAYING_TO_SEND_KEY";

    public static void setIsDelayingToSend(Context context, boolean isDelayingToSend) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(IS_DELAYING_TO_SEND_KEY, isDelayingToSend);
        editor.commit();
    }

    public static boolean getIsDelayingToSend(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_FILENAME, 0);
        return settings.getBoolean(IS_DELAYING_TO_SEND_KEY, false);
    }
}