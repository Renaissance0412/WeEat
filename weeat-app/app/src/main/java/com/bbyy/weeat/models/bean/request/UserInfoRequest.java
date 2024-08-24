package com.bbyy.weeat.models.bean.request;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class UserInfoRequest {
    private String username;
    private String token;
    private String interests;

    public UserInfoRequest() {
    }

    public UserInfoRequest(String username, String token, String interests) {
        this.username = username;
        this.token = token;
        this.interests = interests;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }
}