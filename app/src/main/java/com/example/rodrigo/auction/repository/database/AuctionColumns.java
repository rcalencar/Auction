package com.example.rodrigo.auction.repository.database;

import android.support.annotation.Nullable;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface AuctionColumns {

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    String ID = "_id";

    @DataType(INTEGER)
    @NotNull
    String OWNER_ID = "auction_ownerId";

    @DataType(INTEGER)
    @Nullable
    String WINNER_BID_ID = "auction_winnerBidId";

    @DataType(TEXT)
    @NotNull
    String TITLE = "auction_title";

    @DataType(TEXT)
    @NotNull
    String DESCRIPTION = "auction_description";

    @DataType(TEXT)
    @Nullable
    String PHOTO = "auction_photo";

    @DataType(INTEGER)
    @NotNull
    String INITIAL_PRICE = "auction_initialPrice";

    @DataType(INTEGER)
    @NotNull
    String BID_INTERVAL = "auction_bidInterval";

    @DataType(INTEGER)
    @Nullable
    String DONE = "auction_done";

    @DataType(INTEGER)
    @Nullable
    String SUCCESS = "auction_success";

    @DataType(INTEGER)
    @NotNull
    String START_DATE = "auction_startDate";

    @DataType(INTEGER)
    @NotNull
    String END_DATE = "auction_endDate";
}
