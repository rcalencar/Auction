package com.example.rodrigo.auction.repository.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

import java.util.Calendar;

@Database(version = AuctionDatabase.VERSION,
        packageName = "com.example.rodrigo.auction.provider")
public final class AuctionDatabase {

    private AuctionDatabase() {
    }

    public static final int VERSION = 1;

    public static class Tables {

        @Table(UserColumns.class)
        public static final String USERS = "users";

        @Table(BidColumns.class)
        public static final String BIDS = "bids";

        @Table(AuctionColumns.class)
        public static final String AUCTIONS = "auctions";
    }

    public interface Joins {
        String AUCTION_WINNER_BID =
                "LEFT JOIN " + Tables.BIDS + " AS " + Tables.BIDS + " ON " + Tables.BIDS + "." + BidColumns.ID + "=" + Tables.AUCTIONS + "." + AuctionColumns.WINNER_BID_ID +
                        " LEFT JOIN " + Tables.USERS + " AS " + Tables.USERS + " ON " + Tables.USERS + "." + UserColumns.ID + "=" + Tables.BIDS + "." + BidColumns.BIDDER_ID;
    }

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {

        db.execSQL("insert into users(user_userName) values ('rodrigo')");
        db.execSQL("insert into users(user_userName) values ('alencar')");
        db.execSQL("insert into users(user_userName) values ('bot')");

        long ONE_MINUTE_IN_MILLIS = 60000;

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();

        Object[] one_minute = new Object[]{(Long) t, (Long) (t + (1 * ONE_MINUTE_IN_MILLIS))};
        Object[] five_minutes = new Object[]{(Long) t, (Long) (t + (5 * ONE_MINUTE_IN_MILLIS))};
        Object[] invalid_auction = new Object[]{(Long) t, (Long) t};

        db.execSQL("insert into auctions(auction_ownerId, auction_title, auction_description, auction_initialPrice, auction_bidInterval, auction_startDate, auction_endDate, auction_done, auction_success) " +
                " values(1, 'Mug', 'BUZZ ALDRIN * APOLLO 11 CAPSULE MUG ''Good Luck! Moon Dust Express!'' NASA 1969', 10000000, 100000, :1, :2, 0, 0)", one_minute);

        db.execSQL("insert into auctions(auction_ownerId, auction_title, auction_description, auction_initialPrice, auction_bidInterval, auction_startDate, auction_endDate, auction_done, auction_success) " +
                " values(1, 'Fork', 'S Kirk & Son Gold Vermeil Seafood Cocktail Fork Old Maryland Floral 9 Piece 5.5\"', 150000, 15000, :1, :2, 0, 0)", five_minutes);

        db.execSQL("insert into auctions(auction_ownerId, auction_title, auction_description, auction_initialPrice, auction_bidInterval, auction_startDate, auction_endDate, auction_done, auction_success) " +
                " values(1, 'Spon', 'Lousiana Purchase Expo 1803-1903 Sterling Souvenir Spon', 50000, 5000, :1, :2, 0, 0)", invalid_auction);
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }
}
