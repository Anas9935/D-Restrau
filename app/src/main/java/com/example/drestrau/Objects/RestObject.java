package com.example.drestrau.Objects;

public class RestObject {
    private String Rid;
    private String email;
    private  String name;
    private String manUid;
    private String add1;
    private String add2;
    private String add3;
    private long pin;
    private float rating;
    private long phno;
    private int seats2;
    private int seats4;
    private int seats6;

    // private int seatsOccupied;
    private int opening;
    private int closing;
    private String pictureUrl;
    private int offer;
    private String spec1;
    private String spec2;
    private String spec3;
    private String about;

    public RestObject(){}
    public RestObject(String nam,String mid,String a1,String a2,String a3,long pincode,float r,long phn,int seat2,int seat4,int seat6,int open,int close,String mail)
    {
        name=nam;
      //  Rid=id;
        manUid=mid;
        add1=a1;
        add2=a2;
        add3=a3;
        rating=r;
        phno=phn;
        seats2=seat2;
        seats4=seat4;
        seats6=seat6;
        opening=open;
        closing=close;
        pictureUrl=null;
        email=mail;
        pin=pincode;
    //    seatsOccupied=0;
    }

    public int getSeats2() {
        return seats2;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }


    public int getSeats4() {
        return seats4;
    }

    public int getSeats6() {
        return seats6;
    }

    public void setSeats2(int seats2) {
        this.seats2 = seats2;
    }

    public void setSeats4(int seats4) {
        this.seats4 = seats4;
    }

    public void setSeats6(int seats6) {
        this.seats6 = seats6;
    }

    public String getSpec1() {
        return spec1;
    }

    public String getSpec2() {
        return spec2;
    }

    public String getSpec3() {
        return spec3;
    }

    public void setSpec1(String spec1) {
        this.spec1 = spec1;
    }

    public void setSpec2(String spec2) {
        this.spec2 = spec2;
    }

    public void setSpec3(String spec3) {
        this.spec3 = spec3;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public String getEmail() {
        return email;
    }

    public long getPin() {
        return pin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPin(long pin) {
        this.pin = pin;
    }

    public String getRid() {
        return Rid;
    }

    public void setRid(String rid) {
        Rid = rid;
    }

    public float getRating() {
        return rating;
    }

    public int getClosing() {
        return closing;
    }

    public int getOpening() {
        return opening;
    }


    public long getPhno() {
        return phno;
    }

    public String getAdd1() {
        return add1;
    }

    public String getAdd2() {
        return add2;
    }

    public String getAdd3() {
        return add3;
    }

    public String getManUid() {
        return manUid;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public void setAdd2(String add2) {
        this.add2 = add2;
    }

    public void setAdd3(String add3) {
        this.add3 = add3;
    }

    public void setClosing(int closing) {
        this.closing = closing;
    }

    public void setManUid(String manUid) {
        this.manUid = manUid;
    }

    public void setOpening(int opening) {
        this.opening = opening;
    }

    public void setPhno(long phno) {
        this.phno = phno;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    public String getpictureUrl() {
        return pictureUrl;
    }

    public void setpictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
