package com.gsccs.smart.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


/**
 *  文章类HTTP接口
 */
public class ArticleHttps extends BaseHttps {

	private static ArticleHttps instance = null;

	public static ArticleHttps getInstance(Context context) {
		if (instance == null) {
			instance = new ArticleHttps();
		}
		instance.context = context;
		return instance;
	}

	private ArticleHttps() {
		
	}

	/**
	 * 查询首页接口
	 *
	 * @param handler
	 */
	public void queryAppIndexInfo(final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		//params.addBodyParameter("cityName", cityName);
		params.addBodyParameter("method", METHOD_APP_INDEX);
		final Message msg = new Message();
		msg.what = WHAT_APP_INDEX;

		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject jsonObj = (JSONObject) result.getData();
				JSONObject trafficData = jsonObj.getJSONObject("trafficData");
				JSONArray articleArray = jsonObj.getJSONArray("articleList");
				JSONArray noticeArray = jsonObj.getJSONArray("noticeList");
				List<ArticleEntity> articleList = new ArrayList<ArticleEntity>();
				List<ArticleEntity> noticeList = new ArrayList<ArticleEntity>();
				if (articleArray != null && articleArray.size() > 0) {
					for (int i = 0; i < articleArray.size(); i++) {
						articleList.add(JSONObject.toJavaObject(
								articleArray.getJSONObject(i), ArticleEntity.class));
					}
				}
				if (noticeArray != null && noticeArray.size() > 0) {
					for (int i = 0; i < noticeArray.size(); i++) {
						noticeList.add(JSONObject.toJavaObject(
								noticeArray.getJSONObject(i), ArticleEntity.class));
					}
				}
				TrafficDataEntity tfData = JSONObject.toJavaObject(trafficData,
						TrafficDataEntity.class);
				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("articleList", articleList);
				resultMap.put("noticeList", noticeList);
				resultMap.put("trafficData",tfData);
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
	public void queryArticlePageList(Integer currPage,Integer pageSize, final Handler handler) {
		Log.d("ArticleEntity",METHOD_ARTICLE_PAGELIST);
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_ARTICLE_PAGELIST);
		params.addParams("currPage", currPage);
		params.addParams("pageSize",pageSize);
		final Message msg = new Message();
		msg.what = WHAT_ARTICLE_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				Log.d("ArticleEntity",result.getCode()+"");

				List<ArticleEntity> infoList = new ArrayList<ArticleEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					Log.d("ArticleEntity",array.size()+"");
					for (int i = 0; i < array.size(); i++) {
						infoList.add(JSONObject.toJavaObject(array.getJSONObject(i), ArticleEntity.class));
					}
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
	 * 查询通知公告
	 *
	 * @param currentPage
	 * @param handler
	 */
	public void queryNoticePageList(int currentPage, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_NOTICE_PAGELIST);
		params.addParams("currentPage", currentPage);
		final Message msg = new Message();
		msg.what = WHAT_NOTICE_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				List<ArticleEntity> infoList = new ArrayList<ArticleEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						ArticleEntity article = JSONObject.toJavaObject(array.getJSONObject(i), ArticleEntity.class);
						infoList.add(article);
					}
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



	/**
	 * 文章添加评论或回复
	 * 
	 * @param userId
	 * @param articleId
	 * @param content
	 * @param handler
	 */
	public void articleCommentAdd(Integer userId, Integer articleId, String content,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_ARTICLE_COMMENT_ADD);
		params.addParams("userid", userId);
		params.addParams("articleid", articleId);
		params.addParams("content", content);
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_ARTICLE_COMMENT_ADD);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}

	/**
	 * 查询更多评论内容
	 * 
	 * @param articleid
	 * @param currentPage
	 * @param handler
	 */
	public void articleCommentPageList(Integer articleid,
			Integer currentPage, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_ARTICLE_COMMENT_PAGELIST);
		params.addParams("articleid", articleid);
		params.addParams("currentPage", currentPage);
		Log.i("articleCommentPageList", "currentPage=" + currentPage);
		final Message msg = new Message();
		msg.what = WHAT_ARTICLE_COMMENT_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject resultData = (JSONObject) result.getData();
				List<ArticleFAQ> faqList = new ArrayList<ArticleFAQ>();
				if (null != resultData) {
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						faqList.add(JSONObject.toJavaObject(array.getJSONObject(i),
								ArticleFAQ.class));
					}
				}
				msg.obj = faqList;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {
			}
		});
	}

	/**
	 * 删除用户评论
	 * 
	 * @param userId
	 * @param faqId
	 * @param handler
	 */
	public void activityCommentDel(Integer userId, Integer faqId,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_ARTICLE_COMMENT_DEL);
		params.addParams("userId", userId);
		params.addParams("faqId", faqId);
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_ARTICLE_COMMENT_DEL);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}


	/**
	 * 分页查询文章收藏接口
	 */
	public void articleCollectPageList(int userid,Integer currPage, final Handler handler) {
		Log.d("ArticleEntity",METHOD_ARTICLE_COLLECT_PAGELIST);
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_ARTICLE_COLLECT_PAGELIST);
		params.addParams("userid", userid);
		params.addParams("currPage", currPage);

		final Message msg = new Message();
		msg.what = WHAT_ARTICLE_COLLECT_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				Log.d("ArticleEntity",result.getCode()+"");

				List<ArticleEntity> infoList = new ArrayList<ArticleEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					Log.d("ArticleEntity",array.size()+"");
					for (int i = 0; i < array.size(); i++) {
						infoList.add(JSONObject.toJavaObject(array.getJSONObject(i), ArticleEntity.class));
					}
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
	 * 文章添加评论或回复
	 *
	 * @param userId
	 * @param articleId
	 * @param handler
	 */
	public void articleCollect(Integer userId, Integer articleId,
								  final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_ARTICLE_COLLECT);
		params.addParams("userid", userId);
		params.addParams("articleid", articleId);

		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_ARTICLE_COLLECT);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}
}
