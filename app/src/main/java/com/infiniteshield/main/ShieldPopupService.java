package com.infiniteshield.main;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ShieldPopupService extends Service {

    private WindowManager windowManager;
    private ImageView shieldHead;
    private LinearLayout buttonsLayout;
    private LinearLayout shieldLayout;
    private Button cancelBtn;
    private Button confBtn;
    private boolean sendBroadcastOnDestroy = true;
    private boolean buttonsDisplayed;

    private BroadcastReceiver stopShieldBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendBroadcastOnDestroy = false;
            stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        shieldHead = new ImageView(this);
        shieldHead.setImageResource(R.drawable.widget_on);

        shieldLayout = new LinearLayout(this);
        shieldLayout.addView(shieldHead);

        final WindowManager.LayoutParams params = getWindowParams();
        windowManager.addView(shieldLayout, params);
        setTouchListeners(params);

        confBtn = new Button(this);
        confBtn.setText(getString(R.string.stop_shield));
        confBtn.setBackgroundResource(R.drawable.btn_selector);

        cancelBtn = new Button(this);
        cancelBtn.setText(getString(android.R.string.cancel));
        cancelBtn.setBackgroundResource(R.drawable.btn_selector);

        buttonsLayout = new LinearLayout(this);
        final WindowManager.LayoutParams buttonsLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        buttonsLayoutParams.gravity = Gravity.CENTER;
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.addView(confBtn);

        final LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cancelParams.leftMargin = dpToPx(this, 5);
        buttonsLayout.addView(cancelBtn, cancelParams);

        shieldHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!buttonsDisplayed) {
                        windowManager.addView(buttonsLayout, buttonsLayoutParams);
                        confBtn.setAnimation(AnimationUtils.loadAnimation(ShieldPopupService.this, R.anim.anim_btn_in));
                        cancelBtn.setAnimation(AnimationUtils.loadAnimation(ShieldPopupService.this, R.anim.anim_btn_in));
                    }else{
                        cancelAction();
                    }
                    buttonsDisplayed = !buttonsDisplayed;
                } catch (Exception e) {
                    Log.d("coc", "ShieldService - buttons layout already added");
                }
            }
        });

        confBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDisplayed = false;
                Animation animation = AnimationUtils.loadAnimation(ShieldPopupService.this, R.anim.anim_btn_out);
                animation.setAnimationListener(new SimpleAnimListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        try {
                            windowManager.removeView(buttonsLayout);
                            Shield.toggle(ShieldPopupService.this);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                });
                confBtn.setAnimation(animation);
                cancelBtn.setAnimation(animation);
                shieldHead.setAnimation(animation);
            }
        });


        View.OnClickListener cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsDisplayed = false;
                cancelAction();
            }
        };
        cancelBtn.setOnClickListener(cancelListener);

        registerReceiver(stopShieldBroadcastReceiver, new IntentFilter(MainActivity.SHIELD_STATE_CHANGED_ACTION));
    }

    private void cancelAction() {
        Animation animation = AnimationUtils.loadAnimation(ShieldPopupService.this, R.anim.anim_btn_out);
        animation.setAnimationListener(new SimpleAnimListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    windowManager.removeView(buttonsLayout);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        });
        confBtn.startAnimation(animation);
        cancelBtn.startAnimation(animation);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NotificationHelper.NOTIFICATION_ID, NotificationHelper.createNotification(this, false));
        return super.onStartCommand(intent, flags, startId);
    }

    private void setTouchListeners(final WindowManager.LayoutParams params) {
        shieldHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return false;
                    case MotionEvent.ACTION_UP:
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(shieldLayout, params);
                        return false;
                }
                return false;
            }
        });
    }

    private WindowManager.LayoutParams getWindowParams() {
        int windowSize = dpToPx(this, 40);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                windowSize,
                windowSize,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        return params;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stopShieldBroadcastReceiver);

        try {//can't check if window already contains this view
            if (shieldLayout != null) windowManager.removeView(shieldLayout);
        } catch (IllegalArgumentException e) {
            Log.d("coc", "ShieldService - onDestroy error");
        }

        if (buttonsLayout != null) {
            try {
                windowManager.removeView(buttonsLayout);
                Shield.toggle(ShieldPopupService.this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        //restarts popup service immidiately if it's killed by system
        if (sendBroadcastOnDestroy) {
            Log.d("coc", "ShieldService - sending rerun broadcast");
            sendBroadcast(new Intent(this, PopupServiceStopedReceiver.class));
        } else {
            Log.d("coc", "ShieldService - NOT sending rerun broadcast");
        }
    }

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    class SimpleAnimListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

}
