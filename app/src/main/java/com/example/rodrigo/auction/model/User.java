package com.example.rodrigo.auction.model;

import com.example.rodrigo.auction.repository.database.UserColumns;

import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigo on 1/6/17.
 */

public class User {
    @Column(UserColumns.ID)
    private long id;

    @Column(UserColumns.NAME)
    private String userName;

    public User() {
    }

    public User(long id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
