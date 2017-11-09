package com.gsccs.smart.activity;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gsccs.smart.R;
import com.gsccs.smart.listener.GetLocalMessageListener;
import com.gsccs.smart.model.ErrorStatus;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.fragment.BusFragment;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.service.BaseLocationService;
import com.gsccs.smart.service.LocationService;
import com.gsccs.smart.utils.Initialize;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 智慧公交
 */
public class BusMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    private ArrayList<Map<String, BusLineItem>> busLineMessage;

    private FragmentManager mFragmentManager;
    private static int FRAGMENT_MAIN;

    private BusFragment busFragment;
    private BusLineItem busLineItem;

    private Intent startLocationServiceIntent;
    //private int currentPage = 0;


    @Bind(R.id.up_image)
    ImageView upImage;
    @Bind(R.id.below_image)
    ImageView belowImage;
    @Bind(R.id.up_text)
    TextView upText;
    @Bind(R.id.below_text)
    TextView belowText;

    @Bind(R.id.exchange_button)
    Button exchangeButton;
    @Bind(R.id.up_button)
    Button upButton;
    @Bind(R.id.below_button)
    Button belowButton;
    @Bind(R.id.search)
    Button searchBtn;
    //@Bind(R.id.protect_bridge)
    //RelativeLayout protectBridge;

    private Intent inputIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_main_activity);
        ButterKnife.bind(this);
        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        if (null==titleName || titleName.equals("")){
            mHeadTextView.setText("智慧公交");
        }else{
            mHeadTextView.setText(titleName);
        }
        mHeadImageView.setOnClickListener(this);

        inputIntent = new Intent(this,BusRouteInputActivity.class);

        //this.setGetLocalMessageListener(this);
        //backButton.setOnClickListener(this);
        exchangeButton.setOnClickListener(this);
        upButton.setOnClickListener(this);
        belowButton.setOnClickListener(this);

        initView();
    }

    public void initView() {
        //fragment初始化
        FRAGMENT_MAIN = R.id.main_fragment;
        mFragmentManager = getFragmentManager();

        //mIndex.performClick();

        // 开启一个Fragment事务
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //mFragmentManager.beginTransaction();

        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        //
        if (busFragment == null) {
            //Log.i("AOISora","mMineFragment Create");
            // 如果mMineFragment为空，则创建一个并添加到界面上
            busFragment = new BusFragment();
            fragmentTransaction.add(FRAGMENT_MAIN, busFragment);
        } else {
            //  Log.i("AOISora","mMineFragment Show");
            // 如果NewsFragment不为空，则直接将它显示出来
            fragmentTransaction.show(busFragment);
        }
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_ico:
                finish();
                break;
            case R.id.exchange_button:
                switchFromAndTo();
                break;
            case R.id.up_button:
                inputIntent.putExtra(Initialize.PLACE,Initialize.UP);
                startActivityForResult(inputIntent, Initialize.REQUEST_CODE_UP);
                break;
            case R.id.below_button:
                inputIntent.putExtra(Initialize.PLACE, Initialize.BELOW);
                startActivityForResult(inputIntent, Initialize.REQUEST_CODE_BELOW);
                break;
            case R.id.search:
                handleMessageTo();
                break;
        }
    }

    private boolean myLocationIsUp = true;
    public void switchFromAndTo(){
        String upString = null;
        String belowString = null;
        if((upText.getText().toString()).equals("输入起点")){
            upString = "输入终点";
            belowString = String.valueOf(belowText.getText());
        }else if(belowText.getText().toString().equals("输入终点")){
            upString = String.valueOf(upText.getText());
            belowString = "输入起点";
        }else{
            upString = String.valueOf(upText.getText());
            belowString = String.valueOf(belowText.getText());
        }

        if(upText.getText().equals(this.getString(R.string.my_location))
                || belowText.getText().equals(this.getString(R.string.my_location))){
            upImage.setImageResource(myLocationIsUp?R.drawable.target_blue:R.drawable.route_location);
            belowImage.setImageResource(myLocationIsUp?R.drawable.route_location:R.drawable.route_target);
        }else{
            upImage.setImageResource(R.drawable.target_blue);
            belowImage.setImageResource(R.drawable.route_target);
        }
        upText.setText(belowString);
        belowText.setText(upString);
        if(null != fromPoint && null != toPoint){
            LatLonPoint mPoint = fromPoint;
            fromPoint = toPoint;
            toPoint = mPoint;
        }
        myLocationIsUp = myLocationIsUp?false:true;
    }


    private LatLonPoint fromPoint;
    private LatLonPoint toPoint;
    Intent pathShowIntent;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Initialize.REQUEST_CODE_UP&& resultCode == Initialize.RESULT_CODE_UP){
            if(data.getStringExtra(Initialize.PLACE_NAME).equals("我的位置")){
                if(belowText.getText().toString().equals("我的位置")){
                    upText.setText("我的位置");
                    belowText.setText("输入终点");
                    upImage.setImageResource(R.drawable.route_location);
                    belowImage.setImageResource(R.drawable.route_target);
                    myLocationIsUp = myLocationIsUp?false:true;
                }else{
                    upText.setText(data.getStringExtra(Initialize.PLACE_NAME));
                    upImage.setImageResource(R.drawable.route_location);
                }
            }else{
                upText.setText(data.getStringExtra(Initialize.PLACE_NAME));
                upImage.setImageResource(R.drawable.target_blue);
            }
            fromPoint = data.getParcelableExtra(Initialize.PLACE_POINT);
        }else if(requestCode == Initialize.REQUEST_CODE_BELOW && resultCode == Initialize.RESULT_CODE_BELOW){
            if(data.getStringExtra(Initialize.PLACE_NAME).equals("我的位置")){
                if(upText.getText().toString().equals("我的位置")){
                    belowText.setText("我的位置");
                    upText.setText("输入起点");
                    upImage.setImageResource(R.drawable.target_blue);
                    belowImage.setImageResource(R.drawable.route_location);
                    myLocationIsUp = myLocationIsUp?false:true;
                }else{
                    belowText.setText(data.getStringExtra(Initialize.PLACE_NAME));
                    belowImage.setImageResource(R.drawable.route_location);
                }
            }else{
                belowText.setText(data.getStringExtra(Initialize.PLACE_NAME));
                belowImage.setImageResource(R.drawable.route_target);
            }
            toPoint = data.getParcelableExtra(Initialize.PLACE_POINT);
        }

        if(!upText.getText().toString().substring(0,2).equals("输入") &&
                !belowText.getText().toString().substring(0,2).equals("输入")){
            searchBtn.setTextColor(Initialize.BLUE_TEXT);
            searchBtn.setEnabled(true);
            searchBtn.setOnClickListener(this);
            handleMessageTo();
        }
    }

    public void handleMessageTo(){
        pathShowIntent = new Intent(this,BusPathActivity.class);
        if(null == fromPoint){
            fromPoint = new LatLonPoint(locationMessage.getLatitude(),locationMessage.getLongitude());
        }else if(null == toPoint){
            toPoint = new LatLonPoint(locationMessage.getLatitude(),locationMessage.getLongitude());
        }

        pathShowIntent.putExtra(Initialize.LOCATION,locationMessage);
        pathShowIntent.putExtra("FromName",upText.getText().toString());
        pathShowIntent.putExtra("ToName",belowText.getText().toString());
        pathShowIntent.putExtra("From",fromPoint);
        pathShowIntent.putExtra("To",toPoint);
        startActivity(pathShowIntent);
    }


    public void openBusView(View view) {
        Intent intent = new Intent(this, BusMapActivity.class);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != startLocationServiceIntent){
            stopService(startLocationServiceIntent);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 每次重新回到界面的时候注册广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.locationReceiver");
        registerReceiver(locationReceiver, filter);

        IntentFilter poiFilter = new IntentFilter();
        poiFilter.addAction("com.PoiBroadcast");
        registerReceiver(poiSearchReceiver, poiFilter);

        IntentFilter busLineFilter = new IntentFilter();
        busLineFilter.addAction("com.BusLineBroadcast");
        registerReceiver(busLineReceiver, busLineFilter);

        //先注册触发器再开启服务发送广播
        startLocationServiceIntent = new Intent(this, LocationService.class);
        startService(startLocationServiceIntent);

        /*getLocationMessage = new Intent(this, BaseLocationService.class);
        IntentFilter receiverFilter = new IntentFilter();
        receiverFilter.addAction("com.BaseLocationReceiver");
        registerReceiver(localMessageReceiver,receiverFilter);
        startService(getLocationMessage);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != locationReceiver) {
            unregisterReceiver(locationReceiver);
        }
        if (null != poiSearchReceiver && poiFlag) {
            unregisterReceiver(poiSearchReceiver);
        }
        if (null != busLineReceiver) {
            unregisterReceiver(busLineReceiver);
        }
    }

    private boolean poiFlag = false;

    private LocationMessage locationMessage;
    private ErrorStatus locationErrorStatus;

    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            locationErrorStatus = intent.getParcelableExtra("ErrorStatus");
            if (locationErrorStatus.getIsError()) {

            } else {
                locationMessage = intent.getExtras().getParcelable(Initialize.LOCATION);
                if (null != mGetLocationMessage) {
                    mGetLocationMessage.OnReceiveMessage(locationMessage, locationErrorStatus);
                }
                if(null == inputIntent.getParcelableExtra(Initialize.LOCATION)){
                    inputIntent.putExtra(Initialize.LOCATION,locationMessage);
                    //protectBridge.setVisibility(View.GONE);
                }
                poiFlag = true;
            }
        }
    };

    private ArrayList<PoiItem> mPoiMessage;
    private ErrorStatus poiErrorStatus;

    private BroadcastReceiver poiSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            poiErrorStatus = intent.getParcelableExtra("ErrorStatus");
            if (poiErrorStatus.getIsError()) {

            } else {

                mPoiMessage = intent.getParcelableArrayListExtra("PoiMessage");
                if (null != mGetPoiMessage) {
                    mGetPoiMessage.OnReceivePoiMessage(mPoiMessage, poiErrorStatus);
                }

//                Log.i("Aki0909090","poiMessage");
            }
        }
    };


    private ArrayList<Map<String, BusLineItem>> busLineItems;
    private ErrorStatus busLineErrorStatus;

    private BroadcastReceiver busLineReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("busLineReceiver", "接收服务");
            busLineErrorStatus = intent.getParcelableExtra("ErrorStatus");
            if (busLineErrorStatus.getIsError()) {
                Log.d("ERRORR", "busline服务获取错误");
            } else {

                String jsonData = intent.getStringExtra("BusLine");
                busLineItems = new Gson()
                        .fromJson(jsonData, new TypeToken<ArrayList<Map<String, BusLineItem>>>() {
                        }.getType());
                Log.d("busLineReceiver", jsonData);
//                Log.i("testAki93937557","广播到达!");

//                String test = intent.getExtras().getString("test");

//                busLineItems = intent.getExtras().getParcelableArrayList("BusLine");
//                ParcelableMap _map = busLineItems.get(0);
//                String b = busLineItems.get(0).get("GoBusLineMessage").getClass().getCanonicalName();
                /**
                 *busLineItems类型：List<Map<String,BusLineItem>>，map里有来去程的busline信息
                 *
                 * */

                /*for(Map<String,BusLineItem> map : busLineItems){
                    BusLineItem Gobus = (BusLineItem) map.get("GoBusLineMessage");
                    BusLineItem Backbus = (BusLineItem) map.get("BackBusLineMessage");
                    Log.i("testAki048585",Gobus.getBusLineType()+ "56");
                    Log.i("resultAki",Backbus.getBusLineName() + "/");
                }*/
                if (null != mGetBusLineMessage) {
                    mGetBusLineMessage.OnReceiveBusLineMessage(busLineItems, busLineErrorStatus);
                    Initialize.RECEIVED = true;
                }

            }
        }
    };


    public boolean isSpace(String text){
        boolean i = false;
        for(char a:text.toCharArray()){
            if(' ' == a){
                i = true;
            }
        }
        if(i){
            return true;
        }else{
            return false;
        }
    }

    private OnGetLocationMessage mGetLocationMessage;

    public void setOnGetLocationMessage(OnGetLocationMessage onGetLocationMessage) {
        mGetLocationMessage = onGetLocationMessage;
    }

    private OnGetPoiMessage mGetPoiMessage;

    public void setOnGetPoiMessage(OnGetPoiMessage onGetPoiMessage) {
        mGetPoiMessage = onGetPoiMessage;
    }

    private OnGetBusLineMessage mGetBusLineMessage;

    public void setOnGetBusLineMessage(OnGetBusLineMessage onGetBusLineMessage) {
        mGetBusLineMessage = onGetBusLineMessage;
    }


    //接口便于其他fragment即使获取locationMessage
    public interface OnGetLocationMessage {
        public void OnReceiveMessage(LocationMessage locationMessage, ErrorStatus errorStatus);
    }

    //poi获取接口
    public interface OnGetPoiMessage {
        public void OnReceivePoiMessage(ArrayList<PoiItem> poiMessage, ErrorStatus errorStatus);
    }

    //busline获取接口
    public interface OnGetBusLineMessage {
        public void OnReceiveBusLineMessage(ArrayList<Map<String, BusLineItem>> busLineItems, ErrorStatus errorStatus);
    }


    public void showSameLineInStation(String station, String snippet,LatLonPoint point) {

        if (null != station) {
            Intent toNextActivity =  new Intent(this, SameStationActivity.class);
            toNextActivity.putExtra("StationName", station);
            toNextActivity.putExtra("Snippet",snippet);
            toNextActivity.putExtra("Point",point);
            startActivity(toNextActivity);
        }
    }

}
