package com.david.smartdiningroom.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.david.smartdiningroom.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageHeaderView extends AbstractItem<HomePageHeaderView, HomePageHeaderView.ViewHolder> implements BaseSliderView.OnSliderClickListener {
    private Context mContext;
    private OnSliderClickListener mListener;

    public HomePageHeaderView(Context mContext, OnSliderClickListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.id_banner;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.home_page_header_view;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        mListener.onSliderClick();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.slider_banner)
        SliderLayout mSliderLayout;
        @BindView(R.id.pager_indicator)
        PagerIndicator mPagerIndicator;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        HashMap<String, String> url_maps = new HashMap<>();
        url_maps.put("精挑细选为健康 精工细作出美味", "http://pic72.nipic.com/file/20150718/12724132_001411192067_2.png");
        url_maps.put("吃货的春天", "http://img.zcool.cn/community/013b92556c7a810000004383d997f6.jpg@2o.jpg");
        url_maps.put("舌尖上的美味", "http://bpic.ooopic.com/15/82/51/15825184-d2c0ad1f98d01e04c7ef4211a0f51fb6-3.jpg");
        url_maps.put("美味大虾", "http://img.zcool.cn/community/0125e6565553fb32f87512f6763192.jpg@1280w_1l_2o_100sh.png");

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(mContext);
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            holder.mSliderLayout.addSlider(textSliderView);
        }
        holder.mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        holder.mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        holder.mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        holder.mSliderLayout.setDuration(4000);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.mSliderLayout.removeAllSliders();
    }

    public interface OnSliderClickListener {
        void onSliderClick();
    }
}
