package com.bbyy.architecture.data.response.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.bbyy.architecture.R;
import com.bbyy.architecture.utils.NetworkUtils;

import java.util.Objects;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class NetworkStateReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (!NetworkUtils.isConnected()) {
                Toast.makeText(context, context.getString(R.string.network_not_good), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
