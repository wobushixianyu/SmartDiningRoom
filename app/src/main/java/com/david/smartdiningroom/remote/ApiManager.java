package com.david.smartdiningroom.remote;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Flowable;

public class ApiManager {

    private SmartService smartService;

    public ApiManager(){
        smartService = SmartServiceFactory.buildTipService();
    }

    public Flowable<JsonObject> getStoreList(@NonNull Map<String,Object> params){
        return smartService.getStoreList(params);
    }
}
