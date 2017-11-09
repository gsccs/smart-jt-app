package com.gsccs.smart.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.adapter.ArticleListAdapter;
import com.gsccs.smart.listener.RecyclerItemClickListener;
import com.gsccs.smart.model.ArticleEntity;
import com.gsccs.smart.model.UserEntity;
import com.gsccs.smart.network.ArticleHttps;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.view.ArticleRecyclerView;
import com.gsccs.smart.widget.SystemToastDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *   收藏
 */
public class CollectActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.article_list_view)
    ArticleRecyclerView mArticleRecyclerView;   //上拉加载
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;      //下拉刷新

    ArticleListAdapter mArticleAdapter;
    List<ArticleEntity> dataList = new ArrayList<>();
    private int page = 0;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.collect_activity);
        ButterKnife.bind(this);

        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        mHeadTextView.setText(titleName);
        mHeadImageView.setOnClickListener(this);

        //保持固定的大小
        mArticleRecyclerView.setHasFixedSize(true);

        mArticleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArticleRecyclerView.setAutoLoadMoreEnable(true);
        mArticleRecyclerView.setLoadMoreListener(new ArticleRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mArticleRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(false);
                        //isupdate = false;
                        mSwipeRefreshLayout.setRefreshing(false);
                        //mArticleAdapter.addDatas(DummyContent.generyData(++page));
                        mArticleRecyclerView.notifyMoreFinish(true);
                    }
                }, 1000);
            }
        });

        mArticleAdapter = new ArticleListAdapter(this);
        mArticleAdapter.setOnItemClickListener(new ArticleListAdapter.onRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View v, int id) {
                Intent intent = new Intent(CollectActivity.this, ArticleActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        mArticleRecyclerView.setAdapter(mArticleAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                page = 0;
                //isupdate = true;
                loadData(true);
                //mArticleAdapter.setData(DummyContent.generyData(page));
                mArticleRecyclerView.setAutoLoadMoreEnable(true);
                mArticleAdapter.notifyDataSetChanged();
            }
        });
        loadData(true);
    }


    private RecyclerItemClickListener.OnItemClickListener onItemClickListener =
            new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int i = dataList.size() - position - 1;
                    ArticleEntity entity = dataList.get(i);
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

    private void loadData(boolean isrefresh) {
        if (isrefresh){
            page = 0;
        }
        page = page+1;
        UserEntity user = SmartApplication.getCurrUser();
        ArticleHttps.getInstance(this).articleCollectPageList(user.getId(),1,refreshHandler);
    }

    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_ARTICLE_COLLECT_PAGELIST:
                    List<ArticleEntity> list = (List<ArticleEntity>) msg.obj;
                    SystemToastDialog.showShortToast(CollectActivity.this, "数据列表："+list.size());
                    mArticleAdapter.addList(list);
                    mArticleRecyclerView.notifyMoreFinish(true);
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };
}
