package com.david.smartdiningroom.mvp.bean;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.david.smartdiningroom.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreBeanClasss extends AbstractItem<StoreBeanClasss, StoreBeanClasss.ViewHolder> {

    /**
     * shop_id : 1008
     * name : 寿司兄弟(银泰城店)
     * sales_volume : 662
     * star_level : 4.8
     * address : 益州大道中段1999号负一楼功夫干锅对面
     * latitude : 31.231341
     * longitude : 103.323131
     * average_price : 17
     * phone : 13613134567
     * img : http://f10.baidu.com/it/u=2066042881,3760618387&fm=72
     */

    private int shop_id;
    private String name;
    private int sales_volume;
    private double star_level;
    private String address;
    private double latitude;
    private double longitude;
    private int average_price;
    private String phone;
    private String img;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(int sales_volume) {
        this.sales_volume = sales_volume;
    }

    public double getStar_level() {
        return star_level;
    }

    public void setStar_level(double star_level) {
        this.star_level = star_level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAverage_price() {
        return average_price;
    }

    public void setAverage_price(int average_price) {
        this.average_price = average_price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        Picasso.with(holder.view.getContext()).load(getImg()).into(holder.mStoreImg);
        holder.mStoreName.setText(getName());
        holder.mStoreInfo.setText("评价 " + getStar_level() + " 丨 " + "月售 " + getSales_volume() + " 丨 " + "人均 ¥" + getAverage_price());
        holder.mStoreAddress.setText("地址：" + getAddress());
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.mStoreImg.setImageResource(R.mipmap.img_load_error);
        holder.mStoreName.setText("");
        holder.mStoreInfo.setText("");
        holder.mStoreAddress.setText("");
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_store_list_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_store_list_layout;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        @BindView(R.id.store_img)
        AppCompatImageView mStoreImg;
        @BindView(R.id.store_name)
        AppCompatTextView mStoreName;
        @BindView(R.id.store_info)
        AppCompatTextView mStoreInfo;
        @BindView(R.id.store_address)
        AppCompatTextView mStoreAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }
    }
}
