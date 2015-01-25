package com.infiniteshield.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private Button startShield;
    private TextView shieldStatus;

    public static final String SHIELD_STATE_CHANGED_ACTION = "com.infiniteshield.shieldStateChanged";

    public BroadcastReceiver shieldStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(Shield.isActive(context));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startShield = (Button) findViewById(R.id.start_shield);
        shieldStatus = (TextView) findViewById(R.id.shield_status);

        startShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shield.toggle(MainActivity.this);
            }
        });
        registerReceiver(shieldStateChangedReceiver, new IntentFilter(SHIELD_STATE_CHANGED_ACTION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(Shield.isActive(this));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(shieldStateChangedReceiver);
        super.onDestroy();
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
