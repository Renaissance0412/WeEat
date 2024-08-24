package com.bbyy.weeat.ui.page.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.databinding.FragmentStorageBinding;
import com.bbyy.weeat.ui.page.adapter.StorageAdapter;
import com.bbyy.weeat.utils.DefaultInterface;
import com.bbyy.weeat.ui.widget.CountdownFloatingWindowService;
import com.bbyy.weeat.viewModels.StorageViewModel;
import com.bbyy.weeat.viewModels.TalkViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class StorageFragment extends Fragment {

    private StorageViewModel mViewModel;
    private FragmentStorageBinding binding;
    private StorageAdapter adapter;

    public static StorageFragment newInstance() {
        return new StorageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_storage,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(StorageViewModel.class);
        GridLayoutManager layoutManager=new GridLayoutManager(requireActivity(),2);
        binding.storage.setLayoutManager(layoutManager);
        mViewModel.getPosition().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==0)
                    binding.place.setText("My Fridge");
                else
                    binding.place.setText("My Room");
            }
        });
        adapter=new StorageAdapter(mViewModel, new DefaultInterface.OnListClickListener() {
            @Override
            public void onClick(String name) {}
            @Override
            public void onClick(int position) {
                StorageInfoFragment.newInstance(mViewModel.getStorageList().getValue().get(position),position).show(getChildFragmentManager(),"null");
            }
        });
        mViewModel.getTabList().observe(getViewLifecycleOwner(), new Observer<List<StorageItem>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<StorageItem> storageItems) {
                adapter.submitList(storageItems);
                Log.d("storage","tab list "+storageItems.size());
                adapter.notifyDataSetChanged();
            }
        });
        mViewModel.getStorageList().observe(getViewLifecycleOwner(), new Observer<List<StorageItem>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<StorageItem> storageItems) {
                adapter.notifyDataSetChanged();
            }
        });
        adapter.submitList(mViewModel.getStorageList().getValue());
        Log.d("storage","storage list "+mViewModel.getStorageList().getValue().size());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertStorageFragment.newInstance().show(getChildFragmentManager(),"null");
//                if (Settings.canDrawOverlays(requireActivity())) {//检测是否具有悬浮窗权限
//                    requireActivity().startService(new Intent(requireActivity(), CountdownFloatingWindowService.class));
//                } else {
//                    requireActivity().startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
//                }
            }
        });
        binding.storage.setAdapter(adapter);
        List<String> tags=new ArrayList<>();
        tags.add("My fridge");
        tags.add("My room");
        binding.placeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagFragment.newInstance(mViewModel.getPosition().getValue(),tags).show(getChildFragmentManager(),"null");
            }
        });
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewModel.changeTabList(tab.getPosition());
                adapter.submitList(mViewModel.getTabList().getValue());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}