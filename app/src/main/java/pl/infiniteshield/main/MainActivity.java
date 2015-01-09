package pl.infiniteshield.main;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private Button startShield;
	private TextView shieldStatusTop;
	private TextView shieldStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        startShield = (Button) findViewById(R.id.start_shield);
        shieldStatus = (TextView) findViewById(R.id.shield_status);
        shieldStatusTop = (TextView) findViewById(R.id.shield_status_top);

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
            }
        });
    }

	private void setShield(boolean on) {
        if (on) {
			startShield.setBackgroundResource(R.drawable.on);
			shieldStatus.setText(R.string.shield_active);
			shieldStatusTop.setText(R.string.shield_active_top);
			ScreenOffTimeout.setInfinite(this);
		} else {
			startShield.setBackgroundResource(R.drawable.off);
			shieldStatusTop.setText(R.string.shield_not_active_top);
			shieldStatus.setText(R.string.shield_not_active);
        }
    }

}
