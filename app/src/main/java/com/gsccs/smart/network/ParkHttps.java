package com.gsccs.smart.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gsccs.smart.model.ParkEntity;
import com.gsccs.smart.model.ResultBean;
import com.gsccs.smart.model.TweetsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *  停车位服务HTTP接口
 */
public class ParkHttps extends BaseHttps {

	private static ParkHttps instance = null;

	public static ParkHttps getInstance(Context context) {
		if (instance == null) {
			instance = new ParkHttps();
		}
		instance.context = context;
		return instance;
	}

	private ParkHttps() {
	}


	/**
	 * 车位查询
	 *
	 * @param currentPage
	 * @param handler
	 */
	public void queryPageList(Integer currentPage,Double lng,Double lat, final Handler handler) {
		Log.d("queryPointPageList","queryPointPageList");
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_PARK_PAGELIST);
		params.addParams("lng", lng);
		params.addParams("lat", lat);
		params.addParams("currentPage", currentPage);
		final Message msg = new Message();
		msg.what = WHAT_PARK_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONArray array = (JSONArray) result.getData();
				List<ParkEntity> parkList = new ArrayList<ParkEntity>();
				for (int i = 0; i < array.size(); i++) {
					ParkEntity park = JSONObject.toJavaObject(array.getJSONObject(i), ParkEntity.class);
					parkList.add(park);
				}
				msg.obj = parkList;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {
				
			}
		});
	}

}
