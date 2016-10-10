package com.firebox.androidapp.entity;

import java.util.ArrayList;

public class FullProduct {
    public int id;
    public String name;
    public String subtitle;
    public ArrayList<String> keyFeatures = new ArrayList<String>();
    public ArrayList<String> imagesUrl = new ArrayList<String>();
    public Integer chartPosition;
    public Integer birthday;
    //public ArrayList<Integer> tags;
    public ArrayList<ProductSku> skus = new  ArrayList<ProductSku>();


    public FullProduct(int id)
    {
        //}, String name, String imageUrl, Integer chartPosition, Integer birthday, ArrayList<Integer> tags) {
        this.id = id;
    }

    public FullProduct setName(String name)
    {
        this.name = name;
        return this;
    }

    public FullProduct addImage(String imageUrl)
    {
        this.imagesUrl.add(imageUrl);
        return this;
    }

    public FullProduct addSku(ProductSku sku)
    {
        this.skus.add(sku);
        return this;
    }

}
