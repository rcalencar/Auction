package com.example.rodrigo.auction.repository.database.adapter;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.rodrigo.auction.model.User;

import org.chalup.microorm.TypeAdapter;

/**
 * Created by rodrigo on 1/6/17.
 */

public class UserTypeAdapter implements TypeAdapter<User> {
    @Override
    public User fromCursor(Cursor c, String columnName) {
        User user = new User();
        user.id = c.getLong(c.getColumnIndexOrThrow(columnName));
        return user;
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, User user) {
        values.put(columnName, user.id);
    }
}