package com.bbyy.weeat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bbyy.weeat.models.config.Const;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class SharedPreferencesUtil {
    private static final String CONTEXT_ID = "context_id";
    private static final String USER_INFO = "user_info";
    private static final String URL = "base_url";
    private static final String USER_ID="user_id";

    public static void incrementValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        long value = sharedPreferences.getLong(CONTEXT_ID, 0);
        value++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(CONTEXT_ID, value);
        editor.apply();
    }

    public static String getUrl(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getString(URL, "");
    }

    public static void saveUrl(Context context,String url) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(URL,url);
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID, Const.ANDROID_ID);
    }

    public static void saveUserId(Context context,String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID,userId);
    }
}
