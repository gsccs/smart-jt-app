package com.gsccs.smart.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.activity.AboutUsActivity;
import com.gsccs.smart.activity.CollectActivity;
import com.gsccs.smart.activity.HistoryActivity;
import com.gsccs.smart.activity.InfoActivity;
import com.gsccs.smart.activity.MainActivity;

import com.gsccs.smart.R;
import com.gsccs.smart.activity.MineActivity;
import com.gsccs.smart.activity.RoundActivity;
import com.gsccs.smart.activity.SignInUpActivity;
import com.gsccs.smart.model.UserEntity;
import com.gsccs.smart.model.VersionInfo;
import com.gsccs.smart.network.AppHttps;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.service.DownloadService;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  我的
 *  Created by x.d zhang on 2016/10/29.
 */
public class MineFragment extends Fragment implements View.OnClickListener {


    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.txt_mecenter)
    TextView mMecenterView;
    @Bind(R.id.txt_round)
    TextView mRoundView;
    @Bind(R.id.txt_history)
    TextView mHistoryView;
    @Bind(R.id.txt_publish)
    TextView mPubishView;
    @Bind(R.id.txt_collect)
    TextView mCollectView;
    @Bind(R.id.txt_setting)
    TextView mSettingView;
    @Bind(R.id.txt_version)
    TextView mVersionView;

    private MainActivity mainActivity;

    /** 系统版本内容信息 */
    private VersionInfo appVersion = null;
    /** 当前APP的本地版本号 */
    private int versionCode = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment,null);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();
        mainActivity.switchIco(3);

        mHeadTextView.setText(R.string.mine);
        mHeadImageView.setOnClickListener(this);
        mHeadImageView.setVisibility(View.GONE);

        mMecenterView.setOnClickListener(this);
        mRoundView.setOnClickListener(this);
        mHistoryView.setOnClickListener(this);
        mHistoryView.setVisibility(View.GONE);
        mPubishView.setOnClickListener(this);
        mCollectView.setOnClickListener(this);
        mVersionView.setOnClickListener(this);
        mCollectView.setOnClickListener(this);
        mSettingView.setOnClickListener(this);


        try {
            versionCode = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 初始化获取版本号
        AppHttps.getInstance(getActivity()).appVersionGet(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj != null){
                    appVersion = (VersionInfo) msg.obj;
                    mVersionView.setText(mVersionView.getText()+"v" + appVersion.getName());
                    if(appVersion.getCode().intValue() > versionCode){
                        mVersionView.setClickable(true);
                    }else{
                        mVersionView.setClickable(false);
                    }
                }
            }
        });
        return  view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivity.switchIco(33);
    }

    @Override
    public void onClick(View v) {
        Log.d("mineFragment","click");
        UserEntity user = SmartApplication.getCurrUser();
        switch (v.getId()) {
            case R.id.txt_mecenter:
                Log.d("mineFragment","txt_mecenter");
                if (null==user){
                    Intent intent = new Intent(getActivity(), SignInUpActivity.class);
                    intent.putExtra(BaseConst.IS_SIGN_IN,true);
                    startActivity(intent);
                }else{
                    Intent mineIntent = new Intent(getActivity(), MineActivity.class);
                    mineIntent.putExtra(BaseConst.TOOLBAR_TITLE,mMecenterView.getText());
                    startActivity(mineIntent);
                }
                break;
            case R.id.txt_round:
                Log.d("mineFragment","txt_round");
                Intent roundIntent = new Intent(getActivity(), RoundActivity.class);
                roundIntent.putExtra(BaseConst.TOOLBAR_TITLE,mRoundView.getText());
                startActivity(roundIntent);
                break;
            case R.id.txt_history:
                Log.d("mineFragment","txt_history");
                if (null==user){
                    Intent intent = new Intent(getActivity(), SignInUpActivity.class);
                    intent.putExtra(BaseConst.IS_SIGN_IN,true);
                    startActivity(intent);
                }else{
                    Intent historyIntent = new Intent(getActivity(), HistoryActivity.class);
                    historyIntent.putExtra(BaseConst.TOOLBAR_TITLE,mHistoryView.getText());
                    startActivity(historyIntent);
                }
                break;
            case R.id.txt_collect:
                Log.d("mineFragment","txt_collect");
                if (null==user){
                    Intent intent = new Intent(getActivity(), SignInUpActivity.class);
                    intent.putExtra(BaseConst.IS_SIGN_IN,true);
                    startActivity(intent);
                }else{
                    Intent collectIntent = new Intent(getActivity(), CollectActivity.class);
                    collectIntent.putExtra(BaseConst.TOOLBAR_TITLE,mCollectView.getText());
                    startActivity(collectIntent);
                }
                break;
            case R.id.txt_publish:
                Log.d("mineFragment","txt_publish");
                if (null==user){
                    Intent intent = new Intent(getActivity(), SignInUpActivity.class);
                    intent.putExtra(BaseConst.IS_SIGN_IN,true);
                    startActivity(intent);
                }else{
                    Intent infoIntent = new Intent(getActivity(), InfoActivity.class);
                    infoIntent.putExtra(BaseConst.TOOLBAR_TITLE,mPubishView.getText());
                    startActivity(infoIntent);
                }
                break;
            case R.id.txt_setting:
                Log.d("mineFragment","txt_setting");
                Intent settingIntent = new Intent(getActivity(), AboutUsActivity.class);
                settingIntent.putExtra(BaseConst.TOOLBAR_TITLE,mSettingView.getText());
                startActivity(settingIntent);

            break;
            case R.id.txt_version:
                if (appVersion != null
                        && appVersion.getCode() > versionCode) {
                    Intent versionIntent = new Intent(getActivity().getApplicationContext(),DownloadService.class);
                    versionIntent.putExtra(BaseConst.APK_DOWNLOAD_URL, appVersion.getUrl());
                    getActivity().startService(versionIntent);
                }
            break;
        }
    }
}
