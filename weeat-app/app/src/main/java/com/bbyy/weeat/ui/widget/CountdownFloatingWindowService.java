package com.bbyy.weeat.ui.widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.icu.text.SimpleDateFormat;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bbyy.weeat.R;

import java.util.Date;

public class CountdownFloatingWindowService extends Service {

    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams params;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        floatingView = LayoutInflater.from(this).inflate(R.layout.countdown_floating_view, null);

        final TextView countdownTextView = floatingView.findViewById(R.id.countdownTextView);

        CountDownTimer timer=new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date(0, 0, 0, secondsRemaining / 3600,secondsRemaining/60, secondsRemaining);
                countdownTextView.setText(sdf.format(date));
            }

            @Override
            public void onFinish() {
                // 倒计时结束的操作
                stopSelf();
            }
        };
        timer.start();

        // 设置悬浮窗参数
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSPARENT
                );
        params.gravity = Gravity.TOP | Gravity.LEFT;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        params.x = 50;
        params.y = 50;
        params.width=WindowManager.LayoutParams.WRAP_CONTENT;
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        floatingView.setOnTouchListener(new FloatingOnTouchListener());
        windowManager.addView(floatingView, params);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView!= null) {
            windowManager.removeView(floatingView);
        }
    }

    public class FloatingOnTouchListener implements View.OnTouchListener{
        private int x;
        private int y;
        private boolean isScroll;   //是否执行move事件
        private boolean isMoved;    //是否移动了位置
        private int startX;
        private int startY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    x=(int)event.getRawX();
                    y=(int)event.getRawY();
                    isMoved=false;
                    isScroll=false;
                    startX=x;
                    startY=y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX=(int)event.getRawX();
                    int nowY=(int)event.getRawY();
                    int movedX = nowX -x;
                    int movedY = nowY -y;
                    x=nowX;
                    y=nowY;
                    params.x =params.x + movedX;
                    params.y =params.y + movedY;
                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(floatingView, params);
                    isScroll=true;
                    break;
                default:
                    break;
            }
            return isMoved;
        }
    }
}