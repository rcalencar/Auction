package com.example.rodrigo.auction.model;

import android.util.Log;

import com.example.rodrigo.auction.repository.database.AuctionColumns;

import net.simonvt.schematic.annotation.DefaultValue;

import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigo on 1/6/17.
 */

public class Auction {
    private static final String LOG_TAG = "Auction";
    @Column(AuctionColumns.ID)
    public long id;

    @Column(AuctionColumns.OWNER_ID)
    public User owner;

    @Column(AuctionColumns.TITLE)
    public String title;

    @Column(AuctionColumns.DESCRIPTION)
    public String description;

    @Column(AuctionColumns.INITIAL_PRICE)
    public long initialPrice;

    @Column(AuctionColumns.BID_INTERVAL)
    public long bidInterval;

    @Column(AuctionColumns.WINNER_BID_ID)
    public Bid winnerBid;

    @Column(AuctionColumns.PHOTO)
    public String photo;

    @Column(AuctionColumns.DONE)
    @DefaultValue("0")
    public boolean done;

    @Column(AuctionColumns.SUCCESS)
    @DefaultValue("0")
    public boolean success;

    @Column(AuctionColumns.START_DATE)
    public long startDate;

    @Column(AuctionColumns.END_DATE)
    public long endDate;

    public Auction() {
    }

    public Auction(long id, User owner, String title, String description, long initialPrice, long bidInterval, Bid winnerBid, String photo, boolean done, boolean success, long startDate, long endDate) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.initialPrice = initialPrice;
        this.bidInterval = bidInterval;
        this.winnerBid = winnerBid;
        this.photo = photo;
        this.done = done;
        this.success = success;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Bid createUserBid(User bidder, long value) {
        if (!done && isStillOpen() && validBid(value)) {
            Log.d(LOG_TAG, "createUserBid " + bidder + " value: " + value);
            Bid bid = new Bid();
            bid.auction = this;
            bid.bidder = bidder;
            bid.value = value;

            winnerBid = bid;

            return bid;

        } else {
            Log.d(LOG_TAG, "createUserBid fail " + bidder + " value: " + value);
            return null;
        }
    }

    public long currentValue() {
        if (winnerBid != null && winnerBid.value != 0) {
            return winnerBid.value;
        } else {
            return initialPrice;
        }
    }

    public long nextBid() {
        return currentValue() + bidInterval;
    }

    public boolean finalizeIf() {
        if (!isStillOpen()) {
            success = winnerBid != null && winnerBid.id != 0;
            done = true;
        }
        return done;
    }

    public boolean validBid(long value) {
        return value > initialPrice && (winnerBid == null || value > winnerBid.value);
    }

    public boolean isStillOpen() {
        return endDate > System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", owner=" + owner.userName +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", initialPrice=" + initialPrice +
                ", bidInterval=" + bidInterval +
                ", winnerBid=" + winnerBid +
                ", photo='" + photo + '\'' +
                ", done=" + done +
                ", success=" + success +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
