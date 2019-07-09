package com.example.drestrau.Objects;

public class menuObject {
    private String fid;
    private String name;
    private String info;
    private float price;
    private float rating;
    private int offer;
    private String picUrl;
    private int type;
    public menuObject(){}
    public menuObject(String n,String inf,float pr,float rat,int off,String url,int tp){
        name=n;
        info=inf;
        price=pr;
        rating=rat;
        offer=off;
        picUrl=url;
        type=tp;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getRating() {
        return rating;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public int getOffer() {
        return offer;
    }

    public float getPrice() {
        return price;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
