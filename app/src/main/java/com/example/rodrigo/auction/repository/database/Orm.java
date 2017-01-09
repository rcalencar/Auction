package com.example.rodrigo.auction.repository.database;

import com.example.rodrigo.auction.model.Auction;
import com.example.rodrigo.auction.model.Bid;
import com.example.rodrigo.auction.model.User;
import com.example.rodrigo.auction.repository.database.adapter.AuctionTypeAdapter;
import com.example.rodrigo.auction.repository.database.adapter.BidTypeAdapter;
import com.example.rodrigo.auction.repository.database.adapter.UserTypeAdapter;

import org.chalup.microorm.MicroOrm;

/**
 * Created by rodrigo on 1/7/17.
 */

public class Orm {
    public static MicroOrm build() {
        MicroOrm uOrm = new MicroOrm.Builder()
                .registerTypeAdapter(Auction.class, new AuctionTypeAdapter())
                .registerTypeAdapter(Bid.class, new BidTypeAdapter())
                .registerTypeAdapter(User.class, new UserTypeAdapter())
                .build();
        return uOrm;
    }
}
