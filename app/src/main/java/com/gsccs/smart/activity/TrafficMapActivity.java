package com.gsccs.smart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviStaticInfo;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.gsccs.smart.R;
import com.gsccs.smart.model.CameraEntity;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.TrafficHttps;
import com.gsccs.smart.widget.SystemToastDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *   交通路况
 */
public class TrafficMapActivity extends AppCompatActivity implements AMapNaviListener, AMapNaviViewListener,View.OnClickListener{

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.traffic_vedio_view)
    LinearLayout trafficVedioView;
    @Bind(R.id.traffic_vedio_up)
    ImageView trafficVedioUp;
    @Bind(R.id.camera_grid_view)
    GridView cameraGridView;

    @Bind(R.id.traffic_map)
    MapView mMapView;
    @Bind(R.id.traffic_choose)
    ImageView mImageTraffic;
    @Bind(R.id.strategy_choose)
    ImageView mImageStrategy;

    private AMap mAMap;
    private LatLng centerPoint;
    private CameraUpdate mCameraUpdate;
    private List<CameraEntity> cameraList = new ArrayList<>();

    private LocationMessage locationMessage;
    private Toast mToast;
    private boolean isinit = true;

    private GridViewAdapter mGridViewAdapter;


    // 视频模块
    private Display currDisplay;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private int vWidth,vHeight;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            initMap();
            mMapView.setVisibility(View.VISIBLE);
            CameraUpdateFactory.newLatLngZoom(new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude()), 4);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_map_activity);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);

        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        if (null==titleName || titleName.equals("")){
            mHeadTextView.setText("智慧路况");
        }else{
            mHeadTextView.setText(titleName);
        }
        mHeadImageView.setOnClickListener(this);

        initMap();

        mImageTraffic.setOnClickListener(this);
        mImageStrategy.setOnClickListener(this);
        trafficVedioUp.setOnClickListener(this);

        TrafficHttps.getInstance(this).queryCameraPageList("",refreshHandler);

        //initCameraList();
        //drawCameraPoint();
        setGridView();

        mGridViewAdapter= new GridViewAdapter(getApplicationContext());
        mGridViewAdapter.setData(cameraList);
        cameraGridView.setAdapter(mGridViewAdapter);
        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
    }


    /**
     * 地图实时交通开关
     */
    private void setTraffic() {
        if (mAMap.isTrafficEnabled()) {
            mImageTraffic.setImageResource(R.drawable.map_traffic_white);
            mAMap.setTrafficEnabled(false);
        } else {
            mImageTraffic.setImageResource(R.drawable.map_traffic_hl_white);
            mAMap.setTrafficEnabled(true);
        }
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            //mAMap.moveCamera(mCameraUpdate);
            mAMap.setTrafficEnabled(true);
            UiSettings uiSettings = mAMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);
        }
    }



    private void drawCameraPoint(){
        Log.i("drawCameraPoint","drawCameraPoint");
        //绘制marker
        mAMap.clear();
        for(CameraEntity camera:cameraList){
            Marker marker = mAMap.addMarker(new MarkerOptions()
                    .position(new LatLng(camera.getLat(), camera.getLng()))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.camera)))
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
        //mAMapNavi.stopNavi();
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
                Log.i("Traffic","获取错误，错误码:" +
                        intent.getExtras().getInt("ErrorCode") +
                        "错误信息:" +
                        intent.getExtras().getString("ErrorMessage"));
            }else{
                locationMessage = intent.getExtras().getParcelable("Location");
                if (null != locationMessage){
                    Log.i("Traffic","获取Location" + locationMessage.getCity());
                    mHandler.sendEmptyMessage(0);
                    centerPoint = new LatLng(locationMessage.getLatitude(), locationMessage.getLongitude());
                    mCameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint, 15);
                }else{
                    centerPoint = new LatLng(BaseConst.lat, BaseConst.lng);
                    mCameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint, 15);
                }

                if (isinit){
                    mAMap.moveCamera(mCameraUpdate);
                    isinit = false;
                }
            }
        }
    };


    private void playVedio(){
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource("http://103.12.234.246:5011/vod/09cae08a361bfa5fee5e692c4ed15f5b.flv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_ico:
                finish();
                break;
            case R.id.traffic_choose:
                setTraffic();
                break;
            case R.id.strategy_choose:
                //strategyChoose();
                if (null!= mAMap && null != mCameraUpdate){
                    mAMap.moveCamera(mCameraUpdate);
                }
                break;
            case R.id.traffic_vedio_up:
                Log.i("traffic_vedio_up","交通视频按钮");
                trafficVedioView.setWeightSum(10);
        }
    }


    /**设置GirdView参数，绑定数据*/
    private void setGridView() {
        int size = cameraList.size();
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        cameraGridView.setLayoutParams(params);             // 设置GirdView布局参数,横向布局的关键
        cameraGridView.setColumnWidth(itemWidth);           // 设置列表项宽
        cameraGridView.setHorizontalSpacing(2);             // 设置列表项水平间距
        cameraGridView.setStretchMode(GridView.NO_STRETCH);
        cameraGridView.setNumColumns(size);                 // 设置列数量=列表集合数

        cameraGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CameraEntity camera = (CameraEntity)mGridViewAdapter.getItem(position);
                centerPoint = new LatLng(camera.getLat(), camera.getLng());
                mCameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint, 15);
                mAMap.moveCamera(mCameraUpdate);
                playVedio();
                showTip("正在对接");
            }
        });

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {

    }

    @Override
    public void onArriveDestination(AMapNaviStaticInfo aMapNaviStaticInfo) {

    }

    @Override
    public void onCalculateRouteSuccess() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }


    /**GirdView 数据适配器*/
    public class GridViewAdapter extends BaseAdapter {
        Context context;
        List<CameraEntity> list;

        public GridViewAdapter(Context _context) {
            this.context = _context;
        }
        public void setData(List<CameraEntity> _list) {
            this.list = _list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.activity_traffic_item, null);
            TextView itemTitle = (TextView) convertView.findViewById(R.id.camera_item_title);
            ImageView itemImage = (ImageView) convertView.findViewById(R.id.camera_item_image);
            CameraEntity camera = list.get(position);
            itemTitle.setText(camera.getName());
            //itemTitle.setText(camera.getId());
            itemImage.setBackgroundResource(R.drawable.vedio);
            return convertView;
        }
    }




    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }


    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_CAMERA_PAGELIST:
                    cameraList = (List<CameraEntity>) msg.obj;
                    if (cameraList.size() == 0) {
                        SystemToastDialog.showShortToast(TrafficMapActivity.this, "没有更多符合条件的数据");
                    }
                    Log.d("camera size",""+cameraList.size());
                    drawCameraPoint();
                    setGridView();
                    mGridViewAdapter.setData(cameraList);
                    mGridViewAdapter.notifyDataSetChanged();
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };

}
