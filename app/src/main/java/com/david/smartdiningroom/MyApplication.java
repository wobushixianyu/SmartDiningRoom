package com.david.smartdiningroom;

import android.app.Application;

import com.david.smartdiningroom.utils.AppManager;
import com.orhanobut.hawk.Hawk;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class MyApplication extends Application{
    private static WeakReference<MyApplication> mApplication;

    public static MyApplication getInstance(){
        return mApplication.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = new WeakReference<>(this);
        AppManager.getInstance().init(mApplication.get());
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());

        Hawk.init(this).build();
    }
}
