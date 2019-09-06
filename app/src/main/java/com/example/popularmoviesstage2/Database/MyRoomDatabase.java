package com.example.popularmoviesstage2.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Movie.class,}, version = 1)
public abstract class MyRoomDatabase extends androidx.room.RoomDatabase {
    //public abstract CategoreyDao categoreyDao();
    //public abstract MealDao mealDao();
    public abstract MovieDao movieDao();
    private static volatile MyRoomDatabase instance;
    private static Context mContext;


    public static MyRoomDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MyRoomDatabase.class){
                instance = Room.databaseBuilder(context.getApplicationContext(),MyRoomDatabase.class,"MyRoomDatabase").build();
                mContext=context;
            }
        }
        return instance;
    }
}
