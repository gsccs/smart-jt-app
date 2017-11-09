package com.gsccs.smart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gsccs.smart.R;
import com.gsccs.smart.adapter.TrafficDataListAdapter;
import com.gsccs.smart.model.TrafficDataEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 *
 *
 * Created by x.d zhang on 2016/11/27.
 */
public class TDataFragment extends Fragment {

    @Bind(R.id.recycler_list)
    RecyclerView mRecyclerList;

    List<TrafficDataEntity> list = new ArrayList<>();
    TrafficDataListAdapter mAdapter = null;

    public static Fragment getInstance(Bundle bundle) {
        TDataFragment fragment = new TDataFragment();
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
        View view = inflater.inflate(R.layout.traffic_data_area_fragment, container, false);
        ButterKnife.bind(this, view);

        mRecyclerList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerList.setLayoutManager(layoutManager);
        //mRecyclerList.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));
        mRecyclerList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TrafficDataListAdapter(list);
        mRecyclerList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        if (null == getArguments().getParcelableArrayList("data")){
            return;
        }

        list = getArguments().getParcelableArrayList("data");
        Log.d("TDataFragment",""+list.size());
        mAdapter = new TrafficDataListAdapter(list);
        mAdapter.notifyDataSetChanged();
        //TextView tv = (TextView) view.findViewById(R.id.tv_id);
        //tv.setText(getArguments().getString("title"));
    }
}
