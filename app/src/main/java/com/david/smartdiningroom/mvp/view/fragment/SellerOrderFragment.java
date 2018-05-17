package com.david.smartdiningroom.mvp.view.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.MyOrderClasss;
import com.david.smartdiningroom.mvp.bean.SellerOrderClasss;
import com.david.smartdiningroom.mvp.view.activity.OrderDetailsActivity;
import com.david.smartdiningroom.utils.AppManager;
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

public class SellerOrderFragment extends Fragment implements OnClickListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ItemAdapter<SellerOrderClasss> itemAdapter;
    private ItemAdapter footerAdapter;
    private FastAdapter mFastAdapter;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_order_fragment_layout,container,false);
        ButterKnife.bind(this,view);
        setUpMyOrderClasss(savedInstanceState);
        initView();
        return view;
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
        mFastAdapter.withOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.recycler_divider);
        if (drawable != null) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
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

        mFastAdapter.saveInstanceState(savedInstanceState);
    }

    private void getHttpData(final boolean isLoadMore) {
        Observable.create(new ObservableOnSubscribe<JsonObject>() {
            @Override
            public void subscribe(ObservableEmitter<JsonObject> emitter) throws Exception {
                JsonObject jsonObject = SdrUtils.readAssets(getContext(), "my_order_list.txt");
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
                        if (!isLoadMore) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        JsonArray list = data.get("list").getAsJsonArray();
                        List<SellerOrderClasss> mMyOrderClasss = new Gson().fromJson(list, new TypeToken<List<SellerOrderClasss>>() {
                        }.getType());
                        setData(mMyOrderClasss, !isLoadMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        endlessRecyclerViewScrollListener.resetState(false);
                        footerAdapter.clear();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        endlessRecyclerViewScrollListener.resetState(false);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(List<SellerOrderClasss> mMyOrderClasss, boolean isRefresh) {
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
    public boolean onClick(View v, IAdapter adapter, IItem item, int position) {
        SellerOrderClasss adapterItem = itemAdapter.getAdapterItem(position);
        Map<String,Serializable> params = new HashMap<>();
        params.put("orderId",adapterItem.getOrder_id());
        params.put("isSeller",true);
        params.put("status",adapterItem.getStatus());
        params.put("price",adapterItem.getPrice());
        AppManager.jump(OrderDetailsActivity.class,params);
        return false;
    }
}
