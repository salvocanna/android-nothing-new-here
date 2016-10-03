package com.firebox.androidapp.entity;

public class ProductBlock {
    public int id;
    public String name;
    public String imageUrl;
    public Integer chartPosition;

    public ProductBlock(int id, String name, String imageUrl, Integer chartPosition) {
        this.id       = id;
        this.name     = name;
        this.imageUrl = imageUrl;
        this.chartPosition = chartPosition;
    }
}
