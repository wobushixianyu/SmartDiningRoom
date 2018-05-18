package com.david.smartdiningroom.mvp.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.OrderDetailsClasss;
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
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    private ItemAdapter<OrderDetailsClasss> itemAdapter;
    private FastAdapter mFastAdapter;
    private Context mContext = this;
    private int orderId;
    private int status;
    private ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        mToolBar.setTitle("订单详情");

        apiManager = new ApiManager();

        setUpOrderDetailsClasss(savedInstanceState);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        boolean isSeller = intent.getBooleanExtra("isSeller", false);
        status = intent.getIntExtra("status", 0);
        orderId = intent.getIntExtra("orderId",0);
        double price = intent.getDoubleExtra("price", 0);
        mTvPrice.setText(String.valueOf(price));
        if (isSeller) {
            switch (status) {
                case 1:
                    mBtnCommit.setText("接单");
                    mBtnCommit.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mBtnCommit.setText("完成");
                    mBtnCommit.setVisibility(View.VISIBLE);
                    break;
                default:
                    mBtnCommit.setVisibility(View.GONE);
                    break;
            }
        } else {
            mBtnCommit.setVisibility(View.GONE);
        }

        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getHttpData();
            }
        }, 1000);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getHttpData();
                    }
                }, 500);
            }
        });

        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterOrderStatus();
            }
        });
    }

    private void setUpOrderDetailsClasss(Bundle savedInstanceState) {
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
    }

    private void getHttpData() {
        /*Observable.create(new ObservableOnSubscribe<JsonObject>() {
            @Override
            public void subscribe(ObservableEmitter<JsonObject> emitter) throws Exception {
                JsonObject jsonObject = SdrUtils.readAssets(mContext, "order_details_json.txt");
                System.out.println("======>jsonObject:" + jsonObject);
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
                        JsonArray data = jsonObject.get("data").getAsJsonArray();
                        List<OrderDetailsClasss> mOrderDetailsClasss = new Gson().fromJson(data, new TypeToken<List<OrderDetailsClasss>>() {
                        }.getType());
                        setData(mOrderDetailsClasss);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });*/
        Map<String,Object> params = new HashMap<>();
        params.put("order_id",orderId);
        apiManager.getOrderDetails(params).subscribe(new SubscriberCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                JsonArray data = jsonObject.get("data").getAsJsonArray();
                List<OrderDetailsClasss> mOrderDetailsClasss = new Gson().fromJson(data, new TypeToken<List<OrderDetailsClasss>>() {
                }.getType());
                setData(mOrderDetailsClasss);
            }

            @Override
            public void onFailure(Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setData(List<OrderDetailsClasss> mOrderDetailsClasss) {
        int size = mOrderDetailsClasss != null ? mOrderDetailsClasss.size() : 0;
        if (size != 0) {
            itemAdapter.clear();
            itemAdapter.add(mOrderDetailsClasss);
        } else {
            showMessage("获取订单数据失败");
        }
    }

    private void showMessage(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        if (!dialog.isShowing()) {
            builder.setMessage(msg);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                    OrderDetailsActivity.this.finish();
                }
            });
            builder.show();
        } else {
            dialog.dismiss();
        }
    }

    private void alterOrderStatus() {
        Map<String,Object> params = new HashMap<>();
        params.put("order_id",orderId);
        if (status == 1){
            params.put("status",2);
        }else if (status == 2){
            params.put("status",3);
        }
        apiManager.alterOrderStatus(params).subscribe(new SubscriberCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                if (jsonObject.get("return_code").getAsInt() == 200){
                    SdrUtils.showToast(OrderDetailsActivity.this,"成功");
                    Hawk.put("changeStatus",true);
                    OrderDetailsActivity.this.finish();
                }else {
                    SdrUtils.showToast(OrderDetailsActivity.this,"失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                SdrUtils.showToast(OrderDetailsActivity.this,"失败");
            }

            @Override
            public void onCompleted() {

            }
        });
    }
}
