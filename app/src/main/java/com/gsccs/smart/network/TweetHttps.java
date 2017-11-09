package com.gsccs.smart.network;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gsccs.smart.listener.OnUpLoadResultListener;
import com.gsccs.smart.model.DictEntity;
import com.gsccs.smart.model.ResultBean;
import com.gsccs.smart.model.TweetsEntity;
import com.gsccs.smart.widget.SystemProgressDialog;

/**
 *  朋友圈服务HTTP接口
 */
public class TweetHttps extends BaseHttps {

	private static TweetHttps instance = null;

	public static TweetHttps getInstance(Context context) {
		if (instance == null) {
			instance = new TweetHttps();
		}
		instance.context = context;
		return instance;
	}

	private TweetHttps() {
	}

	/**
	 * 发布晒晒 的后台请求方法
	 * 
	 * @param userId
	 *            用户ID
	 * @param imageInfos
	 *            所有图片
	 * @param content
	 *            发布时补充的说明内容

	 */
	public void sendTweet(final Integer userId, final Integer typeid,final String content, List<String> imageInfos,
			final Handler handler) {
		//SystemProgressDialog.newInstance(context).show();
		Log.d("sendTweet","sendTweet");
		if (null==imageInfos || imageInfos.size()<=0){
			tweetTextSend(userId,typeid,content,"",handler);
		}else{
			upLoadImage(UPLOAD_IMAGE_ACTION_TWEET, imageInfos,
					new OnUpLoadResultListener() {
						@Override
						public void upLoadImageResultSuccess(String imageUrl) {
							Log.d("imageUrl",imageUrl);
							tweetTextSend(userId,typeid,content,imageUrl,handler);
						}

						@Override
						public void upLoadImageResultFailure(String msg) {
							SystemProgressDialog.newInstance(context).dismiss();
						}
			});
		}
	}

	private void tweetTextSend(final Integer userId,final Integer typeid, final String content,String imgurls,final Handler handler){
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_TWEET_ADD);
		params.addParams("userid", userId);
		params.addParams("imgurls", imgurls);
		params.addParams("content", content);
		params.addParams("typeid", typeid);

		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				//SystemProgressDialog.newInstance(context).dismiss();
				handler.sendEmptyMessage(WHAT_TWEET_SEND);
			}

			@Override
			public void onFailure(ResultBean result) {
				//SystemProgressDialog.newInstance(context).dismiss();
			}
		});
	}




	/**
	 * 信息查询
	 *
	 * @param currentPage
	 * @param handler
	 */
	public void queryTweetPageList(Integer currentPage, final Handler handler) {
		Log.d("queryTweetPageList","queryTweetPageList");
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_TWEET_PAGELIST);
		//params.addParams("userId", userId);
		params.addParams("currPage", currentPage);
		final Message msg = new Message();
		msg.what = WHAT_TWEET_PAGELIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				List<TweetsEntity> tweetList = new ArrayList<TweetsEntity>();
				JSONObject resultData = (JSONObject) result.getData();
				if (null != resultData){
					JSONArray array = resultData.getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						TweetsEntity tweetsEntity = JSONObject.toJavaObject(array.getJSONObject(i), TweetsEntity.class);
						tweetList.add(tweetsEntity);
					}
				}
				msg.obj = tweetList;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(ResultBean result) {
				
			}
		});
	}

	/**
	 * 详情
	 * 
	 * @param tweetId
	 * @param userId
	 * @param handler
	 */
	public void tweetDetail(Integer tweetId, Integer userId,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addParams("method", METHOD_TWEET_DETAILS);
		params.addParams("newsId", tweetId);
		params.addParams("userId", userId);

		final Message msg = new Message();
		msg.what = WHAT_TWEET_DETAILS;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				JSONObject jsonObj = (JSONObject) result.getData();
				TweetsEntity details = JSONObject.toJavaObject(jsonObj,
						TweetsEntity.class);
				msg.obj = details;
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
	 * @param tweetId
	 * @param currentPage
	 * @param handler
	 */
	public void tweetCommentPageList(Integer tweetId, Integer currentPage,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addParams("method", METHOD_TWEET_COMMENT_PAGELIST);
		params.addParams("tweetId", tweetId);
		params.addParams("currentPage", currentPage);

		final Message msg = new Message();
		msg.what = WHAT_TWEET_COMMENT_PAGELIST;
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
	 * 查询类型列表
	 *
	 * @param handler
	 */
	public void tweetTypeList(final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addParams("method", METHOD_TWEET_TYPE_LIST);
		final Message msg = new Message();
		msg.what = WHAT_TWEET_TYPE_LIST;
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				List<DictEntity> dictList = new ArrayList<DictEntity>();
				JSONArray array = (JSONArray) result.getData();
				for (int i = 0; i < array.size(); i++) {
					dictList.add(JSONObject.toJavaObject(array.getJSONObject(i), DictEntity.class));
				}
				msg.obj = dictList;
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
	 * @param tweetId
	 * @param content
	 * @param handler
	 */
	public void sendComment(Integer userId, Integer tweetId, String content,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_TWEET_COMMENT_ADD);
		params.addParams("userid", userId);
		params.addParams("tweetId", tweetId);
		params.addParams("content", content);

		final Message msg = new Message();
		msg.what = WHAT_TWEET_COMMENT_ADD;
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
	public void tweetCommentDel(Integer userId, Integer commentId,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_TWEET_COMMENT_DEL);
		params.addParams("userId", userId);
		params.addParams("commentId", commentId);
		sendHttpPost(params, new OnHttpResultListener() {
			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_TWEET_COMMENT_DEL);
			}

			@Override
			public void onFailure(ResultBean result) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 点赞（匿名）
	 *
	 * @param newsId
	 * @param handler
	 */
	public void tweetPraiseAdd(Integer newsId, final Handler handler) {
		BaseRequestParams params = new BaseRequestParams();
		params.addParams("method", METHOD_TWEET_PRAISE_ADD);
		// params.addParams("userId", userId);
		params.addParams("newsId", newsId);

		sendHttpPost(params, new OnHttpResultListener() {

			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_TWEET_PRAISE_ADD);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}


	/**
	 * 删除晒晒
	 *
	 * @param userId
	 * @param tweetId
	 * @param handler
	 */
	public void tweetDel(Integer userId, Integer tweetId,
			final Handler handler) {
		BaseRequestParams params = new BaseRequestParams(context);
		params.addParams("method", METHOD_TWEET_DEL);
		params.addParams("userId", userId);
		params.addParams("newsId", tweetId);
		sendHttpPost(params, new OnHttpResultListener() {

			@Override
			public void onSuccess(ResultBean result) {
				handler.sendEmptyMessage(WHAT_TWEET_DEL);
			}

			@Override
			public void onFailure(ResultBean result) {

			}
		});
	}
}
