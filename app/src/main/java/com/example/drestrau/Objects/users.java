package com.example.drestrau.Objects;

public class users {
    private String uid=null;
    private String rid;
    private String name=null;
    private String address1=null;
    private String address2=null;
    private long phno1;
    private long phno2;
    private int desig=-1;
    private String email=null;
    private long pincode;
    private pres_rest pres_data;
    public users(){}


    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getAddress2() {
        return address2;
    }

    public int getDesig() {
        return desig;
    }

    public long getPhno1() {
        return phno1;
    }

    public long getPhno2() {
        return phno2;
    }

    public String getAddress1() {
        return address1;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setDesig(int desig) {
        this.desig = desig;
    }

    public void setPhno1(long phno1) {
        this.phno1 = phno1;
    }

    public void setPhno2(long phno2) {
        this.phno2 = phno2;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public pres_rest getPres_data() {
        return pres_data;
    }

    public void setPres_data(pres_rest pres_data) {
        this.pres_data = pres_data;
    }
}
