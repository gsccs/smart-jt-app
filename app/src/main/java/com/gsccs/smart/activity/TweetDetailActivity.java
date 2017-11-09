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
import com.gsccs.smart.adapter.CommentsAdapter;
import com.gsccs.smart.adapter.GridPhotoAdapter;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.TweetHttps;
import com.gsccs.smart.model.TweetsEntity;
import com.gsccs.smart.event.RefreshLikeEvent;
import com.gsccs.smart.fragment.EmojiFragment;
import com.gsccs.smart.network.NetworkManager;
import com.gsccs.smart.utils.StringUtils;
import com.gsccs.smart.view.AutoHeightGridView;
import com.gsccs.smart.widget.EnterEmojiLayout;
import com.gsccs.smart.widget.EnterLayout;
import com.gsccs.smart.widget.SystemToastDialog;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 */
public class TweetDetailActivity extends BaseActivity implements View.OnClickListener {

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

    private TweetsEntity tweetsEntity;
    private static final String INFO = "tweetsEntity";

    private List<TweetsEntity> commentList = new ArrayList<>();
    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_detail_activity);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mHeadTextView.setText("随手拍详情");
        mHeadImageView.setOnClickListener(this);

        tweetsEntity = getIntent().getParcelableExtra(INFO);

        Log.d("tweetsEntity","id:"+tweetsEntity.getId());
        Log.d("tweetsEntity","getUsername:"+tweetsEntity.getUsername());
        TweetHttps.getInstance(this).tweetCommentPageList(tweetsEntity.getId(),1,refreshHandler);
        initViews();

        //GlobalUtils.setListViewHeightBasedOnChildren(commentsList);
        adapter = new CommentsAdapter(TweetDetailActivity.this, commentList);
        commentsList.setAdapter(adapter);

        List<String> urls = StringUtils.getPicUrlList(tweetsEntity.getImgurls());
        GridPhotoAdapter adapter = new GridPhotoAdapter(SmartApplication.getAppContext(), urls);
        gridView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
            //  this.finish();
            default:
                this.finish();
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.sendmsg)
    protected void sendComment(){
        String content = msgEdit.getText().toString();
        if(content.equals("")) {
            SystemToastDialog.showShortToast(TweetDetailActivity.this, "评论内容为空!");
            return;
        }

        if (null== SmartApplication.getCurrUser()){
            Toast.makeText(TweetDetailActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TweetDetailActivity.this, SignInUpActivity.class);
            intent.putExtra(BaseConst.IS_SIGN_IN,true);
            startActivity(intent);
        }else{
            int userid = SmartApplication.getCurrUser().getId();
            TweetHttps.getInstance(this).sendComment(userid, tweetsEntity.getId(), content,refreshHandler);
        }
        msgEdit.setText("");
    }

    private void initViews() {
        Picasso.with(SmartApplication.getAppContext())
                .load(tweetsEntity.getUserlogo())
                .resize(200, 200)
                .centerCrop()
                .into(avatar);
        nickname.setText(tweetsEntity.getUsername());
        likeCount.setText(tweetsEntity.getTypestr());
        commentCount.setText(Integer.toString(tweetsEntity.getCommentnum()));
        content.setText(StringUtils.getEmotionContent(
                SmartApplication.getAppContext(),
                tweetsEntity.getContent()));
        time.setText(tweetsEntity.getAddtimestr());
        if (tweetsEntity.getUpvote_status() == NetworkManager.UPVOTE_STATUS_NO) {
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

        //initEnter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final RefreshLikeEvent event){

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public static void actionStart(Context context, TweetsEntity info) {
        Intent intent = new Intent(context, TweetDetailActivity.class);
        intent.putExtra(INFO, info);
        context.startActivity(intent);
    }


    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_TWEET_COMMENT_PAGELIST:
                    List<TweetsEntity> tweets = (List<TweetsEntity>) msg.obj;
                    commentList.clear();
                    commentList.addAll(tweets);
                    adapter.notifyDataSetChanged();
                    commentCount.setText(Integer.toString(commentList.size()));
                    break;
                case BaseConst.WHAT_TWEET_COMMENT_ADD:
                    TweetsEntity tweet = (TweetsEntity) msg.obj;
                    commentList.add(tweet);
                    adapter.notifyDataSetChanged();
                    commentCount.setText(Integer.toString(commentList.size()));
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
