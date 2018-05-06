package com.david.smartdiningroom.mvp.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.ShopDetailsClasss;
import com.david.smartdiningroom.utils.SdrUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

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
    private FastAdapter mFastAdapter;
    private double countPrice = 0;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        ButterKnife.bind(this);
        setUpShopDetailsClasss(savedInstanceState);
        getHttpData();
    }

    private void setUpShopDetailsClasss(Bundle savedInstanceState) {
        itemAdapter = new ItemAdapter<>();
        mFastAdapter = FastAdapter.with(itemAdapter);

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
        Observable.create(new ObservableOnSubscribe<JsonObject>() {
            @Override
            public void subscribe(ObservableEmitter<JsonObject> emitter) throws Exception {
                JsonObject jsonObject = SdrUtils.readAssets(mContext, "shop_details_data.txt");
                System.out.println("======>jsonObject:"+jsonObject);
                emitter.onNext(jsonObject);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        Picasso.with(mContext).load(data.get("img").getAsString()).into(mIvLogo);
                        mTvName.setText(data.get("name").getAsString());
                        mTvAddress.setText(data.get("address").getAsString());
                        JsonArray list = data.get("list").getAsJsonArray();
                        List<ShopDetailsClasss> shopDetailsClasss = new Gson().fromJson(list, new TypeToken<List<ShopDetailsClasss>>() {
                        }.getType());
                        itemAdapter.clear();
                        itemAdapter.add(shopDetailsClasss);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @OnClick({R.id.btn_sure})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_sure:
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onAddClick(TextView tv_num, ImageView imgAdd, ImageView imgReduce, double price, int id) {
        if (TextUtils.isEmpty(tv_num.getText().toString())){
            tv_num.setText("0");
        }
        int num = Integer.parseInt(tv_num.getText().toString());
        num++;
        tv_num.setText(""+num);
        if (num > 0){
            tv_num.setVisibility(View.VISIBLE);
            imgReduce.setVisibility(View.VISIBLE);
        }else {
            tv_num.setVisibility(View.INVISIBLE);
            imgReduce.setVisibility(View.INVISIBLE);
        }
        countPrice += price;
        mTvCountPrice.setText("¥："+countPrice);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onReduceClick(TextView tv_num, ImageView imgAdd, ImageView imgReduce, double price, int id) {
        int num = Integer.parseInt(tv_num.getText().toString());
        num--;
        tv_num.setText(""+num);
        if (num <= 0){
            tv_num.setText("0");
            tv_num.setVisibility(View.INVISIBLE);
            imgReduce.setVisibility(View.INVISIBLE);
        }else {
            tv_num.setVisibility(View.VISIBLE);
            imgReduce.setVisibility(View.VISIBLE);
        }
        countPrice -= price;
        mTvCountPrice.setText("¥："+countPrice);
    }
}
