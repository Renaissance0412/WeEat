package com.bbyy.weeat.ui.widget;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bbyy.weeat.R;
import com.bbyy.weeat.ui.page.MainActivity;

import java.util.List;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class MyForegroundService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "foreground_service_channel";

    private static MyForegroundService instance;
    private boolean monitor=true;
    private Thread thread;

    public static synchronized MyForegroundService getInstance() {
        if (instance == null) {
            instance = new MyForegroundService();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service","Foreground Service Channel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 创建前台通知
        Log.d("service","Foreground Service Channel");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("WeEat")
                .setContentText("Monitoring……")
                .setSmallIcon(R.drawable.hamburger)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        // 启动一个线程监控应用状态
        thread=new Thread(() -> {
            while (monitor) {
                // 检测应用是否在前台
                if (isAppInBackground(getApplicationContext())) {
                    // 应用在后台，启动 MainActivity
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 检测应用是否在后台的方法
    private boolean isAppInBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager!= null) {
            List<ActivityManager.RunningAppProcessInfo> runningTasks = activityManager.getRunningAppProcesses();
            if (!runningTasks.isEmpty()) {
                ActivityManager.RunningAppProcessInfo processInfo = runningTasks.get(0);
                Log.d("test ",processInfo.processName+" vs "+context.getPackageName());
                if(processInfo.processName.equals(context.getPackageName())
                        &&processInfo.importance==ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                    Log.d("test ","is foreground");
                    return true;
                }
            }
        }
        Log.d("test ","is not foreground");
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        monitor = false;
        if (thread!= null && thread.isAlive()) {
            thread.interrupt();
        }
    }
}