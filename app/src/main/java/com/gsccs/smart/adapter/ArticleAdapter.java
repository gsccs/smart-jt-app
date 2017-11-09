package com.gsccs.smart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gsccs.smart.R;
import com.gsccs.smart.model.ArticleEntity;
import com.gsccs.smart.utils.Initialize;

import java.util.ArrayList;

/**
 * Created by x.d zhang on 2016/10/27.
 */
public class ArticleAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ArticleEntity> data;

    public ArticleAdapter(Context context, ArrayList<ArticleEntity> articles) {
        this.data = articles;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        String title = data.get(i).getTitle();
        ViewHolder viewHolder = null;
        if(null == view){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.traffic_articles,null);
            viewHolder.title = $(view,R.id.article_title);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.title.setText(title);

        view.setLayoutParams(new ListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Initialize.SCREEN_HEIGHT/15));


        return view;
    }

    private <T extends View> T $(View view,int resId){
        return (T) view.findViewById(resId);
    }

    public static class ViewHolder{
        private TextView title;
    }
}
