package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ClipData;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.util.Log;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.github.scribejava.apis.TwitterApi;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
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
}