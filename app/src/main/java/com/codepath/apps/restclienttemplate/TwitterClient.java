package com.codepath.apps.restclienttemplate;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    private static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
    private static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    private static final String REST_CONSUMER_KEY = "PXZuhfIR5Vk7hi1X4BKOzf8Jv"; // Change this TODO - GIT IGNORE THIS FILE OR CHANGE KEY WHEN UPLOAD
    private static final String REST_CONSUMER_SECRET = "2xCkNFtQy4evatLxCblAI8RlbSPuFsXLb207NzZVAp5jcByxU3"; // Change this TODO - GIT IGNORE THIS FILE

    // TAG for logging stuffs
    public static final String TAG = "TwitterClient";

    // Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
    private static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

    // See https://developer.chrome.com/multidevice/android/intents
    private static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

    public TwitterClient(Context context) {
        super(context, REST_API_INSTANCE,
                REST_URL,
                REST_CONSUMER_KEY, // key
                REST_CONSUMER_SECRET,
                String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
                        context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
    }

    // DEFINE METHODS for different API endpoints here
    public void getHomeTimeline(int count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("include_entities", true);
        params.put("exclude_replies", false);
        // params.put("since_id", 1);
        // params.put("max_id", 1);
        client.get(apiUrl, params, handler);
    }

    public void addToTimeline(long maxid, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 15);
        params.put("max_id", maxid-1);
        params.put("include_entities", true);
        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(int count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("include_entities", true);
        client.get(apiUrl, params, handler);
    }

    public void addToMentionsTimeline(long maxid, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 15);
        params.put("max_id", maxid-1);
        params.put("include_entities", true);
        client.get(apiUrl, params, handler);
    }

    // Can specify query string params directly or through RequestParams.
    public void sendTweet(String message, long reply_uid, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", message);
        params.put("in_reply_to_status_id", reply_uid);
        client.post(apiUrl, params, handler);
    }

    public void retweetTweet(long uid, AsyncHttpResponseHandler handler) {
        // String apiUrl = getApiUrl("statuses/retweet/:id.json"); // TODO - does this work?
        String apiUrl = getApiUrl("statuses/retweet/"+Long.toString(uid)+".json");
        // RequestParams params = new RequestParams();
        // params.put("id", uid);
        client.post(apiUrl, null, handler);
    }

    public void unretweetTweet(long uid, AsyncHttpResponseHandler handler) {

        // String apiUrl = getApiUrl("statuses/unretweet/:id.json"); // TODO - does this work?
        String apiUrl = getApiUrl("statuses/unretweet/"+Long.toString(uid)+".json"); // TODO - does this work?
        //"statuses/unretweet/"+Long.toString(uid)+".json"
        // RequestParams params = new RequestParams();
        // params.put("id", uid);
        client.post(apiUrl, null, handler);
    }


    public void likeTweet(long uid, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", uid);
        client.post(apiUrl, params, handler);
    }

    public void unlikeTweet(long uid, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", uid);
        client.post(apiUrl, params, handler);
    }

    public void getUsingUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        params.put("include_entities", true);
        client.get(apiUrl, params, handler);
    }

    public void user(long uid, String screenName, AsyncHttpResponseHandler handler) {
        // returns the info about a users profile
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("user_id", uid);
        params.put("screen_name", screenName);
        params.put("include_entities", true);
        client.get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, long count, AsyncHttpResponseHandler handler) {
        // returns the statuses of the user specified by the uid, set count to 20 and maxId to LONG.MAXVALUE for now
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        params.put("count", count); // how many tweets to get
        // params.put("include_rts", true);
        // params.put("exclude_replies", false);
        client.get(apiUrl, params, handler);
    }

    public void addToUserTimeline(String screenName, long maxId, AsyncHttpResponseHandler handler) {
        // returns the statuses of the user specified by the uid, set count to 20 and maxId to LONG.MAXVALUE for now
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("max_id", maxId); // used for infinite paginations
        params.put("screen_name", screenName);
        params.put("count", 30); // how many tweets to get
        params.put("include_rts", true);
        params.put("exclude_replies", false);
        client.get(apiUrl, params, handler);
    }

    public void userLookup(String screenNames, AsyncHttpResponseHandler handler) {
        // returns whether the current user follows the users in the list of longs
        // screenNames is a concatenation of user screen names separated by just commas
        String apiUrl = getApiUrl("friendships/lookup.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenNames);
        client.get(apiUrl, params, handler);
    }

    public void followToggle(String screenName, boolean following, AsyncHttpResponseHandler handler){
        String apiUrl;
        if (!following) apiUrl = getApiUrl("friendships/create.json");
        else apiUrl = getApiUrl("friendships/destroy.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        client.post(apiUrl, params, handler);
    }

    public void userFollowers(){
        // https://dev.twitter.com/rest/reference/get/followers/list
        // get the users followers
        String apiUrl = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();
    }

    public void userFollowings(){
        // https://dev.twitter.com/rest/reference/get/friends/list
        // get the users followings
        String apiUrl = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();
    }



	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
