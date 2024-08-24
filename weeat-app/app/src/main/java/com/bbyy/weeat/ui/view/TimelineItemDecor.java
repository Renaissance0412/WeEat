package com.bbyy.weeat.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bbyy.weeat.R;

//实现时间轴效果
public class TimelineItemDecor extends RecyclerView.ItemDecoration {
    // 轴点
    private final Paint mPaint;

    // 写左边日期字的画笔(时间)
    private final Paint mPaint1;
    private Paint mPaint2;

    // 左 上偏移长度
    private final int itemView_leftinterval;
    private final int itemView_topinterval;

    // 图标
    // private Bitmap mIcon;
    // 在构造函数里进行绘制的初始化，如画笔属性设置等
    public TimelineItemDecor(Context context) {

        // 轴点画笔
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, R.color.orange));

        // 左边时间文本画笔(蓝色)
        // 此处设置了两只分别设置 时分 & 年月
        mPaint1 = new Paint();
        mPaint1.setColor(ContextCompat.getColor(context, R.color.orange));
        mPaint1.setTextSize(55);

        // 设置自定义字体
        // 从 res/font 中加载自定义字体
        Typeface typeface = ResourcesCompat.getFont(context, R.font.varela_round);
        if (typeface != null) {
            // 创建粗体的 Typeface
            typeface = Typeface.create(typeface, Typeface.BOLD);
        }
        mPaint1.setTypeface(typeface);

        // 轴线画笔
        mPaint2 = new Paint();
        mPaint2.setColor(ContextCompat.getColor(context, R.color.gray_400));

        // 赋值ItemView的左偏移长度（可以理解为时间轴宽度）
        itemView_leftinterval = 250;

        // 赋值ItemView的上偏移长度(0时为item中间)
        itemView_topinterval = 50;
    }
    // 作用:在间隔区域里绘制时光轴线 & 时间文本
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        // 获取RecyclerView的Child view的个数
        int childCount = parent.getChildCount();

        // 遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
        for (int i = 0; i < childCount; i++) {
            // 获取每个Item对象
            View child = parent.getChildAt(i);
            // 绘制轴点
            // 轴点 = 圆 = 圆心(x,y)
//            int centerx = child.getLeft() - itemView_leftinterval / 5;
//            int centery = child.getTop() - itemView_topinterval + (itemView_topinterval + child.getHeight()) / 2;
            int centerx = child.getLeft() - itemView_leftinterval / 5;
            int centery = child.getTop() +  child.getHeight() / 2;
            int circle_radius=16;
            // 绘制轴点圆
            c.drawCircle(centerx, centery, circle_radius, mPaint);
            /**
             * 绘制上半轴线
             */
            // 上端点坐标(x,y)
            float upLine_up_x = centerx;
            float upLine_up_y = child.getTop() - itemView_topinterval;

            // 下端点坐标(x,y)
            float upLine_bottom_x = centerx;
            float upLine_bottom_y = centery - circle_radius;

            //绘制上半部轴线
            c.drawLine(upLine_up_x, upLine_up_y, upLine_bottom_x, upLine_bottom_y, mPaint2);

            /**
             * 绘制下半轴线
             */
            // 上端点坐标(x,y)
            float bottomLine_up_x = centerx;
            float bottom_up_y = centery + circle_radius;

            // 下端点坐标(x,y)
            float bottomLine_bottom_x = centerx;
            float bottomLine_bottom_y = child.getBottom();

            //绘制下半部轴线
            c.drawLine(bottomLine_up_x, bottom_up_y, bottomLine_bottom_x, bottomLine_bottom_y, mPaint2);
            // 绘制左边时间文本
            // 获取每个Item的位置
            // int index = parent.getChildAdapterPosition(child);
            // 设置文本起始坐标
            float Text_x = child.getLeft() - itemView_leftinterval;
            float Text_y = centery+itemView_topinterval/2;

            // 设置日期绘制位置
            c.drawText(child.getTag(R.id.cur_time_test).toString(), Text_x, Text_y, mPaint1);
            //c.drawText(child.getTag(R.id.cur_day_test).toString(), Text_x + 5, Text_y + 20, mPaint2);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 设置ItemView的左 & 上偏移长度px,即此为onDraw()可绘制的区域
        outRect.set(itemView_leftinterval, itemView_topinterval, 0, 0);
    }

    public void drawDashLine(Paint paint,Canvas canvas, int startX, int startY, int endX, int endY) {
        paint.setStyle(Paint.Style.STROKE); // 设置画笔样式为描边
        paint.setStrokeWidth(5); // 设置画笔宽度
        // 创建虚线效果
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{10, 10}, 0);
        paint.setPathEffect(dashPathEffect);
        canvas.drawLine(startX, startY, endX, endY, paint);
    }
}
