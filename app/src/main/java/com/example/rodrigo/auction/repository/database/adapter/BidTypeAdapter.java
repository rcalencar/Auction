package com.example.rodrigo.auction.repository.database.adapter;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.rodrigo.auction.model.Bid;
import com.example.rodrigo.auction.model.User;
import com.example.rodrigo.auction.repository.database.BidColumns;
import com.example.rodrigo.auction.repository.database.UserColumns;

import org.chalup.microorm.TypeAdapter;

/**
 * Created by rodrigo on 1/6/17.
 */

public class BidTypeAdapter implements TypeAdapter<Bid> {

    public static final int INVALID_COLUMN = -1;

    @Override
    public Bid fromCursor(Cursor c, String columnName) {
        Bid bid = new Bid();
        bid.setId(c.getLong(c.getColumnIndexOrThrow(columnName)));

        if (c.getColumnIndex(BidColumns.VALUE) != INVALID_COLUMN) {
            bid.setValue(c.getLong(c.getColumnIndex(BidColumns.VALUE)));
        }
        if (c.getColumnIndex(UserColumns.NAME) != INVALID_COLUMN) {
            if (bid.getBidder() == null) {
                bid.setBidder(new User());
            }
            bid.getBidder().setUserName(c.getString(c.getColumnIndex(UserColumns.NAME)));
        }

        return bid;
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, Bid bid) {
        values.put(columnName, bid.getId());
    }
}