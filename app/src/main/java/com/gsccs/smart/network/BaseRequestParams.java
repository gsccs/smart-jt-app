package com.gsccs.smart.network;

import android.content.Context;
import android.text.TextUtils;

import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.utils.MD5Utils;
import com.lidroid.xutils.http.RequestParams;

/**
 * post提交 将系统级的必传参数通过自定义的类进行初始化，以确保每一个接口调用中都会初始化这些必传参数
 * 
 * @author lenove
 *
 */
public class BaseRequestParams extends RequestParams {

	public BaseRequestParams() {
		long time = System.currentTimeMillis();
		this.addBodyParameter("appKey", "android");
		this.addBodyParameter("locale", "zh_CN");
		this.addBodyParameter("format", "json");
		this.addBodyParameter("timeStamp", time + "");
		this.addQueryStringParameter("sign",
				MD5Utils.getMD5Code("com.gsccs.www.smart" + time));
		this.addBodyParameter("v", "1.0");
	}

	/**
	 * 初始化方法，添加的sessionkey的值
	 * 
	 * @param context
	 */
	public BaseRequestParams(Context context) {
		long time = System.currentTimeMillis();
		this.addBodyParameter("appKey", "android");
		this.addBodyParameter("locale", "zh_CN");
		this.addBodyParameter("format", "json");
		this.addBodyParameter("timeStamp", time + "");
		this.addBodyParameter("sign", MD5Utils.getMD5Code("com.gsccs.www.smart" + time));
		this.addBodyParameter("v", "1.0");
		this.addBodyParameter("sessionKey",SmartApplication.sessionKey);
	}

	/**
	 * 自定义的添加方法，如果为空，默认不添加
	 * 
	 * @param key
	 * @param value
	 */
	public void addParams(String key, String value) {
		if (!TextUtils.isEmpty(value)) {
			this.addQueryStringParameter(key, value);
		}
	}

	public void addParams(String key, Double value) {
		if (value != null && value.doubleValue() != 0) {
			this.addBodyParameter(key, value.doubleValue() + "");
		}
	}

	public void addParams(String key, Long value) {
		if (value != null && value.longValue() != 0) {
			this.addBodyParameter(key, value.longValue() + "");
		}
	}

	public void addParams(String key, int value) {
		if (value != 0) {
			this.addBodyParameter(key, value + "");
		}
	}
	public void addParams(String key, Integer value) {
		if (value != null && value.intValue() != 0) {
			this.addBodyParameter(key, value.intValue() + "");
		}
	}

	public void addParamFile(String key, String file) {
		if (file != null) {
			this.addBodyParameter(key, file);
		}
	}

}
