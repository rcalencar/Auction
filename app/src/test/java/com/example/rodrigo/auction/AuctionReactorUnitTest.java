package com.example.rodrigo.auction;

import android.content.Context;
import android.os.Handler;
import android.test.mock.MockContext;
import android.util.Log;

import com.example.rodrigo.auction.model.Auction;
import com.example.rodrigo.auction.model.AuctionReactor;
import com.example.rodrigo.auction.model.User;
import com.example.rodrigo.auction.repository.database.dao.AuctionDAO;
import com.example.rodrigo.auction.repository.database.dao.BidDAO;
import com.example.rodrigo.auction.repository.database.dao.UserDAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by rodrigo on 1/9/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AuctionReactor.class, Log.class, Auction.class, AuctionDAO.class, BidDAO.class, UserDAO.class})
public class AuctionReactorUnitTest {

    @Test
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
        PowerMockito.spy(AuctionDAO.class);
        PowerMockito.mockStatic(AuctionDAO.class);
        PowerMockito.spy(UserDAO.class);
        PowerMockito.mockStatic(UserDAO.class);
        PowerMockito.spy(BidDAO.class);
        PowerMockito.mockStatic(BidDAO.class);

        MockContext context = new MockContext();
        User owner = new User(1, "owner");
        User bidder = new User(1, "rodrigo");
        long start = Calendar.getInstance().getTimeInMillis();
        long end = start + (60 * 1000);
        Auction auction = new Auction(1, owner, "spon", "golden spon", 100000, 10000, null, null, false, false, start, end);

        Mockito.when(AuctionDAO.selectAuction(Matchers.any(Context.class), anyLong())).thenReturn(auction);
        Mockito.when(UserDAO.selectUser(Matchers.any(Context.class), anyLong())).thenReturn(bidder);

        AuctionReactor auctionReactor = AuctionReactor.build(context);
        long value = auction.nextBid();
        auctionReactor.addRequest(new AuctionReactor.BidRequest(1l, 1l, value));
        auctionReactor.addRequest(new AuctionReactor.NoMoreRequests());
        AuctionReactor.runnigThread.join();
        assertEquals(value, auction.winnerBid.value);
    }
}
