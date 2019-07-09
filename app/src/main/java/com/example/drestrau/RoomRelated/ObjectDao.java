package com.example.drestrau.RoomRelated;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ObjectDao {

    @Query("SELECT * From My_Orders ORDER BY dateStamp")
    List<MyOrderObject> loadAllObjects();

    @Insert
    void insertObject(MyOrderObject object);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void  updateObject(MyOrderObject object);

    @Delete
    void deleteObject(MyOrderObject object);

}
