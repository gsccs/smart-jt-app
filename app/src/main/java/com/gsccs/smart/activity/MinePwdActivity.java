package com.gsccs.smart.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.model.UserEntity;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.UserHttps;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  修改密码
 *  Created by x.d zhang on 2016/10/29.
 */
public class MinePwdActivity extends Activity implements View.OnClickListener {


    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.txt_old_pwd)
    EditText mOldPwdView;
    @Bind(R.id.txt_new_pwd)
    EditText mNewPwdView;
    @Bind(R.id.save)
    Button mSaveButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_pwd_activity);
        ButterKnife.bind(this);

        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        mHeadTextView.setText(titleName);
        mHeadImageView.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_ico:
                finish();
                break;
            case R.id.save:
                savePwd();
                break;
        }
    }


    private void savePwd(){
        String oldpwd = mOldPwdView.getText().toString();
        String newpwd = mNewPwdView.getText().toString();
        if (null==oldpwd || oldpwd.trim().equals("")){
            Toast.makeText(MinePwdActivity.this, getString(R.string.error_invalid_password)
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        if (null==newpwd || newpwd.trim().equals("")){
            Toast.makeText(MinePwdActivity.this, getString(R.string.error_invalid_password)
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        UserEntity user = SmartApplication.getCurrUser();
        UserHttps.getInstance(this).updateLoginPwd(user.getId(),oldpwd,newpwd,refreshHandler);
    }

    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_RESET_LOGINPWD:

                    Toast.makeText(MinePwdActivity.this, getString(R.string.tweet_send_success)
                            , Toast.LENGTH_SHORT).show();
                    MinePwdActivity.this.finish();
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };
}
