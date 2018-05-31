package com.david.smartdiningroom.mvp.view.calendar;

import java.util.Date;

/**
 * Created by lenovo on 2018-03-01.
 */

public class MNCalendarItemModel {

    public MNCalendarItemModel() {
    }

    public MNCalendarItemModel(Date date, Lunar lunar) {
        mDate = date;
        mLunar = lunar;
    }

    private Date mDate;
    private Lunar mLunar;
    private boolean hide;
    private boolean isClick = true;//该日期Item是否可点击

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Lunar getLunar() {
        return mLunar;
    }

    public void setLunar(Lunar lunar) {
        mLunar = lunar;
    }
}
