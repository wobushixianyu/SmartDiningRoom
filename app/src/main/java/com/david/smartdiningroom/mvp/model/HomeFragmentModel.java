package com.david.smartdiningroom.mvp.model;

import android.content.Context;

import com.google.gson.JsonObject;

import java.util.Map;

public interface HomeFragmentModel {

    void getHttpData(Context context, Map<String, Object> params, OnGetStoreListListener onGetStoreListListener);

    interface OnGetStoreListListener{
        void onSuccess(JsonObject jsonObject);
        void onError(String msg);
        void onCompleted();
    }
}
