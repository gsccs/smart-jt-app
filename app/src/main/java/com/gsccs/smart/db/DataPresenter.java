package com.gsccs.smart.db;

import com.gsccs.smart.model.UserEntity;


/**用于封装所有除图片加载外的数据请求(数据库和网络)，将结果提供给UI显示
 * YouJoin-Android
 * Created by ZQ on 2015/12/13.
 */
public class DataPresenter {

    public static final String TAG = "smart-jt";

    public static UserEntity requestUserInfoFromCache(int userId){
        return DatabaseManager.getUserInfoById(userId);
    }

    public static UserEntity requestUserInfoFromCache(String username){
        return DatabaseManager.getUserInfoByUserName(username);
    }



    public static void requestUserInfoById(int userId, final GetUserInfo q){
        UserEntity cookieInfo = DatabaseManager.getUserInfoById(userId);
    }





    public interface GetUserInfo{
        void onGetUserInfo(UserEntity info);
    }



}
