package com.gsccs.smart.activity;

import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  道路救援
 */
public class AssistActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.recycler_list)
    RecyclerView mRecyclerList;
    @Bind(R.id.refresher)
    SwipeRefreshLayout mRefresher;


    List<CorpEntity> assistList = new ArrayList<>();
    AssistAdapter assistAdapter = new AssistAdapter(assistList);

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.roadassist_activity);
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
        mRecyclerList.setAdapter(assistAdapter);

        refreshData();
    }


    private RecyclerItemClickListener.OnItemClickListener onItemClickListener =
            new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int i = assistList.size() - position - 1;
                    CorpEntity entity = assistList.get(i);
                    switch (view.getId()){
                        case R.id.phone:
                            String phonenum = entity.getPhone();
                            if (null==phonenum || phonenum==""){
                                return;
                            }
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + phonenum);
                            intent.setData(data);
                            startActivity(intent);
                            break;
                    }
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
        ButterKnife.unbind(this);
    }

    private void refreshData() {
        mRefresher.setRefreshing(true);
        InfoHttps.getInstance(this).queryAssistPageList(0d,0d,1,refreshHandler);
    }

    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_ASSIST_PAGELIST:
                    mRefresher.setRefreshing(false);
                    List<CorpEntity> list = (List<CorpEntity>) msg.obj;
                    if (list.size() == 0) {
                        SystemToastDialog.showShortToast(AssistActivity.this, "没有更多符合条件的数据");
                    }
                    SystemToastDialog.showShortToast(AssistActivity.this, "数据列表："+list.size());
                    assistList.clear();
                    assistList.addAll(list);
                    assistAdapter.notifyDataSetChanged();
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };
}
