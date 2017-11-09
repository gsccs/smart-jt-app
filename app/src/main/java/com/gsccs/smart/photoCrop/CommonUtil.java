
package com.gsccs.smart.photoCrop;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;


public class CommonUtil {
	private static final String PRE = "com.gsccs.smart.photo.CommonUtil";
	public static final int ICON_BOUND_SIZE = 320;	//头像宽高尺寸（单位：像素）
	public static final int ICON_MAX_BYTES = 30; 	//头像最大字节数（单位：Kb）
	public static final int PIC_MAX_BYTES = 60;		//图片最大字节数（单位：Kb）
	public static int[] getScreenWidthAndHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return new int[] { dm.widthPixels, dm.heightPixels };
	}
	// 一般都使用默认的存储
	private static SharedPreferences getShare(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 上传图片时使用
	 * 
	 * @param context
	 */
	public static String[] getPaths(Context context) {
		SharedPreferences sp = getShare(context);
		return stringToArray(sp.getString(PRE + "Paths", null));
	}
	// 集合处理
	private static final String SPAN = "_//'_//'";

	public static void setPaths(Context context, String[] info) {
		Editor ed = getShare(context).edit();
		ed.putString(PRE + "Paths", arrayToString(info)).commit();
	}
	private static String arrayToString(String[] info) {
		StringBuffer sb = null;
		if (info != null && info.length > 0) {
			sb = new StringBuffer();
			for (int n = 0; n < info.length; n++) {
				sb.append(info[n]);
				if (n < info.length - 1) {
					sb.append(SPAN);
				}
			}
			return sb.toString();
		}
		return null;
	}
	private static String[] stringToArray(String string) {
		String[] result = null;
		if (string != null && string.length() > 0) {
			result = string.split(SPAN);
			for (int n = 0; n < result.length; n++) {
				if ("null".equals(result[n])) {
					result[n] = null;
				}
			}
		}
		return result;
	}
	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}
}
