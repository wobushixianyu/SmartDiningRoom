package com.david.smartdiningroom.mvp.view.calendar;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.smartdiningroom.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lenovo on 2018-03-01.
 */

public class MNCalendarVerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HashMap<String, ArrayList<MNCalendarItemModel>> mDatas;

    private LayoutInflater layoutInflater;

    private Context context;

    private Calendar currentCalendar;

    private MNCalendarVerticalConfig mnCalendarVerticalConfig;

    private int offset = 0;

    public Date startDate = null;
    public Date endDate = null;

    public MNCalendarVerticalAdapter(Context context, HashMap<String, ArrayList<MNCalendarItemModel>> mDatas, Calendar currentCalendar, MNCalendarVerticalConfig mnCalendarVerticalConfig) {
        this.context = context;
        this.mDatas = mDatas;
        this.currentCalendar = currentCalendar;
        this.mnCalendarVerticalConfig = mnCalendarVerticalConfig;
        layoutInflater = LayoutInflater.from(this.context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.mn_item_calendar_vertical, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            //标题
            Calendar calendarTitle = (Calendar) currentCalendar.clone();
            calendarTitle.add(Calendar.MONTH, position + offset);
            calendarTitle.set(Calendar.HOUR_OF_DAY, 23);
            calendarTitle.set(Calendar.MINUTE, 59);
            calendarTitle.set(Calendar.SECOND, 59);
            int temp = calendarTitle.get(Calendar.MONTH);
            //如果第一个月，需要判断是否是最后一天，最后一天，需要向后偏移一个月
            if(position == 0){
                calendarTitle.add(Calendar.DAY_OF_MONTH, 1);
                offset = calendarTitle.get(Calendar.MONTH) - temp > 0 ? 1 : 0;
            }
            Date titleDate = calendarTitle.getTime();
            //设置标题的格式
            String mnCalendar_titleFormat = mnCalendarVerticalConfig.getMnCalendar_titleFormat();
            SimpleDateFormat sdf = new SimpleDateFormat(mnCalendar_titleFormat);
            String datatext = sdf.format(titleDate);
            myViewHolder.tv_item_title.setText(datatext);

            //设置标题的颜色
            myViewHolder.tv_item_title.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorTitle());

            //日期数据
            ArrayList<MNCalendarItemModel> dates = mDatas.get(String.valueOf(position));
            //初始化RecycleerView
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 7);
            myViewHolder.recyclerViewItem.setLayoutManager(gridLayoutManager);
            //初始化Adapter
            Calendar calendarItem = (Calendar) currentCalendar.clone();
            if(position == 0){//对应上面调整，判断最后一天，不显示当天
                calendarItem.add(Calendar.DAY_OF_MONTH, 1);
//                calendarItem.add(Calendar.MONTH, position);
            }else{
                calendarItem.add(Calendar.MONTH, position + offset);
            }

            MNCalendarVerticalItemAdapter mnCalendarVerticalItemAdapter = new MNCalendarVerticalItemAdapter(context, dates, calendarItem, this, mnCalendarVerticalConfig);
            myViewHolder.recyclerViewItem.setAdapter(mnCalendarVerticalItemAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_item_title;
        private RecyclerView recyclerViewItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_title = (TextView) itemView.findViewById(R.id.tv_item_title);
            recyclerViewItem = (RecyclerView) itemView.findViewById(R.id.recyclerViewItem);
        }
    }

    public OnCalendarRangeChooseListener onCalendarRangeChooseListener;

    public void setOnCalendarRangeChooseListener(OnCalendarRangeChooseListener onCalendarRangeChooseListener) {
        this.onCalendarRangeChooseListener = onCalendarRangeChooseListener;
        notifyDataSetChanged();
    }

    public void notifyChoose() {
        if (this.onCalendarRangeChooseListener != null) {
            if (mnCalendarVerticalConfig.isIssingine()) {
                onCalendarRangeChooseListener.onRangeDate(startDate, null);
            } else {
                if (startDate != null && endDate != null) {
                    onCalendarRangeChooseListener.onRangeDate(startDate, endDate);
                }
            }
        }
    }

    public void updateDatas(HashMap<String, ArrayList<MNCalendarItemModel>> mDatas, Calendar currentCalendar, MNCalendarVerticalConfig mnCalendarVerticalConfig, Date startDate, Date endDate) {
        this.mDatas = mDatas;
        this.currentCalendar = currentCalendar;
        this.mnCalendarVerticalConfig = mnCalendarVerticalConfig;
        this.startDate = startDate;
        this.endDate = null;
        notifyDataSetChanged();
    }

}
