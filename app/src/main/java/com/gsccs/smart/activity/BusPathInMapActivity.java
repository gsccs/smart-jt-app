package com.gsccs.smart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.gsccs.smart.R;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.utils.AMapUtil;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.BusRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;

public class BusPathInMapActivity extends AppCompatActivity implements View.OnClickListener {

    private BusRouteResult mBusRouteResult;
    private BusPath mBusPath;
    private int position;
    private TextView title;
    private Button backButton;
    private AMap mAMap;
    private MapView mapView;
    private LatLonPoint mStartPoint,mEndPoint;
    private ImageView locationView;
    private CameraUpdate mCameraUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_path_in_map_activity);

        getIntentData();
        initView(savedInstanceState);
        initMap();
    }

    public void getIntentData(){
        Intent intent = getIntent();
        if(null != intent){
            mBusRouteResult = intent.getParcelableExtra("BusResult");
            position = intent.getIntExtra("Position",0);
            mBusPath = mBusRouteResult.getPaths().get(position);
            mStartPoint = mBusRouteResult.getStartPos();
            mEndPoint = mBusRouteResult.getTargetPos();
        }
    }


    public void initView(Bundle bundle){
        mapView = $(R.id.map_view);
        title = $(R.id.title);
        backButton = $(R.id.back);
        locationView = $(R.id.iv_location);
        mapView.onCreate(bundle);
        backButton.setOnClickListener(this);
        title.setText("方案" + (position+1));
    }


    public void initMap(){
        /**
         * 初始化AMap对象
         */
        if (mAMap == null) {
            mAMap = mapView.getMap();
        }

        setfromandtoMarker();

        BusRouteOverlay busRouteOverlay =
                new BusRouteOverlay(this,mAMap,mBusPath,mBusRouteResult.getStartPos(),mBusRouteResult.getTargetPos());
        busRouteOverlay.addToMap();
        busRouteOverlay.zoomToSpan();

    }

    private <T extends View> T $(int resId){
        return (T) super.findViewById(resId);
    }

    private void setfromandtoMarker() {
        mAMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        mAMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.iv_location:
                if (null!= mAMap && null != mCameraUpdate){
                    mAMap.moveCamera(mCameraUpdate);
                }
                break;
        }
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


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private LocationMessage locationMessage;
    private LatLng centerPoint;

    private BroadcastReceiver mapReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras().getBoolean("Fail")){
                Log.i("Traffic","获取错误，错误码:" +
                        intent.getExtras().getInt("ErrorCode") +
                        "错误信息:" +
                        intent.getExtras().getString("ErrorMessage"));
            }else{
                locationMessage = intent.getExtras().getParcelable("Location");
                if (null != locationMessage){
                    Log.i("Traffic","获取Location" + locationMessage.getCity());
                    centerPoint = new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude());
                    mCameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint, 15);
                }else{
                    centerPoint = new LatLng(BaseConst.lat, BaseConst.lng);
                    mCameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint, 15);
                }
                //mHandler.sendEmptyMessage(0);
            }
        }
    };


}
