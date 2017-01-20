package com.example.rodrigo.auction;

import android.content.Context;

import com.example.rodrigo.auction.model.Auction;
import com.example.rodrigo.auction.model.AuctionReactor;
import com.example.rodrigo.auction.model.User;
import com.example.rodrigo.auction.repository.database.dao.AuctionDAO;
import com.example.rodrigo.auction.repository.database.dao.BidDAO;
import com.example.rodrigo.auction.repository.database.dao.UserDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.internal.Shadow;
import org.robolectric.shadows.ShadowLooper;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by rodrigo on 1/9/2017.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({AuctionDAO.class, BidDAO.class, UserDAO.class})
public class AuctionReactorUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUpMockito() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDownMockito() {
        Mockito.validateMockitoUsage();
    }

    @Test
    public void reactor() throws Exception {
        PowerMockito.mockStatic(AuctionDAO.class);
        PowerMockito.mockStatic(UserDAO.class);
        PowerMockito.mockStatic(BidDAO.class);

        Context context = RuntimeEnvironment.application.getApplicationContext();
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
        shadowOf(auctionReactor.getRunningThread().getLooper()).getScheduler().advanceToNextPostedRunnable();
        assertEquals(value, auction.getWinnerBid().getValue());
    }
}
