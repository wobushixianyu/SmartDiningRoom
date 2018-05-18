package com.david.smartdiningroom.mvp.bean;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.utils.SdrUtils;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.fastadapter.utils.EventHookUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyOrderClasss extends AbstractItem<MyOrderClasss,MyOrderClasss.ViewHolder>{

    /**
     * id : 0
     * order_id : 201804241104abc0101
     * time : 1524539083000
     * shop_id : 1001
     * img : http://img1.gtimg.com/cq/pics/hv1/104/207/1894/123210239.jpg
     * shop_name : 乡村基(天府四街餐厅)
     * price : 88.0
     * status : 1
     */

    private String order_id;
    private long time;
    private int shop_id;
    private String img;
    private String shop_name;
    private double price;
    private int status;
    private int id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.id_my_order_list;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_my_order_list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View view;

        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_orderId)
        TextView mTvOrderId;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.tv_status)
        TextView mTvStatus;
        @BindView(R.id.tv_evaluate)
        TextView mTvEvaluate;
        @BindView(R.id.img_logo)
        CircleImageView mImgLogo;

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
        Picasso.with(holder.view.getContext()).load(getImg()).into(holder.mImgLogo);
        holder.mTvTime.setText(SdrUtils.formatDate(getTime()));
        holder.mTvName.setText(getShop_name());
        holder.mTvOrderId.setText("订单号："+getOrder_id());
        holder.mTvPrice.setText("¥"+getPrice());
        switch (getStatus()){
            case 1:
                holder.mTvStatus.setText("等待接单");
                holder.mTvStatus.setTextColor(Color.BLUE);
                holder.mTvStatus.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.mTvStatus.setText("已接单");
                holder.mTvStatus.setTextColor(Color.parseColor("#FF7449"));
                holder.mTvStatus.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.mTvStatus.setVisibility(View.GONE);
                holder.mTvEvaluate.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.mTvStatus.setText("已完成");
                holder.mTvStatus.setTextColor(Color.parseColor("#777777"));
                holder.mTvStatus.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.mTvTime.setText("");
        holder.mTvName.setText("");
        holder.mTvOrderId.setText("");
        holder.mTvPrice.setText("");
        holder.mTvStatus.setText("");
        holder.mTvEvaluate.setVisibility(View.GONE);
    }

    public static class OnEvaluateClickListener extends ClickEventHook<MyOrderClasss>{

        private OnItemClickListener listener;

        public OnEvaluateClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public List<View> onBindMany(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof ViewHolder){
                return EventHookUtil.toList(((ViewHolder) viewHolder).mTvEvaluate);
            }
            return super.onBindMany(viewHolder);
        }

        @Override
        public void onClick(View v, int position, FastAdapter<MyOrderClasss> fastAdapter, MyOrderClasss item) {
            listener.onEvaluateClick(item.getId(),item.getId());
        }
    }

    public interface OnItemClickListener{
        void onEvaluateClick(int orderId,int shopId);
    }
}
