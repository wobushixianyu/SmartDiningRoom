package com.david.smartdiningroom.remote;

import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.PATCH;

public class ApiManager {

    private SmartService smartService;

    public ApiManager(){
        smartService = SmartServiceFactory.buildTipService();
    }

    private RequestBody parseJson(@NonNull String json) { // MediaType
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    }

    public Flowable<JsonObject> getStoreList(@NonNull Map<String,Object> params){
        return smartService.getStoreList(params);
    }

    public Flowable<JsonArray> getShopDetails(@NonNull Map<String,Object> params){
        return smartService.getShopDetails(params);
    }

    public Flowable<JsonObject> submitOrder(@NonNull String json){
        return smartService.submitOrder(parseJson(json));
    }

    public Flowable<JsonObject> getOrderList(@NonNull Map<String,Object> params){
        return smartService.getOrderList(params);
    }

    public Flowable<JsonObject> getOrderDetails(@NonNull Map<String,Object> params){
        return smartService.getOrderDetails(params);
    }

    public Flowable<JsonObject> submitEvaluation(@NonNull String json){
        return smartService.submitEvaluation(parseJson(json));
    }

    public Flowable<JsonObject> alterOrderStatus(@NonNull Map<String,Object> params){
        return smartService.alterOrderStatus(params);
    }

    public Flowable<JsonObject> getEvaluationList(@NonNull Map<String,Object> params){
        return smartService.getEvaluationList(params);
    }

    public Flowable<JsonArray> getStatistics(@NonNull Map<String,Object> params){
        return smartService.getStatistics(params);
    }
}
