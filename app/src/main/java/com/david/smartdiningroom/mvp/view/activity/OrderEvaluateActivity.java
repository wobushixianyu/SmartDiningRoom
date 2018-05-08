package com.david.smartdiningroom.mvp.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.david.smartdiningroom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderEvaluateActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluate);
        ButterKnife.bind(this);
        mToolBar.setTitle("订单评价");
    }
}
