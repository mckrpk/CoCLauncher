package pl.infiniteshield.main;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String PREFS_FILENAME = "ShieldPrefs";
    private static final String IS_DELAYING_TO_SEND_KEY = "IS_DELAYING_TO_SEND_KEY";
    private static final String USER_SLEEP_SETTING_KEY = "USER_SLEEP_SETTING_KEY";
    private static final String IS_SCREEN_SETTING_CHANGED_KEY = "SETTING_CHANGED_KEY";
    private static final String SHIELD_START_TIME_KEY = "SHIELD_START_TIME_KEY";

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

    public static void setIsSettingChanged(Context context, boolean isScreenSettingChanged) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(IS_SCREEN_SETTING_CHANGED_KEY, isScreenSettingChanged);
        editor.commit();
    }

    /**
     * Have application already changed screen sleep time to infinity?
     */
    public static boolean getIsSettingChanged(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_FILENAME, 0);
        return settings.getBoolean(IS_SCREEN_SETTING_CHANGED_KEY, false);
    }


    public static void setUserSleepSetting(Context context, int userSleepSetting) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(USER_SLEEP_SETTING_KEY, userSleepSetting);
        editor.commit();
    }

    public static int getUserSleepSetting(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_FILENAME, 0);
        return settings.getInt(USER_SLEEP_SETTING_KEY, Shield.DEFAULT_SCREEN_OFF_TIMEOUT);
    }

    public static void setShieldStartTime(Context context, long time) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(SHIELD_START_TIME_KEY, time);
        editor.commit();
    }

    public static long getShieldStartTime(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_FILENAME, 0);
        return settings.getLong(SHIELD_START_TIME_KEY, System.currentTimeMillis());
    }


}