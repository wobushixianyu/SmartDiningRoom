package com.david.smartdiningroom.mvp.view.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.david.smartdiningroom.MainActivity;
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

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.WHITE);

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
        AppManager.jumpAndFinish(MainActivity.class);
    }
}
