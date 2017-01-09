package com.example.rodrigo.auction.repository.database.adapter;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.rodrigo.auction.model.Auction;

import org.chalup.microorm.TypeAdapter;

/**
 * Created by rodrigo on 1/6/17.
 */

public class AuctionTypeAdapter implements TypeAdapter<Auction> {
    @Override
    public Auction fromCursor(Cursor c, String columnName) {
        Auction auction = new Auction();
        auction.id = c.getLong(c.getColumnIndexOrThrow(columnName));
        return auction;
    }

    @Override
    public void toContentValues(ContentValues values, String columnName, Auction auction) {
        values.put(columnName, auction.id);
    }
}