package com.bbyy.weeat.ui.page.fragment;

import static android.app.Activity.RESULT_OK;
import static com.bbyy.weeat.models.config.Const.REQUEST_IMAGE_CAPTURE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bbyy.weeat.R;
import com.bbyy.weeat.databinding.FragmentInsertStorageBinding;
import com.bbyy.weeat.models.bean.StorageItem;
import com.bbyy.weeat.models.bean.TalkItem;
import com.bbyy.weeat.models.bean.event.GptMessageEvent;
import com.bbyy.weeat.models.config.Const;
import com.bbyy.weeat.utils.DateUtils;
import com.bbyy.weeat.utils.DefaultInterface;
import com.bbyy.weeat.utils.ViewUtils;
import com.bbyy.weeat.viewModels.StorageViewModel;
import com.bbyy.weeat.viewModels.TalkViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class InsertStorageFragment extends DialogFragment {
    private FragmentInsertStorageBinding binding;
    private TalkViewModel talkViewModel;
    private StorageViewModel storageViewModel;
    private String imagePath;
    private Bitmap bitmap;
    private String name;
    private String description;
    private long remainDays;
    private String amount;
    private int quantity = 0;
    private long ddl;

    public InsertStorageFragment() {
    }

    public static InsertStorageFragment newInstance() {
        return new InsertStorageFragment();
    }

    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //只有把window的背景设为透明，对话框圆角样式才起效
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_insert_storage, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        talkViewModel = new ViewModelProvider(requireActivity()).get(TalkViewModel.class);
        storageViewModel=new ViewModelProvider(requireParentFragment()).get(StorageViewModel.class);
        binding.ddl.setText(DateUtils.getDateString(System.currentTimeMillis()));
        binding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = binding.amount.getText().toString();
                description = binding.descrption.getText().toString();
                if (isItemValid()) {
                    savePhoto();
                    addItem();
                    dismiss();
                }
            }
        });
        binding.editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.showDatePickerDialog(requireActivity(), new DefaultInterface.OnDialogConfirmListener() {
                    @Override
                    public void onConfirm(String content, long timestamp) {
                        binding.ddl.setText(DateUtils.getDateString(timestamp));
                        ddl = timestamp;
                        remainDays = DateUtils.getDurationDays(timestamp);
                        binding.deadline.setText(DateUtils.getRemainDayString(remainDays));
                    }
                });
            }
        });
//        binding.gpt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EventBus.getDefault().post(new GptMessageEvent(Const.STORAGE_MESSAGE));
//                talkViewModel.addItem(new TalkItem(talkViewModel.getDataListSize(), Const.STORAGE_MESSAGE, null, true));
//                switchGptFragment();
//                dismiss();
//            }
//        });
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 这里需要获取拍照成功回传的图片
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 1);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 检查并清理可能存在的 Bitmap 资源
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public void switchGptFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        //FragmentTransaction transaction=fragmentManager.beginTransaction();
        //fragment容器
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.fragmentContainerView);
        //导航控制器
        NavController navController = navHostFragment.getNavController();
        navController.navigate(R.id.gpt);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            Glide.with(this).asBitmap()
                    .load(bitmap)
                    .into(binding.image);
        }
    }

    private boolean isItemValid() {
        if (bitmap == null) {
            Snackbar.make(requireView(), "Please take a photo!", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        name=binding.name.getText().toString();
        if(name==null){
            Snackbar.make(requireView(), "Please enter the name!", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        amount=binding.amount.getText().toString();
        try {
            quantity = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            Snackbar.make(requireView(), "Please enter a valid number!", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void savePhoto() {
        File folder = new File(Const.IMAGE_PATH);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
        File file = new File(folder, System.currentTimeMillis()+name);
        imagePath=file.getAbsolutePath();
        Glide.with(this)
                .asBitmap()
                .load(bitmap)
                .transform(new RoundedCorners(40))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            // 使用 JPEG 格式压缩，质量为 60
                            resource.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                            //fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void addItem() {
        StorageItem item = new StorageItem(ddl, name, description, imagePath, quantity);
        storageViewModel.addItem(item);
    }
}
