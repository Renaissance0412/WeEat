package com.bbyy.weeat.ui.page.fragment;

import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbyy.weeat.R;
import com.bbyy.weeat.databinding.FragmentSuggestBinding;
import com.bbyy.weeat.viewModels.SuggestViewModel;

public class SuggestFragment extends Fragment {

    private SuggestViewModel mViewModel;
    private FragmentSuggestBinding binding;

    public static SuggestFragment newInstance() {
        return new SuggestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_suggest,container,false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
    }
}