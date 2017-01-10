package com.example.rodrigo.auction;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rodrigo.auction.model.AuctionReactor;
import com.example.rodrigo.auction.repository.database.AuctionProvider;
import com.example.rodrigo.auction.repository.local.LocalLogin;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AuctionAdapter.OnItemSelectedListener {
    private static final String LOG_TAG = "MainActivityFra";
    private static final String TAG_LIFECYCLE = LOG_TAG + " L_CYCLE";
    private static final int LOADER_ID = 2404;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private AuctionAdapter mAdapter;

    private AuctionReactor auctionReactor;
    private BidBot bidBot;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG_LIFECYCLE, "onCreate");

        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mAdapter = new AuctionAdapter(getContext(), this);

        auctionReactor = AuctionReactor.build(getContext());

        bidBot = new BidBot(getContext(), new Handler(), auctionReactor);
        getContext().getContentResolver().registerContentObserver(AuctionProvider.Auctions.CONTENT_URI, true, bidBot);

        auctionReactor.start();
    }

    @Override
    public void onStop() {
        getContext().getContentResolver().unregisterContentObserver(bidBot);
        auctionReactor.addRequest(new AuctionReactor.NoMoreRequests());
        auctionReactor.stop();
        super.onStop();
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(LOADER_ID, null, MainActivityFragment.this);

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG_LIFECYCLE, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_auctions);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");

        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getActivity(), AuctionProvider.Auctions.CONTENT_URI, AuctionAdapter.PROJECTION, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");

        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");

        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onItemSelected(Long auctionId, Long value) {
        auctionReactor.addRequest(new AuctionReactor.BidRequest(auctionId, LocalLogin.loginId(getContext()), value));
    }
}
