package com.bbyy.weeat.utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class DefaultInterface {
    public interface OnDialogConfirmListener {
        void onConfirm(String content,long timestamp);
    }

    public interface OnListClickListener {
        void onClick(String name);
        void onClick(int position);
    }
}
