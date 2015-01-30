package com.shield.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SchedulerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("coc", "Scheduler broadcast received");
        if (!Shield.isActive(context)) {
            Shield.toggle(context);
        }
    }
}
