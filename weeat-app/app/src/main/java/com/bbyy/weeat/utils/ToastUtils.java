package com.bbyy.weeat.utils;

import android.widget.Toast;

/**
 * <pre>
 *     author: wy
 *     desc  : 弹toast的工具类，在application的context弹出
 * </pre>
 */
public class ToastUtils {
    public static void showLongToast(String text) {
        Toast.makeText(Utils.getApp().getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(String text) {
        Toast.makeText(Utils.getApp().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
