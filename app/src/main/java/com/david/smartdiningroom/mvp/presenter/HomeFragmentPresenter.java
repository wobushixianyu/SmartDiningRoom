package com.david.smartdiningroom.mvp.presenter;

import android.content.Context;

import com.david.smartdiningroom.mvp.model.HomeFragmentModel;
import com.david.smartdiningroom.mvp.model.HomeFragmentModelImpl;
import com.david.smartdiningroom.mvp.view.HomeFragmentView;
import com.google.gson.JsonObject;

import java.util.Map;

public class HomeFragmentPresenter {
    private static HomeFragmentPresenter presenter;
    private HomeFragmentView homeFragmentView;
    private final HomeFragmentModelImpl homeFragmentModel;

    public static HomeFragmentPresenter getInstance(HomeFragmentView homeFragmentView){
        if (presenter == null){
            presenter = new HomeFragmentPresenter(homeFragmentView);
        }
        return presenter;
    }

    private HomeFragmentPresenter(final HomeFragmentView homeFragmentView){
        this.homeFragmentView = homeFragmentView;
        homeFragmentModel = new HomeFragmentModelImpl();
    }

    public void getStoreList(Context context, Map<String, Object> params, final boolean isLoadmore){
        homeFragmentView.setupWaitingDialog(true);
        homeFragmentModel.getHttpData(context.getApplicationContext(),params, new HomeFragmentModel.OnGetStoreListListener() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                homeFragmentView.onGetStoreListSuccess(jsonObject,isLoadmore);
            }

            @Override
            public void onError(String msg) {
                homeFragmentView.showMessage(msg);
                homeFragmentView.setupWaitingDialog(false);
            }

            @Override
            public void onCompleted() {
                homeFragmentView.onGetStoreListCompleted();
                homeFragmentView.setupWaitingDialog(false);
            }
        });
    }
}
