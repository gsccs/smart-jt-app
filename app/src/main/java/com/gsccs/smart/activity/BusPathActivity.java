package com.gsccs.smart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.*;

import com.amap.api.services.route.RideRouteResult;
import com.gsccs.smart.R;
import com.gsccs.smart.adapter.PathListAdapter;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.utils.Initialize;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BusPathActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener,
        View.OnClickListener, AdapterView.OnItemClickListener {

    private LocationMessage locationMessage;
    private RouteSearch mRouteSearch;
    private LatLonPoint fromPoint;
    private LatLonPoint toPoint;

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.tv_path_txt)
    TextView mPathTxt;
    @Bind(R.id.no_traffic)
    TextView noTraffic;
    @Bind(R.id.route_paths)
    ListView pathListView;
    @Bind(R.id.protect_bridge)
    RelativeLayout protectBridge;

    private String fromName;
    private String toName;

    private PathListAdapter pathListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_show_path_activity);
        ButterKnife.bind(this);
        initView();

    }

    public void initView(){
        if(null != Initialize.LOCAL_MESSAGE){
            locationMessage = Initialize.LOCAL_MESSAGE;
        }else{
            locationMessage = getIntent().getParcelableExtra("Location");
        }
        fromPoint = getIntent().getParcelableExtra("From");
        toPoint = getIntent().getParcelableExtra("To");
        fromName = getIntent().getStringExtra("FromName");
        toName = getIntent().getStringExtra("ToName");

        mHeadImageView.setOnClickListener(this);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        pathListView.setOnItemClickListener(this);
        mPathTxt.setText(fromName + "→" + toName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        busRouteResult();
    }

    public void hideView(){
        protectBridge.setVisibility(View.GONE);
    }

    public void noTraffic(){
        noTraffic.setVisibility(View.VISIBLE);
        protectBridge.setVisibility(View.GONE);
    }

    public void showView(){
        pathListView.setVisibility(View.VISIBLE);
    }

    public void showResult(){
        if(null != pathList){
            pathListAdapter = new PathListAdapter(this,pathList);
            pathListView.setAdapter(pathListAdapter);
            hideView();
            showView();
        }else{
            noTraffic();
        }
    }


    private String mCurrentCityName;
    public void busRouteResult(){
        if(null != locationMessage && null != locationMessage.getCity()){
            mCurrentCityName = locationMessage.getCity();
        }else{
            mCurrentCityName = "酒泉市";
        }
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                fromPoint, toPoint);
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusDefault,
                mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
        mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
    }

    private BusRouteResult mBusRouteResult;
    private List<BusPath> pathList = null;
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {
        if(errorCode == 1000){
            if(null != busRouteResult && null != busRouteResult.getPaths()){
                if(busRouteResult.getPaths().size() > 0){
                    mBusRouteResult = busRouteResult;
                    pathList = busRouteResult.getPaths();
                    showResult();

                }else{
                    noTraffic();
                }
            }else{
                noTraffic();
            }
        }else{
            noTraffic();
        }
        Log.i("getRoute",busRouteResult.toString());
//        busRouteResult.
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back_ico:
                finish();
                break;
        }
    }

    private Intent showPathInMap;
    private BusPath item;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        item = pathList.get(i);
        showPathInMap = new Intent(this,BusRouteDetailActivity.class);
        showPathInMap.putExtra("bus_path", item);
        showPathInMap.putExtra("bus_result", mBusRouteResult);
        showPathInMap.putExtra("Position",i);
        showPathInMap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(showPathInMap);
    }

}
