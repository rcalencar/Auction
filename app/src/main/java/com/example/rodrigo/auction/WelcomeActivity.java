package com.example.rodrigo.auction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.rodrigo.auction.repository.local.LocalLoginDAO;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!LocalLoginDAO.isLoggedIn(this)) {
            LoginActivity.startLoginActivity(this);

        } else {
            MainActivity.startMainActivity(this);
        }
    }
}
