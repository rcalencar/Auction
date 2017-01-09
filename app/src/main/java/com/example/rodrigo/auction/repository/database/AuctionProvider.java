package com.example.rodrigo.auction.repository.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = AuctionProvider.AUTHORITY
        , database = AuctionDatabase.class
        , packageName = "com.example.rodrigo.auction.provider")
public final class AuctionProvider {

    public static final String AUTHORITY = "com.example.rodrigo.auction.AuctionProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private AuctionProvider() {
    }

    public static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    interface Path {
        String USERS = "songs";
        String AUCTIONS = "auctions";
        String BIDS = "bids";
    }

    @TableEndpoint(table = AuctionDatabase.Tables.USERS)
    public static class Users {
        @ContentUri(
                path = Path.USERS,
                type = "vnd.android.cursor.dir/users")
        public static final Uri CONTENT_URI = buildUri(Path.USERS);

        @InexactContentUri(
                name = "USER_ID",
                path = Path.USERS + "/#",
                type = "vnd.android.cursor.item/user",
                whereColumn = UserColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.USERS, String.valueOf(id));
        }
    }

    @TableEndpoint(table = AuctionDatabase.Tables.AUCTIONS)
    public static class Auctions {
        @ContentUri(
                path = Path.AUCTIONS,
                type = "vnd.android.cursor.dir/auctions",
                join = AuctionDatabase.Joins.AUCTION_WINNER_BID
        )
        public static final Uri CONTENT_URI = buildUri(Path.AUCTIONS);

        @InexactContentUri(
                name = "AUCTIONS_ID",
                path = Path.AUCTIONS + "/#",
                type = "vnd.android.cursor.item/auction",
                join = AuctionDatabase.Joins.AUCTION_WINNER_BID,
                whereColumn = AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.AUCTIONS, String.valueOf(id));
        }
    }
    @TableEndpoint(table = AuctionDatabase.Tables.BIDS)
    public static class Bids {
        @ContentUri(
                path = Path.BIDS,
                type = "vnd.android.cursor.dir/bids")
        public static final Uri CONTENT_URI = buildUri(Path.BIDS);

        @InexactContentUri(
                name = "BIDS_ID",
                path = Path.BIDS + "/#",
                type = "vnd.android.cursor.item/bid",
                whereColumn = BidColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.BIDS, String.valueOf(id));
        }
    }
}
