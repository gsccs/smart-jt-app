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
import com.gsccs.smart.model.CorpEntity;
import com.gsccs.smart.utils.StringUtils;
import com.gsccs.smart.view.AutoHeightGridView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by x.d zhang on 2016/12/10.
 */
public class DomesticAdapter extends RecyclerView.Adapter<DomesticAdapter.ViewHolder>{

    public static final String TAG = "Domestic";

    public OnItemClickListener itemClickListener;

    private List<CorpEntity> dataList;

    public DomesticAdapter(List<CorpEntity> data){
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
        public TextView mTitle;
        public TextView mAddress;
        public TextView mPhone;
        public TextView content;
        public TextView mAddtime;
        public CircleImageView mLogo;
        public AutoHeightGridView gridView;

        public ViewHolder(View itemView){
            super(itemView);
            mAddress = (TextView) itemView.findViewById(R.id.tv_address);
            mPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            content = (TextView) itemView.findViewById(R.id.content);
            mLogo = (CircleImageView) itemView.findViewById(R.id.iv_logo);
            btnLike = (CheckBox) itemView.findViewById(R.id.btnLike);
            btnComments = (ImageButton) itemView.findViewById(R.id.btnComments);
            gridView = (AutoHeightGridView) itemView.findViewById(R.id.gridView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mAddtime = (TextView) itemView.findViewById(R.id.tv_addtime);
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
        viewHolder.mAddress.setText(dataList.get(location).getAddress());
        viewHolder.mPhone.setText(dataList.get(location).getPhone());
        viewHolder.content.setText(dataList.get(location).getContent());
        viewHolder.mAddtime.setText(dataList.get(location).getAddtimestr());

        Picasso.with(SmartApplication.getAppContext())
                .load(dataList.get(location).getLogo())
                .resize(200, 200)
                .centerCrop()
                .into(viewHolder.mLogo);
        viewHolder.mTitle.setText(dataList.get(location).getTitle());
        List<String> urls = StringUtils.getPicUrlList(dataList.get(location).getImgurls());
        GridPhotoAdapter adapter = new GridPhotoAdapter(SmartApplication.getAppContext(), urls);
        viewHolder.gridView.setAdapter(adapter);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        //将布局转化为view并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.domestic_item_view,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount(){
        return dataList.size();
    }

}
