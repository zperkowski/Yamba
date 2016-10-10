package com.zperkowski.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

/**
 * Created by zperkowski on 27.09.16.
 */

public class UpdaterService extends Service{
    static final String TAG = "UpdaterService";
    boolean running = false;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "OnCreated");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "OnStarted");
        running = true;
        new Thread() {
            public void run() {
                try {
                    while (running) {
                        List<Status> timeline =
                                ((YambaApp) getApplication()).getTwitter().getPublicTimeline();

                        for (Status status : timeline) {
                            ((YambaApp)getApplication()).statusData.insert(status);
                            Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
                        }
                        int delay = Integer.parseInt(((YambaApp)getApplication()).prefs.getString("delay", "30"));
                        Thread.sleep(delay * 1000);
                    }
                } catch (TwitterException e) {
                    Log.d(TAG, "Failed because of network error", e);
                } catch (InterruptedException e) {
                    Log.d(TAG, "Updater interrupted", e);
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        Log.d(TAG, "OnDestroied");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
