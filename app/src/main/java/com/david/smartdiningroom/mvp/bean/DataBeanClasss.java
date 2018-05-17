package com.david.smartdiningroom.mvp.bean;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.david.smartdiningroom.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DataBeanClasss extends AbstractItem<DataBeanClasss,DataBeanClasss.ViewHolder>{

    /**
     * id : 1001
     * name : 泡菜回锅肉套饭
     * img : http://img01.taopic.com/141107/235086-14110FU12838.jpg
     * num : 18
     */

    private int id;
    private String name;
    private String img;
    private int num;

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.id_order_details;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_order_details_list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View view;
        @BindView(R.id.img_food)
        CircleImageView mImgFood;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.tv_num)
        TextView mTvNum;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        Picasso.with(holder.view.getContext()).load(getImg()).into(holder.mImgFood);
        holder.mTvName.setText(getName());
        holder.mTvPrice.setVisibility(View.GONE);
        holder.mTvNum.setText("月售："+getNum()+"份");
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.mImgFood.setImageResource(R.mipmap.img_load_error);
        holder.mTvNum.setText("--");
        holder.mTvName.setText("");
        holder.mTvPrice.setVisibility(View.GONE);
    }
}
