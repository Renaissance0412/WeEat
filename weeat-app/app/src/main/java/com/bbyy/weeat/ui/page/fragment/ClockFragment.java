package com.bbyy.weeat.ui.page.fragment;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.event.TagSelectedEvent;
import com.bbyy.weeat.models.bean.event.TimePickerEvent;
import com.bbyy.weeat.databinding.FragmentClockBinding;
import com.bbyy.weeat.utils.ViewUtils;
import com.bbyy.weeat.viewModels.ClockViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ClockFragment extends Fragment {
    private final int[] imgs = {
            R.drawable.hamburger
    };
    private final int[] wavs = {
            R.raw.usagi_ha,
            R.raw.usagi_pulu
    };
    private final int BUBBLE_SIZE=150;
    private SoundPool soundPool;
    private ClockViewModel viewModel;
    private FragmentClockBinding binding;
    private final SensorEventListener listerner = new SensorEventListener(){

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                float x =  event.values[0];
                float y =   event.values[1] * 2.0f;
                binding.bubble.getBubble().onSensorChanged(-x,y);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private SensorManager sensorManager;
    private Sensor defaultSensor;
    private FragmentManager fragmentManager;
    private Handler handler;
    private int MAX=20;
    private int progress=0;
    private int current_position=0;

    private ClockFragment(){}

    public static ClockFragment newInstance() {
        ClockFragment clockFragment=new ClockFragment();
        return clockFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_clock,container,false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler=new Handler();
        if(getActivity()!=null){
            fragmentManager= getActivity().getSupportFragmentManager();
        }
        viewModel=new ViewModelProvider(requireActivity()).get(ClockViewModel.class);
        //注意这里viewmodel的owner是activity,不然mainactivity监听不到，无法启动service
        binding.setData(viewModel);
        binding.setLifecycleOwner(this);
        MAX= ViewUtils.getMaxIcons(requireActivity().getResources().getDisplayMetrics(), BUBBLE_SIZE);
        Log.d("bubble","Max"+MAX);
        if(soundPool==null){
            soundPool=new SoundPool.Builder().build();
        }
        // 加载音频文件,这里提前加载，不然会来不及播放
        int soundId = soundPool.load(requireActivity(), wavs[1], 1);
        int initBubbleNumber=viewModel.getNumber().getValue();
        //超过上限则显示上限
        initBubbles(Math.min(initBubbleNumber, MAX));
        viewModel.getNumber().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer<MAX&&integer>0&&integer!=initBubbleNumber) {
                    //注意这里手动判断了integer是否等于viewmodel中初始的number，否则初始会走一遍addimage逻辑
                    Log.d("bubble","add"+integer);
                    addImage();
                }
            }
        });
        if(getArguments()!=null){
            viewModel.getTotal_seconds().setValue(getArguments().getLong("time",viewModel.getTotal_seconds().getValue()));
        }
        viewModel.getRest_seconds().observe(getViewLifecycleOwner(), aLong -> {
            if(aLong== 0L){
                viewModel.complete();
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        });
        viewModel.isStarted().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                //binding.tag.setClickable(false);
                binding.time.setClickable(false);
            }else{
                //binding.tag.setClickable(true);
                binding.time.setClickable(true);
            }
        });
        binding.time.setOnClickListener(v -> {
            PickerFragment.newInstance(viewModel.getRest_seconds().getValue()).show(fragmentManager,null);
        });
        binding.tag.setOnClickListener(v -> {
            RecipeSelectFragment fragment=RecipeSelectFragment.newInstance(viewModel.getPosition().getValue());
            fragment.show(getChildFragmentManager(),null);
        });
        binding.tip.setOnTouchListener((v, event) -> {
            Runnable progressRunnable=new Runnable() {
                @Override
                public void run() {
                    while (progress < 100) {
                        progress += 2;
                        // 更新 ProgressBar 的进度
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.progressBar.setProgress(progress);
                            }
                        });
                        try {
                            Thread.sleep(50); // 模拟进度增长的时间间隔
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewModel.restart();
                                    progress=0;
                                }
                            });
                            return;
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.end();
                        }
                    });
                    progress=0;
                }
            };
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // 按下时启动进度更新任务
                viewModel.pause();
                new Thread(progressRunnable).start();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                // 抬起时取消进度更新任务
                viewModel.restart();
                progress=0;
                handler.removeCallbacks(progressRunnable);
            }
            return false;
        });
        // 初始化加速度传感器
        sensorManager = (SensorManager) requireActivity().getSystemService(requireActivity().SENSOR_SERVICE);
        defaultSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(listerner, defaultSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listerner);
        viewModel.save();
    }

    private void initBubbles(int num) {
        while(num-->0){
            addImage();
        }
    }

    private void addImage() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(BUBBLE_SIZE,BUBBLE_SIZE);
        layoutParams.gravity = Gravity.CENTER;
        ImageView imageView = new ImageView(requireActivity());
        imageView.setImageResource(imgs[0]);
        imageView.setTag(R.id.bubble_view_circle_tag,false);
        binding.bubble.addView(imageView,layoutParams);
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.bubble.getBubble().onStart();
        Log.d("test"," child bubble number "+binding.bubble.getChildCount());
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.bubble.getBubble().onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(TimePickerEvent event) {
        viewModel.getRest_seconds().setValue(event.getTime());
    }
}