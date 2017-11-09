package com.gsccs.smart.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;
import com.gsccs.smart.model.ResultBean;
import com.gsccs.smart.model.VersionInfo;

/**
 *
 */
public class AppHttps extends BaseHttps {

	private static AppHttps instance = null;

	public static AppHttps getInstance(Context context) {
		if (instance == null) {
			instance = new AppHttps();
		}
		instance.context = context;
		return instance;
	}

	private AppHttps() {
	}

	/**
	 * 获取系统版本号
	 *
	 * @param handler
	 */
	public void appVersionGet(final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_APP_VERSION_GET);
		final Message msg = new Message();
		msg.what = WHAT_APP_VERSION_GET;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				VersionInfo appVersion = JSONObject.toJavaObject(
						JSONObject.parseObject(result.getData().toString()),
						VersionInfo.class);
				msg.obj = appVersion;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}
}
