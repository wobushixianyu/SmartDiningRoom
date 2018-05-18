package com.david.smartdiningroom.mvp.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.david.smartdiningroom.BaseFragment;
import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.StoreBeanClasss;
import com.david.smartdiningroom.mvp.presenter.HomeFragmentPresenter;
import com.david.smartdiningroom.mvp.view.HomeFragmentView;
import com.david.smartdiningroom.mvp.view.activity.ShopDetailsActivity;
import com.david.smartdiningroom.remote.ApiManager;
import com.david.smartdiningroom.remote.SubscriberCallBack;
import com.david.smartdiningroom.utils.AppManager;
import com.david.smartdiningroom.utils.SdrUtils;
import com.david.smartdiningroom.utils.WeakHandler;
import com.david.smartdiningroom.widget.HomePageHeaderView;
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
import java.util.Objects;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class HomeFragment2 extends BaseFragment implements HomePageHeaderView.OnSliderClickListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Map<String, Object> params = new HashMap<>();
    private static final String SALES_VOLUME = "1001";
    private static final String STAR_LEVEL = "1002";
    private String sortMethod = SALES_VOLUME;
    private int pageIndex = 1;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private ItemAdapter footerAdapter;
    private FastAdapter mFastAdapter;
    private ItemAdapter<StoreBeanClasss> itemAdapter;
    private ItemAdapter<HomePageHeaderView> headerAdapter;
    private ApiManager apiManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        ButterKnife.bind(this, view);

        apiManager = new ApiManager();

        setupStoreClasss(savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getHttpData(false);
            }
        }, 1500);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("======>onRefresh");
                        footerAdapter.clear();
                        getHttpData(false);
                    }
                }, 1500);
            }
        });


    }

    private void setupStoreClasss(Bundle savedInstanceState) {
        headerAdapter = new ItemAdapter<>();
        itemAdapter = new ItemAdapter<>();
        footerAdapter = new ItemAdapter();
        mFastAdapter = FastAdapter.with(Arrays.asList(headerAdapter, itemAdapter, footerAdapter));

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
                                    getHttpData(true);
                                }
                            }, 1500);
                        }
                    }, 300);
                }

            }
        };

        mFastAdapter.withOnClickListener(new OnClickListener() {
            @Override
            public boolean onClick(View v, IAdapter adapter, IItem item, int position) {
                StoreBeanClasss beanClasss = itemAdapter.getAdapterItem(position - 1);
                Map<String,Serializable> params = new HashMap<>();
                params.put("shopId",beanClasss.getShop_id());
                params.put("shopName",beanClasss.getName());
                params.put("shopAddress",beanClasss.getAddress());
                params.put("shopLogo",beanClasss.getImg());
                AppManager.jump(ShopDetailsActivity.class,params);
                return false;
            }
        });

        mRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

        mFastAdapter.saveInstanceState(savedInstanceState);
    }

    private void setData(List<StoreBeanClasss> storeBeanClassses, boolean isRefresh) {
        int size = storeBeanClassses != null ? storeBeanClassses.size() : 0;
        if (isRefresh) {
            if (size != 0) {
                itemAdapter.clear();
                itemAdapter.add(storeBeanClassses);
            } else {
                showMessage("暂无数据");
            }
        } else {
            if (size != 0) {
                itemAdapter.add(storeBeanClassses);
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
    public void onSliderClick() {
        SdrUtils.showToast(getContext(),"banner");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mFastAdapter.saveInstanceState(outState);
    }

    private void getHttpData(final boolean isLoadMore){
        if (isLoadMore){
            pageIndex++;
        }else {
            pageIndex = 1;
        }

        Map<String,Object> params = new HashMap<>();
        params.put("pageIndex",pageIndex);
        params.put("type","0");
        apiManager.getStoreList(params).subscribe(new SubscriberCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                if (!isLoadMore) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                JsonArray data = jsonObject.get("list").getAsJsonArray();
                List<StoreBeanClasss> storeBeanClassses = new Gson().fromJson(data, new TypeToken<List<StoreBeanClasss>>() {
                }.getType());
                setData(storeBeanClassses, !isLoadMore);
                headerAdapter.clear();
                headerAdapter.add(new HomePageHeaderView(getContext(), HomeFragment2.this));
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
}
