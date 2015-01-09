package pl.infiniteshield.main;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class ScreenOffTimeout
{

	public static final int INFINITE_SCREEN_OFF_TIMEOUT = 2147483647;
	public static final int DEFAULT_SCREEN_OFF_TIMEOUT = 60 * 1000;

	public static void setInfinite(Context context)
	{
		if (!Prefs.getIsSettingChanged(context)) {
			int previousSetting = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
					DEFAULT_SCREEN_OFF_TIMEOUT);
			Prefs.setUserSleepSetting(context, previousSetting);
		}
		Prefs.setIsSettingChanged(context, true);
		Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, INFINITE_SCREEN_OFF_TIMEOUT);
	}

	public static void restoreUserScreenSetting(Context context)
	{
		int previousSetting = Prefs.getUserSleepSetting(context);
		Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, previousSetting);
		Prefs.setIsSettingChanged(context, false);
	}
}
