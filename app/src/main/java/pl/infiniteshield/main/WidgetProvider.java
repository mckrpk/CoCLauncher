package pl.infiniteshield.main;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

    public static final String TOGGLE_ACTION = "pl.infiniteshield.TOGGLE_SHIELD_ACTION";
    public static final String UPDATE_WIDGET_ACTION = "pl.infiniteshield.UPDATE_WIDGET_ACTION";
    private static final int REQUEST_CODE = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            int resId = Shield.isActivated(context) ? R.drawable.widget_on : R.drawable.widget_off;
            views.setInt(R.id.widget_btn, "setBackgroundResource", resId);

            Intent toggleIntent = new Intent(context, WidgetProvider.class);
            toggleIntent.setAction(WidgetProvider.TOGGLE_ACTION);
            toggleIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, toggleIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_btn, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        if (action.equals(TOGGLE_ACTION)) {
            boolean shieldActive = Shield.toggle(context);
            int resId = shieldActive ? R.drawable.widget_on : R.drawable.widget_off;
            views.setInt(R.id.widget_btn, "setBackgroundResource", resId);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } else if (action.equals(UPDATE_WIDGET_ACTION)) {
            if (Shield.isActivated(context)) {
                views.setInt(R.id.widget_btn, "setBackgroundResource", R.drawable.widget_on);
            } else {
                views.setInt(R.id.widget_btn, "setBackgroundResource", R.drawable.widget_off);
            }
            appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), views);
        }
        super.onReceive(context, intent);
    }
}
