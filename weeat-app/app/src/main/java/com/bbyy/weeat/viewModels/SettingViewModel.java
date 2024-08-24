package com.bbyy.weeat.viewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Set;

public class SettingViewModel extends AndroidViewModel {
    private final SharedPreferences sharedPreferences;

    public SettingViewModel(@NonNull Application application) {
        super(application);
        String USER_INFO = "user_info";
        sharedPreferences=application.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    }

    public void clearAll(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String BUBBLE_NUMBER = "bubble_number";
        editor.putInt(BUBBLE_NUMBER,0);
        editor.apply();
    }

    public void recordWhitelist(Set<String> whitelistedApps){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String WHITELIST = "whitelist_apps";
        editor.putStringSet(WHITELIST, whitelistedApps);
        editor.apply();
    }
}