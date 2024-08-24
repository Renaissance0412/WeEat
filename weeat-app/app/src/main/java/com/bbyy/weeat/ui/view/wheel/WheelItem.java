package com.bbyy.weeat.ui.view.wheel;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class WheelItem {
    // 起点Y坐标、宽度、高度
    private float startY;
    private final int width;
    private final int height;
    //四点坐标
    private final RectF rect = new RectF();
    //字体大小、颜色
    private final int fontColor;
    private final int fontSize;
    private String text;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WheelItem(float startY, int width, int height, int fontColor, int fontSize, String text) {
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.fontColor = fontColor;
        this.fontSize = fontSize;
        this.text = text;
        adjust(0);
    }

    /**
     * 根据Y坐标的变化值，调整四点坐标值
     */
    public void adjust(float dy){
        startY += dy;
        rect.left = 0;
        rect.top = startY;
        rect.right = width;
        rect.bottom = startY + height;
    }

    public float getStartY() {
        return startY;
    }

    /**
     * 直接设置Y坐标属性，调整四点坐标属性
     */
    public void setStartY(float startY) {
        this.startY = startY;
        rect.left = 0;
        rect.top = startY;
        rect.right = width;
        rect.bottom = startY + height;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void onDraw(Canvas mCanvas){
        //设置钢笔属性
        mPaint.setTextSize(fontSize);
        mPaint.setColor(fontColor);
        //得到字体的宽度
        int textWidth = (int)mPaint.measureText(text);
        //drawText的绘制起点是左下角,y轴起点为baseLine
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        int baseLine = (int)(rect.centerY() + (metrics.bottom - metrics.top) / 2 - metrics.bottom);
        //居中绘制
        mCanvas.drawText(text, rect.centerX() - textWidth / 2, baseLine, mPaint);
    }
}