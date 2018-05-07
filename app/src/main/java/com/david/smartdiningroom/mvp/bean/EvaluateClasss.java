package com.david.smartdiningroom.mvp.bean;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.david.smartdiningroom.R;
import com.david.smartdiningroom.utils.SdrUtils;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EvaluateClasss extends AbstractItem<EvaluateClasss,EvaluateClasss.ViewHolder>{

    /**
     * id : 1001
     * time : 1524539083000
     * content : 非常好吃，下次再来
     */

    private int id;
    private long time;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.id_evaluate_list;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_evaluate_list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_content)
        TextView mTvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        holder.mTvTime.setText(SdrUtils.formatDate(getTime()));
        holder.mTvContent.setText("         "+getContent());
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.mTvTime.setText("");
        holder.mTvContent.setText("");
    }
}
