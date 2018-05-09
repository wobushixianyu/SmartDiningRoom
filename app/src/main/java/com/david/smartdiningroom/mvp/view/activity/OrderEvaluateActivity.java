package com.david.smartdiningroom.mvp.view.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.david.smartdiningroom.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderEvaluateActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.btn01)
    ImageButton mBtn01;
    @BindView(R.id.btn02)
    ImageButton mBtn02;
    @BindView(R.id.btn03)
    ImageButton mBtn03;
    @BindView(R.id.btn04)
    ImageButton mBtn04;
    @BindView(R.id.btn05)
    ImageButton mBtn05;
    private List<ImageButton> levels;
    private int shopScore = 0;
    private Drawable icSmileYellow;
    private Drawable icSmileGray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluate);
        ButterKnife.bind(this);
        mToolBar.setTitle("订单评价");
        initView();
    }

    private void initView() {
        levels = Arrays.asList(mBtn01, mBtn02, mBtn03, mBtn04, mBtn05);
        icSmileYellow = getDrawable(R.mipmap.ic_smile_yellow);
        icSmileGray = getDrawable(R.mipmap.ic_smile_gray);
    }

    @OnClick({R.id.btn01,R.id.btn02,R.id.btn03,R.id.btn04,R.id.btn05,R.id.btn_commit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn01:
                setUpLevel(1);
                shopScore = 1;
                break;
            case R.id.btn02:
                setUpLevel(2);
                shopScore = 2;
                break;
            case R.id.btn03:
                setUpLevel(3);
                shopScore = 3;
                break;
            case R.id.btn04:
                setUpLevel(4);
                shopScore = 4;
                break;
            case R.id.btn05:
                setUpLevel(5);
                shopScore = 5;
                break;
            case R.id.btn_commit:
                break;
        }
    }

    private void setUpLevel(int score) {
        for (int i = 0; i < 5; i++) {
            if (i < score){
                levels.get(i).setBackground(icSmileYellow);
            }else {
                levels.get(i).setBackground(icSmileGray);
            }
        }
    }
}
