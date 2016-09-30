package com.zperkowski.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener {
    // Tag for easier use the logcat - you can implement different for every class
    static final String TAG = "StatusActivity.java";

    Button buttonUpdate;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        editText = (EditText) findViewById(R.id.editText);
        buttonUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String statusText = editText.getText().toString();
        new PostToTwitter().execute(statusText);
    }

    class PostToTwitter extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Twitter twitter = new Twitter("student", "password");
                twitter.setAPIRootUrl("http://yamba.newcircle.com/api");
                twitter.setStatus(params[0]);
                Log.d(TAG, "Successfully posted: " + params[0]);
                return "Successfully posted: " + params[0];
            } catch (TwitterException e) {
                Log.e(TAG, "Died: ", e);
                e.printStackTrace();
                return "Failed posting: " + params[0];
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(StatusActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intentUpdater = new Intent(this, UpdaterService.class);
        Intent intentRefresh = new Intent(this, RefreshService.class);

        switch (item.getItemId()) {
            case R.id.item_refresh_service:
                startService(intentRefresh);
                return true;
            case R.id.item_start_service:
                startService(intentUpdater);
                return true;
            case R.id.item_stop_service:
                stopService(intentUpdater);
                return true;
            default:

                return false;
        }
    }
}
