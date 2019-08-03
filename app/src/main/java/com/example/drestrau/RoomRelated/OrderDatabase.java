package com.example.drestrau.RoomRelated;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {MyOrderObject.class,searchObject.class},version = 1,exportSchema = false)
public abstract class OrderDatabase extends RoomDatabase {

    private static final Object LOCK=new Object();
    private static final String DB_NAME="My_Database";
    private static OrderDatabase sInstance;

    public static OrderDatabase getInstance(Context context){
        if(sInstance==null){
            synchronized (LOCK){
                sInstance= Room.databaseBuilder(context.getApplicationContext(),OrderDatabase.class, DB_NAME)
                        //.allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract ObjectDao objectDao();
    public abstract SearchDao searchDao();
}
