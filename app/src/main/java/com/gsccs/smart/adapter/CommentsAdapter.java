package com.gsccs.smart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.model.TweetsEntity;
import com.gsccs.smart.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by x.d zhang on 2016/12/13.
 */
public class CommentsAdapter extends BaseAdapter {

    public static final String TAG = "CommentsAdapter";

    private List<TweetsEntity> dataList;
    private LayoutInflater inflater;
    private Context context;

    public CommentsAdapter(Context context, List<TweetsEntity> dataList){
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount(){
        return dataList.size();
    }

    @Override
    public Object getItem(int position){
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
            final ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.comment_list_item, null);
                holder.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
                holder.content = (TextView) convertView.findViewById(R.id.comment_content);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.nickname = (TextView) convertView.findViewById(R.id.nickname);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            TweetsEntity comment = dataList.get(position);
            if(null != comment){

                if (null!=comment.getUserlogo()){
                    Picasso.with(context)
                            .load(comment.getUserlogo())
                            .resize(200, 200)
                            .centerCrop()
                            .into(holder.avatar);
                }

                holder.nickname.setText(comment.getUsername());
                holder.time.setText(comment.getAddtimestr());
                holder.content.setText(comment.getContent());
            }

            return convertView;
    }

    public final class ViewHolder{
        public CircleImageView avatar;
        public TextView content;
        public TextView time;
        public TextView nickname;
    }
}
