package com.david.smartdiningroom.mvp.presenter;

import android.content.Context;

import com.david.smartdiningroom.mvp.bean.UserBean;
import com.david.smartdiningroom.mvp.model.LoginModel;
import com.david.smartdiningroom.mvp.model.LoginModelImpl;
import com.david.smartdiningroom.mvp.view.LoginView;


public class LoginPresenter{
    private static LoginPresenter presenter;
    private LoginModelImpl loginModel;
    private LoginView loginView;
    private static Context mContext;

    public static LoginPresenter getInstance(Context context,LoginView loginView){
        mContext = context.getApplicationContext();
        if (presenter == null){
            return presenter = new LoginPresenter(loginView);
        }
        return presenter;
    }

    private LoginPresenter(LoginView loginView){
        this.loginView = loginView;
        loginModel = new LoginModelImpl();
    }

    public void login(){
        loginView.showWaitingDialog();
        loginModel.login(mContext,loginView.getUserName(), loginView.getPassWord(), new LoginModel.onLoginListener() {
            @Override
            public void loginSuccess(UserBean user) {
                loginView.dismissWaitingDialog();
                loginView.showSuccessMsg(user);
            }

            @Override
            public void loginFailed(String msg) {
                loginView.dismissWaitingDialog();
                loginView.showFailedMsg(msg);
            }
        });
    }
}
