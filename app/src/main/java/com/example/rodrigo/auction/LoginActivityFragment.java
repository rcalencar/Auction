package com.example.rodrigo.auction;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigo.auction.repository.database.AuctionProvider;
import com.example.rodrigo.auction.repository.database.Orm;
import com.example.rodrigo.auction.repository.database.UserColumns;
import com.example.rodrigo.auction.repository.local.LocalLogin;
import com.example.rodrigo.auction.model.User;

public class LoginActivityFragment extends Fragment {

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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = tokenEditText.getText().toString();
                Cursor cursorUser = getContext().getContentResolver().query(
                        AuctionProvider.Users.CONTENT_URI,
                        null,
                        UserColumns.NAME + " = ?",
                        new String[]{ token },
                        null);
                if (cursorUser.moveToFirst()) {
                    User u = Orm.build().fromCursor(cursorUser, User.class);
                    LocalLogin.login(getContext(), u.id);
                    MainActivity.startMainActivity(getContext());

                } else {
                    Toast.makeText(getActivity(), getContext().getText(R.string.not_logged), Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }
}