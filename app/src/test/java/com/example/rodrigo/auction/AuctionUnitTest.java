package com.example.rodrigo.auction;

import android.util.Log;

import com.example.rodrigo.auction.model.Auction;
import com.example.rodrigo.auction.model.Bid;
import com.example.rodrigo.auction.model.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class AuctionUnitTest {
    @Test
    public void auction_CreateUserBid() throws Exception {
        PowerMockito.mockStatic(Log.class);
        User owner = new User(1, "owner");
        User bidder = new User(1, "rodrigo");

        long start = Calendar.getInstance().getTimeInMillis();
        long end = start + (60 * 1000);
        Auction auction = new Auction(1, owner, "spon", "golden spon", 100000, 10000, null, null, false, false, start, end);

        long v1 = auction.nextBid();
        Bid b1 = auction.createUserBid(bidder, v1);

        assertEquals(auction.currentValue(), v1);
    }

    @Test
    public void auction_CreateUserBid_ExpiredAuction() throws Exception {
        PowerMockito.mockStatic(Log.class);
        User owner = new User(1, "owner");
        User bidder = new User(1, "rodrigo");

        long start = Calendar.getInstance().getTimeInMillis();
        Auction auction = new Auction(1, owner, "spon", "golden spon", 100000, 10000, null, null, false, false, start, start);

        long v1 = auction.nextBid();
        Bid b1 = auction.createUserBid(bidder, v1);

        assertNull(b1);
    }

    @Test
    public void auction_CreateUserBid_Done() throws Exception {
        PowerMockito.mockStatic(Log.class);
        User owner = new User(1, "owner");
        User bidder = new User(1, "rodrigo");

        long start = Calendar.getInstance().getTimeInMillis();
        Auction auction = new Auction(1, owner, "spon", "golden spon", 100000, 10000, null, null, false, false, start, start);
        auction.finalizeIf();
        long v1 = auction.nextBid();
        Bid b1 = auction.createUserBid(bidder, v1);

        assertTrue(auction.isDone());
        assertNull(b1);
    }
}