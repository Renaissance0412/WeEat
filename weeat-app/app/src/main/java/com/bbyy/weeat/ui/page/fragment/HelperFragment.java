package com.bbyy.weeat.ui.page.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbyy.weeat.R;

import java.util.Objects;

public class HelperFragment extends DialogFragment {

    public HelperFragment() {}

    public static HelperFragment newInstance(String param1, String param2) {
        HelperFragment fragment = new HelperFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //只有把window的背景设为透明，对话框圆角样式才起效
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_helper, container, false);
    }
}