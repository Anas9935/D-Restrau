package com.example.drestrau.Objects;

public class staffObject {
    private String uid;
    private String sid;
    private String rid;
    private String name1,name2,name3;
    private int gender;
    private int salary;
    private long doj;
    private long dob;
    private String address1,address2,address3;
    private long pincode;
    private String email;
    private long contact1,contact2;
    private int designation;
    private String picUrl;
    public staffObject(){
    }
    public staffObject(String n1,String n2,String n3,int gen,int sal,long dob,long doj,String add1,String add2,String add3,long pin,String eml,long con1,long con2,int desig){
        name1=n1;
        name2=n2;
        name3=n3;
        gender=gen;
        salary=sal;
        this.doj=doj;
        this.dob=dob;
        address1=add1;
        address2=add2;
        address3=add3;
        pincode=pin;
        email=eml;
        contact1=con1;
        contact2=con2;
        designation=desig;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getRid() {
        return rid;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public int getGender() {
        return gender;
    }

    public int getDesignation() {
        return designation;
    }

    public int getSalary() {
        return salary;
    }

    public long getContact1() {
        return contact1;
    }

    public long getContact2() {
        return contact2;
    }

    public long getDob() {
        return dob;
    }

    public long getDoj() {
        return doj;
    }

    public long getPincode() {
        return pincode;
    }

    public String getAddress3() {
        return address3;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getName3() {
        return name3;
    }

    public String getSid() {
        return sid;
    }


    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setContact1(long contact1) {
        this.contact1 = contact1;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public void setContact2(long contact2) {
        this.contact2 = contact2;
    }

    public void setDesignation(int designation) {
        this.designation = designation;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    public void setDoj(long doj) {
        this.doj = doj;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
