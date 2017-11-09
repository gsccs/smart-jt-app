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
import android.widget.Toast;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.adapter.TweetsAdapter;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.TweetHttps;
import com.gsccs.smart.listener.RecyclerItemClickListener;
import com.gsccs.smart.model.TweetsEntity;
import com.gsccs.smart.widget.SystemToastDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TweetActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.recycler_list)
    RecyclerView tweetsRecyclerList;
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;
    @Bind(R.id.add_tweets_fab)
    FloatingActionButton addTweetsFab;

    List<TweetsEntity> tweetsList = new ArrayList<>();
    TweetsAdapter mAdapter = new TweetsAdapter(tweetsList);

    private  int page = 1;
    public TweetActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweets_activity);
        ButterKnife.bind(this);

        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        if (null==titleName || titleName.equals("")){
            mHeadTextView.setText(R.string.tweet_title);
        }else{
            mHeadTextView.setText(titleName);
        }
        mHeadImageView.setOnClickListener(this);

        addTweetsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null== SmartApplication.getCurrUser()){
                    Intent intent = new Intent(TweetActivity.this, SignInUpActivity.class);
                    intent.putExtra(BaseConst.IS_SIGN_IN,true);
                    startActivity(intent);
                }else{
                    TweetFormActivity.actionStart(TweetActivity.this);
                }
            }
        });

        refresher.setColorSchemeResources(R.color.colorPrimary);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        tweetsRecyclerList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tweetsRecyclerList.setLayoutManager(layoutManager);
        tweetsRecyclerList.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));
        tweetsRecyclerList.setItemAnimator(new DefaultItemAnimator());
        tweetsRecyclerList.setAdapter(mAdapter);

        refreshData();

        //return view;
    }

    private void refreshData() {
        refresher.setRefreshing(true);
        TweetHttps.getInstance(this).queryTweetPageList(page,refreshHandler);
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener =
            new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int i = tweetsList.size() - position - 1;
                    TweetsEntity entity = tweetsList.get(i);
                    entity.setImgurls(tweetsList.get(i).getImgurls());
                    TweetDetailActivity.actionStart(TweetActivity.this, entity);
                }
            };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }


    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_TWEET_PAGELIST:
                    refresher.setRefreshing(false);
                    List<TweetsEntity> tweets = (List<TweetsEntity>) msg.obj;
                    if (tweets.size() == 0) {
                        SystemToastDialog.showShortToast(TweetActivity.this, "没有更多数据");
                    }

                    tweetsList.clear();
                    tweetsList.addAll(tweets);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
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
}
