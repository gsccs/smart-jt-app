package com.gsccs.smart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.model.InfoEntity;
import com.gsccs.smart.utils.IconUtil;
import com.gsccs.smart.utils.StringUtils;
import com.gsccs.smart.view.AutoHeightGridView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by x.d zhang on 2016/12/10.
 */
public class InfoAdapter extends BaseAdapter{

    public static final String TAG = "MINEINFO";

    private Context context;
    private List<InfoEntity> data;
    private LayoutInflater inflater;

    public InfoAdapter(Context context, List<InfoEntity> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holoder = null;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.mine_info_item,null);
            holoder = new ViewHolder();
            holoder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holoder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            holoder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holoder);
        }
        else {
            holoder = (ViewHolder) convertView.getTag();
        }
        InfoEntity item = data.get(position);
        Integer channel = item.getChannel();
        if (null != channel && channel==1){
            holoder.ivIcon.setImageBitmap(IconUtil.getBitmapFromResource(R.drawable.icon_lostfound));
        }
        if (null != channel && channel==2){
            holoder.ivIcon.setImageBitmap(IconUtil.getBitmapFromResource(R.drawable.icon_gongqiu));
        }
        holoder.tvTime.setText(item.getAddtimestr());
        holoder.tvContent.setText(item.getContent());
        return convertView;
    }

    public class ViewHolder{
        TextView tvContent;
        TextView tvTime;
        ImageView ivIcon;
    }


}
