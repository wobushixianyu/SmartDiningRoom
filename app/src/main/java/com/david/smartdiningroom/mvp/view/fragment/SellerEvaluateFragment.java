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
import com.david.smartdiningroom.mvp.bean.EvaluateClasss;
import com.david.smartdiningroom.remote.ApiManager;
import com.david.smartdiningroom.remote.SubscriberCallBack;
import com.david.smartdiningroom.utils.SdrUtils;
import com.david.smartdiningroom.utils.WeakHandler;
import com.david.smartdiningroom.widget.scroll.EndlessRecyclerViewScrollListener;
import com.david.smartdiningroom.widget.scroll.ProgressItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SellerEvaluateFragment extends Fragment{

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ItemAdapter<EvaluateClasss> itemAdapter;
    private ItemAdapter footerAdapter;
    private FastAdapter mFastAdapter;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private int pageIndex = 1;
    private ApiManager apiManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_evaluate_fragment_layout,container,false);

        apiManager = new ApiManager();

        ButterKnife.bind(this,view);
        setUpEvaluateClasss(savedInstanceState);
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
                        footerAdapter.clear();
                        getHttpData(false);
                    }
                }, 1500);
            }
        });


    }

    private void setUpEvaluateClasss(Bundle savedInstanceState) {
        itemAdapter = new ItemAdapter<>();
        footerAdapter = new ItemAdapter();
        mFastAdapter = FastAdapter.with(Arrays.asList(itemAdapter,footerAdapter));

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

        mRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

        mFastAdapter.saveInstanceState(savedInstanceState);
    }

    private void getHttpData(final boolean isLoadMore) {
        /*Observable.create(new ObservableOnSubscribe<JsonObject>() {
            @Override
            public void subscribe(ObservableEmitter<JsonObject> emitter) throws Exception {
                JsonObject jsonObject = SdrUtils.readAssets(Objects.requireNonNull(getContext()), "evaluate_list.txt");
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
                        List<EvaluateClasss> mEvaluateClasss = new Gson().fromJson(list, new TypeToken<List<EvaluateClasss>>() {
                        }.getType());
                        setData(mEvaluateClasss, !isLoadMore);
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
                });*/
        if (isLoadMore){
            pageIndex++;
        }else {
            pageIndex = 1;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("shop_id",1);
        params.put("pageIndex",pageIndex);
        apiManager.getEvaluationList(params).subscribe(new SubscriberCallBack<JsonObject>() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                JsonArray list = jsonObject.get("list").getAsJsonArray();
                List<EvaluateClasss> mEvaluateClasss = new Gson().fromJson(list, new TypeToken<List<EvaluateClasss>>() {
                }.getType());
                setData(mEvaluateClasss, !isLoadMore);
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

    private void setData(List<EvaluateClasss> mEvaluateClasss, boolean isRefresh) {
        int size = mEvaluateClasss != null ? mEvaluateClasss.size() : 0;
        if (isRefresh) {
            if (size != 0) {
                itemAdapter.clear();
                itemAdapter.add(mEvaluateClasss);
            } else {
                showMessage("暂无数据");
            }
        } else {
            if (size != 0) {
                itemAdapter.add(mEvaluateClasss);
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
