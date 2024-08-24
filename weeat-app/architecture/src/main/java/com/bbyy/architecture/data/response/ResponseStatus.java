package com.bbyy.architecture.data.response;

/**
 * <pre>
 *     author: wy
 *     desc  : 响应状态元信息
 * </pre>
 */
public class ResponseStatus {

    private String responseCode = "";
    private boolean success = true;
    private Enum<ResultSource> source = ResultSource.NETWORK;

    public ResponseStatus() {
    }

    public ResponseStatus(String responseCode, boolean success) {
        this.responseCode = responseCode;
        this.success = success;
    }

    public ResponseStatus(String responseCode, boolean success, Enum<ResultSource> source) {
        this(responseCode, success);
        this.source = source;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public Enum<ResultSource> getSource() {
        return source;
    }
}
