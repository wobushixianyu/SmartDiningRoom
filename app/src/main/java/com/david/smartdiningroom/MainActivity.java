package com.david.smartdiningroom;

import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.david.smartdiningroom.mvp.view.fragment.ClassifyFragment;
import com.david.smartdiningroom.mvp.view.fragment.HomeFragment;
import com.david.smartdiningroom.mvp.view.fragment.MineFragment;
import com.david.smartdiningroom.utils.WeakHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private FragmentTransaction transaction;
    private HomeFragment homeFragment;
    private MineFragment mineFragment;
    private ClassifyFragment classifyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.app_name));
        navigation.setOnNavigationItemSelectedListener(this);

        setupFragment(PageType.home);
    }

    //设置要展示的fragment
    private void setupFragment(PageType type) {
        transaction = getSupportFragmentManager().beginTransaction();
        if (type == PageType.home) {
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
                transaction.add(R.id.fragment_container, homeFragment);
            }
            hindFragment();
            transaction.show(homeFragment);
        } else if (type == PageType.mine) {
            if (mineFragment == null) {
                mineFragment = new MineFragment();
                transaction.add(R.id.fragment_container, mineFragment);
            }
            hindFragment();
            transaction.show(mineFragment);
        } else if (type == PageType.classify){
            if (classifyFragment == null){
                classifyFragment = new ClassifyFragment();
                transaction.add(R.id.fragment_container, classifyFragment);
            }
            hindFragment();
            transaction.show(classifyFragment);
        }
        transaction.commit();
    }

    private void hindFragment() {
        if (homeFragment != null) transaction.hide(homeFragment);
        if (mineFragment != null) transaction.hide(mineFragment);
        if (classifyFragment != null) transaction.hide(classifyFragment);
    }

    //根据导航按钮，切换界面
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                mToolbar.setTitle(getString(R.string.toolbar_title_home));
                setupFragment(PageType.home);
                return true;
            case R.id.navigation_classify:
                mToolbar.setTitle(getString(R.string.toolbar_title_classify));
                setupFragment(PageType.classify);
                return true;
            case R.id.navigation_mine:
                mToolbar.setTitle(getString(R.string.toolbar_title_mine));
                setupFragment(PageType.mine);
                return true;
        }
        return false;
    }

    //枚举fragment页面常量，用于标识
    enum PageType {
        home, mine, classify
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
}
