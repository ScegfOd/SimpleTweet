package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 280;
    EditText tweet_text;
    RestClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        tweet_text = findViewById(R.id.tweet_text);
        client = RestApplication.getRestClient(this);
    }

    public void postTweet(View view) {
        final String tweet = tweet_text.getText().toString();
        if(tweet.isEmpty()){
            //too short
            Toast.makeText(view.getContext(), "Your tweet is empty! Can't post!", Toast.LENGTH_SHORT).show();
        }else if(tweet.length() > MAX_TWEET_LENGTH){
            //too long
            Toast.makeText(view.getContext(), "Your tweet is too long! Can't post!", Toast.LENGTH_SHORT).show();
        }else{
            //just right, make the api call
            client.publishTweet(tweet, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess: published Tweet!");
                    try {
                        Tweet published_tweet = Tweet.from_JSON(json.jsonObject);
                        Log.i(TAG, "Published Tweet says: " + published_tweet.body);
                        Intent intent = new Intent();
                        intent.putExtra("tweet", Parcels.wrap(tweet) );
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure: failed to publish Tweet!", throwable);
                }
            });
        }
    }
}