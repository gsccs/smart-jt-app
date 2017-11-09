package com.gsccs.smart.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gsccs.smart.model.ArticleEntity;
import com.gsccs.smart.model.ArticleFAQ;
import com.gsccs.smart.model.ResultBean;
import com.gsccs.smart.model.TrafficDataEntity;
import com.gsccs.smart.widget.SystemProgressDialog;
import com.gsccs.smart.widget.SystemToastDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *  微信支付HTTP接口
 */
public class WxpayHttps extends BaseHttps {

	private HttpUtils https = null;
	public Context context;

	public WxpayHttps() {
		if (https == null) {
			https = new HttpUtils();
		}
	}
	private static WxpayHttps instance = null;

	public static WxpayHttps getInstance(Context context) {
		if (instance == null) {
			instance = new WxpayHttps();
		}
		instance.context = context;
		return instance;
	}


	/**
	 * 发起支付请求
	 *
	 * @param handler
	 */
	public void sendWxPay(String account, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addParams("method", METHOD_ARTICLE_DETAILS);
		params.addParams("id", account);
		final Message msg = new Message();
		msg.what = WHAT_ARTICLE_DETAILS;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject jsonObj = (JSONObject) result.getData();
				ArticleEntity article = JSONObject.toJavaObject(jsonObj,
						ArticleEntity.class);
				msg.obj = article;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {
			}
		}, true);
	}


	/**
	 * 后台post请求，同时是否需要 有加载提示框
	 *
	 * @param params
	 * @param onHttpResultListener
	 * @author x.d zhang
	 * @date 2016/10/29 void
	 */
	public void sendHttpPost(final String url,final BaseRequestParams params,
							 final OnHttpResultListener onHttpResultListener) {
		SystemProgressDialog.newInstance(context).show();

		https.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						SystemProgressDialog.newInstance(context).dismiss();
						Log.i("post请求返回的json结果", responseInfo.result);
						ResultBean result = JSONObject.toJavaObject(
								JSONObject.parseObject(responseInfo.result),
								ResultBean.class);

						if (result.getCode() == CODE_SYSTEM_SUCCESS) {
							onHttpResultListener.onSuccess(result);
						} else if (result.getCode() == CODE_SYSTEM_FAILURE) {
							if (SystemProgressDialog.newInstance(context)
									.isShowing()) {
								SystemProgressDialog.newInstance(context)
										.dismiss();
							}

							SystemToastDialog.showShortToast(context,
									result.getMessage());
						}else {
							if (SystemProgressDialog.newInstance(context)
									.isShowing()) {
								SystemProgressDialog.newInstance(context)
										.dismiss();
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.d("BaseHttps",error.getLocalizedMessage());
						SystemProgressDialog.newInstance(context).dismiss();
						Log.i("post请求失败返回的信息", msg);
						onHttpResultListener.onFailure(new ResultBean(
								CODE_ERROR_FAILURE, null, msg));
					}
				});
	}



}
