package com.gsccs.smart.fragment;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gsccs.smart.R;
import com.gsccs.smart.model.VersionInfo;
import com.gsccs.smart.network.AppHttps;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.service.DownloadService;
import com.gsccs.smart.view.UpdateDialog;

/**
 *  APP更新
 */
public class UpdateFragment extends Fragment {

	//private static final String NOTIFICATION_ICON_RES_ID_KEY = "resId";
	private static final String NOTICE_TYPE_KEY = "type";
	private static final int NOTICE_NOTIFICATION = 2;
	private static final int NOTICE_DIALOG = 1;
	private static final String TAG = "UpdateFragment";

	private FragmentActivity mContext;
	private Thread mThread;
	private int mTypeOfNotice;

	/**
	 * 
	 * @param fragmentActivity
	 *            Required.
	 */
	public static void checkForDialog(FragmentActivity fragmentActivity) {
		FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
		UpdateFragment updateChecker = new UpdateFragment();
		Bundle args = new Bundle();
		args.putInt(NOTICE_TYPE_KEY, NOTICE_DIALOG);
		//args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
		updateChecker.setArguments(args);
		content.add(updateChecker, null).commit();
	}

	/**
	 * 
	 * @param fragmentActivity
	 *            Required.
	 */
	public static void checkForNotification(FragmentActivity fragmentActivity) {
		FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
		UpdateFragment updateChecker = new UpdateFragment();
		Bundle args = new Bundle();
		args.putInt(NOTICE_TYPE_KEY, NOTICE_NOTIFICATION);
		//args.putInt(NOTIFICATION_ICON_RES_ID_KEY, notificationIconResId);
		//args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
		updateChecker.setArguments(args);
		content.add(updateChecker, null).commit();
	}

	
	/**
	 * This class is a Fragment. Check for the method you have chosen.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = (FragmentActivity) activity;
		Bundle args = getArguments();
		mTypeOfNotice = args.getInt(NOTICE_TYPE_KEY);
		checkForUpdates();
	}

	/**
	 *
	 *  解析json数据
	 *
	 */
	private void checkForUpdates() {
		// 初始化获取版本号
		AppHttps.getInstance(getActivity()).appVersionGet(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.obj != null){
					VersionInfo appVersion = (VersionInfo) msg.obj;
					parseJson(appVersion);
				}
			}
		});
	}

	private void parseJson(VersionInfo versionInfo) {
		try {
			String apkUrl = versionInfo.getUrl();
			String remark = versionInfo.getRemark();
			int apkCode = versionInfo.getCode().intValue();
			int versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
			if (apkCode > versionCode) {
				if (mTypeOfNotice == NOTICE_NOTIFICATION) {
					showNotification(remark,apkUrl);
				} else if (mTypeOfNotice == NOTICE_DIALOG) {
					showDialog(remark,apkUrl);
				}
			} else {
				//Toast.makeText(mContext, mContext.getString(R.string.app_no_new_update), Toast.LENGTH_SHORT).show();
			}
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "NameNotFoundException", e);
		}
	}

	/**
	 * Show dialog
	 * 
	 */
	public void showDialog(String content, String apkUrl) {
        UpdateDialog d = new UpdateDialog();
		Bundle args = new Bundle();
		args.putString(BaseConst.APK_UPDATE_CONTENT, content);
		args.putString(BaseConst.APK_DOWNLOAD_URL, apkUrl);
		d.setArguments(args);
		d.show(mContext.getSupportFragmentManager(), null);
	}

	/**
	 * Show Notification
	 * 
	 */
	public void showNotification(String content, String apkUrl) {
		android.app.Notification noti;
		Intent myIntent = new Intent(mContext, DownloadService.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		myIntent.putExtra(BaseConst.APK_DOWNLOAD_URL, apkUrl);
		PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		int smallIcon = mContext.getApplicationInfo().icon;
		noti = new NotificationCompat.Builder(mContext).setTicker(getString(R.string.newUpdateAvailable))
				.setContentTitle(getString(R.string.newUpdateAvailable)).setContentText(content).setSmallIcon(smallIcon)
				.setContentIntent(pendingIntent).build();

		noti.flags = android.app.Notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, noti);
	}

	

	/**
	 * Check if a network available
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean connected = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				connected = ni.isConnected();
			}
		}
		return connected;
	}

	
}
