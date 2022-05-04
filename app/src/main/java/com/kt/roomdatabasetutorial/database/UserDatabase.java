package com.kt.roomdatabasetutorial.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kt.roomdatabasetutorial.User;

//Database
//entities : để biết database này của entity nào
//version : để trong khi thao tác với sql khi cần thêm cột thì ta cần version mới
@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "user.db";
    private static UserDatabase instance;

    //synchronized : tuần tự
    //UserDatabase : kiểu mà ta muốn trả về
    public static synchronized UserDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class,  DATABASE_NAME)
                    .allowMainThreadQueries()  //allowMainThreadQueries() : cho phép query trên main thread
                    .build();
        }
        return instance;
    }

    //
    public abstract UserDAO userDAO();
}
