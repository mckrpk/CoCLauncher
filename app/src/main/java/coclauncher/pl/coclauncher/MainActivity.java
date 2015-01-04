package coclauncher.pl.coclauncher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    private Button startBtn;
    private TextView descriptionView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startNowBtn = (Button) findViewById(R.id.start_now_btn);
        startBtn = (Button) findViewById(R.id.start_btn);
        descriptionView = (TextView) findViewById(R.id.description_view);

        if (isLauncherActive()) {
            startBtn.setBackgroundResource(R.drawable.on);
            descriptionView.setText(R.string.shield_active);
        } else {
            startBtn.setBackgroundResource(R.drawable.off);
            descriptionView.setText(R.string.shield_not_active);
        }

        startNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
                startActivity(launchIntent);
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLauncherActive()) {
                    LauncherService.cancelChecker(MainActivity.this);
                    startBtn.setBackgroundResource(R.drawable.off);
                    descriptionView.setText(R.string.shield_not_active);
                    setPrefLauncherActive(false);

                } else {
                    LauncherService.startLauncher(MainActivity.this, Settings.DEFAULT_FREQUENCY);
                    startBtn.setBackgroundResource(R.drawable.on);
                    descriptionView.setText(R.string.shield_active);
                    setPrefLauncherActive(true);
                }
            }
        });
    }

    private boolean isLauncherActive() {
        boolean launcherActive = LauncherService.isLauncherIntentActive(MainActivity.this);
        boolean launcherActiveFromPrefs = getPrefLauncherActive();
        return launcherActive && launcherActiveFromPrefs;
    }

    public void setPrefLauncherActive(boolean active) {
        SharedPreferences settings = getSharedPreferences(Settings.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Settings.PREF_LAUNCHER_ACTIVE, active);
        editor.commit();
    }

    public boolean getPrefLauncherActive() {
        SharedPreferences settings = getSharedPreferences(Settings.PREFS_NAME, 0);
        return settings.getBoolean(Settings.PREF_LAUNCHER_ACTIVE, false);
    }

}
