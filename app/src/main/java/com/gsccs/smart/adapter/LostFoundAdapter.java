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
import com.gsccs.smart.model.LostFoundEntity;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.utils.StringUtils;
import com.gsccs.smart.view.AutoHeightGridView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 *
 * Created by x.d zhang on 2016/12/10.
 */
public class LostFoundAdapter extends RecyclerView.Adapter<LostFoundAdapter.ViewHolder>{

    public static final String TAG = "LostFoundAdapter";

    public LostFoundAdapter.OnItemClickListener itemClickListener;

    private List<LostFoundEntity> dataList;

    public LostFoundAdapter(List<LostFoundEntity> data){
        dataList = data;
    }

    public void setOnItemClickListener(LostFoundAdapter.OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CheckBox btnLike;
        public ImageButton btnComments;
        public TextView nickname;
        public TextView likeCount;
        public TextView commentnum;
        public TextView content;
        public TextView time;
        public CircleImageView avatar;
        public AutoHeightGridView gridView;

        public ViewHolder(View itemView){
            super(itemView);
            likeCount = (TextView) itemView.findViewById(R.id.like_count);
            commentnum = (TextView) itemView.findViewById(R.id.comment_count);
            content = (TextView) itemView.findViewById(R.id.content);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            btnLike = (CheckBox) itemView.findViewById(R.id.btnLike);
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
    public void onBindViewHolder(final LostFoundAdapter.ViewHolder viewHolder, int i){
        final int index = dataList.size() - i - 1;
        //建立起ViewHolder中视图与数据的关联
        LostFoundEntity entity = dataList.get(index);
        //viewHolder.likeCount.setText(Integer.toString(dataList.get(location).getUpvote_num()));
        viewHolder.commentnum.setText(""+entity.getCommentnum());
        viewHolder.content.setText(entity.getContent());
        viewHolder.time.setText(entity.getAddtimestr());

        if (null !=entity.getUserlogo() && entity.getUserlogo()!=""){
            Picasso.with(SmartApplication.getAppContext())
                    .load(entity.getUserlogo())
                    .resize(200, 200)
                    .centerCrop()
                    .into(viewHolder.avatar);
        }
        viewHolder.nickname.setText(entity.getUsername());
        List<String> urls = StringUtils.getPicUrlList(entity.getImgurls());
        GridPhotoAdapter adapter = new GridPhotoAdapter(SmartApplication.getAppContext(), urls);
        viewHolder.gridView.setAdapter(adapter);
    }

    @Override
    public LostFoundAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //将布局转化为view并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lostfound_item_view,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount(){
        return dataList.size();
    }

}
