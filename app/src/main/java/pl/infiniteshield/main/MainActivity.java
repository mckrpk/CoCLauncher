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

        final IntentSender intentSender = new IntentSender(this);

        if (intentSender.isDelayingToSend()) {
            setShield(true);
        } else {
            setShield(false);
        }

        startShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intentSender.isDelayingToSend()) {
                    intentSender.cancelSendAfterDelay();
                    setShield(false);
                } else {
                    intentSender.sendAfterDelay(0);
                    setShield(true);
                }
				sendBroadcast(new Intent(ShieldWidgetProvider.UPDATE_WIDGET_ACTION));
            }
        });
    }

	private void setShield(boolean on) {
        if (on) {
			startShield.setBackgroundResource(R.drawable.on);
			shieldStatus.setText(R.string.shield_active);
			ScreenOffTimeout.setInfinite(this);
		} else {
			startShield.setBackgroundResource(R.drawable.off);
			shieldStatus.setText(R.string.shield_not_active);
        }
    }

}
