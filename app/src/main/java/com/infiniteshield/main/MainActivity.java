package com.infiniteshield.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private Button startShield;
    private TextView shieldStatus;
    private View scheduleLayout;
    private View pickersLayout;
    private CheckBox scheduleCheckBox;
    private NumberPicker hoursPicker;
    private NumberPicker minutesPicker;
    private TextView scheduleCounterView;
    private CountDownTimer countDownTimer;

    public static final String SHIELD_STATE_CHANGED_ACTION = "com.infiniteshield.shieldStateChanged";

    public BroadcastReceiver shieldStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startShield = (Button) findViewById(R.id.start_shield);
        shieldStatus = (TextView) findViewById(R.id.shield_status);
        scheduleLayout = findViewById(R.id.schedule_layout);
        pickersLayout = findViewById(R.id.pickers_layout);
        scheduleCheckBox = (CheckBox) findViewById(R.id.schedule_check_box);
        hoursPicker = (NumberPicker) findViewById(R.id.hours_picker);
        minutesPicker = (NumberPicker) findViewById(R.id.minutes_picker);
        scheduleCounterView = (TextView) findViewById(R.id.counter_view);

        scheduleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    IntentSender.cancelScheduler(MainActivity.this);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                }
                updateUI();
            }
        });
        scheduleCheckBox.setChecked(Shield.isScheduled(this));
        if (scheduleCheckBox.isChecked()) {
            Log.d("coc", "previously scheduled");
            restoreScheduleCaunter();
        }

        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(24);

        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        startShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Shield.isActive(MainActivity.this) && scheduleCheckBox.isChecked() && !Shield.isScheduled(MainActivity.this)) {
                    long scheduleMilis = getScheduleMilis();
                    startScheduleCounter(scheduleMilis);
                    IntentSender.schedule(MainActivity.this, scheduleMilis);
                    updateUI();
                } else {
                    Shield.toggle(MainActivity.this);
                }
            }
        });
        registerReceiver(shieldStateChangedReceiver, new IntentFilter(SHIELD_STATE_CHANGED_ACTION));
    }

    private void restoreScheduleCaunter() {
        long scheduledTime = Prefs.getLong(this, Prefs.SCHEDULER_DUE_TIME);
        pickersLayout.setVisibility(View.GONE);
        scheduleCounterView.setVisibility(View.VISIBLE);
        startScheduleCounter(scheduledTime - System.currentTimeMillis());
    }

    private long getScheduleMilis() {
        return hoursPicker.getValue() * 60 * 60 * 1000 + minutesPicker.getValue() * 60 * 1000;
    }

    private void startScheduleCounter(long milis) {
        countDownTimer = new CountDownTimer(milis, 1000) {
            public void onTick(long millisUntilFinished) {
                scheduleCounterView.setText(prepareCountText(millisUntilFinished));
            }

            public void onFinish() {
                scheduleCounterView.setText("Starting CoC");
            }
        };
        countDownTimer.start();
    }

    private String prepareCountText(long millisUntilFinished) {
        int hours = (int) millisUntilFinished / (60 * 60 * 1000);
        millisUntilFinished -= hours * 60 * 60 * 1000;
        int minutes = (int) millisUntilFinished / (60 * 1000);
        millisUntilFinished -= minutes * 60 * 1000;
        int seconds = (int) millisUntilFinished / 1000;
        StringBuilder builder = new StringBuilder();
        if (hours > 0) {
            if (hours < 10) {
                builder.append("0");
            }
            builder.append(hours);
            builder.append(":");
        }
        if (minutes > 0) {
            if (minutes < 10) {
                builder.append("0");
            }
            builder.append(minutes);
        } else {
            builder.append("00");
        }
        builder.append(":");
        if (seconds > 0) {
            if (seconds < 10) {
                builder.append("0");
            }
            builder.append(seconds);
        }
        return builder.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(shieldStateChangedReceiver);
        super.onDestroy();
    }

    private void updateUI() {
        if (Shield.isActive(this)) {
            startShield.setBackgroundResource(R.drawable.on);
            shieldStatus.setText(R.string.shield_active);
            scheduleLayout.setVisibility(View.GONE);
            Prefs.setBoolean(this, Prefs.IS_SCHEDULED, false);
        } else {
            scheduleLayout.setVisibility(View.VISIBLE);
            if (scheduleCheckBox.isChecked()) {
                if (Shield.isScheduled(this)) {
                    scheduleCounterView.setVisibility(View.VISIBLE);
                    pickersLayout.setVisibility(View.GONE);
                } else {
                    scheduleCounterView.setVisibility(View.GONE);
                    pickersLayout.setVisibility(View.VISIBLE);
                }
            } else {
                pickersLayout.setVisibility(View.GONE);
                scheduleCounterView.setVisibility(View.GONE);
            }
            startShield.setBackgroundResource(R.drawable.off);
            shieldStatus.setText(R.string.shield_not_active);
        }
    }

}
