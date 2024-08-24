package com.bbyy.weeat.ui.page;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.bbyy.weeat.R;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {

    private static final int AUTO_HIDE_DELAY_MILLIS = 1000; //跳转前需要的时间ms
    private final Handler mHideHandler = new Handler(Objects.requireNonNull(Looper.myLooper()));

    //跳转activity
    private final Runnable mSwtichRunnable = () -> {
        Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.main_enter,R.anim.main_exit);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mHideHandler.removeCallbacks(mSwtichRunnable);
        mHideHandler.postDelayed(mSwtichRunnable,AUTO_HIDE_DELAY_MILLIS);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHideHandler.removeCallbacksAndMessages(null);
    }
}