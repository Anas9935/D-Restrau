package com.example.drestrau.Objects;

public class messageObject {
    private String message;
    private long timestamp;
    private String fromUid;
    private String toUid;
    public messageObject(){}
    public messageObject(String mess,long time,String fromid,String toid){
        message=mess;
        timestamp=time;
        fromUid=fromid;
        toUid=toid;
    }

    public String getFromUid() {
        return fromUid;
    }

    public String getToUid() {
        return toUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
