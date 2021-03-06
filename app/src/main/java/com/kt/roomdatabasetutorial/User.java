package com.kt.roomdatabasetutorial;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Entity
//tableName : đặt tên cho bảng
@Entity(tableName = "user")
public class User implements Serializable {

    //Khóa chính, tự động tăng
    @PrimaryKey(autoGenerate = true)
    private int id;

    //set lại tên thuộc tính : @ColumnInfo(name = "user_name")
    private String username;
    private String address;
    private String year;

    public User(String username, String address, String year) {
        this.username = username;
        this.address = address;
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
