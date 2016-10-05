package com.zperkowski.yamba;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import winterwell.jtwitter.Twitter;

/**
 * Created by zperkowski on 30-Sep-16.
 */

public class YambaApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener{
    static final String TAG = "YambaApp";
    private Twitter twitter;
    SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        Log.d(TAG, "onCreated");
    }

    public Twitter getTwitter() {
        if (twitter == null) {
            String username = prefs.getString(getString(R.string.username), "student");
            String password = prefs.getString(getString(R.string.password), "password");
            String server = prefs.getString(getString(R.string.server), "http://yamba.newcircle.com/api");

            twitter = new Twitter(username, password);
            twitter.setAPIRootUrl(server);
        }
        return twitter;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        twitter = null;
        Log.d(TAG, "onSharedPreferenceChanged for key: " + key);
    }
}
