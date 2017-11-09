package com.gsccs.smart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.gsccs.smart.R;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.network.BaseConst;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 公交站点
 */
public class BusMapActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.bus_map)
    MapView mMapView;

    private AMap aMap;

    private LatLng centerPoint;

    private CameraUpdate mCameraUpdate;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            mListener.onLocationChanged();
//            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude())));

            init();
            mMapView.setVisibility(View.VISIBLE);
//            CameraUpdateFactory.newLatLngZoom(new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude()), 4);

            //绘制marker
            Marker marker = aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.location_marker)))
                    .draggable(true));
        }
    };
    private LocationMessage locationMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_map_activity);
        ButterKnife.bind(this);

        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        if (null==titleName || titleName.equals("")){
            mHeadTextView.setText("附近站点");
        }else{
            mHeadTextView.setText(titleName);
        }
        mHeadImageView.setOnClickListener(this);
        mMapView.onCreate(savedInstanceState);

//        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (null == aMap) {
            aMap = mMapView.getMap();
            aMap.moveCamera(mCameraUpdate);
//            setUpMap();
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

                mHandler.sendEmptyMessage(0);

                centerPoint = new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude());

                mCameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint, 17);
//                unregisterReceiver(mapReceiver);


//                Log.i("Aki1dasdasd234", locationMessage.getCountry() +
//                        locationMessage.getProvince() +
//                        locationMessage.getCity() +
//                        locationMessage.getStreet());
//                Message msg = new Message();
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("Location",mapLocation);
//                mHandler.sendMessage(msg);


            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_ico:
                finish();
                break;
        }
    }
}
