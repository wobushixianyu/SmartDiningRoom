package com.david.smartdiningroom.mvp.bean;

import java.util.Observable;

public class BigHouse extends Observable {
    private float price;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.setChanged();
        this.notifyObservers(price);
        this.price = price;
    }

    public BigHouse(float price) {
        this.price = price;
    }

    public String toString(){
        return "当前房价为："+this.price+"/平米";
    }
}
