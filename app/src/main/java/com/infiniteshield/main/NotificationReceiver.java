package com.infiniteshield.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_STOP = "com.infiniteshield.action_stop";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_STOP)) {
            Shield.toggle(context);
        }
    }
}

