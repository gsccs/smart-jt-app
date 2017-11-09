package com.gsccs.smart.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.adapter.InfoAdapter;
import com.gsccs.smart.model.InfoEntity;
import com.gsccs.smart.model.UserEntity;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.InfoHttps;
import com.gsccs.smart.widget.SystemToastDialog;
import com.hankkin.library.RefreshSwipeMenuListView;
import com.hankkin.library.SwipeMenu;
import com.hankkin.library.SwipeMenuCreator;
import com.hankkin.library.SwipeMenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *   发布的信息
 */
public class InfoActivity extends AppCompatActivity implements View.OnClickListener,RefreshSwipeMenuListView.OnRefreshListener{

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.refresher)
    RefreshSwipeMenuListView mRefresher;

    List<InfoEntity> infoList = new ArrayList<>();
    InfoAdapter mAdapter;
    private int po;
    private int currPage = 0;
    private int totalPage = 1;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.mine_info_activity);
        ButterKnife.bind(this);

        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        mHeadTextView.setText(titleName);
        mHeadImageView.setOnClickListener(this);


        mRefresher.setListViewMode(RefreshSwipeMenuListView.HEADER);
        mRefresher.setOnRefreshListener(this);

        refreshData();


        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                /*// 创建滑动选项
                SwipeMenuItem rejectItem = new SwipeMenuItem(getApplicationContext());
                // 设置选项背景
                rejectItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                // 设置选项宽度
                rejectItem.setWidth(dp2px(80,getApplicationContext()));
                // 设置选项标题
                rejectItem.setTitle("置顶");
                // 设置选项标题
                rejectItem.setTitleSize(16);
                // 设置选项标题颜色
                rejectItem.setTitleColor(Color.WHITE);
                // 添加选项
                menu.addMenuItem(rejectItem);*/

                // 创建删除选项
                SwipeMenuItem argeeItem = new SwipeMenuItem(getApplicationContext());
                argeeItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                argeeItem.setWidth(dp2px(80, getApplicationContext()));
                argeeItem.setTitle("删除");
                argeeItem.setTitleSize(16);
                argeeItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(argeeItem);
            }
        };

        mRefresher.setMenuCreator(creator);

        mRefresher.setOnMenuItemClickListener(new RefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0: //删除选项
                        InfoEntity entity = (InfoEntity) mAdapter.getItem(position);
                        Log.d("INFO",entity.getContent());
                        infoDel(entity.getId(),entity.getChannel());
                        del(position,mRefresher.getChildAt(position+1-mRefresher.getFirstVisiblePosition()));
                        break;
                }
            }
        });

    }

    private void infoDel(Integer id,Integer channel){
        InfoHttps.getInstance(this).infoDel(id,channel,refreshHandler);
    }

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
        //ButterKnife.unbind(this);
    }

    private void refreshData() {
        UserEntity currUser = SmartApplication.getCurrUser();
        if (null==currUser){
            Toast.makeText(InfoActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(InfoActivity.this, SignInUpActivity.class);
            intent.putExtra(BaseConst.IS_SIGN_IN,true);
            startActivity(intent);
        }else {
            InfoHttps.getInstance(this).queryMyInfoPageList(currUser.getId(),currPage, refreshHandler);
        }
    }


    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_MYINFO_PAGELIST:
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) msg.obj;
                    List<InfoEntity> list = (ArrayList<InfoEntity>) resultMap.get("list");
                    totalPage = (Integer) resultMap.get("total_quantity");
                    SystemToastDialog.showShortToast(InfoActivity.this, "数据列表："+list.size());
                    infoList.addAll(list);
                    mAdapter = new InfoAdapter(InfoActivity.this,infoList);
                    mRefresher.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    break;
                case BaseConst.WHAT_MYINFO_DEL:
                    SystemToastDialog.showShortToast(InfoActivity.this, "删除成功!");
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };


    /**
     * 删除item动画
     * @param index
     * @param v
     */
    private void del(final int index, View v){
        final Animation animation = (Animation) AnimationUtils.loadAnimation(v.getContext(), R.anim.list_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                infoList.remove(index);
                po = index;
                mAdapter.notifyDataSetChanged();
                animation.cancel();
            }
        });
        v.startAnimation(animation);
    }


    @Override
    public void onRefresh() {
        currPage = 1;
        mRefresher.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresher.complete();
                Toast.makeText(InfoActivity.this,"已完成",Toast.LENGTH_SHORT).show();
            }
        },2000);
    }

    @Override
    public void onLoadMore() {
        if (currPage>=totalPage){
            Toast.makeText(InfoActivity.this,"无更多数据",Toast.LENGTH_SHORT).show();
            return;
        }
        mRefresher.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresher.complete();
                Toast.makeText(InfoActivity.this,"已完成",Toast.LENGTH_SHORT).show();
            }
        },2000);

    }

    public  int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
