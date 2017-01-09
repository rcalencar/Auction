package com.example.rodrigo.auction.repository.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.rodrigo.auction.AuctionAdapter;
import com.example.rodrigo.auction.model.Auction;
import com.example.rodrigo.auction.repository.database.AuctionColumns;
import com.example.rodrigo.auction.repository.database.AuctionProvider;
import com.example.rodrigo.auction.repository.database.Orm;

/**
 * Created by rodrigo on 1/7/17.
 */

public class AuctionDAO {
    public static Auction selectAuction(Context context, Long id) {
        Cursor cursor = context.getContentResolver().query(AuctionProvider.Auctions.withId(id), AuctionAdapter.PROJECTION, null, null, null);
        if (cursor.moveToFirst()) {
            return Orm.build().fromCursor(cursor, Auction.class);

        }
        return null;
    }

    public static Cursor selectLiveAuction(Context context) {
        return context.getContentResolver().query(AuctionProvider.Auctions.CONTENT_URI, AuctionAdapter.PROJECTION, "auction_done != 1", null, null);
    }

    public static void updateAuction(Context context, Auction auction) {
        ContentValues auctionValues = Orm.build().toContentValues(auction);
        context.getContentResolver().update(AuctionProvider.Auctions.CONTENT_URI, auctionValues, AuctionColumns.ID + " = ?", new String[]{String.valueOf(auction.id)});
        context.getContentResolver().notifyChange(AuctionProvider.Auctions.withId(auction.id), null);
    }
}
