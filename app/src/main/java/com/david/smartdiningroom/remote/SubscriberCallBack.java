package com.david.smartdiningroom.remote;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import timber.log.Timber;

public abstract class SubscriberCallBack<T> implements Subscriber<T> {
    // 1：登录；2：不显示Toast；
    private int mSubscriberTag = -1;

    protected SubscriberCallBack() {
    }

    protected SubscriberCallBack(int subscriberTag) {
        this.mSubscriberTag = subscriberTag;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
        Timber.i("======>onSubscribe!");
    }

    @Override
    public void onNext(T t) {
        Timber.i("======>:"+t.toString());
        onSuccess(t);
        onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e);
        onCompleted();
        Timber.e(e);
    }
    @Override
    public void onComplete() {
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(Throwable t);

    public abstract void onCompleted();
}
