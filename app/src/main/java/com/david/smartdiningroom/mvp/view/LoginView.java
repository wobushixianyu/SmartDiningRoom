package com.david.smartdiningroom.mvp.view;

import com.david.smartdiningroom.mvp.bean.UserBean;

public interface LoginView{
    String getUserName();
    String getPassWord();

    void showWaitingDialog();
    void dismissWaitingDialog();

    void showSuccessMsg(UserBean userBean);
    void showFailedMsg(String msg);
}
