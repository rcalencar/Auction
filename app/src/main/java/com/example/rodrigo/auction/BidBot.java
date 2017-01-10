package com.example.rodrigo.auction;

import android.content.ContentUris;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.example.rodrigo.auction.model.Auction;
import com.example.rodrigo.auction.model.AuctionReactor;
import com.example.rodrigo.auction.model.Bid;
import com.example.rodrigo.auction.model.User;
import com.example.rodrigo.auction.repository.database.dao.AuctionDAO;
import com.example.rodrigo.auction.repository.database.dao.BidDAO;
import com.example.rodrigo.auction.repository.database.dao.UserDAO;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rodrigo on 1/8/17.
 */

class BidBot extends ContentObserver {
    private static final int USER_CHANCE = 9;
    private static final int CHANGE_INTERVAL = 100;
    private static final String BOT_NAME = "bot";
    private static final String LOG_TAG = "Auction";
    private final Context context;
    private User user;
    private AuctionReactor auctionReactor;

    public BidBot(Context context, Handler handler, AuctionReactor auctionReactor) {
        super(handler);
        this.context = context;
        this.auctionReactor = auctionReactor;
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        long id;
        try {
            id = ContentUris.parseId(uri);
            if (id == 0) {
                return;
            }
        } catch (NumberFormatException e) {
            return;
        }
        if (user == null) {
            user = UserDAO.selectUser(context, BOT_NAME);
        }
        final Auction auction = AuctionDAO.selectAuction(context, id);
        final Bid winnerBid = BidDAO.selectBid(context, auction.getWinnerBid().getId());
        if (winnerBid != null && winnerBid.getBidder() != null && winnerBid.getBidder().getId() != user.getId()) {
            final Random r = new Random();
            int chance = r.nextInt(CHANGE_INTERVAL);
            if (chance > USER_CHANCE) {
                Log.d(LOG_TAG, "Bot is going to bid auction " + auction);
                final Handler handler = new Handler();
                Timer timer = new Timer();
                TimerTask doAsynchronousTask = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                auctionReactor.addRequest(new AuctionReactor.BidRequest(auction.getId(), user.getId(), auction.nextBid()));
                            }
                        });
                    }
                };
                int delayToBid = 2 * 1000;
                timer.schedule(doAsynchronousTask, delayToBid);

            } else {
                Log.d(LOG_TAG, "Bot is NOT going to bid.");
            }
        }
    }
}
