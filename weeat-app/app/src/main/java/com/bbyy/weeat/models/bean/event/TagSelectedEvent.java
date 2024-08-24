package com.bbyy.weeat.models.bean.event;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class TagSelectedEvent {
    private String tag;
    private int position;

    public TagSelectedEvent(String tag, int position) {
        this.tag = tag;
        this.position = position;
    }

    public String getTag() {
        return tag;
    }

    public int getPosition() {
        return position;
    }

}
