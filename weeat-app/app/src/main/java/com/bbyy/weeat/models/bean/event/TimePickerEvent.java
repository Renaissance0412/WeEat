package com.bbyy.weeat.models.bean.event;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class TimePickerEvent {
    private long time;

    public TimePickerEvent(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
