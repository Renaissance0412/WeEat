package com.bbyy.weeat.models.api;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
import android.util.Log;

import com.bbyy.weeat.models.bean.TalkItem;
import com.bbyy.weeat.models.bean.request.RagChatSyncRequest;
import com.bbyy.weeat.models.bean.request.UserInfoRequest;
import com.bbyy.weeat.models.bean.response.AudioResponse;
import com.bbyy.weeat.models.bean.response.ImageUploadResponse;
import com.bbyy.weeat.models.bean.response.Recipe;
import com.bbyy.weeat.models.bean.response.SparkResponse;
import com.bbyy.weeat.models.bean.response.UserInfo;
import com.bbyy.weeat.models.config.Const;
import com.bbyy.weeat.utils.ResponseFormatter;
import com.bumptech.glide.RequestBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OkHttpExample {
    private static Gson gson = new Gson();
    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(15, TimeUnit.SECONDS) // 设置连接超时时间为 15 秒
            .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时时间为 30 秒
            .connectionPool(new ConnectionPool(2,5, TimeUnit.MINUTES))
            .build();
    public interface onLineListener{ void onNewLine(String line);}
    public interface onRecipeListener{ void onNewList(List<Recipe> list);}

    /*
    * 发送文字消息
    * */
    public static void postRagChatSync(String question,String context_id,onLineListener listener,onRecipeListener recipeListener) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(new RagChatSyncRequest(Const.getUser_id(),context_id,question)));
        Request request = new Request.Builder()
                .url(APIs.getInstance().getRagChatStream())
                .addHeader("Connection", "Keep-Alive")
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            // 使用 Source 来处理流式数据
            BufferedSource source = response.body().source();
            boolean json_started=false;
            StringBuilder builder=null;
            while (!source.exhausted()) {
                try {
                    String line = source.readUtf8Line().replaceAll("#","")
                            .replaceAll("=","")
                            .replaceAll("/*","");
                    Log.d("api"," source accept "+line);
                    if(line!=null){
                        if(json_started){
                            if(!line.equals("```")){
                                builder.append(line);
                            }else{
                                json_started=false;
                                //Log.d("api"," recipe origin json "+builder.toString());
                                //Log.d("api"," recipe list string "+parseRecipes(builder.toString()).toString());
                                List<Recipe> list=parseRecipes(builder.toString());
                                if(list!=null){
                                    recipeListener.onNewList(list);
                                }
                            }
                        }else{
                            if(line.equals("```json")){
                                json_started=true;
                                builder=new StringBuilder();
                            }else{
                                listener.onNewLine(line);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void postImageSync(String imagePath, onLineListener listener) {
        try {
            File imageFile = new File(imagePath);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imageFile.getName(), fileBody)
                    .build();
            Request request = new Request.Builder()
                    .url(APIs.getInstance().getImageSync())
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                // 假设响应对象为 AudioResponse
                ImageUploadResponse imageUploadResponse=gson.fromJson(jsonResponse, ImageUploadResponse.class);
                listener.onNewLine(ResponseFormatter.formatResponse(imageUploadResponse));
            }else{
                Log.d("api"," false "+response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void postUserInfo(String userInfo, onLineListener listener) {
        try {
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(new UserInfoRequest(Const.ANDROID_ID,Const.ANDROID_ID,userInfo)));
            Request request = new Request.Builder()
                    .url(APIs.getInstance().getUserInfo())
                    .addHeader("Connection", "Keep-Alive")
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                UserInfo info=gson.fromJson(jsonResponse, UserInfo.class);
                listener.onNewLine(info.getUser_id());
            }else{
                Log.d("api"," false "+response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void postAsrSync(TalkItem item, onLineListener listener) {
        try {
            File audioFile = new File(item.getMediaPath());
            RequestBody fileBody = RequestBody.create(MediaType.parse("audio/*"), audioFile);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("audio", audioFile.getName(), fileBody)
                    .build();
            Request request = new Request.Builder()
                    .url(APIs.getInstance().getAsrSync())
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                // 假设响应对象为 AudioResponse
                AudioResponse audioResponse = new Gson().fromJson(jsonResponse, AudioResponse.class);
                listener.onNewLine(audioResponse.getTranscript());
            }else{
                Log.d("api"," false "+response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Recipe> parseRecipes(String text) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            recipes = new Gson().fromJson(text, new TypeToken<List<Recipe>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return recipes;
    }
}
