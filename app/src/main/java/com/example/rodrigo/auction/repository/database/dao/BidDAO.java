package com.example.rodrigo.auction.repository.database.dao;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.rodrigo.auction.model.Bid;
import com.example.rodrigo.auction.repository.database.AuctionProvider;
import com.example.rodrigo.auction.repository.database.BidColumns;
import com.example.rodrigo.auction.repository.database.Orm;

/**
 * Created by rodrigo on 1/7/17.
 */

public class BidDAO {

    public static void insertBid(Context context, Bid bid) {
        ContentValues bidValues = Orm.build().toContentValues(bid);
        bidValues.remove(BidColumns.ID);
        Uri uri = context.getContentResolver().insert(AuctionProvider.Bids.CONTENT_URI, bidValues);
        bid.setId(ContentUris.parseId(uri));
        context.getContentResolver().notifyChange(uri, null);
    }

    public static Bid selectBid(Context context, Long id) {
        Cursor cursor = context.getContentResolver().query(AuctionProvider.Bids.withId(id), null, null, null, null);
        if (cursor.moveToFirst()) {
            return Orm.build().fromCursor(cursor, Bid.class);
        }
        return null;
    }
}
