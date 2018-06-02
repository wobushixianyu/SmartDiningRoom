package com.david.smartdiningroom.mvp.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.BigHouse;
import com.david.smartdiningroom.mvp.bean.DishManHouseObserver;
import com.david.smartdiningroom.utils.AppManager;
import com.david.smartdiningroom.widget.toolbar.ToolbarView;

import timber.log.Timber;

public class GuideActivity extends AppCompatActivity {

    private ToolbarView mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
//        AppManager.jumpAndFinish(LoginActivity.class);

        BigHouse bigHouse = new BigHouse(10000);
        DishManHouseObserver dmA = new DishManHouseObserver("张三");
        DishManHouseObserver dmB = new DishManHouseObserver("李四");
        DishManHouseObserver dmC = new DishManHouseObserver("王二麻子");
        bigHouse.addObserver(dmA);
        bigHouse.addObserver(dmC);
        bigHouse.addObserver(dmB);
        Timber.e("======>start:"+bigHouse.toString());
        bigHouse.setPrice(15000);
        Timber.e("======>end:"+bigHouse.toString());

        ToolbarView mToolbar = findViewById(R.id.my_toolbar);
        mToolbar.setTitle("智能管家智能管家智能管家智能管家智能管家智能管家智能管家智能管家");
        mToolbar.setBackImgButton(R.mipmap.ic_back_toolbar, null);
        mToolbar.setShareImgBtn(R.mipmap.ic_share_toolbar, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GuideActivity.this, "分享", Toast.LENGTH_SHORT).show();
            }
        });
        mToolbar.setCloseImgBtn(R.mipmap.ic_close_toolbar, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GuideActivity.this, "关闭", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
