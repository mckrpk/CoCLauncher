package pl.infiniteshield.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InternetConnection {

    private static final int SLEEP_AFTER_DISABLE = 3000;
    private static final int SLEEP_AFTER_ENABLE = 7000;

    public static void reset(Context context) {
        Log.d("coc", "reset: disable");
        InternetConnection.setEnabled(context, false);
        sleep(SLEEP_AFTER_DISABLE);
        Log.d("coc", "reset: enable");
        InternetConnection.setEnabled(context, true);
        sleep(SLEEP_AFTER_ENABLE);
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void setEnabled(Context context, boolean enabled) {
        if (enabled) {
            if (Prefs.getIsUserWifiOverridden(context)) {
                Prefs.setIsUserWifiOverridden(context, false);
                setWifiEnabled(context, true);
                Log.d("coc", "enable wifi");
            }
            if (Prefs.getIsUserNetworkOverridden(context)) {
                Prefs.setIsUserNetworkOverridden(context, false);
                setNetworkEnabled(context, true);
                Log.d("coc", "enable network");
            }
        } else {
            if (isWifiEnabled(context)) {
                Prefs.setIsUserWifiOverridden(context, true);
                setWifiEnabled(context, false);
                Log.d("coc", "disable wifi");
            }
            if (isNetworkEnabled(context)) {
                Prefs.setIsUserNetworkOverridden(context, true);
                setNetworkEnabled(context, false);
                Log.d("coc", "disable network");
            }
        }
    }

    /**
     * @return True, if WiFi is enabled.
     */
    private static boolean isWifiEnabled(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm.isWifiEnabled();
    }

    private static void setWifiEnabled(Context context, boolean enabled) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wm.setWifiEnabled(enabled);
    }

    /**
     * @return True, if network is enabled and WiFi is not.
     */
    private static boolean isNetworkEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
    }

    private static void setNetworkEnabled(Context context, boolean enabled) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class conmanClass = Class.forName(cm.getClass().getName());
            Field connectivityManagerField = conmanClass.getDeclaredField("mService");
            connectivityManagerField.setAccessible(true);
            Object connectivityManager = connectivityManagerField.get(cm);
            Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
            Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
