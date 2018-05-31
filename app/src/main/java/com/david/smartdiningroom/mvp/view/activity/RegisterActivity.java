package com.david.smartdiningroom.mvp.view.activity;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.utils.DBUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_uName)
    AppCompatEditText mEtName;
    @BindView(R.id.et_uPwd)
    AppCompatEditText mEtPwd;
    @BindView(R.id.et_repeat)
    AppCompatEditText mEtRepeat;
    @BindView(R.id.ll_container)
    LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mToolbar.setTitle("新用户注册");
    }

    @OnClick(R.id.btn_register)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_register:
                checkAccount();
                break;
        }
    }

    private void checkAccount() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                if (TextUtils.isEmpty(mEtName.getText()) || TextUtils.isEmpty(mEtPwd.getText()) || TextUtils.isEmpty(mEtRepeat.getText())){
                    emitter.onNext(103);
                }else if (mEtName.getText().toString().length() != 11){
                    emitter.onNext(104);
                } else if (mEtPwd.getText().toString().length()<6){
                    emitter.onNext(105);
                } else {
                    String uName = mEtName.getText().toString();
                    String uPwd = mEtPwd.getText().toString();
                    String repeat = mEtRepeat.getText().toString();
                    if (!uPwd.equals(repeat)){
                            emitter.onNext(102);
                    }else{
                        DBUtils dbUtils = DBUtils.getInstance(RegisterActivity.this);
                        if (dbUtils.queryIsExist(uName)){
                            emitter.onNext(101);
                        }else {
                            dbUtils.insertData(uName,uPwd);
                            emitter.onNext(100);
                        }
                        dbUtils.closeDatabase();//关闭数据库，只需在判断是否存在的时候才调用
                    }
                }

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Timber.i("======>registerCode:"+integer);
                switch (integer){
                    case 100:
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        final AlertDialog dialog = builder.create();
                        if (!dialog.isShowing()){
                            builder.setMessage("注册成功");
                            builder.setCancelable(false);
                            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    RegisterActivity.this.finish();
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }else {
                            dialog.dismiss();
                        }
                        break;
                    case 101:
                        Snackbar.make(mContainer,"该账户已被注册",Snackbar.LENGTH_SHORT).show();
                        break;
                    case 102:
                        Snackbar.make(mContainer,"两次输入的密码不一致",Snackbar.LENGTH_SHORT).show();
                        break;
                    case 103:
                        Snackbar.make(mContainer,"请完整填写注册信息",Snackbar.LENGTH_SHORT).show();
                        break;
                    case 104:
                        Snackbar.make(mContainer,"请填写正确的电话号码",Snackbar.LENGTH_SHORT).show();
                        break;
                    case 105:
                        Snackbar.make(mContainer,"密码长度不能少于6位",Snackbar.LENGTH_SHORT).show();
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
