package com.gsccs.smart.activity;

import android.app.Activity;
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

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.adapter.LostFoundAdapter;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.InfoHttps;
import com.gsccs.smart.listener.RecyclerItemClickListener;
import com.gsccs.smart.model.LostFoundEntity;
import com.gsccs.smart.widget.SystemToastDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 失物招领
 */
public class LostFoundActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.recycler_list)
    RecyclerView mRecyclerList;
    @Bind(R.id.refresher)
    SwipeRefreshLayout mRefresher;
    @Bind(R.id.add_btn_fab)
    FloatingActionButton mFloatBtn;

    private int page = 1;
    List<LostFoundEntity> dataList = new ArrayList<LostFoundEntity>();
    LostFoundAdapter mAdapter = new LostFoundAdapter(dataList);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostfound_activity);
        ButterKnife.bind(this);

        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        mHeadTextView.setText(titleName);
        mHeadImageView.setOnClickListener(this);

        mFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null== SmartApplication.getCurrUser()){
                    Intent intent = new Intent(LostFoundActivity.this, SignInUpActivity.class);
                    intent.putExtra(BaseConst.IS_SIGN_IN,true);
                    startActivity(intent);
                }else{
                    LostFoundFormActivity.actionStart(LostFoundActivity.this);
                }
            }
        });

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

        refreshData();
    }

    private void refreshData() {
        mRefresher.setRefreshing(true);
        InfoHttps.getInstance(this).queryLostPageList(page,refreshHandler);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_ico:
                finish();
                break;
        }
    }

    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_LOSTFOUND_PAGELIST:
                    mRefresher.setRefreshing(false);
                    List<LostFoundEntity> list = (List<LostFoundEntity>) msg.obj;
                    if (list.size() == 0) {
                        SystemToastDialog.showShortToast(LostFoundActivity.this, "没有更多符合条件的数据");
                    }
                    //SystemToastDialog.showShortToast(LostFoundActivity.this, "数据列表："+list.size());
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


    private RecyclerItemClickListener.OnItemClickListener onItemClickListener =
            new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int i = dataList.size() - position - 1;
                    LostFoundEntity entity = dataList.get(i);
                    entity.setImgurls(dataList.get(i).getImgurls());
                    LostFoundDetailActivity.actionStart(LostFoundActivity.this, entity);
                }
            };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
