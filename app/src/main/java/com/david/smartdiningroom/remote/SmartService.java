package com.david.smartdiningroom.remote;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface SmartService {

    @GET("book/bangumi")
    Flowable<JsonObject> getStoreList(@QueryMap Map<String, Object> params);
}
