package com.david.smartdiningroom.remote;


import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RxJavaCallAdapter extends CallAdapter.Factory{

    private RxJava2CallAdapterFactory rxFactory = RxJava2CallAdapterFactory.create();
    private Scheduler subscribeOnScheduler;
    private Scheduler observeOnScheduler;

    public RxJavaCallAdapter(Scheduler subscribeOnScheduler,Scheduler observeOnScheduler){
        this.subscribeOnScheduler = subscribeOnScheduler;
        this.observeOnScheduler = observeOnScheduler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        CallAdapter<Flowable<?>, Flowable<?>> callAdapter = (CallAdapter<Flowable<?>, Flowable<?>>) rxFactory.get(returnType, annotations, retrofit);
        return callAdapter != null ? new ThreadCallAdapter(callAdapter) : null;
    }

    private final class ThreadCallAdapter implements CallAdapter<Flowable<?>, Flowable<?>> {
        CallAdapter<Flowable<?>, Flowable<?>> delegateAdapter;

        ThreadCallAdapter(CallAdapter<Flowable<?>, Flowable<?>> delegateAdapter) {
            this.delegateAdapter = delegateAdapter;
        }

        @Override
        public Type responseType() {
            return delegateAdapter.responseType();
        }

        @Override
        public Flowable<?> adapt(@NonNull Call<Flowable<?>> call) {
            return delegateAdapter.adapt(call).subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler);
        }
    }
}
