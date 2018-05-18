package com.david.smartdiningroom.remote;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;

public interface SmartService {

    @GET("shopList")
    Flowable<JsonObject> getStoreList(@QueryMap Map<String, Object> params);

    @GET("shopDeatils")
    Flowable<JsonArray> getShopDetails(@QueryMap Map<String, Object> params);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("submitOrder")
    Flowable<JsonObject> submitOrder(@Body RequestBody params);

    @GET("orderList")
    Flowable<JsonObject> getOrderList(@QueryMap Map<String, Object> params);

    @GET("orderDetails")
    Flowable<JsonObject> getOrderDetails(@QueryMap Map<String, Object> params);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("book/bangumi")
    Flowable<JsonObject> alterOrderStatus(@Body RequestBody params);
}
