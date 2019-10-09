package com.yalkansoft.bean;

import java.io.Serializable;

public class OrderFood implements Serializable {

    public static final int TYPE_RECAI = 1;
    public static final int TYPE_LIANGCAI = 2;
    public static final int TYPE_KAOROU = 3;
    public static final int TYPE_ZHUSHI = 4;
    public static final int TYPE_YINGLIAO = 5;
    public static final int TYPE_ZHUAFAN = 6;
    public static final int TYPE_HUOGUO = 7;

    private int id;
    private String name;
    private int price;
    private int number;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
