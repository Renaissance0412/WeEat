package com.bbyy.weeat.models.bean.response;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class Recipe {
    private String name;
    private String desc;
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}