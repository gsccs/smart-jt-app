package com.gsccs.smart.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.model.ArticleEntity;
import com.gsccs.smart.model.ArticleFAQ;
import com.gsccs.smart.network.ArticleHttps;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.utils.IconUtil;
import com.gsccs.smart.widget.SystemToastDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 新闻详情页
 */
public class ArticleActivity extends Activity implements View.OnClickListener{


	@Bind(R.id.back_ico)
	ImageView mHeadImageView;
	@Bind(R.id.back_head)
	TextView mHeadTextView;

	@Bind(R.id.webView)
	WebView articleWebView;
	@Bind(R.id.ib_comment_btn)
	ImageButton mImageButton;
	@Bind(R.id.comment)
	EditText mCommentEditView;
	@Bind(R.id.back_menu)
	ImageView mCollectImageView;
	private Integer id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_web_view);
		ButterKnife.bind(this);
		mHeadTextView.setText(R.string.news_detail);
		mHeadImageView.setOnClickListener(this);

		WebSettings setting = articleWebView.getSettings();
		//  自适应屏幕
		setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		setting.setLoadWithOverviewMode(true);
		//  支持javascript
		setting.setJavaScriptEnabled(true);
		// 设置可以支持缩放
		//setting.setSupportZoom(true);
		// 设置出现缩放工具
		//setting.setBuiltInZoomControls(true);
		//  扩大比例的缩放
		//setting.setUseWideViewPort(true);
		setting.setTextSize(WebSettings.TextSize.NORMAL);
		id = getIntent().getIntExtra("id",0);
		loadArticleDetail(id);
		loadArticleComment(id);
		mImageButton.setOnClickListener(this);
		mCollectImageView.setImageBitmap(IconUtil.getBitmapFromResource(R.drawable.ic_collect_blank));
		mCollectImageView.setOnClickListener(this);
	}

	private void loadArticleComment(Integer id){
		ArticleHttps.getInstance(this).articleCommentPageList(id,1,refreshHandler);
	}
	private void loadArticleDetail(Integer id) {
		articleWebView.loadUrl(BaseConst.APP_ARTICLE_URL+"article-"+id+".html");
		articleWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		articleWebView.loadUrl("javascript:changeTextSize(200)");

	}

	private Handler refreshHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.d("WebView",""+msg.what);
			switch (msg.what) {
				case BaseConst.WHAT_ARTICLE_DETAILS:
					ArticleEntity article = (ArticleEntity) msg.obj;
					if (article == null) {
						SystemToastDialog.showShortToast(ArticleActivity.this, "没有更多符合条件的数据");
						break;
					}
					if (article.getUrl() == null) {
						articleWebView.loadDataWithBaseURL("about:blank", article.getUrl(), "textml",
								"utf-8", null);
					} else {
						articleWebView.loadUrl(article.getUrl());
					}
					break;
				case BaseConst.WHAT_ARTICLE_COMMENT_PAGELIST:
					List<ArticleFAQ> list = (List<ArticleFAQ>) msg.obj;
					break;
				case BaseConst.WHAT_ARTICLE_COMMENT_ADD:
					Toast.makeText(ArticleActivity.this, "评论内容提交成功!", Toast.LENGTH_SHORT).show();
					break;
				case BaseConst.WHAT_ARTICLE_COLLECT:
					mCollectImageView.setImageBitmap(IconUtil.getBitmapFromResource(R.drawable.ic_collect_red));
					Toast.makeText(ArticleActivity.this, "收藏成功!", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.back_ico:
				finish();
				break;
			case R.id.ib_comment_btn:
				sendComment();
				break;
			case R.id.back_menu:
				sendCollect();
				break;
		}
	}

	protected void sendCollect(){
		if (null== SmartApplication.getCurrUser()){
			Toast.makeText(ArticleActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(ArticleActivity.this, SignInUpActivity.class);
			intent.putExtra(BaseConst.IS_SIGN_IN,true);
			startActivity(intent);
		}else{
			int userid = SmartApplication.getCurrUser().getId();
			ArticleHttps.getInstance(this).articleCollect(userid, id, refreshHandler);
		}
	}

	protected void sendComment(){
		String content = mCommentEditView.getText().toString();
		if(content.equals("")) {
			Toast.makeText(ArticleActivity.this, "评论内容为空!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (null== SmartApplication.getCurrUser()){
			Toast.makeText(ArticleActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(ArticleActivity.this, SignInUpActivity.class);
			intent.putExtra(BaseConst.IS_SIGN_IN,true);
			startActivity(intent);
		}else{
			int userid = SmartApplication.getCurrUser().getId();
			ArticleHttps.getInstance(this).articleCommentAdd(userid, id, content,refreshHandler);
		}
		mCommentEditView.setText("");
	}
}
