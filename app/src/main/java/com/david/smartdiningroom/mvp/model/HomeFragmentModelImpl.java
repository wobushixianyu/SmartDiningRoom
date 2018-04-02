package com.david.smartdiningroom.mvp.model;

import android.content.Context;

import com.david.smartdiningroom.utils.SdrUtils;
import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragmentModelImpl implements HomeFragmentModel {

    @Override
    public void getHttpData(final Context context, Map<String, Object> params, final OnGetStoreListListener onGetStoreListListener) {

        Observable.create(new ObservableOnSubscribe<JsonObject>() {
            @Override
            public void subscribe(ObservableEmitter<JsonObject> emitter) throws Exception {
                JsonObject jsonObject = SdrUtils.readAssets(context, "storeSalesPage1.txt");
                System.out.println("======>jsonObject:"+jsonObject);
                emitter.onNext(jsonObject);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        onGetStoreListListener.onSuccess(jsonObject);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetStoreListListener.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        onGetStoreListListener.onCompleted();
                    }
                });
    }
}
