package com.gsccs.smart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.*;

import com.gsccs.smart.listener.OnBusStationClickListener;
import com.gsccs.smart.listener.PoiSearchedListener;
import com.gsccs.smart.R;
import com.gsccs.smart.model.ErrorStatus;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.utils.Initialize;
import com.gsccs.smart.utils.PoiAddressUtil;
import com.gsccs.smart.view.BusLineView;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class BusLineShowActivity extends AppCompatActivity implements
        View.OnClickListener,  PoiSearchedListener, OnBusStationClickListener {

    @Bind(R.id.bus_name)
    TextView busName;
    @Bind(R.id.destination)
    TextView destination;
    @Bind(R.id.time_price)
    TextView timeAndPrice;
    @Bind(R.id.below_title)
    TextView belowTitle;
    @Bind(R.id.back)
    Button back;
    @Bind(R.id.waiting)
    RelativeLayout waiting;
    @Bind(R.id.busline_message)
    LinearLayout busLineMainPage;
    @Bind(R.id.switch_blank)
    LinearLayout switchBlank;
    @Bind(R.id.switch_line)
    RelativeLayout switchLine;
    @Bind(R.id.hide_bus)
    RelativeLayout hidePage;
    @Bind(R.id.bus_line)
    BusLineView busLineView;

    private String busLineName;
    private String destinationText;
    private String timeText;
    private List<BusStationItem> goStation;

    private LocationMessage locationMessage;
    private Intent showSameStation;
    private PoiAddressUtil mPoiAddressUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_line_show_activity);
        ButterKnife.bind(this);
        initData();
        initView();
    }


    public void initData(){
        showSameStation = new Intent(this,SameStationActivity.class);
        mPoiAddressUtil = new PoiAddressUtil(this,null,null,true,null);

        goStation = getIntent().getParcelableArrayListExtra("GoLine");
        List<BusStationItem> BackStation = null;
        if(null != getIntent().getParcelableArrayListExtra("BackLine")){
            BackStation = getIntent().getParcelableArrayListExtra("BackLine");
        }
        busLineName = getIntent().getStringExtra("BusName");
        if(-1 != busLineName.indexOf("(")){
            busLineName = busLineName.substring(0,busLineName.indexOf("("));
        }
        destinationText = goStation.get(0).getBusStationName()
                + "→"
                + goStation.get(goStation.size()-1).getBusStationName();
        if(null != getIntent().getSerializableExtra("FirstBus")){
            String startTime = getIntent().getSerializableExtra("FirstBus").toString();
            String endTime = getIntent().getSerializableExtra("LastBus").toString();
            startTime = startTime.substring(12,20);
            endTime = endTime.substring(12,20);
            timeText = "首车:" +
                    startTime +
                    "·" +
                    "末车:" +
                    endTime ;
        }else{
            timeText = "无该公交信息!!";
        }
        if(null != Initialize.LOCAL_MESSAGE){
            locationMessage = Initialize.LOCAL_MESSAGE;
        }
    }

    public void initView(){
        back.setOnClickListener(this);
        switchLine.setOnClickListener(this);
        switchBlank.setLayoutParams(new RelativeLayout.LayoutParams(Initialize.SCREEN_WIDTH/4,LinearLayout.LayoutParams.MATCH_PARENT));

        busLineView.setBusStation(goStation);
        busName.setText(busLineName);
        destination.setText(destinationText);
        timeAndPrice.setText(timeText);

        busLineView.setOnStationClickListener(this,"else");
        switchLine.setEnabled(false);
        mPoiAddressUtil.setPoiSearchedListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.switch_line:
                showSameStation();
                break;
        }
    }


    public void showSameStation(){
        startActivity(showSameStation);
    }

    public void showMessage(ErrorStatus status){
        if(!status.getIsError()){
            switchLine.setEnabled(true);
        }
        busLineView.setOnClickEnable(true);
        belowTitle.setVisibility(View.VISIBLE);
        waiting.setVisibility(View.GONE);
        switchLine.setEnabled(true);
    }

    public void hideMessage(){
        busLineView.setOnClickEnable(false);
        belowTitle.setVisibility(View.GONE);
        waiting.setVisibility(View.VISIBLE);
        switchLine.setEnabled(false);
    }


    private int position;
    @Override
    public void OnClickItem(int position) {

        this.position = position;

        hideMessage();

        mPoiAddressUtil.setmLatLng(goStation.get(position).getLatLonPoint())
                .setmCity(locationMessage.getCity())
                .setKeyWord(goStation.get(position).getBusStationName());
        mPoiAddressUtil.startPoiSearch();
        showSameStation.putExtra("StationName",goStation.get(position).getBusStationName());
        showSameStation.putExtra("Point",goStation.get(position).getLatLonPoint());
    }

    private String snippet;
    private String stationName;
    @Override
    public void onGetPoiMessage(List<PoiItem> poiItems, String poiTitle, LatLonPoint addressPoint, ErrorStatus status) {
        if(status.getIsError()){
            belowTitle.setText("该站点暂无信息!");
            showMessage(status);
        }else{
            for(PoiItem item: poiItems){
                if(item.getTitle().equals(goStation.get(position).getBusStationName() + "(公交站)")){
                    this.snippet = item.getSnippet();
                    this.stationName = item.getTitle();
                }
            }
            if(null != snippet){
                showSameStation.putExtra("Snippet",snippet);
                belowTitle.setText(stationName + "该站点公交线路:" + snippet);
                showMessage(status);
            }
        }
    }
}
