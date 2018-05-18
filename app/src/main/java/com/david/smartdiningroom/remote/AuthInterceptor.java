package com.david.smartdiningroom.remote;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        Request newRequest;

        if (oldRequest.url().toString().contains("?")){
            newRequest = oldRequest.newBuilder()
                    .url(oldRequest.url().toString())
                    .build();
        }else {
            newRequest = oldRequest.newBuilder()
                    .url(oldRequest.url().toString())
                    .build();
        }

        System.out.println("======>url:"+newRequest.url());

        return chain.proceed(newRequest);
    }
}
