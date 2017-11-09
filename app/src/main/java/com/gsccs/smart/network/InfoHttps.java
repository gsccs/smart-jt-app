package com.gsccs.smart.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gsccs.smart.listener.OnUpLoadResultListener;
import com.gsccs.smart.model.CorpEntity;
import com.gsccs.smart.model.DemandEntity;
import com.gsccs.smart.model.InfoEntity;
import com.gsccs.smart.model.LostFoundEntity;
import com.gsccs.smart.model.ResultBean;
import com.gsccs.smart.model.TweetsEntity;
import com.gsccs.smart.widget.SystemProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  生活服务HTTP接口
 */
public class InfoHttps extends BaseHttps {

	private static InfoHttps instance = null;

	public static InfoHttps getInstance(Context context) {
		if (instance == null) {
			instance = new InfoHttps();
		}
		instance.context = context;
		return instance;
	}

	private InfoHttps() {
	}

	/**
	 * 发布失物招领
	 * 
	 * @param userId
	 *            用户ID
	 * @param imageInfos
	 *            所有图片
	 * @param content
	 *            发布时补充的说明内容

	 */
	public void lostFoundAdd(final Integer userId,final Integer type, final String content, List<String> imageInfos,
			final Handler handler) {
		Log.d("lostFoundAdd","lostFoundAdd");
		if (null==imageInfos || imageInfos.size()<=0){
			lostTextPost(userId,type,content,"",handler);
		}else{
			upLoadImage(UPLOAD_IMAGE_ACTION_LOST, imageInfos,
					new OnUpLoadResultListener() {
						@Override
						public void upLoadImageResultSuccess(String imageUrl) {
							Log.d("imageUrl",imageUrl);
							lostTextPost(userId,type,content,imageUrl,handler);
						}

						@Override
						public void upLoadImageResultFailure(String msg) {
							SystemProgressDialog.newInstance(context).dismiss();
						}
			});
		}
	}

