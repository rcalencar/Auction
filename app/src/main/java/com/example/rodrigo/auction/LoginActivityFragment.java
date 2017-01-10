package com.example.rodrigo.auction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigo.auction.model.User;
import com.example.rodrigo.auction.repository.database.dao.UserDAO;
import com.example.rodrigo.auction.repository.local.LocalLoginDAO;
import com.jakewharton.rxbinding.view.RxView;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginActivityFragment extends Fragment implements Observer<User> {
    private static final String LOG_TAG = "LoginActivityFragment";

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
        Button loginButton = (Button) view.findViewById(R.id.button_login_enter);
        final EditText tokenEditText = (EditText) view.findViewById(R.id.edit_text_login_token);

        RxView.clicks(loginButton)
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String token = tokenEditText.getText().toString();
                        User user = UserDAO.selectUser(getContext(), token);
                        Observable.just(user)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(LoginActivityFragment.this);
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
        if (user == null) {
            throw Exceptions.propagate(new Exception("user not found"));
        }
        Log.d(LOG_TAG, "login result: " + user);
        LocalLoginDAO.login(getContext(), user.getId());
        MainActivity.startMainActivity(getContext());
    }
}