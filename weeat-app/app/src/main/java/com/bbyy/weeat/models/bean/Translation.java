package com.bbyy.weeat.models.bean;

import android.util.Log;

/**
 * <pre>
 *     author: wy
 *     desc  : test retrofit api
 * </pre>
 */
public class Translation {
    private int status;

    private content content;
    private static class content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;
    }

    //定义 输出返回数据 的方法
    public void show() {
        Log.d("test",""+status);
        Log.d("test",content.from);
        Log.d("test",content.to);
        Log.d("test",content.vendor);
        Log.d("test",content.out);
        Log.d("test",""+content.errNo);
    }
}
