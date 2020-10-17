package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    private final int TWEET_POST_REQUEST_CODE = 1337;

    RestClient client;
    RecyclerView timeline;
    ItemsAdapter itemsAdapter;
    List<Tweet> tweets;
    SwipeRefreshLayout swipe_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = RestApplication.getRestClient(this);

        timeline = findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(this, tweets);
        timeline.setLayoutManager(new LinearLayoutManager(this));
        timeline.setAdapter(itemsAdapter);
        swipe_container = findViewById(R.id.swipe_container);
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetch new data!");
                populateHomeTimeline();
            }
        });
        // Configure the refreshing colors
        swipe_container.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        populateHomeTimeline();
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG,"successful call to populateHomeTimeline()");
                try {
                    itemsAdapter.clear();
                    itemsAdapter.add_all(Tweet.from_JSON_array(json.jsonArray));
                    swipe_container.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "json exception when loading tweets:", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "OOPH in populateHomeTimeline()", throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onComposeClick(MenuItem item) {
        // start compose activity
        startActivityForResult(new Intent(this, ComposeActivity.class), TWEET_POST_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == TWEET_POST_REQUEST_CODE && resultCode == RESULT_OK){
            // get tweet
            tweets.add(0, (Tweet)Parcels.unwrap( data.getParcelableExtra("tweet")) );
            // update recyclerview
            itemsAdapter.notifyItemInserted(0);
            timeline.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}