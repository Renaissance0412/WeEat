package com.bbyy.weeat.ui.view;

import android.content.Context;
import android.widget.TextView;

import com.bbyy.weeat.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class XYMarkerView extends MarkerView {
    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null) + ": " + (int)e.getY()+" min");
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}