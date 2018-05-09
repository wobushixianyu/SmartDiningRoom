package com.david.smartdiningroom.mvp.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.david.smartdiningroom.BaseActivity;
import com.david.smartdiningroom.MainActivity;
import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.UserBean;
import com.david.smartdiningroom.mvp.presenter.LoginPresenter;
import com.david.smartdiningroom.mvp.view.LoginView;
import com.david.smartdiningroom.utils.AppManager;
import com.david.smartdiningroom.utils.ContentsUtils;
import com.david.smartdiningroom.utils.SdrUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.et_name)
    AppCompatEditText mEtName;
    @BindView(R.id.et_pwd)
    AppCompatEditText mEtPwd;
    @BindView(R.id.ll_container)
    LinearLayout mContainer;
    @BindView(R.id.rgp_login)
    RadioGroup mRgpLogin;
    @BindView(R.id.customer_login)
    RadioButton mCustomerLogin;
    @BindView(R.id.seller_login)
    RadioButton mSellerLogin;
    private LoginPresenter mPresenter;
    private Context mContext = this;
    private int loginType = 0; //客户版0，商家版1


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPresenter = LoginPresenter.getInstance(this, this);
        initView();
    }

    private void initView() {
        mRgpLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.customer_login:
                        SdrUtils.showToast(mContext,"客户版");
                        loginType = 0;
                        break;
                    case R.id.seller_login:
                        SdrUtils.showToast(mContext,"商家版");
                        loginType = 1;
                        break;
                }
            }
        });
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
        ContentsUtils.userName = getUserName();
        ContentsUtils.userPwd = getPassWord();
        AppManager.jumpAndFinish(MainActivity.class);
    }

    @Override
    public void showFailedMsg(String msg) {
        Snackbar.make(mContainer,msg,Snackbar.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btn_login, R.id.tv_forgetPwd, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (loginType == 0){
                    mPresenter.login();
                }else {
                    AppManager.jump(SellerMainActivity.class);
                }
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
