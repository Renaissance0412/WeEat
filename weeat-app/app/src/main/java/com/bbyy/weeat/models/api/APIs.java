package com.bbyy.weeat.models.api;

import com.bbyy.weeat.utils.SharedPreferencesUtil;
import com.bbyy.weeat.utils.Utils;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class APIs {
    public static String BASE_URL=null;
    private static APIs instance;

    public static APIs getInstance(){
        if(instance==null){
            instance=new APIs();
        }
        return instance;
    }

    public String getBaseUrl() {
        if(BASE_URL!=null){
            return BASE_URL;
        }else{
            return SharedPreferencesUtil.getUrl(Utils.getApp());
        }
    }

    public void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
        SharedPreferencesUtil.saveUrl(Utils.getApp(),baseUrl);
    }

    public String getRagChatSync() {
        return getBaseUrl()+"/v1/rag/chat/sync";
    }

    public String getRagChatStream() {
        return getBaseUrl()+"/v1/rag/chat/stream";
    }

    public String getUserInfo(){
        return getBaseUrl()+"/v1/rag/user";
    }

    public String getAsrSync() {
        return getBaseUrl()+"/v1/asr/sync";
    }

    public String getImageSync() {
        return getBaseUrl()+"/v1/recognition";
    }
}
