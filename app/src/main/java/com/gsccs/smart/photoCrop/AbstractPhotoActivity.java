package com.gsccs.smart.photoCrop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.gsccs.smart.R;

import java.io.File;


public abstract class AbstractPhotoActivity extends Activity {

	public static final int REQUEST_CODE_CROP_PHOTO = 0x97;
	private static final int REQUEST_CODE_PHOTO = 0x98;
//	private static final int REQUEST_CODE_PICTURE = 0x99;
	private static final int REQUEST_CODE_PIC_KITKAT = 0x99;
	private static final int REQUEST_CODE_SELECT_PIC = 0x9a;

	public static final String RESULT_SELECTED_PHOTO_PATH = "resultSelectedPhotoPath";
	private Dialog dialog;

	protected File captureFile;
	/** 保存裁剪后的图片路径 */
	private String photoPath = null;

	private boolean isScaleRatio = false;
	private DisplayMetrics metrics;

	int permsRequestCode = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager winManager = getWindowManager();
		metrics = new DisplayMetrics();
		winManager.getDefaultDisplay().getMetrics(metrics);
	}
	protected void popup(Context context, String photoPath, boolean isScaleRatio){
		this.isScaleRatio = isScaleRatio;
		popup(context, photoPath);
	}
	
	@SuppressLint("InflateParams")
	protected void popup(Context context, String photoPath) {
		this.photoPath = photoPath;
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View holder = inflater.inflate(R.layout.photo_popup_view,
				null, false);
		View gallery = holder.findViewById(R.id.btnPictrue);
		View capture = holder.findViewById(R.id.btnCapture);
		View cancel = holder.findViewById(R.id.btnCanel);

		ButtonClick click = new ButtonClick(this);
		gallery.setOnClickListener(click);// 图库选择
		capture.setOnClickListener(click);// 拍照选择
		cancel.setOnClickListener(click);// 取消

		dialog = PopupUtil.makePopup(context, holder);
		dialog.show();
	}

	public class ButtonClick implements View.OnClickListener {

		private Context context;

		public ButtonClick(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View v) {

			if (dialog != null) {
				dialog.dismiss();
			}

			if (v.getId() == R.id.btnPictrue) {// 图库选取
//				Intent intent = new Intent(Intent.ACTION_PICK, null);
//				intent.setDataAndType(
//						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//				startActivityForResult(intent, REQUEST_CODE_PICTURE);
				
				Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/jpeg");
				if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
				        startActivityForResult(intent, REQUEST_CODE_PIC_KITKAT);  
				}else{              
				        startActivityForResult(intent, REQUEST_CODE_SELECT_PIC); 
				} 
			}

			if (v.getId() == R.id.btnCapture) {// 拍照
				/*// 从相册选取
				Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				in.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
				in.putExtra("return-data", false);
				in.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
				startActivityForResult(in, REQUEST_CODE_PHOTO);*/
				checkCameraAuth();
			}
		}
	}

	private void callCamera(){
		// 从相册选取
		Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		in.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
		in.putExtra("return-data", false);
		in.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(in, REQUEST_CODE_PHOTO);
	}

	private boolean checkSdkVersion(){
		return(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
	}

	public void checkCameraAuth()
	{
		if(checkSdkVersion()){
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
			{
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, permsRequestCode);
			} else
			{
				callCamera();
			}
		}else{
			callCamera();
		}

	}


	@Override
	public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults)
	{
		switch(permsRequestCode){

			case 200:

				boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
				if(cameraAccepted){
					//授权成功之后，调用系统相机进行拍照操作等
					callCamera();
				}else{
					//用户授权拒绝之后，友情提示一下就可以了
				}
				break;
		}
		//super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
//		case REQUEST_CODE_PICTURE://相册获取
//			if(data == null){
//				return;
//			}
//			String picturePath = getPhotoPathByLocalUri(this, data);
////			Bitmap mBitmap = BitmapUtils.getBmpFromFile(path,
////					metrics.widthPixels, metrics.heightPixels, null);
////			try {
////				mBitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(new File(photoPath)));
////			} catch (FileNotFoundException e) {
////				e.printStackTrace();
////			}
//			ImageUtil.cropPhotoSelf(this,picturePath, photoPath, isScaleRatio);
//			break;
		case REQUEST_CODE_SELECT_PIC:
			if (data == null) {
				return;
			}
			String picturePath = getPhotoPathByLocalUri(this, data);
			ImageUtil.cropPhotoSelf(this, picturePath, photoPath, isScaleRatio);
			break;
		case REQUEST_CODE_PIC_KITKAT:
			if (data == null) {
				return;
			}
			String picRealPath = getPath(this, data.getData());
			ImageUtil.cropPhotoSelf(this, picRealPath, photoPath, isScaleRatio);
			break;
		case REQUEST_CODE_PHOTO://手机拍照
			Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
			if(bitmap == null){
				return;//如果把这个路径转换为bitmap图片失败，说明图片不存在
			}
			ImageUtil.cropPhotoSelf(this, photoPath,"", isScaleRatio);
			break;
		case ImageUtil.CROP_PHOTO:
			if(data == null){
				return;
			}
			String resultPath = data.getStringExtra(RESULT_SELECTED_PHOTO_PATH);
			onPhotoSelected(resultPath);
			break;
		default:
			break;
		}

	}

	/**
	 * 获取从本地图库返回来的时候的URI解析出来的文件路径
	 * 
	 * @return
	 */
	private String getPhotoPathByLocalUri(Context context, Intent data) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		if(cursor == null){
//			SystemToastDialog.showShortToast(context, ");
//			return;
		}
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return picturePath;
	}
	
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {

	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	public abstract void onPhotoSelected(String imgPath);
}