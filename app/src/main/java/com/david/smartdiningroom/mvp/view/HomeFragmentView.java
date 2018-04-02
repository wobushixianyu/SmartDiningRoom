package com.david.smartdiningroom.mvp.view;

import com.google.gson.JsonObject;

public interface HomeFragmentView {

    void onGetStoreListSuccess(JsonObject jsonObject,boolean isLoadmore);
    void onGetStoreListCompleted();
    void onGetStoreListError(String msg);

    void showMessage(String msg);
    void setupWaitingDialog(boolean show);
}
