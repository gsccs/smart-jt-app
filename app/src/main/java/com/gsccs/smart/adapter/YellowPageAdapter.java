package com.gsccs.smart.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.model.CorpEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by x.d zhang on 2016/12/10.
 */
public class YellowPageAdapter extends RecyclerView.Adapter<YellowPageAdapter.ViewHolder>{

    public static final String TAG = "YellowPage";

    public OnItemClickListener itemClickListener;
    private List<CorpEntity> dataList;

    public YellowPageAdapter(List<CorpEntity> data){
        dataList = data;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView name;
        public TextView phone;
        public TextView address;
        public CircleImageView logo;

        public ViewHolder(View itemView){
            super(itemView);
            phone = (TextView) itemView.findViewById(R.id.phone);
            logo = (CircleImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.username);
            address = (TextView) itemView.findViewById(R.id.address);
            //phone.setOnClickListener(this);
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
        //建立起ViewHolder中视图与数据的关联

        Picasso.with(SmartApplication.getAppContext())
                .load(dataList.get(location).getLogo())
                .resize(200, 200)
                .centerCrop()
                .into(viewHolder.logo);
        viewHolder.name.setText(dataList.get(location).getTitle());
        viewHolder.address.setText(dataList.get(location).getAddress());
        viewHolder.phone.setText(dataList.get(location).getPhone());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //将布局转化为view并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.yellowpage_item_view,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount(){
        return dataList.size();
    }



}
