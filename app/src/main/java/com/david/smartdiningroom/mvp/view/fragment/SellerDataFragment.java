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
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.mvp.bean.DataBeanClasss;
import com.david.smartdiningroom.remote.ApiManager;
import com.david.smartdiningroom.remote.SubscriberCallBack;
import com.david.smartdiningroom.utils.SdrUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

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
import timber.log.Timber;

public class SellerDataFragment extends Fragment {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.spinner)
    AppCompatSpinner mSpinner;
    private ItemAdapter<DataBeanClasss> itemAdapter;
    private FastAdapter mFastAdapter;
    private ApiManager apiManager;
    private String month;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_data_fragment_layout, container, false);

        apiManager = new ApiManager();

        ButterKnife.bind(this, view);
        setUpDataBeanClasss(savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position < 10 ? "0"+(position+1) : ""+position+1;
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getHttpData();
                    }
                }, 1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    }

    private void setUpDataBeanClasss(Bundle savedInstanceState) {
        itemAdapter = new ItemAdapter<>();
        mFastAdapter = FastAdapter.with(itemAdapter);

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

        mFastAdapter.saveInstanceState(savedInstanceState);
    }

    private void getHttpData() {
        /*Timber.e("======>getHttpData");
        Observable.create(new ObservableOnSubscribe<JsonObject>() {
            @Override
            public void subscribe(ObservableEmitter<JsonObject> emitter) throws Exception {
                JsonObject jsonObject = SdrUtils.readAssets(getContext(), "month_data_json.txt");
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
                        List<DataBeanClasss> mDataBeanClasss = new Gson().fromJson(data, new TypeToken<List<DataBeanClasss>>() {
                        }.getType());
                        setData(mDataBeanClasss);
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
        params.put("shop_id",1);
        params.put("time","2018"+month);
        apiManager.getStatistics(params).subscribe(new SubscriberCallBack<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {
                List<DataBeanClasss> mDataBeanClasss = new Gson().fromJson(jsonArray, new TypeToken<List<DataBeanClasss>>() {
                }.getType());
                setData(mDataBeanClasss);
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

    private void setData(List<DataBeanClasss> mDataBeanClasss) {
        int size = mDataBeanClasss != null ? mDataBeanClasss.size() : 0;
        if (size != 0) {
            itemAdapter.clear();
            itemAdapter.add(mDataBeanClasss);
        } else {
            showMessage("暂无数据");
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
