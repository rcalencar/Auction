package com.example.rodrigo.auction;

import android.os.Handler;
import android.os.Message;
import android.test.mock.MockContext;
import android.util.Log;

import com.example.rodrigo.auction.model.Auction;
import com.example.rodrigo.auction.model.AuctionReactor;
import com.example.rodrigo.auction.model.Bid;
import com.example.rodrigo.auction.model.User;
import com.example.rodrigo.auction.repository.local.LocalLogin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

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

        assertTrue(auction.done);
        assertNull(b1);
    }

    @Test
    @PrepareForTest({AuctionReactor.class, Log.class, Auction.class})
    public void reactor() throws Exception {
        PowerMockito.mockStatic(Log.class);
        final Handler handler = mock(Handler.class);
        when(handler.post(any(Runnable.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgumentAt(0, Runnable.class);
                runnable.run();
                return null;
            }
        });
        whenNew(Handler.class).withNoArguments().thenReturn(handler);

        MockContext context = new MockContext();
        AuctionReactor auctionReactor = AuctionReactor.build(context);
        auctionReactor.start();

        auctionReactor.addRequest(new AuctionReactor.BidRequest(1l, 1l, 1000l));

//        auctionReactor.stop();
        auctionReactor.runnigThread.join();
    }
}