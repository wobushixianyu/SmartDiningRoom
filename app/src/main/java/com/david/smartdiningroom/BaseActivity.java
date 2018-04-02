package com.david.smartdiningroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    /**
     * 显示隐藏软件盘
     */
    public void softInputVisible(boolean visibility) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && imm != null) {
            if (visibility){
                imm.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(),0);
            }else {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

        }
    }

    public void showWaitingDialog(){

    }

    public void dismissWaitingDialog(){

    }
}
