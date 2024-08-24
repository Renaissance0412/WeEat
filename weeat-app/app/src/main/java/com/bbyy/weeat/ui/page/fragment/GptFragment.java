package com.bbyy.weeat.ui.page.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbyy.weeat.R;
import com.bbyy.weeat.databinding.FragmentGptBinding;
import com.bbyy.weeat.models.bean.event.GptMessageEvent;
import com.bbyy.weeat.viewModels.GptViewModel;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class GptFragment extends Fragment {

    private GptViewModel mViewModel;
    private FragmentGptBinding binding;
    private List<Fragment> fragmentList;
    private FragmentStateAdapter fragmentAdapter;

    public static GptFragment newInstance() {
        return new GptFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_gpt,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GptViewModel.class);
        setViewPager();
        setTabLayout();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(GptMessageEvent event){
        Log.d("test"," gpt event ");
        binding.viewpager.setCurrentItem(0);
    }

    public void setViewPager(){
        fragmentList = new ArrayList<>();
        fragmentList.add(TalkFragment.newInstance());
        fragmentList.add(ClockFragment.newInstance());
        fragmentList.add(TodoListFragment.newInstance());
        fragmentAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }
            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };
        binding.viewpager.setAdapter(fragmentAdapter);
        binding.viewpager.setCurrentItem(0);
    }

    private void setTabLayout() {
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabs.selectTab(binding.tabs.getTabAt(position));
            }
        });
    }
}