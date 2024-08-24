package com.bbyy.weeat.models.bean;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
/**
 * <pre>
 *     author: wy
 *     desc  : 白名单项
 * </pre>
 */
public class WhitelistItem {
    private String appName;
    private String packageName;
    private Drawable icon;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected;

    public WhitelistItem(String appName, String packageName,Drawable icon) {
        this.appName = appName;
        this.packageName = packageName;
        this.icon=icon;
        this.selected=false;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @NonNull
    @Override
    public String toString() {
        return "WhitelistItem{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
