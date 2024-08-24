package com.bbyy.weeat.ui.view.bubble;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bbyy.weeat.ui.view.bubble.Bubble;

public class BubbleView extends FrameLayout {
    private final Bubble bubble;

    public BubbleView(@NonNull Context context) {
        this(context,null);
    }
    public BubbleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 防止onDraw被跳过
        setWillNotDraw(false);
        bubble=new Bubble(this);
    }

    public Bubble getBubble() {
        return bubble;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        bubble.onLayout(changed);
        Log.d("test"," bubble view onlayout changed="+changed);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bubble.onSizeChanged(w,h);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        bubble.onDraw(canvas);
    }
}
