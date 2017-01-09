package com.example.rodrigo.auction.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rodrigo.auction.R;

public class LocalLogin {
    private static final String LOGGED = "logged";

    public static void login(Context context, Long userId) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOGGED, userId);
        editor.apply();
    }

    public static void logout(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_preference), Context.MODE_PRIVATE);
        return sharedPref.getLong(LOGGED, -1) != -1;
    }

    public static Long loginId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_preference), Context.MODE_PRIVATE);
        return sharedPref.getLong(LOGGED, -1) == -1 ? null : sharedPref.getLong(LOGGED, -1);
    }
}
