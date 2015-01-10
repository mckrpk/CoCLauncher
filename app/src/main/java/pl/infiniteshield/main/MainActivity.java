package pl.infiniteshield.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.infiniteshield.main.widget.ShieldWidgetProvider;

public class MainActivity extends ActionBarActivity {

    private Button startShield;
	private TextView shieldStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        startShield = (Button) findViewById(R.id.start_shield);
        shieldStatus = (TextView) findViewById(R.id.shield_status);

        if (Shield.isActivated(this)) {
            updateUI(true);
        } else {
            updateUI(false);
        }

        startShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
			{
				updateUI(Shield.toggle(MainActivity.this));
				sendBroadcast(new Intent(ShieldWidgetProvider.UPDATE_WIDGET_ACTION));
			}
		});
    }

	private void updateUI(boolean shieldActive) {
        if (shieldActive) {
			startShield.setBackgroundResource(R.drawable.on);
			shieldStatus.setText(R.string.shield_active);
		} else {
			startShield.setBackgroundResource(R.drawable.off);
			shieldStatus.setText(R.string.shield_not_active);
        }
    }

}
