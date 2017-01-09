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
    public long id;

    @Column(UserColumns.NAME)
    public String userName;

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
}
