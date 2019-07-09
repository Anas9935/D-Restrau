package com.example.drestrau.Objects;

public class pres_rest {
    private String rid;
    private String selId;
    public pres_rest(){}
    public pres_rest(String rd,String sd){
        rid=rd;
        selId=sd;
    }

    public String getRid() {
        return rid;
    }

    public String getSelId() {
        return selId;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setSelId(String selId) {
        this.selId = selId;
    }
}
