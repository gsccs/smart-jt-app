package com.gsccs.smart.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gsccs.smart.model.ArticleEntity;
import com.gsccs.smart.model.CameraEntity;
import com.gsccs.smart.model.ResultBean;
import com.gsccs.smart.model.TrafficDataEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *   交通指数HTTP接口
 */
public class TrafficHttps extends BaseHttps {

	private static TrafficHttps instance = null;

	public static TrafficHttps getInstance(Context context) {
		if (instance == null) {
			instance = new TrafficHttps();
		}
		instance.context = context;
		return instance;
	}

	private TrafficHttps() {
		
	}

	/**
	 * 查询交通指数接口
	 *
	 * @param handler
	 */
	public void queryTrafficData(final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		//params.addBodyParameter("cityName", cityName);
		params.addBodyParameter("method", METHOD_TRAFFIC_DATA);
		final Message msg = new Message();
		msg.what = WHAT_TRAFFIC_DATA;

		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject jsonObj = (JSONObject) result.getData();
				JSONObject qsdata = (JSONObject) jsonObj.get("qsdata");
				JSONArray sqDatasArray = jsonObj.getJSONArray("sqDatas");
				JSONArray gxDatasArray = jsonObj.getJSONArray("gxDatas");
				JSONArray jckDatasArray =jsonObj.getJSONArray("jckDatas");

				List<TrafficDataEntity> sqDataList = new ArrayList<TrafficDataEntity>();
				List<TrafficDataEntity> gxDataList = new ArrayList<TrafficDataEntity>();
				List<TrafficDataEntity> jckDataList = new ArrayList<TrafficDataEntity>();
				if (sqDatasArray != null && sqDatasArray.size() > 0) {
					for (int i = 0; i < sqDatasArray.size(); i++) {
						sqDataList.add(JSONObject.toJavaObject(
								sqDatasArray.getJSONObject(i), TrafficDataEntity.class));
					}
				}
				if (gxDatasArray != null && gxDatasArray.size() > 0) {
					for (int i = 0; i < gxDatasArray.size(); i++) {
						gxDataList.add(JSONObject.toJavaObject(
								gxDatasArray.getJSONObject(i), TrafficDataEntity.class));
					}
				}
				if (jckDatasArray != null && jckDatasArray.size() > 0) {
					for (int i = 0; i < jckDatasArray.size(); i++) {
						jckDataList.add(JSONObject.toJavaObject(
								jckDatasArray.getJSONObject(i), TrafficDataEntity.class));
					}
				}
				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				TrafficDataEntity qsData = JSONObject.toJavaObject(qsdata,
						TrafficDataEntity.class);
				resultMap.put("qsData", qsData);
				resultMap.put("sqDataList", sqDataList);
				resultMap.put("gxDataList", gxDataList);
				resultMap.put("jckDataList", jckDataList);
				msg.obj = resultMap;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 分页查询文章列表
	 */
	public void queryArticlePageList(Integer channel,Integer currPage, final Handler handler) {
		Log.d("ArticleEntity",METHOD_ARTICLE_PAGELIST);
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_ARTICLE_PAGELIST);
		params.addParams("currPage", currPage);
		//params.addParams("channel",channel);
		final Message msg = new Message();
		msg.what = WHAT_ARTICLE_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				Log.d("ArticleEntity",result.getCode()+"");
				List<ArticleEntity> infoList = new ArrayList<ArticleEntity>();
				JSONArray array = (JSONArray) result.getData();
				Log.d("ArticleEntity",array.size()+"");
				for (int i = 0; i < array.size(); i++) {
					infoList.add(JSONObject.toJavaObject(
							array.getJSONObject(i), ArticleEntity.class));
				}
				msg.obj = infoList;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}


	/**
	 * 查询摄像头列表
	 * 
	 * @param keyword
	 * @param handler
	 */
	public void queryCameraPageList(String keyword, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_CAMEAR_PAGELIST);
		params.addParams("keyword", keyword);
		final Message msg = new Message();
		msg.what = WHAT_CAMERA_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONArray array = (JSONArray) result.getData();
				List<CameraEntity> cameraList = new ArrayList<CameraEntity>();
				for (int i = 0; i < array.size(); i++) {
					CameraEntity camera = JSONObject.toJavaObject(
							array.getJSONObject(i), CameraEntity.class);
					cameraList.add(camera);
				}
				msg.obj = cameraList;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 查询文章详情
	 * 
	 * @param articleId
	 *            活动ID
	 * @param handler
	 */
	public void getArticleDetail(Integer articleId, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addParams("method", METHOD_ARTICLE_DETAILS);
		params.addParams("id", articleId);
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






}
