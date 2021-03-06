package com.gsccs.smart.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.adapter.AssistAdapter;
import com.gsccs.smart.listener.RecyclerItemClickListener;
import com.gsccs.smart.model.CorpEntity;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.InfoHttps;
import com.gsccs.smart.widget.SystemToastDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *   收藏
 */
public class HistoryActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.recycler_list)
    RecyclerView mRecyclerList;
    @Bind(R.id.refresher)
    SwipeRefreshLayout mRefresher;

    List<CorpEntity> corpList = new ArrayList<>();
    AssistAdapter tweetsAdapter = new AssistAdapter(corpList);

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.washcar_activity);
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
        //mRecyclerList.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));
        mRecyclerList.setItemAnimator(new DefaultItemAnimator());
        mRecyclerList.setAdapter(tweetsAdapter);

        refreshData();
    }


    private RecyclerItemClickListener.OnItemClickListener onItemClickListener =
            new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int i = corpList.size() - position - 1;
                    CorpEntity entity = corpList.get(i);
                    //entity.setImgurls(assistList.get(i).getImgurls());

                    //TweetDetailActivity.actionStart(AssistActivity.this, entity);
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
        InfoHttps.getInstance(this).queryWashcarPageList(1,refreshHandler);
    }


    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_WASHCAR_PAGELIST:
                    mRefresher.setRefreshing(false);
                    List<CorpEntity> assistList = (List<CorpEntity>) msg.obj;
                    if (assistList.size() == 0) {
                        SystemToastDialog.showShortToast(HistoryActivity.this, "没有更多符合条件的数据");
                    }
                    SystemToastDialog.showShortToast(HistoryActivity.this, "数据列表："+assistList.size());
                    assistList.clear();
                    assistList.addAll(assistList);
                    tweetsAdapter.notifyDataSetChanged();
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };
}
