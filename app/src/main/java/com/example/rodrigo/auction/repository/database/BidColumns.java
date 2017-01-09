package com.example.rodrigo.auction.repository.database;

import android.support.annotation.Nullable;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;

public interface BidColumns {

    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    public String ID = "_id";

    @DataType(INTEGER)
    @Nullable
    public String VALUE = "bid_value";

    @DataType(INTEGER)
    @Nullable
    public String BIDDER_ID = "bid_bidderId";

    @DataType(INTEGER)
    @Nullable
    public String AUCTION_ID = "bid_auctionId";
}
