package pl.infiniteshield.main;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    public static final String PREFS_FILENAME = "ShieldPrefs";
    public static final String IS_DELAYING_TO_SEND_KEY = "pref_launcher_active";

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