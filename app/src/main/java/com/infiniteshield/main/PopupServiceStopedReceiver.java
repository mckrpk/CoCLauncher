package com.infiniteshield.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PopupServiceStopedReceiver  extends BroadcastReceiver{
    public static final String ACTION_RERUN_SERVICE = "com.infiniteshield.action_rerun_popup_service";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("coc", "rerun popup broadcast received");
        context.startService(new Intent(context, ShieldPopupService.class));
    }
}
