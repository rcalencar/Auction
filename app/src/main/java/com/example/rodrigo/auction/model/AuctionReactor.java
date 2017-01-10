package com.example.rodrigo.auction.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
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
    private BidRequestConsumer processingConsumer;
    private LinkedBlockingQueue<Request> requests = new LinkedBlockingQueue<>();
    private Thread runnigThread;

    private AuctionReactor(Context context) {
        this.context = context;
    }

    public static AuctionReactor build(Context context) {
        AuctionReactor instance = new AuctionReactor(context);
        instance.processingConsumer = new BidRequestConsumer(instance, instance.requests);
        instance.runnigThread = new Thread(instance.processingConsumer);
        instance.runnigThread.start();
        return instance;
    }

    public Thread getRunningThread() {
        return runnigThread;
    }

    public void stop() {
        processingConsumer.isRunning = false;
    }

    public boolean addRequest(Request bidRequest) {
        try {
            requests.put(bidRequest);
            return true;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean process(Request request) {
        if (request instanceof BidBeats) {
            Cursor cursor = AuctionDAO.selectLiveAuction(context);
            try {
                while (cursor.moveToNext()) {
                    Auction auction = Orm.build().fromCursor(cursor, Auction.class);
                    if (auction.finalizeIf()) {
                        AuctionDAO.updateAuction(context, auction);
                    }
                }
            } finally {
                cursor.close();
            }

            return true;

        } else {
            BidRequest bidRequest = (BidRequest) request;
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

    private static class BidRequestConsumer implements Runnable {
        private final AuctionReactor auctionReactor;
        private LinkedBlockingQueue<Request> queue;
        private boolean isRunning = true;

        BidRequestConsumer(AuctionReactor auctionReactor, LinkedBlockingQueue<Request> queue) {
            this.auctionReactor = auctionReactor;
            this.queue = queue;
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    Request bidRequest = queue.take();
                    if (bidRequest instanceof LastRequests) {
                        break;
                    }
                    auctionReactor.process(bidRequest);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public static class LastRequests implements Request {
    }
}
