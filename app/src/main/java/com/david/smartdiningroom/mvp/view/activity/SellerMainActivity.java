package com.david.smartdiningroom.mvp.view.activity;

import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.view.fragment.SellerDataFragment;
import com.david.smartdiningroom.mvp.view.fragment.SellerEvaluateFragment;
import com.david.smartdiningroom.mvp.view.fragment.SellerOrderFragment;
import com.david.smartdiningroom.utils.WeakHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SellerMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private FragmentTransaction transaction;
    private final FragmentManager manager = getSupportFragmentManager();
    private SellerOrderFragment mSellerOrderFragment;
    private SellerDataFragment mSellerDataFragment;
    private SellerEvaluateFragment mSellerEvaluateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.app_name));
        navigation.setOnNavigationItemSelectedListener(this);

        setupFragment(PageType.order);
    }

    //设置要展示的fragment
    private void setupFragment(PageType type) {
        transaction = manager.beginTransaction();
        if (type == PageType.order) {
            if (mSellerOrderFragment == null) {
                mSellerOrderFragment = new SellerOrderFragment();
                transaction.add(R.id.fragment_container, mSellerOrderFragment);
            }
            hindFragment();
            transaction.show(mSellerOrderFragment);
        } else if (type == PageType.count) {
            if (mSellerDataFragment == null) {
                mSellerDataFragment = new SellerDataFragment();
                transaction.add(R.id.fragment_container, mSellerDataFragment);
            }
            hindFragment();
            transaction.show(mSellerDataFragment);
        } else if (type == PageType.evaluate) {
            if (mSellerEvaluateFragment == null) {
                mSellerEvaluateFragment = new SellerEvaluateFragment();
                transaction.add(R.id.fragment_container, mSellerEvaluateFragment);
            }
            hindFragment();
            transaction.show(mSellerEvaluateFragment);
        }
        transaction.commit();
    }

    private void hindFragment() {
        if (mSellerOrderFragment != null) transaction.hide(mSellerOrderFragment);
        if (mSellerEvaluateFragment != null) transaction.hide(mSellerEvaluateFragment);
        if (mSellerDataFragment != null) transaction.hide(mSellerDataFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_order:
                mToolbar.setTitle(getString(R.string.title_order));
                return true;
            case R.id.navigation_count:
                mToolbar.setTitle(getString(R.string.title_count));
                return true;
            case R.id.navigation_evaluate:
                mToolbar.setTitle(getString(R.string.title_evaluate));
                return true;
        }
        return false;
    }

    //枚举fragment页面常量，用于标识
    enum PageType {
        order, count, evaluate
    }

    private long mBackPressedTime;

    @Override
    public void onBackPressed() {
        //当两次点击返回按钮的间隔时间小于2秒的时候才退出程序
        if (mBackPressedTime + 2000 > System.currentTimeMillis()) {
            WeakHandler handler = new WeakHandler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    supportFinishAfterTransition();
                    Process.killProcess(Process.myPid());
                }
            }, 100);
            return;
        } else {
            Snackbar.make(navigation, "再次点击[返回]键退出程序", Snackbar.LENGTH_SHORT).show();
        }
        mBackPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        transaction = null;
    }
}
