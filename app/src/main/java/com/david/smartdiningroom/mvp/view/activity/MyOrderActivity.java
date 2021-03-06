package com.david.smartdiningroom.mvp.view.activity;

import android.content.Context;
import android.content.DialogInterface;
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

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.MyOrderClasss;
import com.david.smartdiningroom.mvp.bean.SellerOrderClasss;
import com.david.smartdiningroom.remote.ApiManager;
import com.david.smartdiningroom.remote.SubscriberCallBack;
import com.david.smartdiningroom.utils.AppManager;
import com.david.smartdiningroom.utils.ContentsUtils;
import com.david.smartdiningroom.utils.SdrUtils;
import com.david.smartdiningroom.utils.WeakHandler;
import com.david.smartdiningroom.widget.scroll.EndlessRecyclerViewScrollListener;
import com.david.smartdiningroom.widget.scroll.ProgressItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.orhanobut.hawk.Hawk;

import java.io.Serializable;
import java.util.Arrays;
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

public class MyOrderActivity extends AppCompatActivity implements MyOrderClasss.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ItemAdapter<MyOrderClasss> itemAdapter;
    private ItemAdapter footerAdapter;
    private FastAdapter mFastAdapter;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private Context mContext = this;
    private ApiManager apiManager;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);

        apiManager = new ApiManager();

        mToolBar.setTitle("我的订单");
        setUpMyOrderClasss(savedInstanceState);
        initView();
    }

    private void initView() {
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getHttpData(false);
            }
        },1000);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                footerAdapter.clear();
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getHttpData(false);
                    }
                },500);
            }
        });
    }

    private void setUpMyOrderClasss(Bundle savedInstanceState) {
        itemAdapter = new ItemAdapter<>();
        footerAdapter = new ItemAdapter();
        mFastAdapter = FastAdapter.with(Arrays.asList(itemAdapter, footerAdapter));

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

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            private WeakHandler handler = new WeakHandler();

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (mFastAdapter.getItemCount() >= SdrUtils.HTTP_PAGE_COUNT) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            footerAdapter.clear();
                            footerAdapter.add(new ProgressItem().withEnabled(false));
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("======>onLoadMore");
                                    getHttpData(true);
                                }
                            }, 1500);
                        }
                    }, 300);
                }
            }
        };

        mRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

        mFastAdapter.withOnClickListener(new OnClickListener() {
            @Override
            public boolean onClick(View v, IAdapter adapter, IItem item, int position) {
                MyOrderClasss adapterItem = itemAdapter.getAdapterItem(position);
                Map<String,Serializable> params = new HashMap<>();
                params.put("orderId",adapterItem.getId());
                params.put("isSeller",false);
                params.put("status",adapterItem.getStatus());
                params.put("price",adapterItem.getPrice());
                AppManager.jump(OrderDetailsActivity.class,params);
                return false;
            }
        });

        mFastAdapter.withEventHook(new MyOrderClasss.OnEvaluateClickListener(this));

        mFastAdapter.saveInstanceState(savedInstanceState);
    }

    private void getHttpData(final boolean isLoadMore) {
        if (isLoadMore){
            pageIndex++;
        }else {
            pageIndex = 1;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("shop_id",0);
        params.put("user_id",Hawk.get(ContentsUtils.CUSTOMER_LOGIN_NAME, ""));
        params.put("pageIndex",pageIndex);
        apiManager.getOrderList(params).subscribe(new SubscriberCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                if (!isLoadMore) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                JsonArray list = jsonObject.get("list").getAsJsonArray();
                List<MyOrderClasss> mMyOrderClasss = new Gson().fromJson(list, new TypeToken<List<MyOrderClasss>>() {
                }.getType());
                setData(mMyOrderClasss, !isLoadMore);
            }

            @Override
            public void onFailure(Throwable t) {
                endlessRecyclerViewScrollListener.resetState(false);
                footerAdapter.clear();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCompleted() {
                endlessRecyclerViewScrollListener.resetState(false);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setData(List<MyOrderClasss> mMyOrderClasss, boolean isRefresh) {
        int size = mMyOrderClasss != null ? mMyOrderClasss.size() : 0;
        if (isRefresh) {
            if (size != 0) {
                itemAdapter.clear();
                itemAdapter.add(mMyOrderClasss);
            } else {
                showMessage("暂无订单");
            }
        } else {
            if (size != 0) {
                itemAdapter.add(mMyOrderClasss);
            } else {
                footerAdapter.clear();
                endlessRecyclerViewScrollListener.resetState(false);
            }
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
                }
            });
            builder.show();
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public void onEvaluateClick(int orderId, int shopId) {
        Map<String,Serializable> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("shopId",shopId);
        AppManager.jump(OrderEvaluateActivity.class,params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Hawk.get("evaluate",false)){
            getHttpData(false);
            Hawk.put("evaluate",false);
        }
    }
}
