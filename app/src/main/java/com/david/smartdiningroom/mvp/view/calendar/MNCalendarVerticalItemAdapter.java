package com.david.smartdiningroom.mvp.view.calendar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boleme.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;

/**
 * @author lenovo
 * @date 2018-03-01
 */
public class MNCalendarVerticalItemAdapter
    extends RecyclerView.Adapter<MNCalendarVerticalItemAdapter.MyViewHolder> {

  private ArrayList<MNCalendarItemModel> mDatas;

  private LayoutInflater layoutInflater;

  private Context context;

  private Calendar currentCalendar;
  private MNCalendarVerticalAdapter adapter;
  private Calendar mCalendar = Calendar.getInstance(Locale.CHINA);

  private Date nowDate = new Date();
  /** 配置信息 */
  private MNCalendarVerticalConfig mnCalendarVerticalConfig;

  MNCalendarVerticalItemAdapter(
      Context context,
      ArrayList<MNCalendarItemModel> mDatas,
      Calendar currentCalendar,
      MNCalendarVerticalAdapter adapter,
      MNCalendarVerticalConfig mnCalendarVerticalConfig) {
    this.context = context;
    this.mDatas = mDatas;
    this.currentCalendar = currentCalendar;
    this.adapter = adapter;
    this.mnCalendarVerticalConfig = mnCalendarVerticalConfig;
    layoutInflater = LayoutInflater.from(this.context);

    removeBeforeDate();
  }
  /** 移除今天以及之前的日期 */
  private void removeBeforeDate() {
    if (mDatas == null || mDatas.isEmpty() || nowDate.getTime() < mDatas.get(0).getDate().getTime()) {
      return;
    }
    ListIterator<MNCalendarItemModel> listIterator = mDatas.listIterator();
    while (listIterator.hasNext()) {
      MNCalendarItemModel itemModel = listIterator.next();
      Date date = itemModel.getDate();
      mCalendar.setTime(date);
      if (isSameDay(date) || isBeforeDate()) {
        listIterator.remove();
      }
    }
    if (mDatas != null && !mDatas.isEmpty()) {
      mCalendar.setTime(mDatas.get(0).getDate());
      // 当天是星期天则不补
      if (mCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        return;
      }
      fixDate();
    }
  }

  /** 明天不是星期日的情况下，补足星期日至明天前中间这段的日期 使得显示不会错乱 */
  private void fixDate() {
    // 需要补多少项
    int fixNum;
    Date date = mDatas.get(0).getDate();
    Calendar mCurrentCalendar = Calendar.getInstance();
    mCurrentCalendar.setTime(date);
    // 得到明天是星期几
    int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
    switch (dayOfWeek - 1) {
      case Calendar.MONDAY:
        fixNum = 1;
        break;
      case Calendar.TUESDAY:
        fixNum = 2;
        break;
      case Calendar.WEDNESDAY:
        fixNum = 3;
        break;
      case Calendar.THURSDAY:
        fixNum = 4;
        break;
      case Calendar.FRIDAY:
        fixNum = 5;
        break;
      case Calendar.SATURDAY:
        fixNum = 6;
        break;
      default:
        fixNum = 0;
        break;
    }
    if (fixNum != 0) {
      for (int i = fixNum; i >= 0; i--) {
        MNCalendarItemModel model = new MNCalendarItemModel();
        mCurrentCalendar.setTime(date);
        switch (i) {
          case 0:
            mCurrentCalendar.add(Calendar.DAY_OF_MONTH, -1);
            break;
          case 1:
            mCurrentCalendar.add(Calendar.DAY_OF_MONTH, -2);
            break;
          case 2:
            mCurrentCalendar.add(Calendar.DAY_OF_MONTH, -3);
            break;
          case 3:
            mCurrentCalendar.add(Calendar.DAY_OF_MONTH, -4);
            break;
          case 4:
            mCurrentCalendar.add(Calendar.DAY_OF_MONTH, -5);
            break;
          case 5:
            mCurrentCalendar.add(Calendar.DAY_OF_MONTH, -6);

            break;
          default:
            break;
        }
        model.setHide(true);
        model.setDate(mCurrentCalendar.getTime());
        mDatas.add(0, model);
      }
    }
  }

  /**
   * 返回制定的日期与当前是否是同一天
   *
   * @param date 校验的日期
   * @return 是否为同一天
   */
  private boolean isSameDay(Date date) {
    DateTime dateTime = new DateTime(date.getTime());
    return dateTime.isEqual(nowDate.getTime());
  }
  /**
   * 检查指定日期是否在当前日期之前
   *
   * @return true 之前，false 之后
   */
  private boolean isBeforeDate() {
    DateTime dateTime = new DateTime(mCalendar.getTime());
    return dateTime.isBeforeNow();
  }

  /** @return 指定的日期与当前日期是否为同一个月 */
  private boolean checkIsSameMonth(Date d) {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTimeInMillis(System.currentTimeMillis());
    c2.setTimeInMillis(d.getTime());
    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
        && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
  }

  @Override
  public MyViewHolder onCreateViewHolder(
      ViewGroup parent, int viewType) {
    View inflate = layoutInflater.inflate(R.layout.mn_item_calendar_vertical_item, parent, false);
    return new MyViewHolder(inflate);
  }

  @Override
  public void onBindViewHolder(
          MyViewHolder myViewHolder, int position) {
    MNCalendarItemModel mnCalendarItemModel = mDatas.get(position);
    Date datePosition = mnCalendarItemModel.getDate();
    Lunar lunar = mnCalendarItemModel.getLunar();
    myViewHolder.itemView.setVisibility(View.VISIBLE);
    myViewHolder.tv_small.setVisibility(View.GONE);
    myViewHolder.iv_bg.setVisibility(View.GONE);
    myViewHolder.tv_small.setText("");
    myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorSolar());
    myViewHolder.tv_small.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorLunar());
    myViewHolder.tvDay.setText(String.valueOf(datePosition.getDate()));
    // 不是本月的隐藏
    Date currentDate = currentCalendar.getTime();
    if (datePosition.getMonth() != currentDate.getMonth() || mDatas.get(position).isHide()) {
      myViewHolder.itemView.setVisibility(View.GONE);
    }

    // 阴历的显示
    if (mnCalendarVerticalConfig.isMnCalendar_showLunar()) {
      myViewHolder.tv_small.setVisibility(View.VISIBLE);
      String lunarDayString = LunarCalendarUtils.getLunarDayString(lunar.lunarDay);
      myViewHolder.tv_small.setText(lunarDayString);
    } else {
      myViewHolder.tv_small.setVisibility(View.GONE);
    }

    Log.e("time", adapter.startDate.getTime() / 1000 + ":" + datePosition.getTime() / 1000);
    // 判断是不是点击了起始日期
    if (adapter.startDate != null
        && adapter.startDate.getTime() / 1000 == datePosition.getTime() / 1000) {
      myViewHolder.iv_bg.setVisibility(View.VISIBLE);
      myViewHolder.iv_bg.setBackgroundResource(R.drawable.mn_selected_bg_start);
      myViewHolder.tv_small.setVisibility(View.GONE);
      myViewHolder.tv_small.setText("开始");
      myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
      myViewHolder.tv_small.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
      // 动态修改颜色
      GradientDrawable myGrad = (GradientDrawable) myViewHolder.iv_bg.getBackground();
      myGrad.setColor(mnCalendarVerticalConfig.getMnCalendar_colorStartAndEndBg());
    }
    if (adapter.endDate != null && adapter.endDate.getTime() == datePosition.getTime()) {
      myViewHolder.iv_bg.setVisibility(View.VISIBLE);
      myViewHolder.iv_bg.setBackgroundResource(R.drawable.mn_selected_bg_center);
      myViewHolder.tv_small.setVisibility(View.GONE);
      myViewHolder.tv_small.setText("结束");
      myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
      myViewHolder.tv_small.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
      // 动态修改颜色
      GradientDrawable myGrad = (GradientDrawable) myViewHolder.iv_bg.getBackground();
      myGrad.setColor(mnCalendarVerticalConfig.getMnCalendar_colorStartAndEndBg());
    }

    // 判断是不是大于起始日期
    if (adapter.startDate != null && adapter.endDate != null) {
      if (datePosition.getTime() > adapter.startDate.getTime()
          && datePosition.getTime() < adapter.endDate.getTime()) {
        myViewHolder.iv_bg.setVisibility(View.VISIBLE);
        myViewHolder.iv_bg.setBackgroundResource(R.drawable.mn_selected_bg_start);
        myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
        myViewHolder.tv_small.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeText());
        // 动态修改颜色
        GradientDrawable myGrad = (GradientDrawable) myViewHolder.iv_bg.getBackground();
        myGrad.setColor(mnCalendarVerticalConfig.getMnCalendar_colorRangeBg());
      }
    }
    /***************///主要修改此段代码
    if(MNCalendarVertical.mIsMark){//需求修改标志位
      if(mnCalendarVerticalConfig.isStartTime()){//如果是选择开始时间，周6高亮，其他置灰
        if(getDaysInWeek(datePosition) != Calendar.SATURDAY) {
          myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorBeforeToday());
          mnCalendarItemModel.setClick(false);
        }else{
          mnCalendarItemModel.setClick(true);
        }
      }else{//如果是选择结束时间
        if(getDaysInWeek(datePosition) != Calendar.FRIDAY || position == 0) {//如果是选择结束时间，周5高亮，其他置灰,因为是必须有一周，所以结束时间第一排的周5不能选
          myViewHolder.tvDay.setTextColor(mnCalendarVerticalConfig.getMnCalendar_colorBeforeToday());
          mnCalendarItemModel.setClick(false);
        }else{
          myViewHolder.itemView.setClickable(true);
        }
      }
    }/**************/

    myViewHolder.itemView.setTag(myViewHolder.getLayoutPosition());
    myViewHolder.itemView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            MNCalendarItemModel mnCalendarItemModel = mDatas.get((int) view.getTag());
            if(MNCalendarVertical.mIsMark){//控件修改标志位
              if(!mnCalendarItemModel.isClick()){//点击事件被关闭，不处理事件
                return;
              }
            }
            Date dateClick = mnCalendarItemModel.getDate();
            // 必须大于今天
            if (dateClick.getTime() < nowDate.getTime()) {
              Toast.makeText(context, "选择的日期必须大于今天", Toast.LENGTH_SHORT).show();
              return;
            }
            if (adapter.startDate != null && adapter.endDate != null) {
              adapter.startDate = null;
              adapter.endDate = null;
            }
            if (!mnCalendarVerticalConfig.isIssingine()) {
              // 是否是单选
              if (adapter.startDate == null) {
                adapter.startDate = dateClick;
              } else {
                // 判断结束位置是不是大于开始位置
                long deteClickTime = dateClick.getTime();
                long deteStartTime = adapter.startDate.getTime();
                if (deteClickTime <= deteStartTime) {
                  adapter.startDate = dateClick;
                } else {
                  adapter.endDate = dateClick;
                }
              }
            }  else {
                if (dateClick != null) {
                  adapter.startDate = dateClick;
                }
              }
            // 选取通知
            adapter.notifyChoose();
            // 刷新
            notifyDataSetChanged();
            adapter.notifyDataSetChanged();
          }
        });
  }

  /**
   * 获取给定日期是星期几
   */
  private int getDaysInWeek(Date date){
    Calendar instance = Calendar.getInstance();
    instance.setTime(date);
    int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
    return dayOfWeek;
  }

  @Override
  public int getItemCount() {
    return mDatas.size();
  }

  static class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView tvDay;
    private TextView tv_small;
    private ImageView iv_bg;

    MyViewHolder(View itemView) {
      super(itemView);
      tvDay = (TextView) itemView.findViewById(R.id.tvDay);
      tv_small = (TextView) itemView.findViewById(R.id.tv_small);
      iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
    }
  }
}
