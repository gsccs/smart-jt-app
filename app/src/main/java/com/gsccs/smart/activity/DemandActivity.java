package com.gsccs.smart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.adapter.DemandAdapter;
import com.gsccs.smart.listener.RecyclerItemClickListener;
import com.gsccs.smart.model.DemandEntity;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.InfoHttps;
import com.gsccs.smart.widget.SystemToastDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  供求信息
 */
public class DemandActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.recycler_list)
    RecyclerView mRecyclerList;
    @Bind(R.id.refresher)
    SwipeRefreshLayout mRefresher;
    @Bind(R.id.add_btn_fab)
    FloatingActionButton addBtnFab;

    List<DemandEntity> dataList = new ArrayList<>();
    DemandAdapter mAdapter = new DemandAdapter(dataList);

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.demand_activity);
        ButterKnife.bind(this);

        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        mHeadTextView.setText(titleName);
        mHeadImageView.setOnClickListener(this);

        mRefresher.setColorSchemeResources(R.color.colorPrimary);
        mRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        mRecyclerList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerList.setLayoutManager(layoutManager);
        mRecyclerList.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));
        mRecyclerList.setItemAnimator(new DefaultItemAnimator());
        mRecyclerList.setAdapter(mAdapter);

        addBtnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null== SmartApplication.getCurrUser()){
                    Intent intent = new Intent(DemandActivity.this, SignInUpActivity.class);
                    intent.putExtra(BaseConst.IS_SIGN_IN,true);
                    startActivity(intent);
                }else{
                    DemandFormActivity.actionStart(DemandActivity.this);
                }
            }
        });
        refreshData();
    }


    private RecyclerItemClickListener.OnItemClickListener onItemClickListener =
            new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int i = dataList.size() - position - 1;
                    DemandEntity entity = dataList.get(i);
                    Toast.makeText(DemandActivity.this, "mEntity！"+entity.getContent(), Toast.LENGTH_SHORT).show();
                    entity.setImgurls(dataList.get(i).getImgurls());
                    DemandDetailActivity.actionStart(DemandActivity.this, entity);
                }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_ico:
                finish();
            break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

    private void refreshData() {
        mRefresher.setRefreshing(true);
            InfoHttps.getInstance(this).queryDemandPageList(1,refreshHandler);
    }


    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_DEMAND_PAGELIST:
                    mRefresher.setRefreshing(false);
                    List<DemandEntity> list = (List<DemandEntity>) msg.obj;
                    if (list.size() == 0) {
                        SystemToastDialog.showShortToast(DemandActivity.this, "没有更多符合条件的数据");
                    }
                    //SystemToastDialog.showShortToast(DemandActivity.this, "数据列表："+list.size());
                    dataList.clear();
                    dataList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };
}
