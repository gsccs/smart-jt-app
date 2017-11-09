package com.gsccs.smart.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

import com.gsccs.smart.model.UserEntity;

public class DbSaveUtils {
	public static final String USER_INFO_DAT = "userinfo.dat";

	public static UserEntity getUserInfoDat(Context context) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(USER_INFO_DAT);
			ois = new ObjectInputStream(fis);
			return (UserEntity) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			// 这里是读取文件产生异常
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// fis流关闭异常
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// ois流关闭异常
					e.printStackTrace();
				}
			}
		}
		// 读取产生异常，返回null
		return null;
	}

	/**
	 *	删除保存在本地的用户dat信息
	 * @param context
	 */
	public static boolean delUserInfoDat(Context context){
		File file = new File(context.getFilesDir(),USER_INFO_DAT);
		return file.delete();
	}
	public static void saveUserInfoDat(Context context, UserEntity userInfo) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context.openFileOutput(USER_INFO_DAT, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
			// 这里是保存文件产生异常
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// fos流关闭异常
					e.printStackTrace();
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// oos流关闭异常
					e.printStackTrace();
				}
			}
		}
	}
}
