package com.david.smartdiningroom.mvp.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.remote.ApiManager;
import com.david.smartdiningroom.remote.SubscriberCallBack;
import com.david.smartdiningroom.utils.AppManager;
import com.david.smartdiningroom.utils.SdrUtils;
import com.google.gson.JsonObject;
import com.orhanobut.hawk.Hawk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.et_content)
    EditText mEtContent;
    private List<ImageButton> levels;
    private int shopScore = 0;
    private Drawable icSmileYellow;
    private Drawable icSmileGray;
    private ApiManager apiManager;
    private int shopId;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluate);
        ButterKnife.bind(this);

        apiManager = new ApiManager();

        mToolBar.setTitle("订单评价");
        initView();
    }

    private void initView() {
        levels = Arrays.asList(mBtn01, mBtn02, mBtn03, mBtn04, mBtn05);
        icSmileYellow = getDrawable(R.mipmap.ic_smile_yellow);
        icSmileGray = getDrawable(R.mipmap.ic_smile_gray);
        Intent intent = getIntent();
        shopId = intent.getIntExtra("shopId", 0);
        orderId = intent.getIntExtra("orderId", 0);
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
            case R.id.btn_commit://提交评价
                if (TextUtils.isEmpty(mEtContent.getText())){
                    SdrUtils.showToast(this,"请填写评价内容");
                    break;
                }
                if (shopScore == 0){
                    SdrUtils.showToast(this,"请对这次用餐评分");
                    break;
                }
                submitEvaluation();
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

    private void submitEvaluation() {
        JsonObject json = new JsonObject();
        json.addProperty("content",mEtContent.getText().toString());
        json.addProperty("score",shopScore);
        json.addProperty("shop_id",shopId);
        System.out.println("======>json:"+json.toString());
        apiManager.submitEvaluation(json.toString()).subscribe(new SubscriberCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                if (jsonObject.get("return_code").getAsInt() == 200){
                    alterOrderStatus();
                }else {
                    SdrUtils.showToast(OrderEvaluateActivity.this,"评价失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                SdrUtils.showToast(OrderEvaluateActivity.this,"评价失败");
            }

            @Override
            public void onCompleted() {
            }
        });
    }

    private void alterOrderStatus() {
        Map<String,Object> params = new HashMap<>();
        params.put("order_id",orderId);
        params.put("status",4);
        apiManager.alterOrderStatus(params).subscribe(new SubscriberCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                SdrUtils.showToast(OrderEvaluateActivity.this,"感谢您的评价");
                Hawk.put("evaluate",true);
                OrderEvaluateActivity.this.finish();
            }

            @Override
            public void onFailure(Throwable t) {
            }

            @Override
            public void onCompleted() {
            }
        });
    }
}
