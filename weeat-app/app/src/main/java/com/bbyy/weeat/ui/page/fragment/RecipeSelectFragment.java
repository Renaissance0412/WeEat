package com.bbyy.weeat.ui.page.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbyy.weeat.R;
import com.bbyy.weeat.databinding.FragmentTagBinding;
import com.bbyy.weeat.ui.page.adapter.TagAdapter;
import com.bbyy.weeat.utils.DefaultInterface;
import com.bbyy.weeat.viewModels.ClockViewModel;
import com.bbyy.weeat.viewModels.StorageViewModel;

import java.util.Objects;

public class RecipeSelectFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private FragmentTagBinding binding;
    private ClockViewModel viewModel;
    private int current_position=0;
    public RecipeSelectFragment() {
    }

    public static RecipeSelectFragment newInstance(int param1) {
        RecipeSelectFragment fragment = new RecipeSelectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            current_position = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_tag,container,false);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        viewModel=new ViewModelProvider(requireParentFragment()).get(ClockViewModel.class);
        TagAdapter tagAdapter=new TagAdapter(viewModel.getRecipeNames(), new DefaultInterface.OnListClickListener() {
            @Override
            public void onClick(String name) {}
            @Override
            public void onClick(int position) {
                viewModel.setPosition(position);
                dismiss();
            }
        },current_position);
        binding.tags.setAdapter(tagAdapter);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        binding.tags.setLayoutManager(manager);
    }
}