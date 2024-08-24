package com.bbyy.weeat.ui.page.fragment;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.api.APIs;
import com.bbyy.weeat.models.api.OkHttpExample;
import com.bbyy.weeat.models.bean.WhitelistItem;
import com.bbyy.weeat.databinding.FragmentSettingBinding;
import com.bbyy.weeat.models.config.Const;
import com.bbyy.weeat.utils.DefaultInterface;
import com.bbyy.weeat.utils.ViewUtils;
import com.bbyy.weeat.viewModels.SettingViewModel;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {

    private SettingViewModel viewModel;
    private FragmentSettingBinding binding;
    private Handler handler;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_setting,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler=new Handler();
        viewModel=new ViewModelProvider(this).get(SettingViewModel.class);
        binding.pay.setOnClickListener(v -> {
            PayFragment dialog=PayFragment.newInstance();
            dialog.show(requireActivity().getSupportFragmentManager(), "null");
        });
        binding.home.setOnClickListener(v -> openHomePage());
        binding.clear.setOnClickListener(v -> ViewUtils.showDialog(requireActivity(), (content, timestamp) -> {
            viewModel.clearAll();
            ViewUtils.showSnackBar(v);
        }, ViewUtils.DIALOG_TYPE.DELETE));
        binding.github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGithubPage();
            }
        });
        binding.host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.showEditDialog(requireContext(), new DefaultInterface.OnDialogConfirmListener() {
                    @Override
                    public void onConfirm(String content, long timestamp) {
                        APIs.getInstance().setBaseUrl(content);
                    }
                }, ViewUtils.DIALOG_TYPE.TODO,0L);
            }
        });
        binding.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.showEditDialog(requireContext(), new DefaultInterface.OnDialogConfirmListener() {
                    @Override
                    public void onConfirm(String content, long timestamp) {new Thread(postUserInfoRunnable(content)).start();
                    }
                }, ViewUtils.DIALOG_TYPE.TODO,0L);
            }
        });
    }

    private void openHomePage() {
        Uri uri = Uri.parse("https://weeat-ai.vercel.app/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void openGithubPage(){
        Uri uri = Uri.parse("https://github.com/Renaissance0412/WeEat");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void adjustWhiteList(){
        PackageManager packageManager = requireActivity().getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        List<WhitelistItem> infos=new ArrayList<>();
        for (ApplicationInfo app : apps) {
            if((app.flags&ApplicationInfo.FLAG_SYSTEM)==0){
                WhitelistItem item=new WhitelistItem(app.loadLabel(packageManager).toString(),app.packageName,app.loadIcon(packageManager));
                infos.add(item);
            }
        }
        handler.post(() -> ViewUtils.showWhitelistDialog(requireActivity(), (content, timestamp) -> {
        },infos));
    }

    public boolean notSystemApp(String packageName){
        List<String> system_tags=new ArrayList<>();
        system_tags.add("com.android");
        system_tags.add("com.miui");
        system_tags.add("com.google");
        system_tags.add("com.xiaomi");
        system_tags.add("com.qualcomm");
        for(String tag:system_tags){
            if(packageName.contains(tag)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
        handler.removeCallbacksAndMessages(null);
    }

    public Runnable postUserInfoRunnable(String userInfo){
        return (new Runnable() {
            @Override
            public void run() {
                OkHttpExample.postUserInfo(userInfo,new OkHttpExample.onLineListener() {
                    @Override
                    public void onNewLine(String line) {
                        Const.setUser_id(line);
                    }
                });
            }
        });
    }
}