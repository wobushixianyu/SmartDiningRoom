package com.david.smartdiningroom.mvp.view.calendar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.david.smartdiningroom.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lenovo on 2018-03-01.
 */

public class MNCalendarVertical extends LinearLayout {
    public static final boolean mIsMark = false;//标志位，目前的修改后续可能不适用，需要修改回来

    private Context context;
    private RecyclerView recyclerViewCalendar;
    private LinearLayout ll_week;
    private TextView tv_week_01;
    private TextView tv_week_02;
    private TextView tv_week_03;
    private TextView tv_week_04;
    private TextView tv_week_05;
    private TextView tv_week_06;
    private TextView tv_week_07;

    private Date startDate;
    private Date endDate;
    private boolean isStartTime;

    private Calendar currentCalendar = Calendar.getInstance();

    private MNCalendarVerticalConfig mnCalendarVerticalConfig = new MNCalendarVerticalConfig.Builder().build();
    private MNCalendarVerticalAdapter mnCalendarVerticalAdapter;
    private HashMap<String, ArrayList<MNCalendarItemModel>> dataMap;
    private OnCalendarRangeChooseListener onCalendarRangeChooseListener;

    public MNCalendarVertical(Context context) {
        this(context, null);
    }

    public MNCalendarVertical(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MNCalendarVertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        currentCalendar = Calendar.getInstance();
        startDate = currentCalendar.getTime();
        endDate = currentCalendar.getTime();
        initViews();
        initCalendarDatas();
    }

    public boolean isStartTime() {
        return isStartTime;
    }

    public void setStartTime(boolean startTime) {
        isStartTime = startTime;
    }

    private void initViews() {
        //绑定View
        View.inflate(context, R.layout.mn_layout_calendar_vertical, this);
        recyclerViewCalendar = (RecyclerView) findViewById(R.id.recyclerViewCalendar);
        ll_week = (LinearLayout) findViewById(R.id.ll_week);
        tv_week_01 = (TextView) findViewById(R.id.tv_week_01);
        tv_week_02 = (TextView) findViewById(R.id.tv_week_02);
        tv_week_03 = (TextView) findViewById(R.id.tv_week_03);
        tv_week_04 = (TextView) findViewById(R.id.tv_week_04);
        tv_week_05 = (TextView) findViewById(R.id.tv_week_05);
        tv_week_06 = (TextView) findViewById(R.id.tv_week_06);
        tv_week_07 = (TextView) findViewById(R.id.tv_week_07);

        //初始化RecycleerView
        recyclerViewCalendar.setLayoutManager(new LinearLayoutManager(context));
    }


    private void initCalendarDatas() {
        //星期栏的显示和隐藏
        boolean mnCalendar_showWeek = mnCalendarVerticalConfig.isMnCalendar_showWeek();
        if (mnCalendar_showWeek) {
            ll_week.setVisibility(View.VISIBLE);
            tv_week_01.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_02.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_03.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_04.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_05.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_06.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
            tv_week_07.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorWeek());
        } else {
            ll_week.setVisibility(View.GONE);
        }

        int offset = 0;
        //日期集合
        dataMap = new HashMap<>(12);

        int mnCalendar_countMonth = mnCalendarVerticalConfig.getMnCalendar_countMonth();
        Calendar calendar = (Calendar) currentCalendar.clone();
        int value = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//获取这个月最后一天的值
        Calendar instance = Calendar.getInstance();
        int today = instance.get(Calendar.DAY_OF_MONTH);
        offset = value == today ? 1 : 0;//判断是否是最后一天，最后一天需要显示下个月，记录偏移量

        //计算日期
        for (int i = 0; i < mnCalendar_countMonth; i++) {
            int count7 = 0;
            ArrayList<MNCalendarItemModel> mDatas = new ArrayList<>();
            calendar = (Calendar) currentCalendar.clone();
            calendar.add(Calendar.MONTH, i + offset);
            //置于当月的第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            //获取当月第一天是星期几
            int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            //移动到需要绘制的第一天
            calendar.add(Calendar.DAY_OF_MONTH, -day_of_week);

            while (mDatas.size() < 6 * 7) {
                Date date = calendar.getTime();
                //阴历计算
                Lunar lunar = LunarCalendarUtils.solarToLunar(date);
                mDatas.add(new MNCalendarItemModel(date, lunar));
                //包含两个7就多了一行

                if (String.valueOf(calendar.getTime().getDate()).equals("7")) {
                    count7++;
                }
                //向前移动一天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            if (count7 >= 2) {
                ArrayList<MNCalendarItemModel> mDatas2 = new ArrayList<>();
                for (int j = 0; j < mDatas.size() - 7; j++) {
                    mDatas2.add(mDatas.get(j));
                }
                mDatas = new ArrayList<>();
                mDatas.addAll(mDatas2);
            }

            dataMap.put(String.valueOf(i), mDatas);

        }
        //设置Adapter
        initAdapter();
    }

    public Date getStartDate() {
        return mnCalendarVerticalAdapter.startDate;
    }

    public Date getEndDate() {
        return mnCalendarVerticalAdapter.endDate;
    }


    private void initAdapter() {
        if (mnCalendarVerticalAdapter == null) {
            mnCalendarVerticalAdapter = new MNCalendarVerticalAdapter(context, dataMap, currentCalendar, mnCalendarVerticalConfig);
            recyclerViewCalendar.setAdapter(mnCalendarVerticalAdapter);
        } else {
            mnCalendarVerticalAdapter.updateDatas(dataMap, currentCalendar, mnCalendarVerticalConfig, startDate, endDate);
        }
        if (onCalendarRangeChooseListener != null) {
            mnCalendarVerticalAdapter.setOnCalendarRangeChooseListener(onCalendarRangeChooseListener);
        }
    }

    public void setStartDate(Date date) {
        startDate = date;
        initCalendarDatas();
    }

    public void setEndDate(Date date) {
        endDate = date;
        initCalendarDatas();
    }

    /**
     * 设置配置文件
     *
     * @param config
     */
    public void setConfig(MNCalendarVerticalConfig config) {
        this.mnCalendarVerticalConfig = config;
        initCalendarDatas();
    }

    public MNCalendarVerticalConfig getMnCalendarVerticalConfig() {
        return mnCalendarVerticalConfig;
    }

    public void setOnCalendarRangeChooseListener(OnCalendarRangeChooseListener onCalendarRangeChooseListener) {
        this.onCalendarRangeChooseListener = onCalendarRangeChooseListener;
        if (mnCalendarVerticalAdapter != null) {
            mnCalendarVerticalAdapter.setOnCalendarRangeChooseListener(onCalendarRangeChooseListener);
        }
    }
}