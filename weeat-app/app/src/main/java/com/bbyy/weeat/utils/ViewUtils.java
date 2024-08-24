package com.bbyy.weeat.utils;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bbyy.weeat.R;
import com.bbyy.weeat.models.bean.WhitelistItem;
import com.bbyy.weeat.ui.page.adapter.WhitelistAdapter;
import com.bbyy.weeat.ui.view.wheel.WheelView;
import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ViewUtils {
    //返回屏幕可容纳的最大icon数目
    public static int getMaxIcons(DisplayMetrics metrics,int image_scale){
        image_scale=pxToDp(metrics,image_scale);
        double width=Math.ceil(metrics.widthPixels/image_scale/metrics.density);
        double height=Math.ceil(metrics.heightPixels/image_scale/metrics.density);
        return (int)(width*height);
    }

    public static void hideSoftKeyboard(View view, boolean forceHide) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (forceHide || imm.isActive(view)) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void showImageDialog(Context context, Bitmap bitmap){
        View view=LayoutInflater.from(context).inflate(R.layout.dialog_image,null,false);
        ImageView dialogImageView = view.findViewById(R.id.image);
        Glide.with(view).asBitmap().load(bitmap).into(dialogImageView);
        MaterialAlertDialogBuilder builder= new MaterialAlertDialogBuilder(context);
        builder.setView(view).show();
    }

    public static void showEditDialog(Context context, final DefaultInterface.OnDialogConfirmListener listener, DIALOG_TYPE type, long timestamp) {
        View view=LayoutInflater.from(context).inflate(R.layout.todo_edit,null,false);
        EditText editText=view.findViewById(R.id.editTextText);
        MaterialAlertDialogBuilder builder= new MaterialAlertDialogBuilder(context);
        //这里不能直接将子view（edittext）传递给setview，否则会报错
        builder.setView(view)
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .setPositiveButton("OK", (dialog, which) -> {
                    if (listener != null) {
                        listener.onConfirm(editText.getText().toString(),timestamp);
                    }
                }).show();
    }

    public static void showWhitelistDialog(Context context, final DefaultInterface.OnDialogConfirmListener listener, List<WhitelistItem> infos) {
        View view=LayoutInflater.from(context).inflate(R.layout.dialog_whitelist,null,false);
        RecyclerView recyclerView=view.findViewById(R.id.apps);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new WhitelistAdapter(infos));
        MaterialAlertDialogBuilder builder= new MaterialAlertDialogBuilder(context);
        builder.setTitle("App lists");
        //这里不能直接将子view（edittext）传递给setview，否则会报错
        builder.setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    //listener.onConfirm(editText.getText().toString(),timestamp);
                }).show();
    }

    public static void showTimePickerDialog(Context context, DefaultInterface.OnDialogConfirmListener listener){
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("选择时间")
                .build();

        timePicker.addOnPositiveButtonClickListener(v -> {
            long timestamp= TimeUtil.getTimestampFromTime(timePicker.getHour(),timePicker.getMinute());
            // 在这里处理选中的时间
            showEditDialog(context,listener,DIALOG_TYPE.TODO,timestamp);
        });

        timePicker.show(((FragmentActivity)context).getSupportFragmentManager(), "timePicker");
    }

    public static void showDatePickerDialog(Context context, DefaultInterface.OnDialogConfirmListener listener){
        Calendar today = Calendar.getInstance();
        // 获取明天的时间戳
        today.add(Calendar.DAY_OF_MONTH, 1);
        long tomorrowInMillis = today.getTimeInMillis();
        // 创建日历约束条件
        CalendarConstraints.Builder constraintsBuilder;
        constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setStart(tomorrowInMillis);
        // 创建日期选择器
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("选择日期")
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                listener.onConfirm("", selection);
            }
        });
        datePicker.show(((FragmentActivity)context).getSupportFragmentManager(), "timePicker");
    }

    public static void showSnackBar(View v){
        Snackbar.make(v,"Success!", Snackbar.LENGTH_SHORT).show();
//                .setAction("Undo", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //
//                    }
//                })
    }

    public static void showDialog(Context context, final DefaultInterface.OnDialogConfirmListener listener, DIALOG_TYPE type) {
        MaterialAlertDialogBuilder builder= new MaterialAlertDialogBuilder(context);
        if (Objects.requireNonNull(type) == DIALOG_TYPE.DELETE) {
            builder.setTitle("Sure you want to delete it?");
        } else {
            builder.setTitle("NULL");
        }
        builder.setNegativeButton("Cancel", (dialog, which) -> {
                })
                .setPositiveButton("OK", (dialog, which) -> {
                    if (listener != null) {
                        listener.onConfirm("",0);
                    }
                }).show();
    }

    public static void showWheelDialog(Context context,final DefaultInterface.OnDialogConfirmListener listener) {
        View view=LayoutInflater.from(context).inflate(R.layout.dialog_wheel,null,false);
        WheelView wheelView = view.findViewById(R.id.wheel);
        List<String> lists = new ArrayList<>();
        for(int i = 2; i <= 5; i++){
            lists.add("" + i);
        }
        wheelView.lists(lists).fontSize(45).showCount(3).selectTip("").select(0).listener(index -> Log.d("cc", "current select:" + wheelView.getSelectItem() + " index :" + index + ",result=" + lists.get(index))).build();
        MaterialAlertDialogBuilder builder= new MaterialAlertDialogBuilder(context);
        builder.setTitle("番茄钟循环");
        //这里不能直接将子view（edittext）传递给setview，否则会报错
        builder.setView(view)
                .setPositiveButton("完成", (dialog, which) -> {
                    //listener.onConfirm(editText.getText().toString(),timestamp);
                }).show();
    }

    public static int pxToDp(DisplayMetrics metrics, float px) {
        float density = metrics.density;
        return (int) (px / density + 0.5f);
    }

    public enum DIALOG_TYPE{
        TODO,
        LIST_NAME,
        DELETE
    }

}
