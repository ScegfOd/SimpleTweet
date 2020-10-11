package com.codepath.apps.restclienttemplate;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public String name;
    public String handle;
    public String profile_image_url;
    public static User from_JSON(JSONObject json) throws JSONException {
        User a_user = new User();
        a_user.profile_image_url = json.getString("profile_image_url_https");
        a_user.name = json.getString("name");
        a_user.handle = json.getString("screen_name");
        return a_user;
    }
}
