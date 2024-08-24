package com.bbyy.weeat.ui.page.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.databinding.FragmentTimelineBinding;
import com.bbyy.weeat.ui.view.TimelineItemDecor;
import com.bbyy.weeat.utils.DateUtils;
import com.bbyy.weeat.viewModels.TimelineViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class TimelineFragment extends Fragment {

    private FragmentTimelineBinding binding;
    private MyAdapter adapter;
    private TimelineViewModel viewModel;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_timeline,container,false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TimelineViewModel.class);
        initView();
        viewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<StorageItem>>() {
            @Override
            public void onChanged(List<StorageItem> storageItems) {
                adapter.notifyDataSetChanged();
            }
        });
        binding.setData(viewModel);
        binding.timeline.setAdapter(adapter);
        int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        binding.week.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.date.setText(DateUtils.getTabDateText(tab.getPosition()));
                viewModel.changeList(tab.getPosition());
                adapter.submitList(viewModel.getList().getValue());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        Objects.requireNonNull(binding.week.getTabAt(currentDayOfWeek - 1)).select();
    }

    public static class MyAdapter extends ListAdapter<StorageItem,MyAdapter.Viewholder> {
        //构造函数，传入数据
        public MyAdapter() {
            super(new DiffUtil.ItemCallback<StorageItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull StorageItem oldItem, @NonNull StorageItem newItem) {
                    return oldItem.getId()==(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull StorageItem oldItem, @NonNull StorageItem newItem) {
                    return false;
                }
            });
        }


        //定义Viewholder
        static class Viewholder extends RecyclerView.ViewHolder  {
            private final View timeBar;
            private final ImageView image;
            private final TextView desc;
            public Viewholder(View root) {
                super(root);
                this.timeBar=root.findViewById(R.id.timeBar);
                this.image=root.findViewById(R.id.image);
                this.desc=root.findViewById(R.id.desc);
            }
        }

        @NonNull
        @Override
        public MyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyAdapter.Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, null));
        }//在这里把ViewHolder绑定Item的布局

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.Viewholder holder, final int position) {
            StorageItem item=getItem(position);
            holder.itemView.setVisibility(View.VISIBLE);
            // 绑定数据到ViewHolder里面
            Glide.with(holder.itemView)
                    .load(item.getImagePath())
                    .transform(new RoundedCorners(40))
                    .into(holder.image);
            holder.desc.setText(item.getFoodName());
            String currentTime= DateUtils.getTimeString(getItem(position).getCompletionTime());
            holder.itemView.setTag(R.id.cur_time_test, currentTime);
        }
    }

    // 绑定数据到RecyclerView
    public void initView(){
        //使用线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        binding.timeline.setLayoutManager(layoutManager);
        binding.timeline.setHasFixedSize(true);

        //用自定义分割线类设置分割线
        binding.timeline.addItemDecoration(new TimelineItemDecor(requireActivity()));
        //为ListView绑定适配器
        adapter = new MyAdapter();
        adapter.submitList(viewModel.getList().getValue());
    }

}