package com.bbyy.weeat.ui.page.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.event.TimePickerEvent;
import com.bbyy.weeat.ui.view.RulerView;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

//TODO:把activity挪这儿来
public class PickerFragment extends DialogFragment {

    private static final String PICKED_TIME = "picked_time";
    private long time;

    public PickerFragment() {
    }

    public static PickerFragment newInstance(long param1) {
        PickerFragment fragment = new PickerFragment();
        Bundle args = new Bundle();
        args.putLong(PICKED_TIME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            time = getArguments().getLong(PICKED_TIME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //只有把window的背景设为透明，对话框圆角样式才起效
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button finish=view.findViewById(R.id.finish);
        RulerView rulerView=view.findViewById(R.id.ruler2);
        rulerView.setTime(time);
        finish.setOnClickListener(v -> {
            //listener.onPicked(rulerView.getTime());
            EventBus.getDefault().post(new TimePickerEvent(rulerView.getTime()));
            dismiss();  //关闭这个fragment
        });
    }
}