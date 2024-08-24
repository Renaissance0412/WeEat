package com.bbyy.weeat.models.bean.response;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class UserInfo {
    public UserInfo(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;
}
