package com.bbyy.weeat.models.bean.request;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class RagChatSyncRequest {
    public RagChatSyncRequest(String user_id, String context_id, String message) {
        this.user_id = user_id;
        this.context_id = context_id;
        this.message = message;
    }
    public RagChatSyncRequest(String user_id, String context_id, String message, String addition) {
        this.user_id = user_id;
        this.context_id = context_id;
        this.message = message;
        this.addition = addition;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContext_id() {
        return context_id;
    }

    public void setContext_id(String context_id) {
        this.context_id = context_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    private String user_id;
    private String context_id;
    private String message;
    private String addition;
}
