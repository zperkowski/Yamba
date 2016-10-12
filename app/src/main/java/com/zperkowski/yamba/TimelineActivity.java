package com.zperkowski.yamba;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cursor = ((YambaApp)getApplication()).statusData.query();
        adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
        adapter.setViewBinder(VIEW_BINDER);
        setTitle(R.string.timeline);
        setListAdapter(adapter);
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
}
