package com.example.rodrigo.auction.model;

import com.example.rodrigo.auction.repository.database.BidColumns;
import com.example.rodrigo.auction.repository.database.UserColumns;

import org.chalup.microorm.annotations.Column;

/**
 * Created by rodrigo on 1/6/17.
 */

public class Bid {
    @Column(BidColumns.ID)
    public long id;

    @Column(BidColumns.BIDDER_ID)
    public User bidder;

    @Column(BidColumns.AUCTION_ID)
    public Auction auction;

    @Column(BidColumns.VALUE)
    public long value;

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", bidder=" + bidder.userName +
                ", auction=" + (auction == null ? "null" : auction.title) +
                ", value=" + value +
                '}';
    }
}
