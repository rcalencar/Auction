package com.example.rodrigo.auction;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rodrigo.auction.model.Auction;
import com.example.rodrigo.auction.repository.database.AuctionColumns;
import com.example.rodrigo.auction.repository.database.AuctionDatabase;
import com.example.rodrigo.auction.repository.database.BidColumns;
import com.example.rodrigo.auction.repository.database.Orm;
import com.example.rodrigo.auction.repository.database.UserColumns;

import java.text.DecimalFormat;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.ViewHolder> {
    private Context context;
    private Cursor cursor;
    private OnItemSelectedListener onItemSelectedListener;

    public static final String[] PROJECTION = new String[]{
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.ID,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.OWNER_ID,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.WINNER_BID_ID,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.TITLE,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.DESCRIPTION,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.PHOTO,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.INITIAL_PRICE,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.BID_INTERVAL,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.DONE,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.SUCCESS,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.START_DATE,
            AuctionDatabase.Tables.AUCTIONS + "." + AuctionColumns.END_DATE,
            AuctionDatabase.Tables.BIDS + "." + BidColumns.VALUE,
            AuctionDatabase.Tables.USERS + "." + UserColumns.NAME
    };

    public AuctionAdapter(Context context, OnItemSelectedListener onItemSelectedListener) {
        this.context = context;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void swapCursor(final Cursor cursor) {
        this.cursor = cursor;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.cursor != null ? this.cursor.getCount() : 0;
    }

    public Cursor getItem(final int position) {
        if (this.cursor != null && !this.cursor.isClosed()) {
            this.cursor.moveToPosition(position);
        }

        return this.cursor;
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.auction_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Cursor cursor = this.getItem(position);
        Auction auction = Orm.build().fromCursor(cursor, Auction.class);
        holder.auctionId = auction.getId();
        holder.text_view_title.setText(auction.getTitle());
        holder.text_view_description.setText(auction.getDescription());
        if (auction.isDone()) {
            holder.text_view_done.setText(R.string.done);
        } else {
            holder.text_view_done.setText(R.string.live);
        }
        if (auction.isSuccess()) {
            holder.text_view_success.setVisibility(View.VISIBLE);
            holder.text_view_success.setText(R.string.winner);
        } else {
            holder.text_view_success.setVisibility(View.GONE);
        }
        if (auction.getWinnerBid() != null && auction.getWinnerBid().getBidder() != null && auction.getWinnerBid().getBidder().getUserName() != null) {
            holder.text_view_bidder.setText(auction.getWinnerBid().getBidder().getUserName());
        } else {
            holder.text_view_bidder.setText(context.getString(R.string.no_bid));
        }
        DecimalFormat df = new DecimalFormat("#,###.00");
        holder.bidValue = auction.nextBid();
        holder.text_view_current_value.setText(df.format(auction.currentValue() / 100.0));
        holder.button_bid.setText(String.format("%s %s", context.getString(R.string.string_bid), df.format(auction.nextBid() / 100.0)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Long auctionId;
        long bidValue;
        TextView text_view_title;
        TextView text_view_description;
        TextView text_view_current_value;
        TextView text_view_bidder;
        Button button_bid;
        TextView text_view_done;
        TextView text_view_success;

        ViewHolder(View v) {
            super(v);
            text_view_title = (TextView) v.findViewById(R.id.text_view_title);
            text_view_description = (TextView) v.findViewById(R.id.text_view_description);
            text_view_current_value = (TextView) v.findViewById(R.id.text_view_current_value);
            text_view_bidder = (TextView) v.findViewById(R.id.text_view_bidder);
            text_view_done = (TextView) v.findViewById(R.id.text_view_done);
            text_view_success = (TextView) v.findViewById(R.id.text_view_success);
            button_bid = (Button) v.findViewById(R.id.button_bid);

            button_bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelectedListener.onItemSelected(auctionId, bidValue);
                }
            });
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Long auctionId, Long value);
    }
}