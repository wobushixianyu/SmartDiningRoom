package com.david.smartdiningroom.mvp.bean;

import java.util.Observable;
import java.util.Observer;

import timber.log.Timber;

public class DishManHouseObserver implements Observer{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishManHouseObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Float){
            Timber.e("======>通知【"+this.name+"】:"+arg);
        }
    }
}
