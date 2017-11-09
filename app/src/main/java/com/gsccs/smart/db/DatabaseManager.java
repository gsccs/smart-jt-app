package com.gsccs.smart.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.model.UserEntity;
import com.gsccs.smart.network.BaseConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by x.d zhang on 2016/11/11.
 */
public class DatabaseManager {


    /**
     * 获取特定URI的每条线程已经下载的文件长度
     * threadid：代表线程的id
     * downlength:代表线程下载的最后位置
     * downpath:代表当前线程下载的资源
     * @param path
     * @return
     */
    public static Map<Integer, Integer> getDataDownload(String path)
    {
        //获取可读取的数据库句柄，一般情况下在该操作的内部实现中，其返回的其实是可写的数据库句柄
        SQLiteDatabase db = DatabaseHelper.getInstance(SmartApplication.getAppContext());
        //建立一个哈希表用于存放每条线程的已经下载的文件长度
        Map<Integer, Integer> data=new HashMap<Integer, Integer>();
        if(db.isOpen()){

            //根据下载路径查询所有线程下载数据，返回的Cursor指向第一条记录之前
            Cursor cursor = db.rawQuery("select " + DatabaseHelper.DOWNLOAD_THREADID + " , "
                    + DatabaseHelper.DOWNLOAD_DOWNLENGTH + " from "
                    + DatabaseHelper.TABLE_DOWNLOAD + " where "
                    + DatabaseHelper.DOWNLOAD_PATH + "=?",new String[]{ path });


            //从第一条记录开始开始遍历Cursor对象
            while(cursor.moveToNext())
            {
                //把线程ID和该线程已下载的长度设置进data哈希表中
                data.put(cursor.getInt(0), cursor.getInt(1));
                data.put(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.DOWNLOAD_THREADID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.DOWNLOAD_DOWNLENGTH)));
            }
            cursor.close();
            db.close();
        }

        return data;
    }
    /**
     * 保存每条线程已经下载的文件长度
     * @param path 下载的路径
     * @param map 现在的ID和已经下载的长度的集合
     */
    public static void saveDownload(String path,Map<Integer, Integer> map){
        //获取可写的数据库句柄
        SQLiteDatabase db = DatabaseHelper.getInstance(SmartApplication.getAppContext());
        if(db.isOpen()){
            //开始食物，因为此处要插入多批数据
            db.beginTransaction();
            try{
                for(Map.Entry<Integer, Integer> entry:map.entrySet())
                {
                    //采用for-each的方式遍历数据集合
                    //插入特定下载路径，特定线程ID,已经下载的数据
                    db.execSQL("insert into " + DatabaseHelper.TABLE_DOWNLOAD
                                    + " (" + DatabaseHelper.DOWNLOAD_PATH
                                    + " ," + DatabaseHelper.DOWNLOAD_THREADID
                                    + " ," + DatabaseHelper.DOWNLOAD_DOWNLENGTH
                                    + ") values(?,?,?)",
                            new Object[]{path, entry.getKey(), entry.getValue()});
                }
                db.setTransactionSuccessful();
            }finally{
                db.endTransaction();
            }
            db.close();
        }

    }

    /**
     * 实时更新每条线程已经下载的文件长度
     * @param path
     * @param threadId
     * @param pos
     */
    public static void updateDownload(String path,int threadId,int pos){
        SQLiteDatabase db = DatabaseHelper.getInstance(SmartApplication.getAppContext());
        if(db.isOpen()){
            //更新特定下载路径，特定线程，已经下载的文件长度
            db.execSQL("update " + DatabaseHelper.TABLE_DOWNLOAD
                    + " set " + DatabaseHelper.DOWNLOAD_DOWNLENGTH
                    + " =? where " + DatabaseHelper.DOWNLOAD_PATH + " =? "
                    + "and " + DatabaseHelper.DOWNLOAD_THREADID + " =? ",new Object[]{pos,path,threadId});
            db.close();
        }

    }

    /**
     * 当文件下载完成后，删除对应的下载记录
     * @param path
     */
    public static void deleteDownload(String path)
    {
        SQLiteDatabase db = DatabaseHelper.getInstance(SmartApplication.getAppContext());
        if(db.isOpen()){
            db.execSQL("delete from " + DatabaseHelper.TABLE_DOWNLOAD
                            + " where " + DatabaseHelper.DOWNLOAD_PATH + " =?",
                    new Object[]{path});
            db.close();
        }

    }





    /**向数据库添加或更新用户信息
     * @param info 要添加或更新的用户信息
     */
    public static void addUserInfo(UserEntity info){
        SQLiteDatabase db = DatabaseHelper.getInstance(SmartApplication.getAppContext());
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_ID, info.getId());
        contentValues.put(DatabaseHelper.USER_NAME, info.getTitle());
        contentValues.put(DatabaseHelper.USER_EMAIL, info.getEmail());
        contentValues.put(DatabaseHelper.USER_SEX, info.getSex());
        contentValues.put(DatabaseHelper.USER_BIRTH, info.getBirth());
        contentValues.put(DatabaseHelper.USER_LOCATION, info.getLocation());
        contentValues.put(DatabaseHelper.USER_PHOTO, info.getLogo());
        contentValues.put(DatabaseHelper.USER_SIGN, info.getUsersign());
        contentValues.put(DatabaseHelper.USER_WORK, info.getWork());
        contentValues.put(DatabaseHelper.USER_NICKNAME, info.getTitle());
        contentValues.put(DatabaseHelper.FOCUS_NUM, info.getFocus_num());
        contentValues.put(DatabaseHelper.FOLLOW_NUM, info.getFollow_num());

        if(getUserInfoById(info.getId()).getResult().equals(BaseConst.SUCCESS)){
            db.update(DatabaseHelper.TABLE_USER_INFO, contentValues,
                    DatabaseHelper.USER_ID + " = ?", new String[] {Integer.toString(info.getId())});
        }else {
            db.insert(DatabaseHelper.TABLE_USER_INFO, null, contentValues);
        }
    }

    /**向数据库添加或更新多个用户信息
     * @param infos 要添加或更新的用户信息列表
     */
    public static void addUsersInfo(List<UserEntity> infos){
        for(UserEntity info : infos){
            addUserInfo(info);
        }
    }



    /**使用userid从数据库获取某用户的信息
     * @param userId 要获取的用户的id
     * @return 获取到的用户信息，result字段为success表示获取成功，failure表示获取失败
     */
    public static UserEntity getUserInfoById(int userId){

        String sql = "select * from " + DatabaseHelper.TABLE_USER_INFO
                + " where " + DatabaseHelper.USER_ID + " = " + Integer.toString(userId);

        return getUserInfo(sql);
    }

    /**使用username从数据库获取某用户的信息
     * @param userName 要获取的用户的username
     * @return 获取到的用户信息，result字段为success表示获取成功，failure表示获取失败
     */
    public static UserEntity getUserInfoByUserName(String userName){

        String sql = "select * from " + DatabaseHelper.TABLE_USER_INFO
                + " where " + DatabaseHelper.USER_NAME + " = '" + userName + "'";

        return getUserInfo(sql);
    }


    /**使用email从数据库获取某用户的信息
     * @param email 要获取的用户的 email
     * @return 获取到的用户信息，result字段为success表示获取成功，failure表示获取失败
     */
    public static UserEntity getUserInfoByEmail(String email){
        String sql = "select * from " + DatabaseHelper.TABLE_USER_INFO
                + " where " + DatabaseHelper.USER_EMAIL + " = '" + email + "'";

        return getUserInfo(sql);
    }


    @NonNull
    private static UserEntity getUserInfo(String sql) {
        UserEntity info = new UserEntity();
        SQLiteDatabase db = DatabaseHelper.getInstance(SmartApplication.getAppContext());
        List<UserEntity> results = parseUserInfoCursor(db.rawQuery(sql, null));

        if(results.isEmpty()){
            info.setResult(BaseConst.FAILURE);
            return info;
        }else{
            info = results.get(0);
            info.setResult(BaseConst.SUCCESS);
            return info;
        }
    }



    private static List<UserEntity> parseUserInfoCursor(Cursor cursor){
        List<UserEntity> list = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                UserEntity info = new UserEntity();
                info.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                info.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NAME)));
                info.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NICKNAME)));
                info.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_EMAIL)));
                info.setSex(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_SEX)));
                info.setBirth(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_BIRTH)));
                info.setLocation(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_LOCATION)));
                info.setLogo(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_PHOTO)));
                info.setUsersign(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_SIGN)));
                info.setFocus_num(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FOCUS_NUM)));
                info.setFollow_num(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FOLLOW_NUM)));
                info.setWork(cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_WORK)));
                list.add(info);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private DatabaseManager(){}


}
