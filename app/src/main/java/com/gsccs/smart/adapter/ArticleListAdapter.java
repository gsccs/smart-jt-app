package com.gsccs.smart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.model.ArticleEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by x.d zhang on 2016/11/06.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    /**
     * item 类型
     */
    public final static int TYPE_HEADER = -1;       //头部--支持头部增加一个headerView
    public final static int TYPE_FOOTER = -2;       //底部--往往是loading_more
    private boolean mIsHeaderEnable;                //是否允许更新
    private boolean mIsFooterEnable = false;        //是否允许加载更多

    private int mHeaderResId;
    private LayoutInflater mLayoutInflater;
    private List<ArticleEntity> mList = new ArrayList<ArticleEntity>();

    private onRecyclerViewItemClickListener itemClickListener = null;

    public ArticleListAdapter(Context context){
        mIsHeaderEnable = false;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<ArticleEntity> list){
        mList.addAll(list);
    }

    public void setList(List<ArticleEntity> list){
        mList.clear();
        mList = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case TYPE_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        mHeaderResId, parent, false));
            case TYPE_FOOTER:
                return new FooterViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.list_foot_loading, parent, false));
            case 1:
                return new ItemTextViewHolder(mLayoutInflater.inflate(R.layout.article_item_text,parent,false));
            case 2:
                return new ItemImageViewHolder(mLayoutInflater.inflate(R.layout.article_item_image,parent,false));
            case 3:
                return new ItemImagesViewHolder(mLayoutInflater.inflate(R.layout.article_item_images,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);
        if (type != TYPE_FOOTER && type != TYPE_HEADER) {
            ((ItemView)holder).bindModel(mList.get(position));
            holder.itemView.setId(mList.get(position).getId());
        }
    }

    @Override
    public int getItemCount() {
        int count = mList.size();
        if (mIsFooterEnable) count++;
        if (mIsHeaderEnable) count++;
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int headerPosition = 0;
        int footerPosition = getItemCount() - 1;
        if (headerPosition == position && mIsHeaderEnable && mHeaderResId > 0) {
            return TYPE_HEADER;
        }
        if (footerPosition == position && mIsFooterEnable) {
            return TYPE_FOOTER;
        }
        ArticleEntity article = mList.get(position);
        Log.d("ArticleEntity","id:"+article.getId()+"type:"+article.getType()+"img:"+article.getMainimg());
        return  mList.get(position).getType();
    }

    abstract class ItemView extends RecyclerView.ViewHolder{
        public ItemView(View itemView) {
            super(itemView);
            //为每一个item绑定监听
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener!=null){
                        itemClickListener.onItemClick(v,v.getId());
                    }
                }
            });
        }
        public abstract void bindModel(ArticleEntity model);
    }


    class ItemTextViewHolder extends  ItemView{
        TextView articleTitleText;
        TextView articleDescText;
        //TextView articleTimeText;
        public ItemTextViewHolder(View itemView){
            super(itemView);
            articleTitleText = (TextView) itemView.findViewById(R.id.article_title_text);
            articleDescText = (TextView) itemView.findViewById(R.id.article_brief_text);
            //articleDescText = (TextView) itemView.findViewById(R.id.article_brief_text);
        }

        public void bindModel(ArticleEntity model){
            articleTitleText.setText(model.getTitle());
            articleDescText.setText(model.getBrief());
        }
    }

    class ItemImageViewHolder extends  ItemView{
        ImageView articleImage;
        TextView articleTitleText;
        TextView articleDescText;
        public ItemImageViewHolder(View itemView){
            super(itemView);
            articleTitleText = (TextView) itemView.findViewById(R.id.article_title_text);
            articleDescText = (TextView) itemView.findViewById(R.id.article_desc_text);
            articleImage = (ImageView)itemView.findViewById(R.id.article_image);
        }

        public void bindModel(ArticleEntity model){
            articleTitleText.setText(model.getTitle());
            articleDescText.setText(model.getBrief());
            if (null==model.getMainimg() || model.getMainimg().equals("")){
                return;
            }
            Log.d("mainimg",model.getMainimg());
            Picasso.with(SmartApplication.getAppContext())
                    .load(model.getMainimg())
                    .resize(120, 100)
                    .centerCrop()
                    .into(articleImage);
        }
    }

    class ItemImagesViewHolder extends  ItemView{
        TextView articleTitleText;
        public ItemImagesViewHolder(View itemView){
            super(itemView);
            articleTitleText = (TextView) itemView.findViewById(R.id.article_title_text);
        }

        public void bindModel(ArticleEntity model){
            articleTitleText.setText(model.getTitle());
        }
    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setHeaderEnable(boolean enable) {
        mIsHeaderEnable = enable;
    }

    public void addHeaderView(int resId) {
        mHeaderResId = resId;
    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
        //  Log.d("ddd", itemClickListener.toString());
    }

    public  interface onRecyclerViewItemClickListener {
        void onItemClick(View v, int id);
    }

}
