package com.example.drestrau.Objects;

public class selectionForChefObject {
    private int table;
    private String choice;
    private String sid;
    public selectionForChefObject(){}
    public selectionForChefObject(int tab,String ch,String id){
        table=tab;
        choice=ch;
        sid=id;
    }

    public String getSid() {
        return sid;
    }

    public String getChoice() {
        return choice;
    }

    public int getTable() {
        return table;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void setTable(int table) {
        this.table = table;
    }
}
