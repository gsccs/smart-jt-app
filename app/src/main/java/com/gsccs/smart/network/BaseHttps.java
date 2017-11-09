package com.gsccs.smart.network;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.gsccs.smart.listener.OnUpLoadResultListener;
import com.gsccs.smart.model.ImageInfo;
import com.gsccs.smart.model.ResultBean;
import com.gsccs.smart.model.VersionInfo;
import com.gsccs.smart.widget.SystemProgressDialog;
import com.gsccs.smart.widget.SystemToastDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class BaseHttps implements BaseConst {

	private HttpUtils https = null;
	public Context context;

	public BaseHttps() {
		if (https == null) {
			https = new HttpUtils();
		}
	}

	/**
	 * 后台post请求，同时是否需要 有加载提示框
	 * 
	 * @param params
	 * @param onHttpResultListener
	 * @param isShowProgress
	 * @author x.d zhang
	 * @date 2016/10/29 void
	 */
	public void sendHttpPost(final BaseRequestParams params,
			final OnHttpResultListener onHttpResultListener,
			final boolean isShowProgress) {
		if (isShowProgress) {
			SystemProgressDialog.newInstance(context).show();
		}
		https.send(HttpMethod.POST, SERVER_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (isShowProgress) {
							SystemProgressDialog.newInstance(context).dismiss();
						}
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
						if (isShowProgress) {
							SystemProgressDialog.newInstance(context).dismiss();
						}
						Log.i("post请求失败返回的信息", msg);
						onHttpResultListener.onFailure(new ResultBean(
								CODE_ERROR_FAILURE, null, msg));
					}
				});
	}

	/**
	 * 上传单张图片
	 *
	 * @param action
	 *
	 * @param picPath
	 *            图片路径
	 */
	public void upLoadImage(final String action, final String picPath, final OnUpLoadResultListener onUpLoadResultListener) {
		if (TextUtils.isEmpty(picPath)) {
			Log.d("BaseHttps", picPath + "上传失败,图片途径为空");
			return;
		}
		Bitmap bmp = BitmapFactory.decodeFile(picPath);
		//File filePic = new File(picPath);
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_UPLOAD_IMAGE);
		params.addParamFile("file", "jpg@" + Base64.encodeToString(Bitmap2Bytes(bmp),Base64.DEFAULT));
		//params.add("photo", "png@" + Base64.encodeBase64String(uploadFile.getContent()));
		final Message msg = new Message();
		msg.what = WHAT_UPLOAD_IMAGE;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				Log.d("OnHttpResultListener","onSuccess");
				String url = (String) result.getData();
				Log.d("OnHttpResultListener",url);
				onUpLoadResultListener.upLoadImageResultSuccess(url);
			}

			@Override
			public void onFailure(ResultBean result) {
				Log.d("OnHttpResultListener","onFailure");
				onUpLoadResultListener.upLoadImageResultFailure("error");
			}
		});
	}


	/**
	 * 多张图片上传
	 * 
	 * @param action
	 * @param imageUrls
	 *            list<String> 图片路径数组
	 * @param onUpLoadResultListener
	 */
	private int i = 0;

	public void upLoadImage(final String action, final List<String> imageInfos,
			final OnUpLoadResultListener onUpLoadResultListener) {
		if (null==imageInfos || imageInfos.size()<=0){
			return;
		}
		Log.d("upLoadImage","imageInfos length"+imageInfos.size());
		final List<String> resultUrls = new ArrayList<String>();// 上传成功后的图片urls
		i = 0;
		upLoadImage(action, imageInfos.get(i), new OnUpLoadResultListener() {
			@Override
			public void upLoadImageResultSuccess(String imageUrl) {
				resultUrls.add(imageUrl);
				i++;
				if (i < imageInfos.size()) {
					upLoadImage(action, imageInfos.get(i), this);
				} else {
					Log.d("resultUrls", resultUrls.toString());
					String urls = "";
					if (resultUrls.size() > 1) {
						for (int i = 0; i < resultUrls.size(); i++) {
							if (i == 0) {
								urls = resultUrls.get(i);
							} else {
								urls += (";" + resultUrls.get(i));
							}
						}
					} else {
						urls = resultUrls.get(0);
					}
					onUpLoadResultListener.upLoadImageResultSuccess(urls);
				}
			}

			@Override
			public void upLoadImageResultFailure(String msg) {
				onUpLoadResultListener.upLoadImageResultFailure(msg);
			}
		});
	}





	public void sendHttpPost(BaseRequestParams params,
			final OnHttpResultListener onHttpResultListener) {
		sendHttpPost(params, onHttpResultListener, false);
	}

	public interface OnHttpResultListener {
		public void onSuccess(ResultBean result);
		public void onFailure(ResultBean result);
	}

	private byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}
