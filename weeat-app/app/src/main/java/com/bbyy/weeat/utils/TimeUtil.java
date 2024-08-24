package com.bbyy.weeat.utils;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class TimeUtil {
    public static class CountdownTimer {
        private Disposable disposable;  //处理资源释放
        private final MutableLiveData<Long> remainingTime; //剩下时间
        private boolean isPaused;   //是否暂停

        public CountdownTimer(MutableLiveData<Long> remainingTime) {
            this.remainingTime=remainingTime;
        }

        // 开始任务
        public void start() {
            //销毁非空且没有被销毁的disposable对象
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            isPaused = false;
            disposable = Observable.interval(1, TimeUnit.SECONDS)   //创建了一个被观察者（Observable），每隔1秒发出一个递增的整数序列
                    .takeWhile(tick -> remainingTime.getValue() > 0)   //过滤操作符，根据条件remainingTime > 0来决定是否继续接收数据
                    .map(tick -> {
                        long temp=remainingTime.getValue()-1;
                        remainingTime.postValue(temp);
                        return temp;
                    })   //映射操作符，将接收到的数据进行处理
                    .subscribe(time -> Log.d("hello","Remaining time: " + time));  //订阅了这个被观察者
        }

        //暂停任务
        public void pause() {
            if (disposable != null && !disposable.isDisposed()) {
                isPaused = true;
                disposable.dispose();
            }
        }

        //重新开始
        public void resume() {
            if (isPaused) {
                isPaused = false;
                start();
            }
        }

        //停止任务
        public void stop() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    public static String formatSecondsToTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        String time_format="%02d:%02d";
        return String.format(time_format, minutes, remainingSeconds);
    }

    public static long getTimestampFromTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    //时间戳转为年月日字符串
    public static String formatTimestampToDate(long timestamp) {
        // 创建SimpleDateFormat对象，指定日期格式为"yyyy-MM-dd"
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        // 将时间戳转换为Date对象
        Date date = new Date(timestamp);

        // 使用SimpleDateFormat对象的format方法将Date对象转换为字符串
        return dateFormat.format(date);
    }

    public static int getCurrentMonth(){
        // 获取当前日期的Calendar对象
        Calendar calendar = Calendar.getInstance();
        // 获取当前日期的月份（0-11）
        return calendar.get(Calendar.MONTH);
    }

    public static void showTimeDialog(FragmentManager fragmentManager, Date startDate) {
        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder();
        //设置时间格式为24小时制
        builder.setTimeFormat(TimeFormat.CLOCK_24H);
        if (startDate != null) {
            builder.setHour(startDate.getHours());
            builder.setMinute(startDate.getMinutes());
        }
        MaterialTimePicker timePicker = builder.build();
        timePicker.addOnPositiveButtonClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String hourStr = hour > 9 ? String.valueOf(hour) : "0" + hour;
            String minuteStr = minute > 9 ? String.valueOf(minute) : "0" + minute;
            String timeStrText = hourStr + ":" + minuteStr;
            //todo 选择时间后的操作
        });
        timePicker.show(fragmentManager, "DATE_PICKER_TAG");
    }

    public static void showTimePicker(Context context, MutableLiveData<Long> rest_seconds) {
        String[] items={"15分钟","30分钟","45分钟","60分钟"};
        new MaterialAlertDialogBuilder(context).setSingleChoiceItems(items,0, (dialog, which) -> {
            switch (which){
                case 0:
                    rest_seconds.setValue(15* 60L);
                    break;
                case 1:
                    rest_seconds.setValue(30* 60L);
                    break;
                case 2:
                    rest_seconds.setValue(45* 60L);
                    break;
                case 3:
                    rest_seconds.setValue(60* 60L);
                    break;
                default:
                    rest_seconds.setValue(0L);
                    break;
            }
        }).setTitle("选择时长").show();
    }

    public static void showDateDialog(FragmentManager fragmentManager, Date startDate) {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        //设置时间格式为24小时制
        MaterialDatePicker datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            int hour = datePicker.getId();
            String hourStr = hour > 9 ? String.valueOf(hour) : "0" + hour;
            //todo 选择时间后的操作
        });
        datePicker.show(fragmentManager, "DATE_PICKER_TAG");
    }

}
