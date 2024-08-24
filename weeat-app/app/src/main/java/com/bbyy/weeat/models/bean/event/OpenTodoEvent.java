package com.bbyy.weeat.models.bean.event;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class OpenTodoEvent {
    public OpenTodoEvent(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    private String name;
}
