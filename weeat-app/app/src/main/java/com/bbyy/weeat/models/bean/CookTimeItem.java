package com.bbyy.weeat.models.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
@Entity(tableName = "COOK")
public class CookTimeItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String tag;
    private long startTime;
    private long time;
    public CookTimeItem(String tag,long startTime, long time) {
        this.tag=tag;
        this.startTime = startTime;
        this.time=time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
