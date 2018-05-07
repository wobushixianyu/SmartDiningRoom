package com.david.smartdiningroom.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.david.smartdiningroom.BaseActivity;
import com.david.smartdiningroom.MainActivity;
import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.UserBean;
import com.david.smartdiningroom.mvp.presenter.LoginPresenter;
import com.david.smartdiningroom.mvp.view.LoginView;
import com.david.smartdiningroom.utils.AppManager;
import com.david.smartdiningroom.utils.JumpUtils;
import com.david.smartdiningroom.utils.WeakHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.et_name)
    AppCompatEditText mEtName;
    @BindView(R.id.et_pwd)
    AppCompatEditText mEtPwd;
    @BindView(R.id.ll_container)
    LinearLayout mContainer;
    private LoginPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPresenter = LoginPresenter.getInstance(this, this);
    }

    @Override
    public String getUserName() {
        return TextUtils.isEmpty(mEtName.getText().toString()) ? "" : mEtName.getText().toString();
    }

    @Override
    public String getPassWord() {
        return TextUtils.isEmpty(mEtPwd.getText().toString()) ? "" : mEtPwd.getText().toString();
    }

    @Override
    public void showWaitingDialog() {

    }

    @Override
    public void dismissWaitingDialog() {

    }

    @Override
    public void showSuccessMsg(UserBean userBean) {
        AppManager.jumpAndFinish(MainActivity.class);
    }

    @Override
    public void showFailedMsg(String msg) {
//        Snackbar.make(mContainer,msg,Snackbar.LENGTH_SHORT).show();
        AppManager.jumpAndFinish(MainActivity.class);
    }

    @OnClick({R.id.btn_login, R.id.qq_login, R.id.tv_forgetPwd, R.id.weChat_login, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mPresenter.login();
                break;
            case R.id.qq_login:
                break;
            case R.id.weChat_login:
                break;
            case R.id.tv_register:
                AppManager.jump(RegisterActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        Process.killProcess(Process.myPid());
    }
}
