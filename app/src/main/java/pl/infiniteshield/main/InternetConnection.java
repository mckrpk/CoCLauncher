package pl.infiniteshield.main;

import android.content.Context;
import android.net.wifi.WifiManager;

public class InternetConnection {

    public static void setEnabled(Context context, boolean enabled) {
        setWifiEnabled(context, enabled);
        setNetworkEnabled(context, enabled);
    }

    private static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    private static boolean isNetworkEnabled(Context context) {
        // TODO: implement
        return false;
    }

    private static void setWifiEnabled(Context context, boolean enabled) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
    }

    private static void setNetworkEnabled(Context context, boolean enabled) {
        // TODO: implement
    }

}
