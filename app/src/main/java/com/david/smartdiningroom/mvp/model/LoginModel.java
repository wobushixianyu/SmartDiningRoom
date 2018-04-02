package com.david.smartdiningroom.mvp.model;

import android.content.Context;

import com.david.smartdiningroom.mvp.bean.UserBean;

public interface LoginModel {
    void login(Context context, String uName, String uPwd, onLoginListener onLoginListener);

    interface onLoginListener{
        void loginSuccess(UserBean userBean);
        void loginFailed(String msg);
    }
}
