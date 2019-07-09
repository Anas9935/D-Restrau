package com.example.drestrau.Objects;

public class paymentObject {
    private String uid;
    private String pid;
    private int total;
    private int status;
    private int mode;
    private long timestamp;

    public paymentObject(){}
    public paymentObject(String userid,String paymentid,int totalamt,int stts,int mde,long time){
        uid=userid;
        pid=paymentid;
        total=totalamt;
        status=stts;
        mode=mde;
        timestamp=time;
    }

    public String getUid() {
        return uid;
    }

    public int getStatus() {
        return status;
    }

    public int getMode() {
        return mode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPid() {
        return pid;
    }

    public int getTotal() {
        return total;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
