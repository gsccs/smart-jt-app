package com.gsccs.smart.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.gsccs.smart.R;
import com.gsccs.smart.adapter.MainPagerAdapter;
import com.gsccs.smart.adapter.RoughDetailPagerAdapter;
import com.gsccs.smart.utils.AMapUtil;
import com.gsccs.smart.utils.Initialize;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 *  停车dao
 */
public class ParkRouteDetailActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {

	private BusPath mBuspath;
	private BusRouteResult mBusRouteResult;
	@Bind(R.id.firstline)
	TextView  mTitleBusRoute;
	@Bind(R.id.secondline)
	TextView mDesBusRoute;
	@Bind(R.id.cursor)
	ImageView cursor;
	@Bind(R.id.schema)
	LinearLayout schema;
	@Bind(R.id.show_in_map)
	RelativeLayout showInMap;
	@Bind(R.id.back)
	Button backButton;
	@Bind(R.id.main_pager)
	ViewPager furtherDetailPath;
	@Bind(R.id.below_pager)
	ViewPager roughDetailPath;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_route_detail_activity);
		ButterKnife.bind(this);
		getIntentData();
		init();
	}

	private int position;
	private void getIntentData() {
		Intent intent = getIntent();
		if (intent != null) {
			mBuspath = intent.getParcelableExtra("bus_path");
			mBusRouteResult = intent.getParcelableExtra("bus_result");
			position = intent.getIntExtra("Position",0);
		}
	}

	private void init() {
		one = Initialize.SCREEN_WIDTH/(mBusRouteResult.getPaths().size());

		showInMap.setOnClickListener(this);
		backButton.setTag(Initialize.BUTTON);
		backButton.setOnClickListener(this);
		String dur = AMapUtil.getFriendlyTime((int) mBuspath.getDuration());
		String dis = AMapUtil.getFriendlyTime((int) mBuspath.getDistance());
		mTitleBusRoute.setText(dur + "(" + dis + ")");
		int taxiCost = (int) mBusRouteResult.getTaxiCost();
		mDesBusRoute.setText("打车约" + taxiCost + "元");
		mDesBusRoute.setVisibility(View.VISIBLE);
		addSchema(schema);
		configureViewPager();
	}

	private List<TextView> textViewList;
	private TextView textView;

	private void addSchema(LinearLayout layout) {
		textViewList = new ArrayList<>();
		for(int count = 0 ;count < mBusRouteResult.getPaths().size();count ++){
			textView = new TextView(this);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
			textView.setText("方案" + (count + 1) );
			textView.setHeight((int) (Initialize.SCREEN_HEIGHT/15));
			textView.setWidth(Initialize.SCREEN_WIDTH/(mBusRouteResult.getPaths().size()));
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.drawable.alpha_button);
			if(0 == count){
				textView.setTextColor(Color.BLUE);
			}else{
				textView.setTextColor(getResources().getColor(R.color.darkgrey));
			}
			textView.setTag(new String(count + ""));

			textView.setOnClickListener(new MyOnClickListener());
			layout.addView(textView,count);
			textViewList.add(textView);
		}
		LinearLayout.LayoutParams params =
				new LinearLayout.LayoutParams(Initialize.SCREEN_WIDTH/(mBusRouteResult.getPaths().size()),
						8);
		cursor.setLayoutParams(params);
	}

	private void configureViewPager() {

		//furtherDetailPath = $(R.id.main_pager);
		//roughDetailPath = $(R.id.below_pager);

		MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(this,mBusRouteResult.getPaths());

		RoughDetailPagerAdapter roughDetailPagerAdapter =
				new RoughDetailPagerAdapter(this,mBusRouteResult.getPaths());

		furtherDetailPath.setAdapter(mainPagerAdapter);
		roughDetailPath.setAdapter(roughDetailPagerAdapter);

		furtherDetailPath.addOnPageChangeListener(this);
		MyViewPagerPageChangedListener pageChangedListener = new MyViewPagerPageChangedListener();
		roughDetailPath.addOnPageChangeListener(pageChangedListener);
		furtherDetailPath.setCurrentItem(position);
	}

	

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.back:
				this.finish();
				break;
			case R.id.show_in_map:
				showInMap();
				break;
		}
	}


	private int cursorIndex = 0;
	private int one = 0;
	private int primIndex = 0;
	public void moveCursor(int index){
		Animation animation = new TranslateAnimation(cursorIndex * one, index*one, 0, 0);//显然这个比较简洁，只有一行代码。
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		cursor.startAnimation(animation);
		cursorIndex = index;
		textViewList.get(primIndex).setTextColor(getResources().getColor(R.color.darkgrey));
		textViewList.get(index).setTextColor(Color.BLUE);
		primIndex = index;
	}
	/**
	 * This method will be invoked when the current page is scrolled, either as part
	 * of a programmatically initiated smooth scroll or a user initiated touch scroll.
	 *
	 * @param position             Position index of the first page currently being displayed.
	 *                             Page position+1 will be visible if positionOffset is nonzero.
	 * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
	 * @param positionOffsetPixels Value in pixels indicating the offset from position.
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}


	@Override
	public void onPageSelected(int position) {
		this.position = position;
		moveCursor(position);
		if(!cursorMove){
			roughDetailPath.setCurrentItem(position);
		}else{
			cursorMove = false;
		}
		Log.i("AkiTest",position + "-00");
	}



	@Override
	public void onPageScrollStateChanged(int state) {

	}


	private void showInMap(){
		Intent goMap = new Intent(this,BusPathInMapActivity.class);
		if(null != mBusRouteResult){
			goMap.putExtra("Position",position);
			goMap.putExtra("BusResult",mBusRouteResult);
			startActivity(goMap);
		}
	}


	private boolean cursorMove = false;
	public class MyOnClickListener implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			cursorMove = true;
			furtherDetailPath.setCurrentItem(Integer.parseInt(view.getTag().toString()));
			roughDetailPath.setCurrentItem(Integer.parseInt(view.getTag().toString()));
		}
	}

	class MyViewPagerPageChangedListener implements ViewPager.OnPageChangeListener{

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			moveCursor(position);
			if(!cursorMove){
				furtherDetailPath.setCurrentItem(position);
			}else{
				cursorMove = false;
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}
}
