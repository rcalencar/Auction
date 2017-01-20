package com.example.rodrigo.auction.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.example.rodrigo.auction.repository.database.Orm;
import com.example.rodrigo.auction.repository.database.dao.AuctionDAO;
import com.example.rodrigo.auction.repository.database.dao.BidDAO;
import com.example.rodrigo.auction.repository.database.dao.UserDAO;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by rodrigo on 1/6/17.
 */

public class AuctionReactor {
    private static final String LOG_TAG = "Auction";
    private Context context;
    private HandlerThread runningThread = new HandlerThread("AuctionBidHandlerThread");
    private Handler mHtHandler;

    private AuctionReactor(Context context) {
        this.context = context;
    }

    public static AuctionReactor build(Context context) {
        final AuctionReactor reactor = new AuctionReactor(context);
        reactor.runningThread.start();
        reactor.mHtHandler = new Handler(reactor.runningThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Request bidRequest = (Request) msg.obj;
                reactor.process(bidRequest);
            }
        };


        return reactor;
    }

    public HandlerThread getRunningThread() {
        return runningThread;
    }

    public void stop() {
        runningThread.quit();
    }

    public void addRequest(Request bidRequest) {
        Message message = new Message();
        message.obj = bidRequest;
        mHtHandler.sendMessage(message);
    }

    private boolean process(Request request) {
        if (request instanceof BidBeats) {
            return processBeat();

        } else {
            return processRequest((BidRequest) request);
        }
    }

    private boolean processRequest(BidRequest request) {
        BidRequest bidRequest = request;
        Log.d(LOG_TAG, "bidRequest " + bidRequest);
        Auction auction = AuctionDAO.selectAuction(context, bidRequest.auctionId);
        if (auction == null) {
            Log.d(LOG_TAG, "bidRequest auction == null");
            return false;
        }

        User user = UserDAO.selectUser(context, bidRequest.userId);
        if (user == null) {
            Log.d(LOG_TAG, "bidRequest user == null");
            return false;
        }

        Bid bid = auction.createUserBid(user, bidRequest.value);
        if (bid == null) {
            Log.d(LOG_TAG, "bidRequest bid == null");
            return false;
        }
        Log.d(LOG_TAG, "bidRequest bid " + bid);

        BidDAO.insertBid(context, bid);
        AuctionDAO.updateAuction(context, auction);

        return true;
    }

    private boolean processBeat() {
        Cursor cursor = AuctionDAO.selectLiveAuction(context);
        try {
            while (cursor.moveToNext()) {
                Auction auction = Orm.build().fromCursor(cursor, Auction.class);
                if (auction.finalizeIfExpired()) {
                    AuctionDAO.updateAuction(context, auction);
                }
            }
        } finally {
            cursor.close();
        }

        return true;
    }

    public void start() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        addRequest(new AuctionReactor.BidBeats());
                    }
                });
            }
        };
        int t = 5 * 1000;
        timer.schedule(doAsynchronousTask, 0, t);
    }

    public interface Request {
    }

    public static class BidRequest implements Request {
        private Long auctionId;
        private Long userId;
        private Long value;

        public BidRequest(Long auctionId, Long userId, Long value) {
            this.auctionId = auctionId;
            this.userId = userId;
            this.value = value;
        }
    }

    private static class BidBeats implements Request {
    }
}
