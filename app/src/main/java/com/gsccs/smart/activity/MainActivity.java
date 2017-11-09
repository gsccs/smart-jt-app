package com.gsccs.smart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.weather.LocalWeatherLive;
import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.fragment.AboutFragment;
import com.gsccs.smart.fragment.UpdateFragment;
import com.gsccs.smart.model.ErrorStatus;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.fragment.IndexFragment;
import com.gsccs.smart.fragment.MineFragment;
import com.gsccs.smart.fragment.ServerFragment;
import com.gsccs.smart.network.ArticleHttps;
import com.gsccs.smart.network.WxpayHttps;
import com.gsccs.smart.service.LocationService;
import com.gsccs.smart.utils.ColorUtil;
import com.gsccs.smart.utils.IconUtil;
import com.gsccs.smart.utils.Initialize;
import com.gsccs.smart.utils.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 **/
public class MainActivity extends BaseActivity implements View.OnClickListener{

    //底部菜单 首页
    @Bind(R.id.index)
    LinearLayout mIndex;
    @Bind(R.id.index_image)
    ImageView mIndexImage;
    @Bind(R.id.index_text)
    TextView mIndexText;

    //底部菜单 服务
    @Bind(R.id.server)
    LinearLayout mServer;
    @Bind(R.id.server_image)
    ImageView mServerImage;
    @Bind(R.id.server_text)
    TextView mServerText;

    //底部菜单 个人
    @Bind(R.id.mine)
    LinearLayout mMine;
    @Bind(R.id.mine_image)
    ImageView mMineImage;
    @Bind(R.id.mine_text)
    TextView mMineText;

    private static int FRAGMENT_MAIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Initialize.init(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBottomView();
        StatusBarUtil.setStatusBarTrans(this, false);
        if(SmartApplication.IS_FIRST){
            UpdateFragment.checkForDialog(MainActivity.this);
        }
        SmartApplication.IS_FIRST = false;
    }

    public void initBottomView() {
        //下方组件初始化
        mIndex.setOnClickListener(this);
        mServer.setOnClickListener(this);
        mMine.setOnClickListener(this);


    }

    //WX Test
    private void wxAppTest(){
        WxpayHttps.getInstance(this).sendWxPay(0, refreshHandler);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.index:        //首页
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new IndexFragment()).commit();
                //setTabSelection(0);
                switchIco(1);
                break;
            case R.id.server:       //服务
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new ServerFragment()).commit();
                //setTabSelection(1);
                switchIco(2);
                break;
            case R.id.mine:          //个人
                //setTabSelection(3);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new MineFragment()).commit();
                switchIco(3);
                break;
        }
    }

    //切换底层图标
    public void switchIco(int index) {

        //初始化
        mIndexImage.setImageBitmap(IconUtil.getBitmapFromResource(R.drawable.index));
        ColorUtil.setColor(this,mIndexText, "default");
        mServerImage.setImageBitmap(IconUtil.getBitmapFromResource(R.drawable.server));
        ColorUtil.setColor(this,mServerText, "default");
        mMineImage.setImageBitmap(IconUtil.getBitmapFromResource(R.drawable.mine));
        ColorUtil.setColor(this,mMineText, "default");

        switch (index) {
            case 1:
                mIndexImage.setImageBitmap(IconUtil.getBitmapFromResource(R.drawable.indexc));
                ColorUtil.setColor(this,mIndexText, "click");
                break;
            case 2:
                mServerImage.setImageBitmap(IconUtil.getBitmapFromResource(R.drawable.serverc));
                ColorUtil.setColor(this,mServerText, "click");
                break;
            case 3:
                mMineImage.setImageBitmap(IconUtil.getBitmapFromResource(R.drawable.minec));
                ColorUtil.setColor(this,mMineText, "click");
                break;

        }
    }

    public void openTweetView(View view) {
        Intent intent = new Intent(this, TweetActivity.class);
        startActivity(intent);
    }

    public void openStopView(View view) {
        Intent intent = new Intent(this, ParkMapActivity.class);
        startActivity(intent);
    }

    public void openRoadView(View view) {
        Intent intent = new Intent(this, TrafficMapActivity.class);
        startActivity(intent);
    }

    public void openBusView(View view) {
        Intent intent = new Intent(this, BusMainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // 每次重新回到界面的时候注册广播接收者
        /*IntentFilter filter = new IntentFilter();
        filter.addAction("com.locationReceiver");
        registerReceiver(locationReceiver, filter);

        IntentFilter poiFilter = new IntentFilter();
        poiFilter.addAction("com.PoiBroadcast");
        registerReceiver(poiSearchReceiver, poiFilter);*/


    }


}
