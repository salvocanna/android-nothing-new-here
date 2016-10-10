package com.firebox.androidapp.entity;

import java.util.ArrayList;

public class ProductBlock {
    public int id;
    public String name;
    public String imageUrl;
    public Integer chartPosition;
    public Integer birthday;
    public ArrayList<Integer> tags;

    public ProductBlock(int id, String name, String imageUrl, Integer chartPosition, Integer birthday, ArrayList<Integer> tags) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.chartPosition = chartPosition;
        this.birthday = birthday;
        this.tags = tags;
    }

}
