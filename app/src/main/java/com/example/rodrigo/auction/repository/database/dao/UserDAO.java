package com.example.rodrigo.auction.repository.database.dao;

import android.content.Context;
import android.database.Cursor;

import com.example.rodrigo.auction.model.User;
import com.example.rodrigo.auction.repository.database.AuctionProvider;
import com.example.rodrigo.auction.repository.database.Orm;
import com.example.rodrigo.auction.repository.database.UserColumns;

/**
 * Created by rodrigo on 1/7/17.
 */

public class UserDAO {
    public static User selectUser(Context context, Long id) {
        Cursor cursorUser = context.getContentResolver().query(AuctionProvider.Users.withId(id), null, null, null, null);
        if (cursorUser.moveToFirst()) {
            return Orm.build().fromCursor(cursorUser, User.class);
        }
        return null;
    }

    public static User selectUser(Context context, String userName) {
        Cursor cursorUser = context.getContentResolver().query(AuctionProvider.Users.CONTENT_URI, null, UserColumns.NAME + " = ?", new String[] {userName}, null);
        if (cursorUser.moveToFirst()) {
            return Orm.build().fromCursor(cursorUser, User.class);
        }
        return null;
    }
}
