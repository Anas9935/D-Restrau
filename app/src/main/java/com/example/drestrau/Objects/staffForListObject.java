package com.example.drestrau.Objects;

public class staffForListObject {
    private String name;
    private String picUrl;
    private String staffId;
    private String staffUid;
    private long contact;
    private int designaton;
    private int paydue;
    public staffForListObject(String nme,String url,String sid,String uid,long phno,int desig){
        name=nme;
        picUrl=url;
        staffId=sid;
        contact=phno;
        designaton=desig;
        staffUid=uid;
    }

    public int getPaydue() {
        return paydue;
    }

    public void setPaydue(int paydue) {
        this.paydue = paydue;
    }

    public String getStaffUid() {
        return staffUid;
    }

    public void setContact(long contact) {
        this.contact = contact;
    }

    public int getDesignaton() {
        return designaton;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getName() {
        return name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public long getContact() {
        return contact;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
