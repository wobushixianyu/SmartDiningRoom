package com.david.smartdiningroom.remote;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface SmartService {

    @GET("book/bangumi")
    Flowable<JsonObject> getStoreList(@QueryMap Map<String, Object> params);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("book/bangumi")
    Flowable<JsonObject> alterOrderStatus(@Body RequestBody params);
}
