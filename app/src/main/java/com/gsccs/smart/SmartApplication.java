package com.gsccs.smart;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;

import com.gsccs.smart.model.UserEntity;


/**
 * Created by x.d zhang on 2016/11/12.
 */
public class SmartApplication extends Application {

    /** 保存登录的用户的sessionKey */
    public static String sessionKey = "";


    public static boolean IS_FIRST;
    public static float sScale;
    public static int sHeightPix;

    private static Context context;

    private static UserEntity currUser;

    @Override
    public void onCreate(){
        MultiDex.install(this);

        super.onCreate();
        // 首次启动不跳过欢迎界面；
        IS_FIRST = true;
        context = getApplicationContext();

        //NetworkManager.initialize(context);
        //Fresco.initialize(this);

        sScale = getResources().getDisplayMetrics().density;
        sHeightPix = getResources().getDisplayMetrics().heightPixels;

        //AVOSCloud.initialize(this, "hmUYX9LRCEa7Of6kQrDVrzes-gzGzoHsz", "NdwBtQEQOmhwftwXMt0I9vn4");
        //AVIMClient.setOfflineMessagePush(true);
        //AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getAppContext(){
        return context;
    }

    public static UserEntity getCurrUser() {
        //currUser = new UserEntity();
        //currUser.setId(17133);
        return currUser;
    }

    public static void setCurrUser(UserEntity currUser) {
        SmartApplication.currUser = currUser;
    }


    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


}
