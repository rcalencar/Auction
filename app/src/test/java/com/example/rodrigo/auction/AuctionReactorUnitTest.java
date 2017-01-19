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
@PrepareForTest({Log.class, AuctionDAO.class, BidDAO.class, UserDAO.class})
public class AuctionReactorUnitTest {

    @Test
    public void reactor() throws Exception {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(AuctionDAO.class);
        PowerMockito.mockStatic(UserDAO.class);
        PowerMockito.mockStatic(BidDAO.class);

        MockContext context = new MockContext();
        User owner = new User(1, "owner");
        User bidder = new User(2, "rodrigo");
        long start = Calendar.getInstance().getTimeInMillis();
        long end = start + (60 * 1000);
        Auction auction = new Auction(1, owner, "spon", "golden spon", 100000, 10000, null, null, false, false, start, end);

        Mockito.when(AuctionDAO.selectAuction(Matchers.any(Context.class), anyLong())).thenReturn(auction);
        Mockito.when(UserDAO.selectUser(Matchers.any(Context.class), anyLong())).thenReturn(bidder);

        AuctionReactor auctionReactor = AuctionReactor.build(context);
        long value = auction.nextBid();
        auctionReactor.addRequest(new AuctionReactor.BidRequest(auction.getId(), bidder.getId(), value));
        auctionReactor.addRequest(new AuctionReactor.RequestToStop());
        auctionReactor.getRunningThread().join();
        assertEquals(value, auction.getWinnerBid().getValue());
    }
}
