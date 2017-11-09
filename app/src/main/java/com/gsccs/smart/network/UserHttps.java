package com.gsccs.smart.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.listener.OnUpLoadResultListener;
import com.gsccs.smart.model.VersionInfo;
import com.gsccs.smart.model.ResultBean;
import com.gsccs.smart.model.UserEntity;
import com.gsccs.smart.utils.DbSaveUtils;
import com.gsccs.smart.utils.MD5Utils;
import com.gsccs.smart.widget.SystemToastDialog;

/**
 *
 */
public class UserHttps extends BaseHttps {

	private static UserHttps instance = null;

	public static UserHttps getInstance(Context context) {
		if (instance == null) {
			instance = new UserHttps();
		}
		instance.context = context;
		return instance;
	}

	private UserHttps() {
	}

	/**
	 * 更新当前用户的经纬坐标
	 * 
	 * @param userId
	 * @param longitude
	 * @param latitude
	 */
	public void updateUserPostion(String userId, double longitude,
			double latitude) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addBodyParameter("method", METHOD_USESR_POSTION_UPDATE);
		params.addBodyParameter("userId", userId);
		params.addBodyParameter("longitude", longitude + "");
		params.addBodyParameter("latitude", latitude + "");
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
			}

			@Override
			public void onFailure(ResultBean result) {
			}
		});
	}


	/**
	 * 完善用户信息 并且更新头像
	 * 
	 * @param userInfo
	 * @param handler
	 */
	public void saveUserInfo(final String avatarPath, final UserEntity userInfo, final Handler handler) {
		if (!TextUtils.isEmpty(avatarPath)) {
			upLoadImage(UPLOAD_IMAGE_ACTION_AVATAR, avatarPath, new OnUpLoadResultListener() {

				@Override
				public void upLoadImageResultSuccess(String imageUrl) {
					userInfo.setLogo(imageUrl);
					BaseRequestParams params = new BaseRequestParams(context);
					params.addParams("method", METHOD_IMPROVE_USERINFO);
					params.addParams("id", userInfo.getId());
					params.addParams("logo", imageUrl);// 用户头像
					params.addParams("title", userInfo.getTitle());
					//params.addParams("signature", userInfo.getUsersign());
					params.addParams("sex", userInfo.getSex());
					sendHttpPost(params, new OnHttpResultListener() {
						@Override
						public void onSuccess(ResultBean result) {
							SystemToastDialog.showShortToast(context, result.getMessage());
							SmartApplication.setCurrUser(userInfo);
							DbSaveUtils.saveUserInfoDat(context.getApplicationContext(), userInfo);
							handler.sendEmptyMessage(WHAT_IMPROVE_USERINFO);
						}

						@Override
						public void onFailure(ResultBean result) {
							SystemToastDialog.showShortToast(context,
									result.getMessage());
						}
					});
				}

				@Override
				public void upLoadImageResultFailure(String msg) {
					SystemToastDialog.showShortToast(context, msg);
				}
			});
		} else {
			BaseRequestParams params = new BaseRequestParams(context);
			params.addParams("method", METHOD_IMPROVE_USERINFO);
			params.addParams("id", userInfo.getId());
			params.addParams("title", userInfo.getTitle());
			params.addParams("sex", userInfo.getSex());
			//params.addParams("area", userInfo.getArea());
			//params.addParams("borndate", userInfo.getBorndate());
			sendHttpPost(params, new OnHttpResultListener() {
				@Override
				public void onSuccess(ResultBean result) {
					SystemToastDialog.showShortToast(context,
							result.getMessage());
					SmartApplication.setCurrUser(userInfo);
					DbSaveUtils.saveUserInfoDat(
							context.getApplicationContext(), userInfo);
					handler.sendEmptyMessage(WHAT_IMPROVE_USERINFO);
				}

				@Override
				public void onFailure(ResultBean result) {
					SystemToastDialog.showShortToast(context,
							result.getMessage());
				}
			});
		}
	}


	/**
	 * 找回登录密码
	 * 
	 * @param phone
	 * @param handler
	 */
	public void getBackLoginPwdSmsCode(String phone, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addParams("method", METHOD_GETBACK_LOGINPWD_SMSCODE);
		params.addParams("phone", phone);
		final Message msg = new Message();
		msg.what = WHAT_GETBACK_LOGINPWD_SMSCODE;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject jsonObj = (JSONObject) result.getData();
				String smsCode = jsonObj.getString("smsCode");
				msg.obj = smsCode;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 重设登录密码
	 * 
	 * @param phone
	 * @param smsCode
	 * @param loginPwd
	 * @param handler
	 */
	public void getBackLoginPwdSubmit(String phone, String smsCode,
			String loginPwd, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addParams("method", METHOD_GETBACK_LOGINPWD_SUBMIT);
		params.addParams("phone", phone);
		params.addParams("smsCode", smsCode);
		params.addParams("loginPwd", MD5Utils.getMD5Code(loginPwd));
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_GETBACK_LOGINPWD_SUBMIT);
			}

			@Override
			public void onFailure(ResultBean result) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 修改登录密码
	 * 
	 * @param userId
	 * @param oldPwd
	 * @param newPwd
	 * @param handler
	 */
	public void updateLoginPwd(Integer userId, String oldPwd, String newPwd,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_RESET_LOGINPWD);
		params.addParams("id", userId);
		params.addParams("oldpwd",oldPwd);
		params.addParams("password", newPwd);
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_RESET_LOGINPWD);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 修改绑定手机号码（第一步）
	 * 
	 * @param userId
	 * @param phone
	 * @param handler
	 */
	public void bindPhone(Integer userId, String phone, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_PHONE_BINDED_SMSCODE);
		params.addParams("userId", userId);
		params.addParams("phone", phone);
		final Message msg = new Message();
		msg.what = WHAT_PHONE_BINDED_SMSCODE;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject jsonObj = (JSONObject) result.getData();
				msg.obj = jsonObj.getString("smsCode");
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 修改绑定手机号码
	 * 
	 * @param userId
	 * @param phone
	 * @param smsCode
	 * @param handler
	 */
	public void bindPhoneSubmit(Integer userId, String phone, String smsCode,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_PHONE_BINDED_SUBMIT);
		params.addParams("userId", userId);
		params.addParams("phone", phone);
		params.addParams("smsCode", smsCode);

		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_PHONE_BINDED_SUBMIT);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 修改绑定手机号码—— 第一步
	 * 
	 * @param userId
	 * @param handler
	 */
	public void updatePhoneStepOne(Integer userId, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_RESET_PHONE_STEPONE);
		params.addParams("userId", userId);
		final Message msg = new Message();
		msg.what = WHAT_RESET_PHONE_STEPONE;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject jsonObj = (JSONObject) result.getData();
				msg.obj = jsonObj.getString("smsCode");
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 修改绑定手机号码—— 第二步
	 * 
	 * @param userId
	 * @param phone
	 * @param smsCode
	 * @param handler
	 */
	public void updatePhoneStepTwo(Integer userId, String phone,
			String smsCode, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_RESET_PHONE_STEPTWO);
		params.addParams("userId", userId);
		params.addParams("phone", phone);
		params.addParams("smsCode", smsCode);

		final Message msg = new Message();
		msg.what = WHAT_RESET_PHONE_STEPTWO;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject jsonObj = (JSONObject) result.getData();
				msg.obj = jsonObj.getString("smsCode");
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 修改绑定手机号码
	 * 
	 * @param userId
	 * @param phone
	 * @param smsCode
	 * @param handler
	 */
	public void updatePhoneSubmit(Integer userId, String phone, String smsCode,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_RESET_PHONE_SUBMIT);
		params.addParams("userId", userId);
		params.addParams("phone", phone);
		params.addParams("smsCode", smsCode);

		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_RESET_PHONE_SUBMIT);
			}

			@Override
			public void onFailure(ResultBean result) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 退出登录
	 * 
	 * @param userId
	 * @param userName
	 * @param handler
	 */
	public void logout(Integer userId, String userName, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_USER_LOGOUT);
		params.addParams("userId", userId);
		params.addParams("secretKey", MD5Utils.getMD5Code(userName));

		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_USER_LOGOUT);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
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
