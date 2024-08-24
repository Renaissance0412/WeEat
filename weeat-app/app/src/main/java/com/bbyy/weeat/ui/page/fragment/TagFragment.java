package com.bbyy.weeat.ui.page.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.event.TagSelectedEvent;
import com.bbyy.weeat.databinding.FragmentTagBinding;
import com.bbyy.weeat.ui.page.adapter.TagAdapter;
import com.bbyy.weeat.utils.DefaultInterface;
import com.bbyy.weeat.viewModels.StorageViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagFragment extends DialogFragment {
    private FragmentTagBinding binding;
    private StorageViewModel viewModel;
    private int current_position=0;
    private List<String> tags;
    public TagFragment() {
    }
    public static TagFragment newInstance(int position,List<String> tags) {
        TagFragment fragment = new TagFragment();
        fragment.current_position=position;
        fragment.tags=tags;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_tag,container,false);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        viewModel=new ViewModelProvider(requireParentFragment()).get(StorageViewModel.class);
        TagAdapter tagAdapter=new TagAdapter(tags, new DefaultInterface.OnListClickListener() {
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