package com.david.smartdiningroom.remote;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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

    public Flowable<JsonObject> alterOrderStatus(@NonNull String json){
        return smartService.alterOrderStatus(parseJson(json));
    }
}
