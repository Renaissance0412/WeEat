package com.bbyy.weeat.viewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bbyy.weeat.repositories.TodoRepository;
import com.bbyy.weeat.utils.TimeUtil;

import java.util.List;

public class ClockViewModel extends AndroidViewModel {
    private final String PICK_TIME="time";
    private final String BUBBLE_NUMBER = "bubble_number";
    private final String MONTH_TOTAL_SECONDS = "month_total_time";
    private final MutableLiveData<Boolean> started = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> paused = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> number = new MutableLiveData<>();
    private final MutableLiveData<Long> total_seconds=new MutableLiveData<>();    //初始的当前总倒计时时间
    private final TodoRepository repository;
    private long currentTotalSeconds = 0;
    private long monthTotalSeconds = 0L;
    private MutableLiveData<Long> rest_seconds = new MutableLiveData<>();
    private final TimeUtil.CountdownTimer countdownTimer = new TimeUtil.CountdownTimer(rest_seconds);
    private List<String> recipeNames;
    private MutableLiveData<Integer> position=new MutableLiveData<>(0);
    private SharedPreferences sharedPreferences;
    public ClockViewModel(@NonNull Application application) {
        super(application);
        repository=new TodoRepository(application.getApplicationContext());
        init(application);
    }

    public List<String> getRecipeNames() {
        recipeNames=repository.getAllListNameLive().getValue();
        return recipeNames;
    }

    public MutableLiveData<Integer> getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position.setValue(position);
    }

    public MutableLiveData<Long> getTotal_seconds() {
        return total_seconds;
    }

    //初始化
    private void init(Application application) {
        String USER_INFO = "user_info";
        sharedPreferences = application.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        int curMonth = TimeUtil.getCurrentMonth();
        String SAVED_MONTH = "saved_month";
        int savedMonth = sharedPreferences.getInt(SAVED_MONTH, curMonth);
        if (curMonth != savedMonth) {
            //是每月第一天，清空气泡和总时长
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(BUBBLE_NUMBER, 0);
            editor.putLong(MONTH_TOTAL_SECONDS, 0L);
            editor.putInt(SAVED_MONTH, curMonth);
            editor.apply();
        }
        number.setValue(sharedPreferences.getInt(BUBBLE_NUMBER, 0));
        total_seconds.setValue(sharedPreferences.getLong(PICK_TIME, 10L));
        if(rest_seconds.getValue()==null)
            rest_seconds.setValue(total_seconds.getValue());
        monthTotalSeconds = sharedPreferences.getLong(MONTH_TOTAL_SECONDS, 0);
    }

    public MutableLiveData<Long> getRest_seconds() {
        return rest_seconds;
    }

    public void setRest_seconds(MutableLiveData<Long> rest_seconds) {
        this.rest_seconds = rest_seconds;
    }

    public MutableLiveData<Boolean> getPaused() {
        return paused;
    }

    public void setPaused(Boolean paused) {
        this.paused.setValue(paused);
    }

    //增加一个bubble
    public void add() {
        this.number.setValue(number.getValue() + 1);
        saveBubbleNumber();
    }

    public MutableLiveData<Integer> getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number.setValue(number);
    }

    public MutableLiveData<Boolean> isStarted() {
        return started;
    }

    public void setStarted(Boolean status) {
        this.started.setValue(status);
    }

    public Boolean isPaused() {
        return paused.getValue();
    }

    public void start() {
        setStarted(true);
        currentTotalSeconds = rest_seconds.getValue();
        countdownTimer.start();
    }

    //暂停
    public void pause() {
        setPaused(true);
        countdownTimer.pause();
    }

    //重新开始事件
    public void restart() {
        setPaused(false);
        countdownTimer.resume();
    }

    public void end() {
        setPaused(false);
        setStarted(false);
        countdownTimer.stop();
        rest_seconds.setValue(total_seconds.getValue());
    }

    //处理点击暂停事件
    public void onPauseClick() {
        if (!isPaused()) {
            pause();
        } else {
            restart();
        }
    }

    //保存气泡数量
    public void saveBubbleNumber() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BUBBLE_NUMBER, number.getValue());
        editor.apply();
    }

    //保存数据工作
    public void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(PICK_TIME, total_seconds.getValue());
        editor.apply();
    }

    //保存一个月的总时间
    public void saveMonthTotalSeconds() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(MONTH_TOTAL_SECONDS, monthTotalSeconds);
        editor.apply();
    }

    //完成一个番茄钟
    public void complete() {
        end();
        //把时间写入总时间
        long pre = monthTotalSeconds;
        monthTotalSeconds += currentTotalSeconds;
        saveMonthTotalSeconds();
        //如果新增60min则加一个view
        if (canAddBubble(pre, monthTotalSeconds)) {
            add();
        }
    }

    //判断是否可以需要增加气泡
    private boolean canAddBubble(long pre, long cur) {
        //一个气泡价值多少秒，一般设置为60*60（一小时）
        int ONE_BUBBLE = 3;
        long cur_bubble = cur / ONE_BUBBLE;
        long pre_bubble = pre / ONE_BUBBLE;
        return cur_bubble > pre_bubble;
    }
}