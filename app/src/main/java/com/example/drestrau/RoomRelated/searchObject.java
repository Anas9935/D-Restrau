package com.example.drestrau.RoomRelated;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "RecentSearches")
public class searchObject {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int type;
    private String rid;
    private String name;
    private Long timestamp;
    @Ignore
    public searchObject(int id,String rid, String name,int type,Long timestamp){
        this.id=id;
        this.rid=rid;
        this.name=name;
        this.timestamp=timestamp;
        this.type=type;
    }
    public searchObject(String rid, String name,int type,Long timestamp){
        this.rid=rid;
        this.name=name;
        this.timestamp=timestamp;
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getRid() {
        return rid;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
