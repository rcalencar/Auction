package com.example.rodrigo.auction.model;

import com.example.rodrigo.auction.repository.database.BidColumns;

import org.chalup.microorm.annotations.Column;

/**
 * Created by rodrigo on 1/6/17.
 */

public class Bid {
    @Column(BidColumns.ID)
    private long id;

    @Column(BidColumns.BIDDER_ID)
    private User bidder;

    @Column(BidColumns.AUCTION_ID)
    private Auction auction;

    @Column(BidColumns.VALUE)
    private long value;

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", bidder=" + bidder.getUserName() +
                ", auction=" + (auction == null ? "null" : auction.getTitle()) +
                ", value=" + value +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
