package com.gsccs.smart.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsccs.smart.R;
import com.gsccs.smart.activity.AssistActivity;
import com.gsccs.smart.activity.BusMainActivity;
import com.gsccs.smart.activity.DemandActivity;
import com.gsccs.smart.activity.DomesticActivity;
import com.gsccs.smart.activity.LostFoundActivity;
import com.gsccs.smart.activity.MainActivity;
import com.gsccs.smart.activity.ParkMapActivity;
import com.gsccs.smart.activity.TrafficMapActivity;
import com.gsccs.smart.activity.TweetActivity;
import com.gsccs.smart.activity.WashcarActivity;
import com.gsccs.smart.activity.YellowPageActivity;
import com.gsccs.smart.network.BaseConst;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  服务中心
 *  Created by x.d zhang on 2016/11/04.
 */
public class ServerFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.class_bus)
    ImageView mBusView;
    @Bind(R.id.class_traffic)
    ImageView mTrafficView;
    @Bind(R.id.class_park)
    ImageView mParkView;
    @Bind(R.id.class_tweet)
    ImageView mTweetView;


    @Bind(R.id.class_querybug)
    ImageView mQueryBusView;
    @Bind(R.id.class_payfee)
    ImageView mPayFeeView;
    @Bind(R.id.class_ycheck)
    ImageView mYcheckView;
    @Bind(R.id.class_querycar)
    ImageView mQueryCarView;
    @Bind(R.id.class_jzquery)
    ImageView mJzQueryView;

    @Bind(R.id.class_assist)
    ImageView mAssistView;
    @Bind(R.id.class_domestic)
    ImageView mDomesticView;
    @Bind(R.id.class_demand)
    ImageView mGongqiuView;
    @Bind(R.id.class_washcar)
    ImageView mWashcarView;
    @Bind(R.id.class_lostfound)
    ImageView mLostFoundView;
    @Bind(R.id.class_yellowpage)
    ImageView mYellowPageView;


    private Toast mToast;
    private MainActivity mainActivity;

    @Override
    @SuppressLint("ShowToast")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.server_fragment, null);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();
        mainActivity.switchIco(2);

        mHeadTextView.setText(R.string.server);
        mHeadImageView.setOnClickListener(this);
        mHeadImageView.setVisibility(View.GONE);

        mBusView.setOnClickListener(this);
        mTrafficView.setOnClickListener(this);
        mParkView.setOnClickListener(this);
        mTweetView.setOnClickListener(this);

        mQueryBusView.setOnClickListener(this);
        mPayFeeView.setOnClickListener(this);
        mYcheckView.setOnClickListener(this);
        mQueryCarView.setOnClickListener(this);
        mJzQueryView.setOnClickListener(this);

        mAssistView.setOnClickListener(this);
        mDomesticView.setOnClickListener(this);
        mGongqiuView.setOnClickListener(this);
        mWashcarView.setOnClickListener(this);
        mLostFoundView.setOnClickListener(this);
        mYellowPageView.setOnClickListener(this);
        mToast = Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.class_bus:
                Intent busIntent = new Intent(getActivity(), BusMainActivity.class);
                busIntent.putExtra(BaseConst.TOOLBAR_TITLE,"智慧公交");
                startActivity(busIntent);
            break;
            case R.id.class_traffic:
                Intent trafficIntent = new Intent(getActivity(), TrafficMapActivity.class);
                trafficIntent.putExtra(BaseConst.TOOLBAR_TITLE,"实时路况");
                startActivity(trafficIntent);
            break;
            case R.id.class_park:
                Intent parkIntent = new Intent(getActivity(), ParkMapActivity.class);
                parkIntent.putExtra(BaseConst.TOOLBAR_TITLE,"智慧停车");
                startActivity(parkIntent);
                break;
            case R.id.class_tweet:
                Intent tweetIntent = new Intent(getActivity(), TweetActivity.class);
                tweetIntent.putExtra(BaseConst.TOOLBAR_TITLE,"不文明随手拍");
                startActivity(tweetIntent);
                break;
            case R.id.class_querybug:
                openCLD(getResources().getString(R.string.apk12123_pkg),getActivity());
                //showTip("对接中");
                break;
            case R.id.class_payfee:
                openCLD(getResources().getString(R.string.apk12123_pkg),getActivity());
                break;
            case R.id.class_ycheck:
                openCLD(getResources().getString(R.string.apk12123_pkg),getActivity());
                break;
            case R.id.class_querycar:
                openCLD(getResources().getString(R.string.apk12123_pkg),getActivity());
                break;
            case R.id.class_jzquery:
                openCLD(getResources().getString(R.string.apk12123_pkg),getActivity());
                break;
            case R.id.class_assist:
                Intent assistIntent = new Intent(getActivity(), AssistActivity.class);
                assistIntent.putExtra(BaseConst.TOOLBAR_TITLE,"道路救援");
                startActivity(assistIntent);
            break;
            case R.id.class_domestic:
                Intent domesticIntent = new Intent(getActivity(), DomesticActivity.class);
                domesticIntent.putExtra(BaseConst.TOOLBAR_TITLE,"家政服务");
                startActivity(domesticIntent);
            break;
            case R.id.class_demand:
                Intent gongqiuIntent = new Intent(getActivity(), DemandActivity.class);
                gongqiuIntent.putExtra(BaseConst.TOOLBAR_TITLE,"供求信息");
                startActivity(gongqiuIntent);
            break;
            case R.id.class_washcar:
                Intent washcarIntent = new Intent(getActivity(), WashcarActivity.class);
                washcarIntent.putExtra(BaseConst.TOOLBAR_TITLE,"便捷洗车");
                startActivity(washcarIntent);
                break;
            case R.id.class_lostfound:
                Intent lostFoundIntent = new Intent(getActivity(), LostFoundActivity.class);
                lostFoundIntent.putExtra(BaseConst.TOOLBAR_TITLE,"失物招领");
                startActivity(lostFoundIntent);
                break;
            case R.id.class_yellowpage:
                Intent yellowPageIntent = new Intent(getActivity(), YellowPageActivity.class);
                yellowPageIntent.putExtra(BaseConst.TOOLBAR_TITLE,"黄页电话");
                startActivity(yellowPageIntent);
                break;

        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    public void openCLD(String packageName,Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = packageManager.getPackageInfo(getResources().getString(R.string.apk12123_pkg), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "请下载安装12123应用！", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);
        ResolveInfo ri = apps.iterator().next();
        if (ri != null ) {
            String className = ri.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }
}
