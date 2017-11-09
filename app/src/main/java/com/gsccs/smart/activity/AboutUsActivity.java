package com.gsccs.smart.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.network.BaseConst;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 关于我们
 */
public class AboutUsActivity extends Activity implements View.OnClickListener{


	@Bind(R.id.back_ico)
	ImageView mHeadImageView;
	@Bind(R.id.back_head)
	TextView mHeadTextView;

	@Bind(R.id.webView)
	WebView mWebView;

	private Integer id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus_activity);
		ButterKnife.bind(this);
		mHeadTextView.setText(R.string.title_aboutus);
		mHeadImageView.setOnClickListener(this);

		WebSettings setting = mWebView.getSettings();
		//  自适应屏幕
		setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		setting.setLoadWithOverviewMode(true);
		//  支持javascript
		setting.setJavaScriptEnabled(true);
		setting.setTextSize(WebSettings.TextSize.NORMAL);
		id = getIntent().getIntExtra("id",0);
		loadArticleDetail(id);
	}


	private void loadArticleDetail(Integer id) {

		mWebView.loadUrl(BaseConst.APP_ABOUTUS_URL);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.back_ico:
				finish();
				break;
		}
	}
}
