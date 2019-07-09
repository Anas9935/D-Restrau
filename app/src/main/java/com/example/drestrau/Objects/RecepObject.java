package com.example.drestrau.Objects;

public class RecepObject {
    private String pid;
    private String uid;
    private String rid;
    private int amount;
    private int status;
    private int mode;
    private int CustStat;
    public  RecepObject(String pd,String ud,String rd,int amt,int sts,int mde,int custst){
        pid=pd;
        uid=ud;
        rid=rd;
        amount=amt;
        status=sts;
        mode=mde;
        CustStat=custst;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUid() {
        return uid;
    }

    public int getMode() {
        return mode;
    }

    public int getStatus() {
        return status;
    }

    public int getAmount() {
        return amount;
    }

    public int getCustStat() {
        return CustStat;
    }

    public String getPid() {
        return pid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCustStat(int custStat) {
        CustStat = custStat;
    }
}
