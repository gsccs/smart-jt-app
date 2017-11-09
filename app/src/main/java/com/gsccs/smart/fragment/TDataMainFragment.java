package com.gsccs.smart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.model.TrafficDataEntity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 交通指数整体情况
 *
 * Created by x.d zhang on 2016/11/27.
 */
public class TDataMainFragment extends Fragment {

    @Bind(R.id.traffic_city)
    TextView mTrafficCity;
    @Bind(R.id.tv_traffic_val)
    TextView mTrafficVal;
    @Bind(R.id.tv_traffic_txt)
    TextView mTrafficTxt;
    @Bind(R.id.tv_traffic_tip)
    TextView mTrafficTip;
    @Bind(R.id.traffic_data)
    GridView mGridView;

    private String[] tDataKey = { "2.0", "4.0", "6.0", "8.0", "10.0" };
    private String[] tDataText = { "畅通", "基本畅通", "轻度拥堵", "中度拥堵", "严重拥堵" };
    ArrayList<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();


    public static Fragment getInstance(Bundle bundle) {
        TDataMainFragment fragment = new TDataMainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.traffic_data_city_fragment, container, false);
        ButterKnife.bind(this, view);
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        datalist.clear();
        for (int i = 0; i < tDataKey.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("key", tDataKey[i]);
            map.put("text", tDataText[i]);
            datalist.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), datalist,
                R.layout.activity_traffic_index_data,
                new String[] { "key", "text" },
                new int[] {R.id.key, R.id.title });
        mGridView.setAdapter(simpleAdapter);
    }

    private void initView(View view) {
        if (null == getArguments().getParcelable("data")){
            Log.d("DataMain","null");
            return;
        }
        TrafficDataEntity trafficDataEntity = getArguments().getParcelable("data");
        mTrafficCity.setText(trafficDataEntity.getName());
        mTrafficVal.setText(""+trafficDataEntity.getValue());
        mTrafficTxt.setText(trafficDataEntity.getValuestr());
        mTrafficTip.setText(trafficDataEntity.getRemark());
        //TextView tv = (TextView) view.findViewById(R.id.tv_id);
        //tv.setText(getArguments().getString("title"));
    }
}
