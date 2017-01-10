package com.example.rodrigo.auction.model;

import android.util.Log;

import com.example.rodrigo.auction.repository.database.AuctionColumns;

import net.simonvt.schematic.annotation.DefaultValue;

import org.chalup.microorm.annotations.Column;

/**
 * Created by rodrigo on 1/6/17.
 */

public class Auction {
    private static final String LOG_TAG = "Auction";
    @Column(AuctionColumns.ID)
    private long id;

    @Column(AuctionColumns.OWNER_ID)
    private User owner;

    @Column(AuctionColumns.TITLE)
    private String title;

    @Column(AuctionColumns.DESCRIPTION)
    private String description;

    @Column(AuctionColumns.INITIAL_PRICE)
    private long initialPrice;

    @Column(AuctionColumns.BID_INTERVAL)
    private long bidInterval;

    @Column(AuctionColumns.WINNER_BID_ID)
    private Bid winnerBid;

    @Column(AuctionColumns.PHOTO)
    private String photo;

    @Column(AuctionColumns.DONE)
    @DefaultValue("0")
    private boolean done;

    @Column(AuctionColumns.SUCCESS)
    @DefaultValue("0")
    private boolean success;

    @Column(AuctionColumns.START_DATE)
    private long startDate;

    @Column(AuctionColumns.END_DATE)
    private long endDate;

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
            bid.setAuction(this);
            bid.setBidder(bidder);
            bid.setValue(value);

            winnerBid = bid;

            return bid;

        } else {
            Log.d(LOG_TAG, "createUserBid fail " + bidder + " value: " + value);
            return null;
        }
    }

    public long currentValue() {
        if (winnerBid != null && winnerBid.getValue() != 0) {
            return winnerBid.getValue();
        } else {
            return initialPrice;
        }
    }

    public long nextBid() {
        return currentValue() + bidInterval;
    }

    public boolean finalizeIfExpired() {
        if (!isStillOpen()) {
            success = winnerBid != null && winnerBid.getId() != 0;
            done = true;
        }
        return done;
    }

    public boolean validBid(long value) {
        return value > initialPrice && (winnerBid == null || value > winnerBid.getValue());
    }

    public boolean isStillOpen() {
        return endDate > System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", owner=" + owner.getUserName() +
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(long initialPrice) {
        this.initialPrice = initialPrice;
    }

    public long getBidInterval() {
        return bidInterval;
    }

    public void setBidInterval(long bidInterval) {
        this.bidInterval = bidInterval;
    }

    public Bid getWinnerBid() {
        return winnerBid;
    }

    public void setWinnerBid(Bid winnerBid) {
        this.winnerBid = winnerBid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
