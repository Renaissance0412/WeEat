package com.bbyy.weeat.utils;

/**
 * <pre>
 *     author: wy
 *     desc  : 屏幕参数单位转化工具类
 * </pre>
 */
public class DisplayUtils {
    /**
     * 将px转换为与之相等的dp
     */
    public static int px2dp(float pxValue) {
        final float scale = Utils.getApp().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dp转换为与之相等的px
     */
    public static int dp2px(float dipValue) {
        final float scale = Utils.getApp().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px转换为sp
     */
    public static int px2sp(float pxValue) {
        final float fontScale = Utils.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp转换为px
     */
    public static int sp2px(float spValue) {
        final float fontScale = Utils.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
