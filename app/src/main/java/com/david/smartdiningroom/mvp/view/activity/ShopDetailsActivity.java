package com.david.smartdiningroom.mvp.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.ShopDetailsClasss;
import com.david.smartdiningroom.remote.ApiManager;
import com.david.smartdiningroom.remote.SubscriberCallBack;
import com.david.smartdiningroom.utils.AppManager;
import com.david.smartdiningroom.utils.SdrUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ShopDetailsActivity extends AppCompatActivity implements ShopDetailsClasss.OnItemClickListener {

    @BindView(R.id.iv_logo)
    ImageView mIvLogo;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_evaluate)
    TextView mTvEvaluate;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.count_price)
    TextView mTvCountPrice;
    private ItemAdapter<ShopDetailsClasss> itemAdapter;
    private double countPrice = 0;
    private Context mContext = this;
    private int shopId;
    private ApiManager apiManager;
    private int[] dishesNum;
    private JsonArray arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        ButterKnife.bind(this);

        apiManager = new ApiManager();

        setUpShopDetailsClasss(savedInstanceState);
        initView();
        getHttpData();
    }

    private void initView() {
        Intent intent = getIntent();
        shopId = intent.getIntExtra("shopId", 0);
        String shopName = intent.getStringExtra("shopName");
        String shopAddress = intent.getStringExtra("shopAddress");
        String shopLogo = intent.getStringExtra("shopLogo");

        Picasso.with(mContext).load(shopLogo).into(mIvLogo);
        mTvName.setText(shopName);
        mTvAddress.setText(shopAddress);
    }

    private void setUpShopDetailsClasss(Bundle savedInstanceState) {
        itemAdapter = new ItemAdapter<>();
        FastAdapter mFastAdapter = FastAdapter.with(itemAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.recycler_divider);
        if (drawable != null) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(drawable);
            mRecyclerView.addItemDecoration(itemDecoration);
        }
        mRecyclerView.setAdapter(mFastAdapter);

        mFastAdapter.saveInstanceState(savedInstanceState);
        mFastAdapter.withEventHook(new ShopDetailsClasss.onAddReduceClickEvent(this));
    }

    private void getHttpData() {
        Map<String,Object> params = new HashMap<>();
        params.put("shop_id",shopId);
        apiManager.getShopDetails(params).subscribe(new SubscriberCallBack<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {
                arrayList = jsonArray;
                List<ShopDetailsClasss> shopDetailsClasss = new Gson().fromJson(jsonArray, new TypeToken<List<ShopDetailsClasss>>() {
                }.getType());
                initArray(jsonArray);
                itemAdapter.clear();
                itemAdapter.add(shopDetailsClasss);
            }

            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        });

    }

    //初始化菜单各个菜品的选择数量，初始化每个菜品选择为0
    private void initArray(JsonArray jsonArray) {
        dishesNum = new int[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            dishesNum[i] = 0;
        }
    }

    @OnClick({R.id.btn_sure,R.id.tv_evaluate})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_sure:
                makeSureOrder();
                break;
            case R.id.tv_evaluate:
                AppManager.jump(EvaluateListActivity.class);
                break;
        }
    }

    private void makeSureOrder() {
        if (countPrice > 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("确认订单");
            builder.setMessage("当前订单总价为:"+countPrice+"\n是否确认提交当前订单?");
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                    commitOrder();
                }
            });
            builder.show();
        }else {
            SdrUtils.showToast(mContext,"您还没有选择食物");
        }
    }

    private void commitOrder() {
        JsonObject json = initParams(arrayList);
        apiManager.submitOrder(json.toString()).subscribe(new SubscriberCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                if (jsonObject.get("return_code").getAsInt() == 200){
                    SdrUtils.showToast(mContext,"提交订单成功");
                    countPrice = 0;
                    mTvCountPrice.setText("");
                    getHttpData();
                }else {
                    SdrUtils.showToast(mContext,"提交订单失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Timber.e(t);
                SdrUtils.showToast(mContext,"提交订单失败");
            }

            @Override
            public void onCompleted() {

            }
        });
    }

    private JsonObject initParams(JsonArray arrayList) {
        JsonObject json = new JsonObject();
        json.addProperty("shop_id",1);
        json.addProperty("price",countPrice);
        json.addProperty("user_id",1);
        JsonArray menu = new JsonArray();
        for (int i = 0; i < arrayList.size(); i++) {
            if (dishesNum[i] != 0){
                JsonObject item = arrayList.get(i).getAsJsonObject();
                item.addProperty("num",dishesNum[i]);
                menu.add(item);
            }
        }
        json.add("menu",menu);
        return json;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onAddClick(double price, int id) {
        countPrice += price;
        mTvCountPrice.setText("¥："+countPrice);
        dishesNum[id]++;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onReduceClick(double price, int id) {
        countPrice -= price;
        mTvCountPrice.setText("¥："+countPrice);
        dishesNum[id]--;
    }
}
