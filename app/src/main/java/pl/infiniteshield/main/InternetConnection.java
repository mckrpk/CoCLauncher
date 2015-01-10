package pl.infiniteshield.main;

import android.content.Context;
import android.net.wifi.WifiManager;

public class InternetConnection {

    public static void setEnabled(Context context, boolean enabled) {
        if (isWifi()) {
            setWifiEnabled(context, enabled);
        }
        if (isNetwork()) {
            setNetworkEnabled(context, enabled);
        }
    }

    private static boolean isWifi() {
        // TODO: implement
        return false;
    }

    private static boolean isNetwork() {
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
