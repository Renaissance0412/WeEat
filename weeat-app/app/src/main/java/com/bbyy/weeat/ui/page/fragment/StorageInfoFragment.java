package com.bbyy.weeat.ui.page.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.databinding.FragmentStorageInfoBinding;
import com.bbyy.weeat.repositories.StorageRepository;
import com.bbyy.weeat.viewModels.StorageInfoViewModel;
import com.bbyy.weeat.viewModels.StorageViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class StorageInfoFragment extends DialogFragment {

    private final String TAG="StorageInfoFragment";
    private StorageInfoViewModel mViewModel;
    private StorageViewModel storageViewModel;
    private StorageItem item;
    private FragmentStorageInfoBinding binding;
    private int position;

    public StorageInfoFragment(){}

    public StorageInfoFragment(StorageItem item, int position){
        this.item=item;
        this.position=position;
    }

    public static StorageInfoFragment newInstance(StorageItem item,int position) {
        return new StorageInfoFragment(item,position);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //只有把window的背景设为透明，对话框圆角样式才起效
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_storage_info,container,false);
        if(savedInstanceState!=null){
            item=savedInstanceState.getParcelable(TAG);
        }
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        storageViewModel=new ViewModelProvider(this).get(StorageViewModel.class);
        Glide.with(this)
                .load(item.getImagePath())
                .transform(new RoundedCorners(40))
                .into(binding.image);
        binding.name.setText(item.getFoodName());
        if(item.getRemainDays()==1||item.getRemainDays()==-1){
            binding.deadline.setText(item.getRemainDays()+" day");
        }else{
            binding.deadline.setText(item.getRemainDays()+" days");
        }
        binding.amount.setText(""+item.getQuantity());
        binding.description.setText(item.getDescription());
        if(item.isExpired())
            binding.finish.setVisibility(View.GONE);
        binding.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Recorded in timeline!!", Toast.LENGTH_SHORT).show();
                item.setCompletionTime(System.currentTimeMillis());
                storageViewModel.updateItem(position,item);
                dismiss();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TAG,item);
    }
}