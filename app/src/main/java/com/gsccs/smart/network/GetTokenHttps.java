package com.gsccs.smart.network;

import android.content.Context;
import android.os.Message;

import com.gsccs.smart.model.ResultBean;

public class GetTokenHttps extends BaseHttps {

	private static GetTokenHttps instance = null;

	public static GetTokenHttps getInstance(Context context) {
		if (instance == null) {
			instance = new GetTokenHttps();
		}
		instance.context = context;
		return instance;
	}

	private GetTokenHttps() {

	}

	public void getToken(final OnGetTokenListener onGetTokenListener) {
		BaseRequestParams params = new BaseRequestParams();
		params.addBodyParameter("method", METHOD_GET_TOKEN);
		final Message msg = new Message();
		msg.what = WHAT_GET_TOKEN;
		sendHttpPost(params, new OnHttpResultListener() {

			@Override
			public void onSuccess(ResultBean result) {
				// 如果成功，则将返回的json结果集封装为对象类型
				onGetTokenListener.onGetTokenSuccess(result.getData().toString());
			}

			@Override
			public void onFailure(ResultBean result) {
				onGetTokenListener.onGetTokenFailure(result.getMessage());
			}
		});
	}

	public interface OnGetTokenListener {
		
		public void onGetTokenSuccess(String token);

		public void onGetTokenFailure(String msg);
	}
}