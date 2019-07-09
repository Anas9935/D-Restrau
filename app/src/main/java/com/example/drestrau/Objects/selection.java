package com.example.drestrau.Objects;

public class selection {
    private String uid;
    private String choice;
    private int timeslot;
    private long date;
    private int nop;
    private int type;   //reserve or Takeaway

    public selection() {
    }

    public selection(String id, String ch, int time,int mode, long dat, int people) {
        uid = id;
        choice = ch;
        timeslot = time;
        date = dat;
        nop = people;
        type=mode;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getUid() {
        return uid;
    }

    public int getNop() {
        return nop;
    }

    public int getTimeslot() {
        return timeslot;
    }

    public long getDate() {
        return date;
    }

    public String getChoice() {
        return choice;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setNop(int nop) {
        this.nop = nop;
    }

    public void setTimeslot(int timeslot) {
        this.timeslot = timeslot;
    }
}
