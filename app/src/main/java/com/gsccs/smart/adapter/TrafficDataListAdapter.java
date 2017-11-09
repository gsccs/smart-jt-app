package com.gsccs.smart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.model.CorpEntity;
import com.gsccs.smart.model.TrafficDataEntity;
import com.gsccs.smart.utils.Initialize;
import com.gsccs.smart.utils.StringUtils;
import com.gsccs.smart.view.AutoHeightGridView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by x.d zhang on 2016/11/02.
 */
public class TrafficDataListAdapter extends RecyclerView.Adapter<TrafficDataListAdapter.ViewHolder>{

    public static final String TAG = "TrafficDataEntity";

    private List<TrafficDataEntity> dataList;

    public TrafficDataListAdapter(List<TrafficDataEntity> data){
        dataList = data;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tdata_name;
        public TextView tdata_value;
        public TextView tdata_speed;
        public TextView tdata_color;
        public TextView tdata_status;

        public ViewHolder(View itemView){
            super(itemView);
            tdata_name = (TextView) itemView.findViewById(R.id.tdata_name);
            tdata_value = (TextView) itemView.findViewById(R.id.tdata_value);
            tdata_speed = (TextView) itemView.findViewById(R.id.tdata_speed);
            tdata_color =(TextView) itemView.findViewById(R.id.tv_tdval_color);
            tdata_status =(TextView) itemView.findViewById(R.id.tdata_status);

        }
    }

    @Override
    public void onBindViewHolder(final TrafficDataListAdapter.ViewHolder viewHolder, int i){
        final int location = dataList.size() - i - 1;
        //建立起ViewHolder中视图与数据的关联
        viewHolder.tdata_name.setText(dataList.get(location).getName());
        viewHolder.tdata_value.setText(""+dataList.get(location).getValue());
        viewHolder.tdata_speed.setText(""+dataList.get(location).getSpeed());
        viewHolder.tdata_status.setText(""+dataList.get(location).getValuestr());
        float value = dataList.get(location).getValue();
        if(value <= 2.0){
            viewHolder.tdata_color.setBackgroundColor(Color.parseColor("#0d9603"));
        }else if(value > 2.0 && value<=4.0){
            viewHolder.tdata_color.setBackgroundColor(Color.parseColor("#d3f54a"));
        }else if(value > 4.0 && value<=6.0){
            viewHolder.tdata_color.setBackgroundColor(Color.parseColor("#fcec01"));
        }else if(value > 6.0 && value<=8.0){
            viewHolder.tdata_color.setBackgroundColor(Color.parseColor("#f27a1e"));
        }else if(value > 8.0){
            viewHolder.tdata_color.setBackgroundColor(Color.parseColor("#ff715c"));
        }

        //viewHolder.itemView.setId(dataList.get(location).getId());
        //viewHolder.itemView.setAdapter(adapter);
    }

    @Override
    public TrafficDataListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //将布局转化为view并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.traffic_data_item_view,
                viewGroup, false);
        return new TrafficDataListAdapter.ViewHolder(v);
    }

    @Override
    public int getItemCount(){
        return dataList.size();
    }


}
