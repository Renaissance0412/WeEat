package com.bbyy.weeat.repositories;

import android.util.Log;

import com.bbyy.weeat.models.api.APIs;
import com.bbyy.weeat.models.bean.Translation;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * <pre>
 *     author: wy
 *     desc  :
 * </pre>
 */
public class DataRepository {
    private static final DataRepository S_REQUEST_MANAGER = new DataRepository();

    private DataRepository() {
    }

    public static DataRepository getInstance() {
        return S_REQUEST_MANAGER;
    }

    private final Retrofit retrofit;

    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(APIs.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * TODO 模拟网络请求
     */
    public void test() {
        // 使用 retrofit 或任意你喜欢的库实现网络请求。此处以 retrofit 写个简单例子，
        // 并且如使用 rxjava，还可额外依赖 RxJavaCallAdapterFactory 来简化编写，具体自行网上查阅，此处不做累述，
        // 创建 网络请求接口 的实例
        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

        //对 发送请求 进行封装
        Call<Translation> call = request.getCall();

        //发送网络请求(异步)
        call.enqueue(new Callback<Translation>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                // 处理返回的数据结果
                response.body().show();
                Log.d("test",response.body().toString());
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Translation> call, Throwable throwable) {
                Log.d("test","连接失败");
            }
        });
    }

    public interface GetRequest_Interface {
        @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
        Call<Translation> getCall();
        // 注解里传入 网络请求 的部分URL地址
        // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
        // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
        // getCall()是接受网络请求数据的方法
    }
}
