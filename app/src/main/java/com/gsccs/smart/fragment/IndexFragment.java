package com.gsccs.smart.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.weather.LocalWeatherLive;
import com.gsccs.smart.R;
import com.gsccs.smart.activity.MainActivity;
import com.gsccs.smart.activity.NoticeActivity;
import com.gsccs.smart.activity.TrafficDataActivity;
import com.gsccs.smart.activity.ArticleActivity;
import com.gsccs.smart.adapter.ArticleListAdapter;
import com.gsccs.smart.model.ArticleEntity;
import com.gsccs.smart.model.ErrorStatus;
import com.gsccs.smart.model.TrafficDataEntity;
import com.gsccs.smart.network.ArticleHttps;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.view.ArticleRecyclerView;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  首页
 *  Created by x.d zhang on 2016/10/29.
 */
public class IndexFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    private MainActivity mainActivity;

    //天气预报
    @Bind(R.id.local_weather)
    TextView weatherText;
    //交通指数
    @Bind(R.id.local_traffic)
    TextView trafficText;

    @Bind(R.id.radio_list_view)
    LinearLayout radioListView;
    @Bind(R.id.marqueeView)
    MarqueeView marqueeView;
    @Bind(R.id.article_list_view)
    ArticleRecyclerView mArticleRecyclerView;   //上拉加载
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;      //下拉刷新

    private List<ArticleEntity> articles = new ArrayList<ArticleEntity>();
    private List<ArticleEntity> notices = new ArrayList<ArticleEntity>();
    private ArticleListAdapter mArticleAdapter;
    private int page = 0;
    private int pageSize = 5;
    private boolean noMoreData = false;
    //更新操作
    private boolean isupdate = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_index, null);
        ButterKnife.bind(this, view);

        mainActivity = (MainActivity) getActivity();
        //mainActivity.setOnGetWeatherMessage(this);
        //保持固定的大小
        mArticleRecyclerView.setHasFixedSize(true);

        mArticleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArticleRecyclerView.setAutoLoadMoreEnable(true);
        mArticleRecyclerView.setLoadMoreListener(new ArticleRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mArticleRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isupdate = false;
                        loadArticleList();
                        swipeRefreshLayout.setRefreshing(false);
                        //mArticleAdapter.addDatas(DummyContent.generyData(++page));
                        mArticleRecyclerView.notifyMoreFinish(true);
                    }
                }, 1000);
            }
        });

        mArticleAdapter = new ArticleListAdapter(getActivity());
        mArticleAdapter.setOnItemClickListener(new ArticleListAdapter.onRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View v, int id) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        mArticleRecyclerView.setAdapter(mArticleAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                page = 0;
                isupdate = true;
                loadArticleList();
                //mArticleAdapter.setData(DummyContent.generyData(page));
                mArticleRecyclerView.setAutoLoadMoreEnable(true);
                mArticleAdapter.notifyDataSetChanged();
            }
        });

        ArticleHttps.getInstance(IndexFragment.this.getActivity()).queryAppIndexInfo(refreshHandler);
        loadArticleList();
        radioListView.setOnClickListener(this);
        trafficText.setOnClickListener(this);
        return view;
    }

    private void loadNoticeList() {
        List<String> noticeTitles = new ArrayList<>();
        if (null != notices && notices.size()>0){
            for(ArticleEntity article:notices){
                noticeTitles.add(article.getTitle());
            }
        }
        //通知公告适配器
        marqueeView.startWithList(noticeTitles);
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Toast.makeText(getActivity(), String.valueOf(marqueeView.getPosition()) + ". " + textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("ArticleEntity","click"+i);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radio_list_view:
                Log.d("Radio","click");
                Intent speechIntent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(speechIntent);
            break;
            case R.id.local_traffic:
                Intent trafficIntent = new Intent(getActivity(), TrafficDataActivity.class);
                startActivity(trafficIntent);
            default:

            break;
        }
    }

    private void loadArticleList() {
        if (noMoreData){
            Toast.makeText(getActivity(), "无更多数据", Toast.LENGTH_SHORT).show();
            return;
        }
        page = page+1;
        ArticleHttps.getInstance(IndexFragment.this.getActivity()).queryArticlePageList(page,pageSize, refreshHandler);
    }

    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_APP_INDEX:
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) msg.obj;
                    notices = (ArrayList<ArticleEntity>) resultMap.get("noticeList");
                    TrafficDataEntity trafficData = (TrafficDataEntity)resultMap.get("trafficData");
                    trafficText.setText(trafficData.getValue()+" | "+trafficData.getValuestr());
                    loadNoticeList();
                    //SystemToastDialog.showShortToast(getActivity(), "数据列表："+articles.size());
                    //mArticleAdapter.setList(articles);
                    //mArticleRecyclerView.notifyMoreFinish(true);
                    //mArticleAdapter.notifyDataSetChanged();
                    break;
                case BaseConst.WHAT_ARTICLE_PAGELIST:
                    articles = (List<ArticleEntity>) msg.obj;
                    if (null==articles || articles.size()<pageSize){
                        noMoreData = true;
                    }
                    if (isupdate){
                        mArticleAdapter.setList(articles);
                    }else{
                        mArticleAdapter.addList(articles);
                    }
                    mArticleRecyclerView.notifyMoreFinish(true);
                    mArticleAdapter.notifyDataSetChanged();
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };


}
