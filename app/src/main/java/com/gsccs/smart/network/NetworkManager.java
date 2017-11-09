package com.gsccs.smart.network;

/**
 * YouJoin
 * 网络管理类，封装网络操作接口
 * Created by ZQ on 2015/11/12.
 */
public class NetworkManager {

    /**
     * 网络接口相关常量
     */
    public static final String USER_USERNAME = "user_name";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_ID = "user_id";
    public static final String USER_WORK = "user_work";
    public static final String USER_BIRTH = "user_birth";
    public static final String USER_SIGN = "user_sign";
    public static final String USER_LOCATION = "user_location";
    public static final String USER_SEX = "user_sex";
    public static final String USER_NICKNAME = "user_nickname";

    public static final String TWEETS_CONTNET = "tweets_content";
    public static final String TWEET_ID = "tweet_id";
    public static final String TWEET_COMMENT = "comment_content";
    public static final int UPVOTE_STATUS_YES = 1;
    public static final int UPVOTE_STATUS_NO = 0;

    public static final String FRIEND_ID = "friend_id";

    public static final String PARAM_TYPE = "type";
    public static final String PARAM = "param";
    public static final String PARAM_TYPE_USER_ID = "1";
    public static final String PARAM_TYPE_USER_NAME = "2";
    public static final String PARAM_TYPE_USER_EMAIL = "3";

    public static final String SEND_USERID = "send_userid";
    public static final String RECEIVE_USERID = "receive_userid";
    public static final String MESSAGE_CONTENT = "message_content";
    public static final String TIME_OLD = "0";
    public static final String TIME_NEW = "1";
    public static final String TIME_TYPE = "time";

    public static final String PRIMSG_ID = "primsg_id";

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String FAILURE_SERVER_BUSY = "";

    public static final String PARAM_LOCATION_CHANGED = "location_changed";
    public static final String PARAM_LOCATION_PRE = "location_";
    /**
     * 服务器接口URL
     */
//    public static final String BASE_API_URL = "http://192.168.0.102:8088/youjoin-server/controllers/";
    public static final String IP = "127.0.0.1";
    public static final String BASE_API_URL = "http://www.tekbroaden.com/";
//    public static final String BASE_API_URL = "http://110.65.7.154:8088/youjoin-server/controllers/";
    public static final String API_SIGN_IN = BASE_API_URL + "signin.php";
    public static final String API_SIGN_UP = BASE_API_URL + "signup.php";
    public static final String API_UPDATE_USERINFO = BASE_API_URL + "update_userinfo.php";
    public static final String API_REQUEST_USERINFO = BASE_API_URL + "request_userinfo.php";
    public static final String API_ADD_FRIEND = BASE_API_URL + "add_friend.php";
    public static final String API_SEND_MESSAGE = BASE_API_URL + "send_message.php";
    public static final String API_RECEIVE_MESSAGE = BASE_API_URL + "receive_message.php";
    public static final String API_SEND_TWEET = BASE_API_URL + "send_tweet.php";
    public static final String API_REQUEST_TWEETS = BASE_API_URL + "get_tweets.php";
    public static final String API_COMMENT_TWEET = BASE_API_URL + "comment_tweet.php";
    public static final String API_UPVOTE_TWEET = BASE_API_URL + "upvote_tweet.php";
    public static final String API_REQUEST_FRIEND_LIST = BASE_API_URL + "get_friendlist.php";
    public static final String API_REQUEST_COMMENTS = BASE_API_URL + "get_comments.php";
    public static final String API_REQUEST_PRIMSG_LOG = BASE_API_URL + "chat_log.php";
    public static final String API_REQUEST_PLUGIN = BASE_API_URL + "get_plugin.php";
    public static final String API_REQUEST_AROUND = BASE_API_URL + "get_aroundlist.php";
    public static final String API_REQUEST_IS_UPVOTE = BASE_API_URL + "isupvote_tweet.php";


    public static final String TAG = "YouJoin_Network";


}