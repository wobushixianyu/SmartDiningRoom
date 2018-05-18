package com.david.smartdiningroom.mvp.view.fragment;

import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.david.smartdiningroom.BaseFragment;
import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.StoreBeanClasss;
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

public class ClassifyFragment extends BaseFragment {
    @BindView(R.id.rbt_fast_food)
    RadioButton mRbtFastFood;
    @BindView(R.id.rbt_stir_fry)
    RadioButton mRbtStirFry;
    @BindView(R.id.rbt_hot_pot)
    RadioButton mRbtHotPot;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private int type = 0; //快餐0,炒菜1,火锅2
    private ItemAdapter<StoreBeanClasss> itemAdapter;
    private ItemAdapter footerAdapter;
    private FastAdapter mFastAdapter;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private ApiManager apiManager;
    private int pageIndex = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classify_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        apiManager = new ApiManager();
        setUpStoreBeanClasss(savedInstanceState);
        initView();
        return view;
    }

    private void setUpStoreBeanClasss(Bundle savedInstanceState) {
        itemAdapter = new ItemAdapter<>();
        footerAdapter = new ItemAdapter();
        mFastAdapter = FastAdapter.with(Arrays.asList(itemAdapter, footerAdapter));

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

        mFastAdapter.withOnClickListener(new OnClickListener() {
            @Override
            public boolean onClick(View v, IAdapter adapter, IItem item, int position) {
                StoreBeanClasss beanClasss = itemAdapter.getAdapterItem(position);
                Map<String, Serializable> params = new HashMap<>();
                params.put("shopId",beanClasss.getShop_id());
                params.put("shopName",beanClasss.getName());
                params.put("shopAddress",beanClasss.getAddress());
                params.put("shopLogo",beanClasss.getImg());
                AppManager.jump(ShopDetailsActivity.class, params);
                return false;
            }
        });

        mRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

        mFastAdapter.saveInstanceState(savedInstanceState);
    }

    private void initView() {
        mRbtFastFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbtStirFry.setChecked(false);
                    mRbtHotPot.setChecked(false);
                    type = 1;
                    getHttpData(false);
                }
            }
        });

        mRbtStirFry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbtFastFood.setChecked(false);
                    mRbtHotPot.setChecked(false);
                    type = 2;
                    getHttpData(false);
                }
            }
        });

        mRbtHotPot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRbtFastFood.setChecked(false);
                    mRbtStirFry.setChecked(false);
                    type = 3;
                    getHttpData(false);
                }
            }
        });

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getHttpData(false);
            }
        },300);

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

    @OnClick({R.id.ll_fast_food, R.id.ll_stir_fry, R.id.ll_hot_pot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fast_food:
                if (!mRbtFastFood.isChecked()) {
                    mRbtFastFood.setChecked(true);
                    mRbtStirFry.setChecked(false);
                    mRbtHotPot.setChecked(false);
                    type = 1;
                }
                break;
            case R.id.ll_stir_fry:
                if (!mRbtStirFry.isChecked()) {
                    mRbtFastFood.setChecked(false);
                    mRbtStirFry.setChecked(true);
                    mRbtHotPot.setChecked(false);
                    type = 2;
                }
                break;
            case R.id.ll_hot_pot:
                if (!mRbtHotPot.isChecked()) {
                    mRbtFastFood.setChecked(false);
                    mRbtStirFry.setChecked(false);
                    mRbtHotPot.setChecked(true);
                    type = 3;
                }
                break;
        }
    }

    private void getHttpData(final boolean isLoadMore) {
        if (isLoadMore){
            pageIndex++;
        }else {
            pageIndex = 1;
        }

        Map<String,Object> params = new HashMap<>();
        params.put("pageIndex",pageIndex);
        params.put("type",type);
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
}
