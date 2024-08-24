package com.bbyy.weeat.utils;

import android.content.Context;
import android.graphics.Color;

import com.bbyy.weeat.ui.view.XYMarkerView;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class ChartUtils {
    /**
     * 初始化水平柱形图图控件属性
     */
    public static void initHBarChart(Context context, HorizontalBarChart chart) {
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        List<String> tags=new ArrayList<>();
        tags.add("Salad");
        tags.add("Ice cream");
        tags.add("Orange juice");
        tags.add("French fries");
        chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        XYMarkerView mv = new XYMarkerView(context, new DecimalFormatter(tags));
        mv.setChartView(chart);
        chart.setMarker(mv);

        setHBarChartData(chart);
        chart.setFitBars(true);
        chart.animateY(1000);
    }

    public static void setHBarChartData(HorizontalBarChart chart) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(0, 4));
        yVals1.add(new BarEntry(1, 2));
        yVals1.add(new BarEntry(2, 6));
        yVals1.add(new BarEntry(3, 1));
        BarDataSet set1;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "DataSet 1");
            set1.setDrawIcons(false);
            set1.setColor(Color.parseColor("#EB6C61"));
            set1.setHighLightColor(Color.parseColor("#FFAA15"));    //点击颜色
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setBarWidth(0.5f);
            chart.setData(data);
        }
    }
}
