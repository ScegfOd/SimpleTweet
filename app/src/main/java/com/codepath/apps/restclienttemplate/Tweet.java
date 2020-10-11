package com.codepath.apps.restclienttemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    public String body;
    public String created_at;
    public User user;

    public static Tweet from_JSON(JSONObject json) throws JSONException {
        Tweet a_tweet = new Tweet();
        a_tweet.body = json.getString("text");
        a_tweet.created_at = json.getString("created_at");
        a_tweet.user = User.from_JSON(json.getJSONObject("user"));

        return a_tweet;
    }

    public static List<Tweet> from_JSON_array(JSONArray json_array) throws JSONException{
        List<Tweet> new_list = new ArrayList<>();
        for(int i = 0; i < json_array.length(); i++){
            new_list.add(from_JSON(json_array.getJSONObject(i)));
        }
        return new_list;
    }
}
