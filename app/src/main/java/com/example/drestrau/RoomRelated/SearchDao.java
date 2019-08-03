package com.example.drestrau.RoomRelated;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface SearchDao {
    @Query("SELECT * From RecentSearches ORDER BY timestamp")
    List<searchObject> loadAllObjects();

    @Insert
    void insertObject(searchObject object);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void  updateObject(searchObject object);

    @Delete
    void deleteObject(searchObject object);
}
