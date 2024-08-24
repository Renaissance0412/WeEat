package com.bbyy.weeat.utils;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.util.Objects;

/**
 * <pre>
 *     author: wy
 *     desc  : 通过resId获得drawable工具类
 * </pre>
 */
public class Res {
    public static Drawable getDrawable(int resId) {
        return Objects.requireNonNull(ContextCompat.getDrawable(Utils.getApp(), resId));
    }
}
