package com.zperkowski.yamba;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

/**
 * Created by zperkowski on 30-Sep-16.
 */

public class YambaApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener{
    static final String TAG = "YambaApp";
    public static final String ACTION_NEW_STATUS = "com.zperkowski.yamba.NEW_STATUS";
    private Twitter twitter;
    SharedPreferences prefs;
    StatusData statusData;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        statusData = new StatusData(this);
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

    long lastTimestampSeen = -1;

    public int pullAndInsert() {
        int count = 0;
        long biggestTimestamp = -1;
        try {
            List<Twitter.Status> timeline = getTwitter().getPublicTimeline();

            for (Twitter.Status status : timeline) {
                statusData.insert(status);
//                if (biggestTimestamp == -1)
//                    biggestTimestamp = status.createdAt.getTime();
                if (status.createdAt.getTime() > this.lastTimestampSeen) {
                    count++;
                    biggestTimestamp = (status.createdAt.getTime() > biggestTimestamp) ?
                            status.createdAt.getTime() : biggestTimestamp;
                    lastTimestampSeen = status.createdAt.getTime();
                }
                Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
            }
        } catch (TwitterException e) {
            Log.e(TAG, "Failed to pull timeline", e);
        }
        if (count > 0) {
            sendBroadcast(new Intent(ACTION_NEW_STATUS).putExtra("count", count));
        }
    return count;
    }
}
