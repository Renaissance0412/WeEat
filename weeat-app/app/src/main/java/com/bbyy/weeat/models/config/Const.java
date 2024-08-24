package com.bbyy.weeat.models.config;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.provider.Settings;

import com.bbyy.weeat.utils.SharedPreferencesUtil;
import com.bbyy.weeat.utils.Utils;
import com.bbyy.weeat.R;

import java.io.File;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class Const {
    public static String getUser_id() {
        if(user_id==null)
            setUser_id(SharedPreferencesUtil.getUserId(Utils.getApp()));
        return user_id;
    }

    public static void setUser_id(String user_id) {
        Const.user_id = user_id;
        SharedPreferencesUtil.saveUserId(Utils.getApp(),user_id);
    }

    private static String user_id=null;
    public static final String MEDIA_CACHE_PATH = Utils.getApp().getExternalCacheDir().getAbsolutePath()+"/Media/";
    public static final String IMAGE_CACHE_PATH = Utils.getApp().getExternalCacheDir().getAbsolutePath()+"/Image/";
    public static final String IMAGE_PATH=Utils.getApp().getExternalFilesDir("Image").getAbsolutePath();
    @SuppressLint("HardwareIds")
    public static final String ANDROID_ID= Settings.Secure.getString(Utils.getApp().getContentResolver(), Settings.Secure.ANDROID_ID);
    public static final String STORAGE_MESSAGE=Utils.getApp().getString(R.string.message_storage);
    public static final int REQUEST_IMAGE_CAPTURE = 1;
}
