package coclauncher.pl.coclauncher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


public class MainActivity extends ActionBarActivity
{
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String PREF_FREQUENCY = "pref_frequency";

	private final static int DEFAULT_FREQUENCY = 2 * 60;
	private Button startNowBtn;
	private ToggleButton serviceToggle;
	private EditText frequencyEditText;

	@Override
	protected void onDestroy()
	{
		try {
			setPrefFrequency(Integer.valueOf(frequencyEditText.getText().toString()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startNowBtn = (Button) findViewById(R.id.start_now_btn);
		serviceToggle = (ToggleButton) findViewById(R.id.service_toggle);
		frequencyEditText = (EditText) findViewById(R.id.frequency_edit_text);

		frequencyEditText.setText(getPrefFrequency() + "");

		startNowBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
				startActivity(launchIntent);
			}
		});

		serviceToggle.setChecked(LauncherService.isLauncherActive(this));
		serviceToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				try {

					boolean launcherActive = LauncherService.isLauncherActive(MainActivity.this);
					if (isChecked && !launcherActive) {
						int frequency = 1000 * Integer.valueOf(frequencyEditText.getText().toString());
						LauncherService.startLauncher(MainActivity.this, frequency);
					} else if (!isChecked && launcherActive) {
						LauncherService.cancelChecker(MainActivity.this);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this, R.string.bad_value_message, Toast.LENGTH_SHORT);
				}
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void setPrefFrequency(int frequency)
	{
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(PREF_FREQUENCY, frequency);
		editor.commit();
	}

	public int getPrefFrequency()
	{
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		return settings.getInt(PREF_FREQUENCY, DEFAULT_FREQUENCY);
	}

}
