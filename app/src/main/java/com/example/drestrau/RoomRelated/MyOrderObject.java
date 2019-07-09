package com.example.drestrau.RoomRelated;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "My_Orders")
public class MyOrderObject {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private  String rid;
    private String choice;
    private int amount;
    private int timeIndex;
    private long dateStamp;
    private String sid;
    private String pid;

    public MyOrderObject(String rid,String choice,int amount,int timeIndex,long dateStamp,String sid,String pid){
        this.rid=rid;
        this.choice=choice;
        this.amount=amount;
        this.timeIndex=timeIndex;
        this.dateStamp=dateStamp;
        this.sid=sid;
        this.pid=pid;
    }
    @Ignore
    public MyOrderObject(int id,String rid,String choice,int amount,int timeIndex,long dateStamp,String sid,String pid){
        this.id=id;
        this.rid=rid;
        this.choice=choice;
        this.amount=amount;
        this.timeIndex=timeIndex;
        this.dateStamp=dateStamp;
        this.sid=sid;
        this.pid=pid;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public int getTimeIndex() {
        return timeIndex;
    }

    public long getDateStamp() {
        return dateStamp;
    }

    public String getChoice() {
        return choice;
    }

    public String getRid() {
        return rid;
    }

    public String getPid() {
        return pid;
    }

    public String getSid() {
        return sid;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setDateStamp(long dateStamp) {
        this.dateStamp = dateStamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setTimeIndex(int timeIndex) {
        this.timeIndex = timeIndex;
    }
}
