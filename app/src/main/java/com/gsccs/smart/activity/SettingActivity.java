package com.gsccs.smart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.network.BaseConst;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  系统设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

	@Bind(R.id.back_ico)
	ImageView mHeadImageView;
	@Bind(R.id.back_head)
	TextView mHeadTextView;

	@Bind(R.id.aboutUs)
	RelativeLayout aboutUs;

	/**
	 * 退出按钮
	 */
	@Bind(R.id.btn_logout)
	Button logoutBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity);
		ButterKnife.bind(this);

		String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
		mHeadTextView.setText(titleName);
		mHeadImageView.setOnClickListener(this);

		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;

		aboutUs.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back_ico:
				finish();
				break;
			case R.id.btn_logout: // 退出登陆
				logout();
				break;

			case R.id.aboutUs:
				Intent aboutUsIntent = new Intent(this, AboutUsActivity.class);
				aboutUsIntent.putExtra("title", "关于我们");
				aboutUsIntent.putExtra("webContent", BaseConst.APP_ABOUTUS_URL);
				aboutUsIntent.putExtra("flag", 2);
				startActivity(aboutUsIntent);
				break;
			default:
				break;
		}
	}

	void logout() {
		Intent intent = new Intent(this, SignInUpActivity.class);
		intent.putExtra(BaseConst.IS_SIGN_IN,true);
		startActivity(intent);
	}
}
