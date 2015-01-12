package pl.infiniteshield.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InternetConnection {

    private static final int SLEEP_AFTER_DISABLE = 5000;
    private static final int MAX_SLEEP_AFTER_ENABLE = 18000;
    private static boolean canContinue = false;
    private static int counter = 0;

    private static class NetworkChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo = null;
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            }
            Log.d("coc", "Internet update: " + String.valueOf(networkInfo));
            if (networkInfo != null && networkInfo.isConnected()) {
                unlockSleep();
            }
        }
    }

    public static void reset(Context context) {
        Log.d("coc", "reset: disable");
        InternetConnection.setEnabled(context, false);
        sleep(SLEEP_AFTER_DISABLE);
        Log.d("coc", "reset: enable");
        InternetConnection.setEnabled(context, true);
        counter = 0;
        canContinue = false;
        NetworkChangedReceiver networkChangedReceiver = new NetworkChangedReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(networkChangedReceiver, intentFilter);

        long startTime = System.currentTimeMillis();
        while (!canContinue && System.currentTimeMillis() - startTime < MAX_SLEEP_AFTER_ENABLE) {
            Log.d("coc", "reset: sleep 1 second");
            sleep(1000);
        }
        context.unregisterReceiver(networkChangedReceiver);
        Log.d("coc", "reset: after enable sleep");
    }

    private static void unlockSleep() {
        counter++;
        if (counter > 1) {
            canContinue = true;
        }
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
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.isConnected();
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
