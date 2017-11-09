package com.gsccs.smart.photoCrop;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.io.IOException;

/**
 * 
 * @author lizengxu
 * 
 */
public class ImageUtil {
	public static final String TAG = "ImageUtil";
	public static final int CROP_PHOTO = 107; // 剪裁照片

	/**
	 * 拍照后剪切(使用自己的剪切)
	 * 
	 * @param activityOrFragment
	 *            选图所处的当前的活动类 如果是fragment一定要传fragment
	 * @param outputFile
	 *            所选图的输出文件路径
	 * @param resultOutputFile
	 * 			  	图片最终要保存的路径
	 * @param square
	 *            是否保持正方形比例
	 */
	public static void cropPhotoSelf(Object activityOrFragment,
			String outputFile,String resultOutputFile, boolean square) {
		Activity activity = null;
		Fragment fragment = null;
		if (activityOrFragment instanceof Activity) {
			activity = (Activity) activityOrFragment;
		} else if (activityOrFragment instanceof Fragment) {
			fragment = (Fragment) activityOrFragment;
			activity = fragment.getActivity();
		}

		if (activity == null && fragment == null) {
			return;
		}

		if (outputFile == null) {
			String[] info = CommonUtil.getPaths(activity);
			if (info != null && info.length > 0) {
				outputFile = info[info.length - 1];
			}
		}

		Intent intent = new Intent();
		intent.putExtra("image-path", outputFile);
		intent.putExtra("image-resultPath", resultOutputFile);//相册获取图片最终的保存路径（主要用于相册获取的参数）
		intent.putExtra("scale", true);
		if (square) {
			intent.putExtra("aspectX", 4);
			intent.putExtra("aspectY", 3);
//			intent.putExtra("outputX", 600);
//			intent.putExtra("outputY", 600);
		}
		intent.setClass(activity, CropImageActivity.class);
		intent.putExtra("return-data", false);
		if (fragment != null) {
			fragment.startActivityForResult(intent, CROP_PHOTO);
		} else {
			activity.startActivityForResult(intent, CROP_PHOTO);
		}
	}

	/**
	 * 根据uri得到绝对路径
	 * 
	 * @param act
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathFromURI(Activity act, Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			@SuppressWarnings("deprecation")
			Cursor cursor = act
					.managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static int getRotate(String source) {
		if (TextUtils.isEmpty(source)) {
			return 0;
		}
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(source);
		} catch (IOException e) {
			e.printStackTrace();
			exif = null;
		}
		int digree = 0;
		if (exif != null) {
			// 读取图片中相机方向信息
			int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			// 计算旋转角度
			switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				digree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				digree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				digree = 270;
				break;
			default:
				digree = 0;
				break;
			}
		}
		return digree;
	}
	
}
