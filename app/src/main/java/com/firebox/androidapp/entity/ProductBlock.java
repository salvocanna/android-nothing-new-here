package com.firebox.androidapp.entity;

public class ProductBlock {
    public int id;
    public String name;
    public String imageUrl;
    public Integer chartPosition;
    public Integer birthday;

    public ProductBlock(int id, String name, String imageUrl, Integer chartPosition, Integer birthday) {
        this.id       = id;
        this.name     = name;
        this.imageUrl = imageUrl;
        this.chartPosition = chartPosition;
        this.birthday = birthday;
    }
}
