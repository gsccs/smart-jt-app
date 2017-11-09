package com.gsccs.smart.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.gsccs.smart.R;
import com.gsccs.smart.adapter.ParkDetailAdapter;
import com.gsccs.smart.listener.ParkClickListener;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.model.ParkEntity;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.ParkHttps;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  智慧停车
 */
public class ParkMapActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener,ParkClickListener{

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.stop_map)
    MapView mMapView;
    @Bind(R.id.below_pager)
    ViewPager mParkViewPage;

    private AMap mAMap;
    private LatLng centerPoint;
    private ParkDetailAdapter mParkDetailPagerAdapter;
    private CameraUpdate mCameraUpdate;

    private Toast mToast;


    List<ParkEntity> parkList = new ArrayList<ParkEntity>();

    private double lat = BaseConst.lat;
    private double lng = BaseConst.lng;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            initMap();
            mMapView.setVisibility(View.VISIBLE);
            //hideBridge.setVisibility(View.GONE);
            if(null!=locationMessage){
                CameraUpdateFactory.newLatLngZoom(new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude()), 4);
            }else{

            }

        }
    };
    private LocationMessage locationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.park_map_activity);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        initMap();
        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        if (null==titleName || titleName.equals("")){
            mHeadTextView.setText("智慧停车");
        }else{
            mHeadTextView.setText(titleName);
        }
        mHeadImageView.setOnClickListener(this);

        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        ParkHttps.getInstance(this).queryPageList(0,null,null,refreshHandler);
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (null == mAMap) {
            mAMap = mMapView.getMap();
            //mAMap.moveCamera(mCameraUpdate);
//            setUpMap();
        }
    }


    private  void drawStopPoint(){
        //绘制marker
        mAMap.clear();
        for(ParkEntity park:parkList){
            Marker marker = mAMap.addMarker(new MarkerOptions()
                    .position(new LatLng(park.getLat(), park.getLng()))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.icon_park)))
                    .draggable(true));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.locationReceiver");
        registerReceiver(mapReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != mapReceiver){
            unregisterReceiver(mapReceiver);
        }
    }

    private BroadcastReceiver mapReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras().getBoolean("Fail")){
                Log.i("Aki1dasdasdd234","获取错误，错误码:" +
                        intent.getExtras().getInt("ErrorCode") +
                        "错误信息:" +
                        intent.getExtras().getString("ErrorMessage"));
            }else{
                locationMessage = intent.getExtras().getParcelable("Location");
                //mHandler.sendEmptyMessage(0);
                if (null!= locationMessage){
                    centerPoint = new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude());
                    mCameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint, 15);
                }else{
                    centerPoint = new LatLng(BaseConst.lat, BaseConst.lng);
                    mCameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint, 15);
                    mAMap.moveCamera(mCameraUpdate);
                    Toast.makeText(ParkMapActivity.this, "定位失败，请确认定位服务已开启!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    public void moveCursor(int index){
        ParkEntity park = parkList.get(index);
        mCameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(park.getLat(), park.getLng()), 15);
        mAMap.moveCamera(mCameraUpdate);
    }

    private void loadViewPage(){
        mParkDetailPagerAdapter = new ParkDetailAdapter(this,parkList);
        mParkDetailPagerAdapter.setOnItemClickListener(this);
        mParkViewPage.setAdapter(mParkDetailPagerAdapter);
        PointViewPagerPageChangedListener pageChangedListener = new PointViewPagerPageChangedListener();
        mParkViewPage.addOnPageChangeListener(pageChangedListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_ico:
                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        moveCursor(position);
        if(!cursorMove){
            mParkViewPage.setCurrentItem(position);
        }else{
            cursorMove = false;
        }
    }



    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_PARK_PAGELIST:
                    parkList = (List<ParkEntity>) msg.obj;
                    Log.d("park size",""+parkList.size());
                    drawStopPoint();
                    loadViewPage();
                    moveCursor(1);
                    break;
                case BaseConst.WHAT_PARK_ADD:

                    break;
                default:
                    break;
            }
        }
    };

    private boolean cursorMove = false;

    @Override
    public void onOpenPark(ParkEntity par) {
        Log.d("onOpenPark",par.getName());
        //Intent goMap = new Intent(this,ParkRouteDetailActivity.class);
        //goMap.putExtra("Position",position);
        //goMap.putExtra("BusResult",mBusRouteResult);
        //startActivity(goMap);
    }

    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            cursorMove = true;
            mParkViewPage.setCurrentItem(Integer.parseInt(view.getTag().toString()));
        }
    }

    class PointViewPagerPageChangedListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            moveCursor(position);
            if(!cursorMove){
                //furtherDetailPath.setCurrentItem(position);
            }else{
                cursorMove = false;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
