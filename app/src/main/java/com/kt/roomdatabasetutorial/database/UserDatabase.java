package com.kt.roomdatabasetutorial.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kt.roomdatabasetutorial.User;

//Database
//entities : để biết database này của entity nào
//version : để trong khi thao tác với sql khi cần thêm cột thì ta cần version mới
@Database(entities = {User.class}, version = 2)
public abstract class UserDatabase extends RoomDatabase {

    //b1: tăng version
    //b2: magration
    //nó phải là biến tĩnh
    //thay đổi thuộc tính table
    static Migration migration_from_1_to_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //logic thêm 1 cột vào trong 1 bảng
            database.execSQL("ALTER TABLE user ADD COLUMN year TEXT ");

            //Thêm nhiều cột vào trong một bảng
            //ALTER TABLE ten_bang ADD cot1 dinh_nghia_cot1, cot2 dinh_nghia_cot2,...cotn dinh_nghia_cotn;

            //chinh sửa kiểu dữ liệu cột trong một bảng
            // ALTER TABLE ten_bang ALTER COLUMN ten_cot kieu_cot;

            //xóa cột trong bảng
            // ALTER TABLE ten_bang DROP COLUMN ten_cot;
        }
    };


    private static final String DATABASE_NAME = "user.db";
    private static UserDatabase instance;

    //synchronized : tuần tự
    //UserDatabase : kiểu mà ta muốn trả về
    public static synchronized UserDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class,  DATABASE_NAME)
                    .allowMainThreadQueries()  //allowMainThreadQueries() : cho phép query trên main thread
                    .addMigrations(migration_from_1_to_2) //update database
                    .build();
        }
        return instance;
    }

    //
    public abstract UserDAO userDAO();
}
