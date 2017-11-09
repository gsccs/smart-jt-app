package com.gsccs.smart.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.model.ResultBean;
import com.gsccs.smart.model.UserEntity;
import com.gsccs.smart.widget.SystemProgressDialog;



/**
 *  登录服务HTTP接口
 */
public class LoginHttps extends BaseHttps {

	private static LoginHttps instance = null;

	public static LoginHttps getInstance(Context context) {
		if (instance == null) {
			instance = new LoginHttps();
		}
		instance.context = context;
		return instance;
	}

	private LoginHttps() {
	}

	/**
	 * 注册第一步发送短信验证码
	 * 
	 * @param phone
	 * @author caiwen
	 * @date 2016/11/10
	 */
	public void sendSmS(String phone, final Handler handler) {
		// 保存参数列表的对象
		BaseRequestParams params = new BaseRequestParams();
		params.addBodyParameter("account", phone);
		params.addBodyParameter("method", METHOD_SMS_REGISTER);

		final Message msg = new Message();
		msg.what = WHAT_SMS_REGISTER;
		sendHttpPost(params, new OnHttpResultListener() {

			@Override
			public void onSuccess(ResultBean result) {
				// 如果成功，则将返回的json结果集封装为对象类型
				msg.obj = result;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {
			}
		});
	}

	/**
	 * 用户注册接口
	 * 
	 * @param phone
	 * @param loginPwd
	 *            该方法中会对 loginPwd 进行加密 传过来的是未加密的
	 * @param smsCode
	 *            该方法中会对 smsCode 进行加密 传过来的是未加密的
	 * @author caiwen
	 * @date 2016/11/10
	 */
	public void register(String phone, String loginPwd, String smsCode,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addBodyParameter("method", METHOD_USER_REGISTER);
		params.addBodyParameter("account", phone);
		params.addBodyParameter("password", loginPwd);
		params.addBodyParameter("smscode", smsCode);
		//params.addBodyParameter("loginPwd", MD5Utils.getMD5Code(loginPwd));
		//params.addBodyParameter("smscode", MD5Utils.getMD5Code(smsCode));
		final Message msg = new Message();
		msg.what = WHAT_USER_REGISTER;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {

				handler.sendEmptyMessage(WHAT_SMS_REGISTER);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 用户登录
	 * 
	 * @param phone
	 * @param loginPwd
	 *            该方法中会对 loginPwd 进行加密 传过来的是未加密的
	 * @param handler
	 * @date 2016/12/10
	 */
	public void userLogin(String phone, String loginPwd, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addBodyParameter("method", METHOD_USER_LOGIN);
		params.addBodyParameter("account", phone);
		params.addBodyParameter("password", loginPwd);
		//params.addBodyParameter("password", MD5Utils.getMD5Code(loginPwd));
		final Message msg = new Message();
		msg.what = WHAT_USER_LOGIN_PHONE;

		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				if (result.getCode() == CODE_SYSTEM_SUCCESS) {// 登录成功

					JSONObject jsonObj = (JSONObject) result.getData();
					String sessionId = jsonObj.getString("sessionId");
					JSONObject userInfoJson = jsonObj.getJSONObject("user");

					UserEntity userInfo = JSONObject.toJavaObject(userInfoJson,
							UserEntity.class);
					SmartApplication.setCurrUser(userInfo);
					SmartApplication.sessionKey = sessionId;
					//msg.obj = resultMap;
					handler.sendMessage(msg);
				}
			}

			@Override
			public void onFailure(ResultBean result) {
				SystemProgressDialog.newInstance(context).dismiss();// 请求失败也关闭提示框
			}
		}, false);
	}

}
