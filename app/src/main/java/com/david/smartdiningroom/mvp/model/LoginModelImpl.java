package com.david.smartdiningroom.mvp.model;

import android.content.Context;

import com.david.smartdiningroom.mvp.bean.UserBean;
import com.david.smartdiningroom.utils.DBUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LoginModelImpl implements LoginModel{

    @Override
    public void login(final Context context, final String uName, final String uPwd, final onLoginListener onLoginListener) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                DBUtils dbUtils = DBUtils.getInstance(context);
                boolean isExist = dbUtils.queryIsExist(uName);
                dbUtils.closeDatabase();//关闭数据库，只需在判断是否存在的时候才调用
                if (isExist){
                    boolean success = dbUtils.queryAccountInfo(uName, uPwd);
                    if (success){
                        emitter.onNext(1000);//登录成功
                    }else {
                        emitter.onNext(1001);//密码错误
                    }
                }else {
                    emitter.onNext(1009);//账号不存在
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Timber.i("======>loginCode:"+integer);
                switch (integer){
                    case 1000:
                        onLoginListener.loginSuccess(new UserBean(1,"橘猫","男",22,12,17));
                        break;
                    case 1001:
                        onLoginListener.loginFailed("用户名或者密码错误");
                        break;
                    case 1009:
                        onLoginListener.loginFailed("账号不存在");
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
