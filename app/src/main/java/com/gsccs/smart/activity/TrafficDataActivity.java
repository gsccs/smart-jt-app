package com.gsccs.smart.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.adapter.TrafficDataAdapter;
import com.gsccs.smart.fragment.TDataFragment;
import com.gsccs.smart.fragment.TDataMainFragment;
import com.gsccs.smart.model.TrafficDataEntity;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.TrafficHttps;
import com.gxz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *   交通路况指数
 */
public class TrafficDataActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.traffic_data_tab)
    PagerSlidingTabStrip mPagerSlidingTabStrip;
    @Bind(R.id.traffic_data_pager)
    ViewPager mPager;

    ArrayList<String> titles=new ArrayList<>();
    ArrayList<Fragment> fragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_data);
        ButterKnife.bind(this);

        mHeadTextView.setText(R.string.traffic_data);
        mHeadImageView.setOnClickListener(this);

        titles.add("全市");
        titles.add("商圈");
        titles.add("干线");
        titles.add("交叉口");

        TrafficHttps.getInstance(this).queryTrafficData(refreshHandler);

       /* fragments.add(TDataMainFragment.getInstance(new Bundle()));
        fragments.add(TDataFragment.getInstance(new Bundle()));
        fragments.add(TDataFragment.getInstance(new Bundle()));
        fragments.add(TDataFragment.getInstance(new Bundle()));*/




    }


    private void initViewPage(){
        mPagerSlidingTabStrip.setViewPager(mPager);


        // 设置Tab是自动填充满屏幕的
        mPagerSlidingTabStrip.setShouldExpand(true);

        // 设置Tab的分割线的颜色
        mPagerSlidingTabStrip.setDividerColor(getResources().getColor(R.color.divider_1));
        // 设置分割线的上下的间距,传入的是dp
        mPagerSlidingTabStrip.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        mPagerSlidingTabStrip.setUnderlineHeight(1);
        //设置Tab底部线的颜色
        mPagerSlidingTabStrip.setUnderlineColor(getResources().getColor(R.color.divider_2));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mPagerSlidingTabStrip.setIndicatorHeight(4);
        // 设置Tab Indicator的颜色
        mPagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.divider_2));

        // 设置Tab标题文字的大小,传入的是sp
        mPagerSlidingTabStrip.setTextSize(16);
        // 设置选中Tab文字的颜色
        mPagerSlidingTabStrip.setSelectedTextColor(getResources().getColor(R.color.primary));
        //设置正常Tab文字的颜色
        mPagerSlidingTabStrip.setTextColor(getResources().getColor(R.color.black));
        //设置Tab文字的左右间距,传入的是dp
        mPagerSlidingTabStrip.setTabPaddingLeftRight(24);

        //设置点击每个Tab时的背景色
        mPagerSlidingTabStrip.setTabBackground(R.drawable.traffic_tab_bg);

        //是否支持动画渐变(颜色渐变和文字大小渐变)
        mPagerSlidingTabStrip.setFadeEnabled(false);
        // 设置最大缩放,是正常状态的0.3倍
        mPagerSlidingTabStrip.setZoomMax(0.3F);

        //这是当点击tab时内容区域Viewpager是否是左右滑动,默认是true
        mPagerSlidingTabStrip.setSmoothScrollWhenClickTab(true);
    }

    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_TRAFFIC_DATA:
                    //mRefresher.setRefreshing(false);
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) msg.obj;
                    TrafficDataEntity trafficData = (TrafficDataEntity) resultMap.get("qsData");

                    ArrayList<TrafficDataEntity> sqDataList= (ArrayList<TrafficDataEntity>) resultMap.get("sqDataList");
                    ArrayList<TrafficDataEntity>  gxDataList = (ArrayList<TrafficDataEntity>) resultMap.get("gxDataList");
                    ArrayList<TrafficDataEntity>  jckDataList = (ArrayList<TrafficDataEntity>) resultMap.get("jckDataList");
                    //SystemToastDialog.showShortToast(TrafficDataActivity.this, "数据列表："+list.size());

                    fragments.clear();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "全市");
                    bundle.putParcelable("data",trafficData);
                    fragments.add(TDataMainFragment.getInstance(bundle));
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("data",sqDataList);
                    fragments.add(TDataFragment.getInstance(bundle));
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("data",gxDataList);
                    fragments.add(TDataFragment.getInstance(bundle));
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("data",jckDataList);
                    fragments.add(TDataFragment.getInstance(bundle));

                    mPager.setAdapter(new TrafficDataAdapter(getSupportFragmentManager(), titles, fragments));
                    initViewPage();
                    mPager.setCurrentItem(0);
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case 1:
                finish();
                break;
            case R.id.back_ico:
                finish();
                break;
        }
    }

}
