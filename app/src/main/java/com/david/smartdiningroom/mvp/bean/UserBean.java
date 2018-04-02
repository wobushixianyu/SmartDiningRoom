package com.david.smartdiningroom.mvp.bean;

public class UserBean {
    private int uId; //用户id
    private String uName; //用户名称
    private String uSex; //用户性别
    private int uAge; //用户年龄
    private int uBmonth; //用户生日-月
    private int uBday; //用户生日-日

    public UserBean(int uId, String uName, String uSex, int uAge, int uBmonth, int uBday) {
        this.uId = uId;
        this.uName = uName;
        this.uSex = uSex;
        this.uAge = uAge;
        this.uBmonth = uBmonth;
        this.uBday = uBday;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuSex() {
        return uSex;
    }

    public void setuSex(String uSex) {
        this.uSex = uSex;
    }

    public int getuAge() {
        return uAge;
    }

    public void setuAge(int uAge) {
        this.uAge = uAge;
    }

    public int getuBmonth() {
        return uBmonth;
    }

    public void setuBmonth(int uBmonth) {
        this.uBmonth = uBmonth;
    }

    public int getuBday() {
        return uBday;
    }

    public void setuBday(int uBday) {
        this.uBday = uBday;
    }

    @Override
    public String toString() {
        return "用户{"+uName+"}登录成功";
    }
}
