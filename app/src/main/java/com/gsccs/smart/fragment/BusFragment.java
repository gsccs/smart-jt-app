package com.gsccs.smart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.gsccs.smart.R;
import com.gsccs.smart.activity.BusLineShowActivity;
import com.gsccs.smart.activity.BusMainActivity;
import com.gsccs.smart.activity.SameStationActivity;
import com.gsccs.smart.adapter.RecentAdapter;
import com.gsccs.smart.model.ErrorStatus;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.utils.Initialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by x.d zhang on 2016/10/29.
 */
public class BusFragment extends Fragment implements BusMainActivity.OnGetLocationMessage,
        BusMainActivity.OnGetPoiMessage, BusMainActivity.OnGetBusLineMessage, AdapterView.OnItemClickListener, View.OnClickListener {

    private BusMainActivity busMainActivity;
    private BusLineItem busLineItem;

    @Bind(R.id.bus_recent_stations)
    TextView recentStation;
    @Bind(R.id.bus_recent_line)
    TextView recentLine;
    @Bind(R.id.bus_next_station)
    TextView nextStation;
    @Bind(R.id.bus_stations_list)
    ListView stationListView;
    @Bind(R.id.bus_now_line)
    LinearLayout nowLine;
    @Bind(R.id.bus_now_station)
    LinearLayout nowStation;
    @Bind(R.id.bus_main_message)
    LinearLayout mainMessage;
    @Bind(R.id.bus_hide_bg)
    RelativeLayout hideBg;

    private ArrayList<Map<String, BusLineItem>> busLineMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_index_fragment, null);
        ButterKnife.bind(this,view);
        busMainActivity = (BusMainActivity) getActivity();
        busMainActivity.setOnGetLocationMessage(this);
        busMainActivity.setOnGetPoiMessage(this);
        busMainActivity.setOnGetBusLineMessage(this);
        stationListView.setOnItemClickListener(this);
        nowLine.setOnClickListener(this);
        nowStation.setOnClickListener(this);

//        mainActivity.set
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void OnReceiveMessage(LocationMessage locationMessage, ErrorStatus errorStatus) {

    }

    private ArrayList<PoiItem> mPoiMessage;
    private RecentAdapter stationAdapter;
    private ArrayList<PoiItem> data = new ArrayList<PoiItem>();
    private boolean isFirst = true;

    @Override
    public void OnReceivePoiMessage(ArrayList<PoiItem> poiMessage, ErrorStatus errorStatus) {
        if (!errorStatus.getIsError()) {
            mPoiMessage = poiMessage;
            String stationName = mPoiMessage.get(0).getTitle();
            recentStation.setText(stationName
                    .substring(0, stationName.lastIndexOf("(")));
            if (data.size() > 0) {
                data.clear();
            }

            addDataToAdapter(data, poiMessage);

            if (isFirst) {
                stationAdapter = new RecentAdapter(busMainActivity, data);
                stationListView.setAdapter(stationAdapter);
                isFirst = false;
            } else {
                stationAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void OnReceiveBusLineMessage(ArrayList<Map<String, BusLineItem>> busLineItems, ErrorStatus errorStatus) {

        if (!errorStatus.getIsError()) {
            busLineMessage = busLineItems;
            busLineItem = busLineItems.get(0).get("GoBusLineMessage");
            recentLine.setText(busLineItem.getBusLineName());
            String result = getNextStation(busLineItem.getBusStations());
            String forward = null;
            if (!Initialize.ERROR.equals(result)) {
                forward = isLast ? "终点站:" : "下一站:";
                nextStation.setText(forward + result);
            }
            showMessage();
        }



        /*MyBusLineAdapter busLineAdapter = new MyBusLineAdapter(getActivity(),busLineItems);

        busList.setVisibility(View.VISIBLE);

        busList.setAdapter(busLineAdapter);

        busList.setOnItemClickListener(this);

        waitingBusLine.setVisibility(View.GONE);*/
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showSameLineInStation(data.get(i).getTitle(),data.get(i).getSnippet(),data.get(i).getLatLonPoint());
    }


    private boolean isLast = false;

    public String getNextStation(List<BusStationItem> stationItems) {
        int correctLocation = -1;
        if (null != mPoiMessage) {
            int count = 0;
            String busStationName = null;
            for (BusStationItem item : stationItems) {
                busStationName = mPoiMessage.get(0).getTitle();
                busStationName = busStationName.substring(0, busStationName.lastIndexOf("("));
                if (item.getBusStationName().indexOf(busStationName) != -1) {
                    correctLocation = count;
                    break;
                }
                count++;
            }
        }
        isLast = (correctLocation == (stationItems.size() - 1)) ? true : false;
        return (correctLocation == -1) ?
                Initialize.ERROR : (isLast ?
                (stationItems.get(correctLocation).getBusStationName()) : (stationItems.get(correctLocation + 1).getBusStationName()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bus_now_line:
                showLine();
                break;
            case R.id.bus_now_station:
                showSameLineInStation(mPoiMessage.get(0).getTitle(),mPoiMessage.get(0).getSnippet(),mPoiMessage.get(0).getLatLonPoint());
                break;
        }
    }

    public void showSameLineInStation(String station, String snippet,LatLonPoint point) {

        if (null != station) {
            Intent toNextActivity =  new Intent(getActivity(), SameStationActivity.class);
            toNextActivity.putExtra("StationName", station);
            toNextActivity.putExtra("Snippet",snippet);
            toNextActivity.putExtra("Point",point);
            startActivity(toNextActivity);
        }
    }

    public void showLine() {
        if (null != busLineMessage) {
            Intent intentShowLine = new Intent(getActivity(), BusLineShowActivity.class);

            intentShowLine.putParcelableArrayListExtra("GoLine",
                    (ArrayList<? extends Parcelable>) busLineMessage.get(0)
                            .get("GoBusLineMessage")
                            .getBusStations());

            intentShowLine.putParcelableArrayListExtra("BackLine",
                    (ArrayList<? extends Parcelable>) busLineMessage.get(0)
                            .get("BackBusLineMessage")
                            .getBusStations());

            intentShowLine.putExtra("BusName", busLineMessage.get(0)
                    .get("GoBusLineMessage")
                    .getBusLineName());

            intentShowLine.putExtra("FirstBus", busLineMessage.get(0)
                    .get("GoBusLineMessage").getFirstBusTime());

            intentShowLine.putExtra("LastBus", busLineMessage.get(0)
                    .get("GoBusLineMessage").getLastBusTime());
            startActivity(intentShowLine);
        }
    }

    private ArrayList<PoiItem> restList = new ArrayList<>();

    public void addDataToAdapter(ArrayList<PoiItem> data, ArrayList<PoiItem> poiMessage) {
        int flag = -1;
        String name = null;
        for (int count = 0; count < poiMessage.size(); count++) {
            name = poiMessage.get(count).getTitle();
            name = name.substring(0, name.indexOf("("));
            if (name.equals(recentStation.getText().toString())) {
                flag = count;
            }
        }
        data.addAll(poiMessage);
        if (-1 != flag) {
            data.remove(flag);
        }
        restList.addAll(data);
    }

    private void showMessage() {
        mainMessage.setVisibility(View.VISIBLE);
        hideBg.setVisibility(View.GONE);
    }
}
