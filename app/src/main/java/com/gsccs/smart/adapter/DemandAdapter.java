package com.gsccs.smart.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.model.DemandEntity;
import com.gsccs.smart.utils.StringUtils;
import com.gsccs.smart.view.AutoHeightGridView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by x.d zhang on 2016/12/10.
 */
public class DemandAdapter extends RecyclerView.Adapter<DemandAdapter.ViewHolder>{

    public static final String TAG = "DemandAdapter";

    public OnItemClickListener itemClickListener;

    private List<DemandEntity> dataList;

    public DemandAdapter(List<DemandEntity> data){
        dataList = data;
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener){
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
        public TextView addtime;
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
            addtime = (TextView) itemView.findViewById(R.id.time);
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
        final int location = dataList.size() - i - 1;
        Log.d("DEMANDAdapter",""+location);
        //建立起ViewHolder中视图与数据的关联
        //viewHolder.likeCount.setText(Integer.toString(dataList.get(location).getUpvote_num()));
        viewHolder.commentnum.setText(dataList.get(location).getCommentnum());
        /*viewHolder.content.setText(StringUtils.getEmotionContent(
                SmartApplication.getAppContext(),
                dataList.get(location).getContent()));*/
        viewHolder.content.setText(dataList.get(location).getContent());
        viewHolder.addtime.setText(dataList.get(location).getAddtimestr());

        /*if(dataList.get(location).getUpvote_status() == BaseConst.UPVOTE_STATUS_NO){
            viewHolder.btnLike.setChecked(false);
        }else {
            viewHolder.btnLike.setChecked(true);
        }*/

        Picasso.with(SmartApplication.getAppContext())
                .load(dataList.get(location).getUserlogo())
                .resize(200, 200)
                .centerCrop()
                .into(viewHolder.avatar);
        viewHolder.nickname.setText(dataList.get(location).getUsername());
        List<String> urls = StringUtils.getPicUrlList(dataList.get(location).getImgurls());
        GridPhotoAdapter adapter = new GridPhotoAdapter(SmartApplication.getAppContext(), urls);
        viewHolder.gridView.setAdapter(adapter);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //将布局转化为view并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.demand_item_view,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount(){
        return dataList.size();
    }

}
