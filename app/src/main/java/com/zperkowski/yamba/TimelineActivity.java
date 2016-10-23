package com.zperkowski.yamba;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by zperkowski on 11.10.16.
 */

public class TimelineActivity extends ListActivity {
    public final static String TAG = "TimelineActivity";
    final static String[] FROM = {StatusData.C_USER, StatusData.C_CREATED_AT, StatusData.C_TEXT};
    final static int[] TO = {R.id.row_user, R.id.row_created_at, R.id.row_text};
    Cursor cursor;
    SimpleCursorAdapter adapter;
    TimelineReceiver receiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cursor = ((YambaApp)getApplication()).statusData.query();
        adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
        adapter.setViewBinder(VIEW_BINDER);
        setTitle(R.string.timeline);
        setListAdapter(adapter);
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (receiver == null)
            receiver = new TimelineReceiver();
        registerReceiver(receiver, new IntentFilter(YambaApp.ACTION_NEW_STATUS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    static final SimpleCursorAdapter.ViewBinder VIEW_BINDER = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int i) {
            if (view.getId() != R.id.row_created_at)
                return false;

            long time = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
            CharSequence relativTime = DateUtils.getRelativeTimeSpanString(time);
            ((TextView) view).setText(relativTime);

            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intentStatusUpdate = new Intent(this, StatusActivity.class);
        Intent intentUpdater = new Intent(this, UpdaterService.class);
        Intent intentRefresh = new Intent(this, RefreshService.class);
        Intent intentPreferences = new Intent(this, PrefsActivity.class);

        switch (item.getItemId()) {
            case R.id.item_status_update:
                startActivity(intentStatusUpdate);
                return true;
            case R.id.item_refresh_service:
                startService(intentRefresh);
                return true;
            case R.id.item_start_service:
                startService(intentUpdater);
                return true;
            case R.id.item_stop_service:
                stopService(intentUpdater);
                return true;
            case R.id.item_preferences:
                startActivity(intentPreferences);
                return true;
            default:
                return false;
        }
    }

    class TimelineReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            cursor = ((YambaApp)getApplication()).statusData.query();
            adapter.changeCursor(cursor);
            Log.d(TAG, "TimelineReceiver onReceive changeCursor with count: "
                    + intent.getIntExtra("count", 0));
        }
    }
}
