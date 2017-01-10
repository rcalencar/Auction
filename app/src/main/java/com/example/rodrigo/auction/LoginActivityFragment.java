package com.example.rodrigo.auction;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigo.auction.model.User;
import com.example.rodrigo.auction.repository.database.AuctionProvider;
import com.example.rodrigo.auction.repository.database.Orm;
import com.example.rodrigo.auction.repository.database.UserColumns;
import com.example.rodrigo.auction.repository.local.LocalLogin;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.venmo.cursor.IterableCursorWrapper;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginActivityFragment extends RxFragment implements Observer<User> {
    private static final String LOG_TAG = "LoginActivityFragment";
    private Button loginButton;
    private EditText tokenEditText;

    public LoginActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = (Button) view.findViewById(R.id.button_login_enter);
        tokenEditText = (EditText) view.findViewById(R.id.edit_text_login_token);

        RxView.clicks(loginButton)
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String token = tokenEditText.getText().toString();
                        Cursor cursorUser = getContext().getContentResolver().query(
                                AuctionProvider.Users.CONTENT_URI,
                                null,
                                UserColumns.NAME + " = ?",
                                new String[]{token},
                                null);
                        Observable.from(new IterableCursorWrapper<User>(cursorUser) {
                            @Override
                            public User peek() {
                                return Orm.build().fromCursor(this, User.class);
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                                .compose(bindToLifecycle())
                                .subscribe((Observer)LoginActivityFragment.this);
                    }
                });

        return view;
    }

    @Override
    public void onCompleted() {
        // nothing
    }

    @Override
    public void onError(Throwable error) {
        Log.e(LOG_TAG, error.getMessage(), error);
        Toast.makeText(getActivity(), getContext().getText(R.string.not_logged), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNext(User user) {
        Log.d(LOG_TAG, "login result: " + user);
        LocalLogin.login(getContext(), user.getId());
        MainActivity.startMainActivity(getContext());
    }
}