	private void lostTextPost(final Integer userId,final Integer type, final String content,String imgurls,final Handler handler){
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_LOSTFOUND_ADD);
		params.addParams("userid", userId);
		params.addParams("type", type+1);
		params.addParams("imgurls", imgurls);
		params.addParams("content", content);
		final Message msg = new Message();
		msg.what = WHAT_LOSTFOUND_ADD;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				Log.d("METHOD_LOSTFOUND_ADD", result.toString());
				//SystemProgressDialog.newInstance(context).dismiss();
				handler.sendEmptyMessage(WHAT_LOSTFOUND_ADD);
			}

			@Override
			public void onFailure(ResultBean result) {
				Log.d("METHOD_LOSTFOUND_ADD", result.toString());
				//SystemProgressDialog.newInstance(context).dismiss();
			}
		});
	}

	/**
	 * 家政服务信息查询
	 *
	 * @param lng
	 * @param lat
	 * @param currentPage
	 * @param handler
	 */
	public void queryDomesticPageList(Double lng,Double lat, Integer currentPage, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_DOMESTIC_PAGELIST);
		params.addParams("currentPage", currentPage);
		final Message msg = new Message();
		msg.what = WHAT_DOMESTIC_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				List<CorpEntity> list = new ArrayList<CorpEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						CorpEntity entity = JSONObject.toJavaObject(array.getJSONObject(i), CorpEntity.class);
						list.add(entity);
					}
				}
				msg.obj = list;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}


	/**
	 *  黄页电话信息查询
	 *
	 * @param currentPage
	 * @param handler
	 */
	public void queryYellowPageList(Integer currentPage, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_YELLOW_PAGELIST);
		params.addParams("currentPage", currentPage);
		final Message msg = new Message();
		msg.what = WHAT_YELLOW_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				List<CorpEntity> list = new ArrayList<CorpEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						CorpEntity entity = JSONObject.toJavaObject(array.getJSONObject(i), CorpEntity.class);
						list.add(entity);
					}
				}
				msg.obj = list;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 道路救援信息查询
	 *
	 * @param lng
	 * @param lat
	 * @param currentPage
	 * @param handler
	 */
	public void queryAssistPageList(Double lng,Double lat, Integer currentPage, final Handler handler) {
		Log.d("METHOD_ASSIST_PAGELIST",METHOD_ASSIST_PAGELIST);
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_ASSIST_PAGELIST);
		params.addParams("lng", lng);
		params.addParams("lat", lat);
		params.addParams("currentPage", currentPage);
		final Message msg = new Message();
		msg.what = WHAT_ASSIST_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				List<CorpEntity> list = new ArrayList<CorpEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						CorpEntity entity = JSONObject.toJavaObject(
								array.getJSONObject(i), CorpEntity.class);
						list.add(entity);
					}
				}
				msg.obj = list;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 便捷洗车信息查询
	 *
	 * @param currentPage
	 * @param handler
	 */
	public void queryWashcarPageList(Integer currentPage, final Handler handler) {
		Log.d("METHOD_WASHCAR_PAGELIST","METHOD_WASHCAR_PAGELIST");
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_WASHCAR_PAGELIST);
		params.addParams("currentPage", currentPage);
		final Message msg = new Message();
		msg.what = WHAT_WASHCAR_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				List<CorpEntity> list = new ArrayList<CorpEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						CorpEntity entity = JSONObject.toJavaObject(
								array.getJSONObject(i), CorpEntity.class);
						list.add(entity);
					}
				}
				msg.obj = list;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}


	/**
	 * 供求信息发布
	 *
	 * @param userId
	 *            用户ID
	 * @param type
	 * 			  供求类型
	 * @param imageInfos
	 *            所有图片
	 * @param content
	 *            说明内容

	 */
	public void demandAdd(final Integer userId, final Integer type,final String content, List<String> imageInfos,
							 final Handler handler) {
		if (null==imageInfos || imageInfos.size()<=0){
			demandTextPost(userId,type,content,"",handler);
		}else{
			upLoadImage(UPLOAD_IMAGE_ACTION_LOST, imageInfos,
					new OnUpLoadResultListener() {
						@Override
						public void upLoadImageResultSuccess(String imageUrl) {
							Log.d("imageUrl",imageUrl);
							demandTextPost(userId,type,content,imageUrl,handler);
						}

						@Override
						public void upLoadImageResultFailure(String msg) {
							SystemProgressDialog.newInstance(context).dismiss();
						}
					});
		}
	}


	private void demandTextPost(final Integer userId, int type,final String content,String imgurls,final Handler handler){
		Log.d("demandTextPost","userid:"+userId);
		Log.d("demandTextPost","type:"+type);
		Log.d("demandTextPost","content:"+content);
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_DEMAND_ADD);
		params.addParams("userid", userId);
		params.addParams("type", type);
		params.addParams("content", content);
		params.addParams("imgurls", imgurls);
		final Message msg = new Message();
		msg.what = WHAT_DEMAND_ADD;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				Log.d("WHAT_DEMAND_ADD", result.toString());
				handler.sendEmptyMessage(WHAT_DEMAND_ADD);
			}

			@Override
			public void onFailure(ResultBean result) {
				Log.d("WHAT_DEMAND_ADD", result.toString());
			}
		});
	}
	/**
	 * 供求信息查询
	 *
	 * @param currentPage
	 * @param handler
	 */
	public void queryDemandPageList(Integer currentPage, final Handler handler) {
		Log.d("METHOD_SUPPLY_PAGELIST",METHOD_DEMAND_PAGELIST);
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_DEMAND_PAGELIST);
		params.addParams("currentPage", currentPage);
		final Message msg = new Message();
		msg.what = WHAT_DEMAND_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				List<DemandEntity> list = new ArrayList<DemandEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						DemandEntity entity = JSONObject.toJavaObject(
								array.getJSONObject(i), DemandEntity.class);
						list.add(entity);
					}
				}
				msg.obj = list;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 查询评论消息列表（分页查询）
	 *
	 * @param infoid
	 * @param currentPage
	 * @param handler
	 */
	public void demandCommentPageList(Integer infoid, Integer currentPage,
									 final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addParams("method", METHOD_DEMAND_COMMENT_PAGELIST);
		params.addParams("infoid", infoid);
		//params.addParams("currPage", currentPage);

		final Message msg = new Message();
		msg.what = WHAT_DEMAND_COMMENT_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				List<TweetsEntity> commList = new ArrayList<TweetsEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						commList.add(JSONObject.toJavaObject(
								array.getJSONObject(i), TweetsEntity.class));
					}
				}
				msg.obj = commList;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 添加评论
	 *
	 * @param userId
	 * @param infoid
	 * @param content
	 * @param handler
	 */
	public void demandCommentAdd(Integer userId, Integer infoid, String content,
							final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_DEMAND_COMMENT_ADD);
		params.addParams("userid", userId);
		params.addParams("infoid", infoid);
		params.addParams("content", content);

		final Message msg = new Message();
		msg.what = WHAT_DEMAND_COMMENT_ADD;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				TweetsEntity comm = JSONObject.toJavaObject((JSONObject) result.getData(), TweetsEntity.class);
				msg.obj = comm;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 失物招领查询
	 *
	 * @param currentPage
	 * @param handler
	 */
	public void queryLostPageList(Integer currentPage, final Handler handler) {
		Log.d("METHOD_LOST_PAGELIST",METHOD_LOSTFOUND_PAGELIST);
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_LOSTFOUND_PAGELIST);
		params.addParams("currPage", currentPage);
		final Message msg = new Message();
		msg.what = WHAT_LOSTFOUND_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				List<LostFoundEntity> list = new ArrayList<LostFoundEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						LostFoundEntity entity = JSONObject.toJavaObject(
								array.getJSONObject(i), LostFoundEntity.class);
						list.add(entity);
					}
				}
				msg.obj = list;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}


	/**
	 * 查询评论消息列表（分页查询）
	 * 
	 * @param infoid
	 * @param currentPage
	 * @param handler
	 */
	public void lostFoundCommentPageList(Integer infoid, Integer currentPage,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addParams("method", METHOD_LOSTFOUND_COMMON_PAGELIST);
		params.addParams("infoid", infoid);
		params.addParams("currentPage", currentPage);

		final Message msg = new Message();
		msg.what = WHAT_LOSTFOUND_COMMENT_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject resultData = (JSONObject) result.getData();
				List<TweetsEntity> commList = new ArrayList<TweetsEntity>();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						commList.add(JSONObject.toJavaObject(
								array.getJSONObject(i), TweetsEntity.class));
					}
				}
				msg.obj = commList;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 添加评论
	 *
	 * @param userId
	 * @param infoid
	 * @param content
	 * @param handler
	 */
	public void lostCommentAdd(Integer userId, Integer infoid, String content,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_LOSTFOUND_COMMON_ADD);
		params.addParams("userid", userId);
		params.addParams("infoid", infoid);
		params.addParams("content", content);

		final Message msg = new Message();
		msg.what = WHAT_LOSTFOUND_COMMENT_ADD;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				TweetsEntity comm = JSONObject.toJavaObject((JSONObject) result.getData(), TweetsEntity.class);
				msg.obj = comm;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 删除回复或评论
	 *
	 * @param userId
	 * @param commentId
	 * @param handler
	 */
	public void lostCommentDel(Integer userId, Integer commentId,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_LOSTFOUND_COMMON_DEL);
		params.addParams("userId", userId);
		params.addParams("commentId", commentId);
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_LOSTFOUND_COMMENT_DEL);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}


	/**
	 * 我的信息查询
	 *
	 * @param currentPage
	 * @param handler
	 */
	public void queryMyInfoPageList(Integer userId,Integer currentPage, final Handler handler) {
		Log.d("METHOD_MYINFO_PAGELIST",METHOD_MYINFO_PAGELIST);
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_MYINFO_PAGELIST);
		params.addParams("userid", userId);
		params.addParams("currPage", currentPage);
		final Message msg = new Message();
		msg.what = WHAT_MYINFO_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject resultData = (JSONObject) result.getData();
				Map<String,Object> map = new HashMap<String, Object>();
				List<InfoEntity> list = new ArrayList<InfoEntity>();
				if (null != resultData) {
					JSONArray array = resultData.getJSONArray("list");
					Integer total_quantity = resultData.getInteger("total_quantity");
					for (int i = 0; i < array.size(); i++) {
						InfoEntity entity = JSONObject.toJavaObject(
								array.getJSONObject(i), InfoEntity.class);
						list.add(entity);
					}
					map.put("list",list);
					map.put("total_quantity",total_quantity);
				}
				msg.obj = map;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 *
	 * 信息删除
	 * @param infoid
	 * @param handler
	 */
	public void infoDel(Integer infoid,Integer channel,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_MYINFO_DEL);
		params.addParams("id", infoid);
		params.addParams("channel", channel);
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_MYINFO_DEL);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}
}
