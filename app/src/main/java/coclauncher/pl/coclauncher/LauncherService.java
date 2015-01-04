package coclauncher.pl.coclauncher;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class LauncherService extends IntentService
{

	private static final int REQUEST_CODE = 1;
	public static final String CHECKER_ACTION = "pl.coclauncher.LaunchAction";

	public LauncherService()
	{
		super("LauncherService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
		startActivity(launchIntent);
	}

	public static void startLauncher(Context context, long frequency)
	{
		PendingIntent pendingIntent = PendingIntent.getService(context, LauncherService.REQUEST_CODE,
				LauncherService.getIntent(context), PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000,
				frequency, pendingIntent);
	}

	public static void cancelChecker(Context context)
	{
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getService(context, REQUEST_CODE, getIntent(context), 0);
		alarmManager.cancel(pendingIntent);
	}

	public static boolean isLauncherActive(Context context)
	{
		return PendingIntent.getService(context, REQUEST_CODE, getIntent(context), PendingIntent.FLAG_NO_CREATE) != null;
	}

	public static Intent getIntent(Context context)
	{
		Intent newIntent = new Intent(CHECKER_ACTION);
		newIntent.setClass(context, LauncherService.class);
		return newIntent;
	}
}
