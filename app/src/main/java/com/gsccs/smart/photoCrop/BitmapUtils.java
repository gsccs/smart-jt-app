package com.gsccs.smart.photoCrop;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

import java.io.File;

public class BitmapUtils {
	
	/**
	 * TODO
	 * 
	 * @param filePath
	 * @param bmpWidth
	 *            -1时候不特殊处理图片
	 * @param config
	 * @return
	 * @throws
	 */
	public static Bitmap getBmpFromFile(String filePath, int bmpWidth,int bmpHeight,
			Config config) {
		if (filePath != null) {
			File file = new File(filePath);
			if (file.exists()) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				Bitmap bmp = null;
				if (bmpWidth != -1 && bmpHeight != -1) {
					opts.inPreferredConfig = Config.ALPHA_8;
					opts.inJustDecodeBounds = true;
					bmp = BitmapFactory.decodeFile(filePath, opts);

					int scaleWidth = opts.outWidth;
					int scaleHeight = opts.outHeight;
					int widthRatio = (int) Math.ceil(scaleWidth / bmpWidth); 
					int heightRatio = (int) Math.ceil(scaleHeight / bmpHeight); 
					if(widthRatio > 1 || heightRatio > 1) { 
						if(widthRatio > heightRatio) { 
							if (scaleWidth > 1280) {
								widthRatio = widthRatio * 2;
							}
							opts.inSampleSize = widthRatio; 
						} else { 
							if (scaleHeight > 1280) {
								heightRatio = heightRatio * 2;
							}
							opts.inSampleSize = heightRatio; 
						} 
					} 

					opts.inPreferredConfig = config;
					opts.inJustDecodeBounds = false;
					try {
						bmp = BitmapFactory.decodeFile(filePath, opts);
					} catch (OutOfMemoryError e) {
						e.printStackTrace();
						return null;
					}
				} else {
					opts.inPreferredConfig = config;
					opts.inJustDecodeBounds = false;
					try {
						bmp = BitmapFactory.decodeFile(filePath, opts);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					} catch (OutOfMemoryError e) {
						// TODO: handle exception
						return null;
					}
				}
				return bmp;
			}
		}
		return null;
	}

}
