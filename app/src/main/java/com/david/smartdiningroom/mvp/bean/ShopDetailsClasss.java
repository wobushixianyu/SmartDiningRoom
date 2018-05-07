package com.david.smartdiningroom.mvp.bean;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.smartdiningroom.R;
import com.google.gson.annotations.Expose;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.fastadapter.utils.EventHookUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopDetailsClasss extends AbstractItem<ShopDetailsClasss, ShopDetailsClasss.ViewHolder> {

    /**
     * id : 1001
     * name : 泡菜回锅肉套饭
     * img : https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1077582164,2006451888&fm=27&gp=0.jpg
     * price : 18
     */

    private int id;
    private String name;
    private String img;
    private double price;
    @Expose(serialize = false) private int num = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.id_food_list;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_food_list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        @BindView(R.id.img_food)
        ImageView mImgFood;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.iv_reduce)
        ImageView mIvReduce;
        @BindView(R.id.tv_num)
        TextView mTvNum;
        @BindView(R.id.iv_add)
        ImageView mIvAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.view = itemView;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        Picasso.with(holder.view.getContext()).load(getImg()).into(holder.mImgFood);
        holder.mTvName.setText(getName());
        holder.mTvPrice.setText("¥："+getPrice());
        holder.mTvNum.setText(""+num);
        holder.mIvAdd.setTag(holder);
        holder.mIvReduce.setTag(holder);
        if (num > 0){
            holder.mIvReduce.setVisibility(View.VISIBLE);
            holder.mTvNum.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.mImgFood.setImageResource(R.mipmap.img_load_error);
        holder.mTvName.setText("");
        holder.mTvPrice.setText("");
        holder.mTvNum.setText("");
        holder.mIvAdd.setTag("");
        holder.mIvReduce.setTag("");
        holder.mIvReduce.setVisibility(View.INVISIBLE);
        holder.mTvNum.setVisibility(View.INVISIBLE);
    }

    public static class onAddReduceClickEvent extends ClickEventHook<ShopDetailsClasss>{

        private OnItemClickListener listener;

        public onAddReduceClickEvent(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public List<View> onBindMany(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof ShopDetailsClasss.ViewHolder) {
                ((ViewHolder) viewHolder).mIvAdd.setTag(viewHolder);
                ((ViewHolder) viewHolder).mIvAdd.setTag(viewHolder);
                return EventHookUtil.toList(((ViewHolder) viewHolder).mIvAdd,((ViewHolder) viewHolder).mIvReduce);
            }
            return super.onBindMany(viewHolder);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v, int position, FastAdapter<ShopDetailsClasss> fastAdapter, ShopDetailsClasss item) {
            if (v.getTag() instanceof ViewHolder){
                ViewHolder holder = (ViewHolder) v.getTag();
                switch (v.getId()){
                    case R.id.iv_add:
                        item.num++;
                        holder.mTvNum.setText(""+item.num);
                        if (item.num > 0){
                            holder.mTvNum.setVisibility(View.VISIBLE);
                            holder.mIvReduce.setVisibility(View.VISIBLE);
                        }else {
                            holder.mTvNum.setVisibility(View.INVISIBLE);
                            holder.mIvReduce.setVisibility(View.INVISIBLE);
                        }
                        listener.onAddClick(item.getPrice(),item.getId());
                        break;
                    case R.id.iv_reduce:
                        item.num--;
                        holder.mTvNum.setText(""+item.num);
                        if (item.num <= 0){
                            holder.mTvNum.setText("0");
                            holder.mTvNum.setVisibility(View.INVISIBLE);
                            holder.mIvReduce.setVisibility(View.INVISIBLE);
                        }else {
                            holder.mTvNum.setVisibility(View.VISIBLE);
                            holder.mIvReduce.setVisibility(View.VISIBLE);
                        }
                        listener.onReduceClick(item.getPrice(),item.getId());
                        break;
                }
            }
        }
    }

    public interface OnItemClickListener{
        void onAddClick(double price,int id);
        void onReduceClick(double price,int id);
    }
}
