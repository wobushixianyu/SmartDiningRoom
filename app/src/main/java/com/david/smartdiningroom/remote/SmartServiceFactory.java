package com.david.smartdiningroom.remote;

import com.david.smartdiningroom.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class SmartServiceFactory {


    public static SmartService buildTipService(){
        Timber.i("======>BaseUrl:" + BuildConfig.BASE_URL);
        OkHttpClient okHttpClient = makeOkHttpClient();
        return makeTipService(okHttpClient);
    }

    private static OkHttpClient makeOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .connectTimeout(20, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).writeTimeout(15, TimeUnit.SECONDS)
                .proxy(Proxy.NO_PROXY)
                .build();
        return okHttpClient;
    }

    private static SmartService makeTipService(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                //.addConverterFactory(com.david.tiptop.remote.converter.GsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new RxJavaCallAdapter(Schedulers.io(), AndroidSchedulers.mainThread()))
                .build();
        return retrofit.create(SmartService.class);
    }
}
