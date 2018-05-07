package com.david.smartdiningroom.mvp.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.utils.AppManager;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        AppManager.jumpAndFinish(LoginActivity.class);
    }
}
