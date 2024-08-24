package com.bbyy.architecture.data.response.manager;

import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;


/**
 * <pre>
 *     author: wy
 *     desc  : 网络状态管理
 * </pre>
 */
public class NetworkStateManager implements DefaultLifecycleObserver {

    private static final NetworkStateManager S_MANAGER = new NetworkStateManager();
    private final NetworkStateReceive mNetworkStateReceive = new NetworkStateReceive();

    private NetworkStateManager() {
    }

    public static NetworkStateManager getInstance() {
        return S_MANAGER;
    }

    //TODO tip：让 NetworkStateManager 可观察页面生命周期，
    //从而在页面失去焦点时及时断开本页面对网络状态的监测，以避免重复回调和一系列不可预期的问题。

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        Utils.getApp().getApplicationContext().registerReceiver(mNetworkStateReceive, filter);
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Utils.getApp().getApplicationContext().unregisterReceiver(mNetworkStateReceive);
    }
}
