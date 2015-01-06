package pl.infiniteshield.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    public static final int DELAY_AFTER_START_SHIELD_PRESS = 3000;

    private Button startShield;
    private TextView shieldStatus;
    private Button startClashOfClans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startShield = (Button) findViewById(R.id.start_shield);
        shieldStatus = (TextView) findViewById(R.id.shield_status);
        startClashOfClans = (Button) findViewById(R.id.start_clash_of_clans);

        if (Global.app.intentSender.isDelayingToSend()) {
            setShield(true);
        } else {
            setShield(false);
        }

        startClashOfClans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.supercell.clashofclans");
                startActivity(launchIntent);
            }
        });

        startShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.app.intentSender.isDelayingToSend()) {
                    Global.app.intentSender.cancelSendAfterDelay();
                    setShield(false);

                } else {
                    Global.app.intentSender.sendAfterDelay(DELAY_AFTER_START_SHIELD_PRESS);
                    setShield(true);
                }
            }
        });
    }

    private void setShield(boolean on) {
        if (on) {
            startShield.setBackgroundResource(R.drawable.on);
            shieldStatus.setText(R.string.shield_active);
        } else {
            startShield.setBackgroundResource(R.drawable.off);
            shieldStatus.setText(R.string.shield_not_active);
        }
    }

}
