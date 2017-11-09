package com.gsccs.smart.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.route.BusStep;
import com.gsccs.smart.R;
import com.gsccs.smart.listener.ParkClickListener;
import com.gsccs.smart.model.ParkEntity;
import com.gsccs.smart.utils.Initialize;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by x.d zhang on 2016/11/02.
 */
public class ParkDetailAdapter extends PagerAdapter {

    private Context context;
    private List<ParkEntity> dataList;
    private List<View> views;
    private LayoutInflater layoutInflater;
    private View view;

    private TextView stopTitleView;
    private TextView stopPriceView;
    private TextView stopNumView;
    private TextView stopDistView;

    private ParkClickListener parkClickListener;

    public ParkDetailAdapter(Context context, List<ParkEntity> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.layoutInflater = LayoutInflater.from(context);
        initViews();
    }


    private void initViews(){
        if(null != dataList) {
            views = new ArrayList<>();
            for (int index = 0; index < dataList.size(); index++) {

                view = layoutInflater.inflate(R.layout.park_item_view, null);
                stopTitleView = $(view,R.id.stop_title);
                stopPriceView = $(view, R.id.stop_price);
                stopNumView = $(view, R.id.stop_num);
                stopDistView = $(view, R.id.stop_distance);

                ParkEntity point = dataList.get(index);
                stopTitleView.setText(point.getName());
                stopPriceView.setText("价格："+point.getCharge());
                stopNumView.setText("车位："+point.getNumber());
                stopDistView.setText("距离："+point.getDis()+"米");

                //stopPriceView.setText(String.valueOf(dataList.get(index).getCharge()) + "元");
                final int index_ = index;
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        Initialize.SCREEN_HEIGHT/11));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parkClickListener.onOpenPark(dataList.get(index_));
                    }
                });
                views.add(view);
            }
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return views==null?0:views.size();
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position  The page position to be removed.
     * @param object    The same object that was returned by
     *                  {@link #instantiateItem(View, int)}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View in which the page will be shown.
     * @param position  The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }



    private <T extends  View> T $(View view,int resId){
        return (T) view.findViewById(resId);
    }

    public String FormatTime(long time){
        long hour = time / 3600;
        long minute = (time % 3600)/60;
        return String.valueOf(hour) + "小时" + String.valueOf(minute) + "分钟";
    }

    public String FormatBusDistance(float distance){
        String number =  new DecimalFormat("0").format(distance/1000);
        return number + "km";
    }

    public List<String> calculateLine(List<BusStep> busStep){
        List<String> lineName = new ArrayList<>();
        String name;
        for(int count = 0;count < busStep.size();count ++){
            if(0 != busStep.get(count).getBusLines().size()){
                name = busStep.get(count).getBusLines().get(0).getBusLineName();
                lineName.add(name.substring(0,name.indexOf("路") + 1));
            }
        }
        return lineName;
    }


    public void setOnItemClickListener(ParkClickListener listener) {
        this.parkClickListener = listener;
    }


}
