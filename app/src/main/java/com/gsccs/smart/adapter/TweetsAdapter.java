package com.gsccs.smart.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.db.DataPresenter;
import com.gsccs.smart.model.UserEntity;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.model.TweetsEntity;
import com.gsccs.smart.utils.StringUtils;
import com.gsccs.smart.view.AutoHeightGridView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by x.d zhang on 2016/12/10.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    public static final String TAG = "TWEET";

    public OnItemClickListener itemClickListener;

    private List<TweetsEntity> dataList;

    public TweetsAdapter(List<TweetsEntity> data){
        dataList = data;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageButton btnComments;
        public TextView nickname;
        public TextView mType;
        public TextView commentCount;
        public TextView tweetContent;
        public TextView time;
        public CircleImageView avatar;
        public AutoHeightGridView gridView;

        public ViewHolder(View itemView){
            super(itemView);
            mType = (TextView) itemView.findViewById(R.id.tv_type);
            commentCount = (TextView) itemView.findViewById(R.id.comment_count);
            tweetContent = (TextView) itemView.findViewById(R.id.content);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);

            btnComments = (ImageButton) itemView.findViewById(R.id.btnComments);
            gridView = (AutoHeightGridView) itemView.findViewById(R.id.gridView);
            nickname = (TextView) itemView.findViewById(R.id.username);
            time = (TextView) itemView.findViewById(R.id.time);
        }

        //通过接口回调来实现点击事件
        @Override
        public void onClick(View v){
            if(itemClickListener != null){
                itemClickListener.onItemClick(v, getPosition());
            }
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i){
        final int index = dataList.size() - i - 1;
        //建立起ViewHolder中视图与数据的关联
        TweetsEntity entity = dataList.get(index);
        if (null==entity.getTypestr() || entity.getTypestr()==""){
            viewHolder.mType.setVisibility(View.GONE);
        }else{
            viewHolder.mType.setText(entity.getTypestr());
        }

        viewHolder.commentCount.setText(Integer.toString(entity.getCommentnum()));
        viewHolder.tweetContent.setText(entity.getContent());
        viewHolder.time.setText(entity.getAddtimestr());

        Picasso.with(SmartApplication.getAppContext())
                .load(entity.getUserlogo())
                .resize(200, 200)
                .centerCrop()
                .into(viewHolder.avatar);
        viewHolder.nickname.setText(entity.getUsername());
        List<String> urls = StringUtils.getPicUrlList(entity.getImgurls());
        GridPhotoAdapter adapter = new GridPhotoAdapter(SmartApplication.getAppContext(), urls);
        viewHolder.gridView.setAdapter(adapter);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //将布局转化为view并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tweets_item,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount(){
        return dataList.size();
    }

}
