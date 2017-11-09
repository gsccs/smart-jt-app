package com.gsccs.smart.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
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
import com.gsccs.smart.photoCrop.AbstractPhotoActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  我的
 *  Created by x.d zhang on 2016/10/29.
 */
public class MineActivity extends AbstractPhotoActivity implements View.OnClickListener {


    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.userImagePic)
    CircleImageView mUserHeadView;
    @Bind(R.id.txt_username)
    TextView mUserNameView;
    @Bind(R.id.txt_user_id)
    TextView mUserIDView;
    @Bind(R.id.txt_user_title)
    EditText mUserTitleView;

    @Bind(R.id.txt_user_account)
    TextView mUserAccountView;

    @Bind(R.id.txt_changpwd)
    TextView mChangepwdView;
    @Bind(R.id.txt_signout)
    Button mSignoutButton;
    @Bind(R.id.txt_save)
    Button mSaveButton;

    private UserEntity user;
    String avatarpath="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity);
        ButterKnife.bind(this);

        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        mHeadTextView.setText(titleName);
        mHeadImageView.setOnClickListener(this);
        mUserHeadView.setOnClickListener(this);
        mChangepwdView.setOnClickListener(this);
        mSignoutButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        initData();
    }

    @Override
    public void onPhotoSelected(String imgPath) {
        Log.d("imgPath",imgPath);
        avatarpath = imgPath;
        //mUserHeadView.setImageURI(Uri.parse(imgPath));
        Picasso.with(SmartApplication.getAppContext())
                .load(new File(imgPath))
                .resize(70, 70)
                .centerCrop()
                .into(mUserHeadView);
    }

    //初始化数据
    public void initData(){
        user = SmartApplication.getCurrUser();
        Picasso.with(SmartApplication.getAppContext())
                .load(user.getLogo())
                .resize(70, 70)
                .centerCrop()
                .into(mUserHeadView);
        mUserNameView.setText(user.getTitle());
        mUserIDView.setText("ID:"+user.getId());
        mUserTitleView.setText(user.getTitle());
        mUserAccountView.setText(user.getAccount());
    }

    @Override
    public void onClick(View v) {
        Log.d("mineFragment","click");
        switch (v.getId()) {
            case R.id.back_ico:
                finish();
                break;
            case R.id.userImagePic:
                Log.d("mineFragment","userImagePic");
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmmss");
                String newPhotoName = "avatvr" + sdf3.format(new Date());
                // 选择保存在这个文件夹，可以随软件 的卸载，程序自动删除这些临时缓存的图片
                String resultPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + newPhotoName + ".jpg";
                popup(this, resultPath, true);
                break;
            case R.id.txt_changpwd:
                //SmartApplication.setCurrUser(null);
                Intent pwdintent = new Intent(this, MinePwdActivity.class);
                startActivity(pwdintent);
                break;
            case R.id.txt_signout:
                //SmartApplication.setCurrUser(null);
                Intent intent = new Intent(this, SignInUpActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_save:
                saveUserInfo();
                break;
        }
    }

    private void saveUserInfo(){
        String usertitle = mUserTitleView.getText().toString();
        if (null!=usertitle && !usertitle.trim().equals("")){
           user.setTitle(usertitle);
        }else{
            Toast.makeText(MineActivity.this, getString(R.string.error_invalid_nickname)
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        UserHttps.getInstance(this).saveUserInfo(avatarpath,user,refreshHandler);
    }
    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_IMPROVE_USERINFO:

                    Toast.makeText(MineActivity.this, getString(R.string.tweet_send_success)
                            , Toast.LENGTH_SHORT).show();
                    MineActivity.this.finish();
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };
}
