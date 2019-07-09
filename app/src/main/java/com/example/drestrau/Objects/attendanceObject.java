package com.example.drestrau.Objects;

public class attendanceObject {
    private int dayCount;
    private int payDue;
    private int maxDayOfMonth;
    private long lastPaidDate;
    private int attendanceToday;
    public attendanceObject(){}
    public attendanceObject(int dc, int pd, int mdm, long presDate, int attToday){
        dayCount=dc;
        payDue=pd;
        maxDayOfMonth=mdm;
        lastPaidDate=presDate;
        attendanceToday=attToday;
    }

    public int getAttendenceToday() {
        return attendanceToday;
    }

    public int getDayCount() {
        return dayCount;
    }

    public int getMaxDayOfMonth() {
        return maxDayOfMonth;
    }

    public int getPayDue() {
        return payDue;
    }

    public long getLastPaidDate() {
        return lastPaidDate;
    }

    public void setAttendenceToday(int attendanceToday) {
        this.attendanceToday = attendanceToday;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public void setLastPaidDate(long lastPaidDate) {
        this.lastPaidDate = lastPaidDate;
    }

    public void setMaxDayOfMonth(int maxDayOfMonth) {
        this.maxDayOfMonth = maxDayOfMonth;
    }

    public void setPayDue(int payDue) {
        this.payDue = payDue;
    }
}
