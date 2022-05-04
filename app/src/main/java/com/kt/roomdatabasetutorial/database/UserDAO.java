package com.kt.roomdatabasetutorial.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kt.roomdatabasetutorial.User;

import java.util.List;

@Dao
public interface UserDAO {

    //insert
    @Insert
    void insetUser(User user);

    //SELECT
    @Query("SELECT * FROM user")
    List<User> getListUser();

    //
    @Query("SELECT * FROM user WHERE username= :username")
    List<User> checkUser(String username);

    //
    @Update
    void updateUser(User user);
}
