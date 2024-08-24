package com.bbyy.weeat.models.bean.event;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class GptMessageEvent {
    public GptMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    private String message;

}
