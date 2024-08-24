package com.bbyy.weeat.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.List;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class DecimalFormatter extends ValueFormatter {
    private List<String> tags;

    public DecimalFormatter(List<String> tags) {
        this.tags=tags;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return tags.get((int)value);
    }
}
