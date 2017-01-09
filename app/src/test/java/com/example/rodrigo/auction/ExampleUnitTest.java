package com.example.rodrigo.auction;

import com.example.rodrigo.auction.model.Auction;
import com.example.rodrigo.auction.model.Bid;
import com.example.rodrigo.auction.model.User;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrectaaaa() throws Exception {
        User owner = new User(1, "owner");
        User bidder = new User(1, "rodrigo");

        long start = Calendar.getInstance().getTimeInMillis();
        long end = start + (60 * 1000);

        Auction auction = new Auction(1, owner, "spon", "golden spon", 100000, 10000, null, null, false, false, start, end);

        long v1 = auction.nextBid();
        Bid b1 = auction.createUserBid(bidder, v1);

        assertEquals(auction.currentValue(), v1);
    }
}