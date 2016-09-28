package com.zperkowski.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Status;
import winterwell.jtwitter.Twitter;

/**
 * Created by zperkowski on 27.09.16.
 */

public class UpdaterService extends Service{
    static final String TAG = "UpdaterService";
    Twitter twitter;

    @Override
    public void onCreate() {
        super.onCreate();
        twitter = new Twitter("student", "password");
        twitter.setAPIRootUrl("http://yamba.newcircle.com/api");
        Log.d(TAG, "OnCreated");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "OnStarted");
        new Thread() {
            public void run() {
                try {
                    List<Status> timeline = twitter.getPublicTimeline();

                    for (Status status : timeline) {
                        Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroied");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
