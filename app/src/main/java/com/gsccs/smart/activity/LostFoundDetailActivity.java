package com.gsccs.smart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.adapter.GridPhotoAdapter;
import com.gsccs.smart.adapter.InfoCommonAdapter;
import com.gsccs.smart.event.RefreshLikeEvent;
import com.gsccs.smart.model.UserEntity;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.InfoHttps;
import com.gsccs.smart.model.LostFoundEntity;
import com.gsccs.smart.model.TweetsEntity;
import com.gsccs.smart.network.NetworkManager;
import com.gsccs.smart.utils.StringUtils;
import com.gsccs.smart.view.AutoHeightGridView;
import com.gsccs.smart.widget.SystemToastDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  失物招领详情
 */
public class LostFoundDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.username)
    TextView nickname;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.gridView)
    AutoHeightGridView gridView;
    @Bind(R.id.btnLike)
    CheckBox btnLike;
    @Bind(R.id.like_count)
    TextView likeCount;
    @Bind(R.id.btnComments)
    ImageButton btnComments;
    @Bind(R.id.comment_count)
    TextView commentCount;
    @Bind(R.id.comments_list)
    ListView commentsList;
    @Bind(R.id.sendmsg)
    ImageButton sendmsg;
    @Bind(R.id.comment)
    EditText msgEdit;

    private LostFoundEntity mEntity;
    private static final String INFO = "lostFoundEntity";

    private List<TweetsEntity> commentList = new ArrayList<>();
    private InfoCommonAdapter mAdapter;
    private boolean mFirstFocus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostfound_detail_activity);
        ButterKnife.bind(this);
        mHeadTextView.setText("失物招领详情");
        mHeadImageView.setOnClickListener(this);
        mEntity = getIntent().getParcelableExtra(INFO);
        Log.d("mEntity","id:"+mEntity.getId());
        InfoHttps.getInstance(this).lostFoundCommentPageList(mEntity.getId(),1,refreshHandler);
        initViews();

        //GlobalUtils.setListViewHeightBasedOnChildren(commentsList);
        mAdapter = new InfoCommonAdapter(LostFoundDetailActivity.this,commentList);
        commentsList.setAdapter(mAdapter);

        List<String> urls = StringUtils.getPicUrlList(mEntity.getImgurls());
        GridPhotoAdapter adapter = new GridPhotoAdapter(SmartApplication.getAppContext(), urls);
        gridView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
//                this.finish();
            default:
                this.finish();
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.sendmsg)
    protected void sendComment(){
        String content = msgEdit.getText().toString();
        if(content.equals("")) {
            SystemToastDialog.showShortToast(LostFoundDetailActivity.this, "评论内容为空!");
            return;
        }

        UserEntity userInfo = SmartApplication.getCurrUser();
        if (null==userInfo){
            Toast.makeText(LostFoundDetailActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LostFoundDetailActivity.this, SignInUpActivity.class);
            intent.putExtra(BaseConst.IS_SIGN_IN,true);
            startActivity(intent);
        }else {
            InfoHttps.getInstance(this).lostCommentAdd(userInfo.getId(), mEntity.getId(),
                    content, refreshHandler);
        }
    }


    private void initViews() {
        Picasso.with(SmartApplication.getAppContext())
                .load(mEntity.getUserlogo())
                .resize(200, 200)
                .centerCrop()
                .into(avatar);
        nickname.setText(mEntity.getUsername());
        likeCount.setText(Integer.toString(mEntity.getUpvote_num()));
        commentCount.setText(Integer.toString(mEntity.getCommentnum()));
        content.setText(mEntity.getContent());
        time.setText(mEntity.getAddtimestr());
        if (mEntity.getUpvote_status() == NetworkManager.UPVOTE_STATUS_NO) {
            btnLike.setChecked(false);
        } else {
            btnLike.setChecked(true);
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int type;
                if(btnLike.isChecked()){
                    type = RefreshLikeEvent.TYPE_UNUPVOTE;
                }else{
                    type = RefreshLikeEvent.TYPE_UPVOTE;
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }


    public static void actionStart(Context context, LostFoundEntity info) {
        Intent intent = new Intent(context, LostFoundDetailActivity.class);
        intent.putExtra(INFO, info);
        context.startActivity(intent);
    }


    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_LOSTFOUND_COMMENT_PAGELIST:
                    List<TweetsEntity> tweets = (List<TweetsEntity>) msg.obj;
                    commentList.clear();
                    commentList.addAll(tweets);
                    mAdapter.notifyDataSetChanged();
                    commentCount.setText(Integer.toString(commentList.size()));
                    break;
                case BaseConst.WHAT_LOSTFOUND_COMMENT_ADD:
                    Log.d("msg.obj",msg.obj.toString());
                    TweetsEntity tweet = (TweetsEntity) msg.obj;
                    if (null != tweet){
                        commentList.add(tweet);
                        mAdapter.notifyDataSetChanged();
                        commentCount.setText(Integer.toString(commentList.size()));
                    }
                    msgEdit.setText("");
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
