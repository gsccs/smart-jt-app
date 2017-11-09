package com.gsccs.smart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gsccs.smart.listener.GetLocalMessageListener;
import com.gsccs.smart.R;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.service.BaseLocationService;
import com.gsccs.smart.utils.Initialize;
import com.amap.api.services.core.LatLonPoint;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 公交路线
 */
public class BusRouteActivity extends AppCompatActivity implements
        GetLocalMessageListener,
        View.OnClickListener{

    private LocationMessage locationMessage;

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.up_image)
    ImageView upImage;
    @Bind(R.id.below_image)
    ImageView belowImage;
    @Bind(R.id.up_text)
    TextView upText;
    @Bind(R.id.below_text)
    TextView belowText;

    @Bind(R.id.exchange_button)
    Button exchangeButton;
    @Bind(R.id.up_button)
    Button upButton;
    @Bind(R.id.below_button)
    Button belowButton;
    @Bind(R.id.search)
    Button searchBtn;
    @Bind(R.id.protect_bridge)
    RelativeLayout protectBridge;

    private Intent inputIntent;
    private Intent getLocationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_route_activity);
        ButterKnife.bind(this);
        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        if (null==titleName || titleName.equals("")){
            mHeadTextView.setText("智慧公交");
        }else{
            mHeadTextView.setText(titleName);
        }
        mHeadImageView.setOnClickListener(this);
        inputIntent = new Intent(this,BusRouteInputActivity.class);
        /*if(null != getIntent().getParcelableExtra("LocationMessage")){
            locationMessage = getIntent().getParcelableExtra("LocationMessage");
        }else {

        }*/
        this.setGetLocalMessageListener(this);
        //backButton.setOnClickListener(this);
        exchangeButton.setOnClickListener(this);
        upButton.setOnClickListener(this);
        belowButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_ico:
                this.finish();
                break;
            case R.id.exchange_button:
                switchFromAndTo();
                break;
            case R.id.up_button:
                inputIntent.putExtra(Initialize.PLACE,Initialize.UP);
                startActivityForResult(inputIntent, Initialize.REQUEST_CODE_UP);
                break;
            case R.id.below_button:
                inputIntent.putExtra(Initialize.PLACE, Initialize.BELOW);
                startActivityForResult(inputIntent, Initialize.REQUEST_CODE_BELOW);
                break;
            case R.id.search:
                handleMessageTo();
                break;
        }
    }

    private boolean myLocationIsUp = true;
    public void switchFromAndTo(){
        String upString = null;
        String belowString = null;
        if((upText.getText().toString()).equals("输入起点")){
            upString = "输入终点";
            belowString = String.valueOf(belowText.getText());
        }else if(belowText.getText().toString().equals("输入终点")){
            upString = String.valueOf(upText.getText());
            belowString = "输入起点";
        }else{
            upString = String.valueOf(upText.getText());
            belowString = String.valueOf(belowText.getText());
        }

        if(upText.getText().equals(this.getString(R.string.my_location))
                || belowText.getText().equals(this.getString(R.string.my_location))){
            upImage.setImageResource(myLocationIsUp?R.drawable.target_blue:R.drawable.route_location);
            belowImage.setImageResource(myLocationIsUp?R.drawable.route_location:R.drawable.route_target);
        }else{
            upImage.setImageResource(R.drawable.target_blue);
            belowImage.setImageResource(R.drawable.route_target);
        }
        upText.setText(belowString);
        belowText.setText(upString);
        if(null != fromPoint && null != toPoint){
            LatLonPoint mPoint = fromPoint;
            fromPoint = toPoint;
            toPoint = mPoint;
        }
        myLocationIsUp = myLocationIsUp?false:true;
    }


    @Override
    public void getLocalMessage(LocationMessage mLocationMessage) {
        /*ui逻辑*/
        if(null == inputIntent.getParcelableExtra(Initialize.LOCATION)){
            inputIntent.putExtra(Initialize.LOCATION,locationMessage);
            protectBridge.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocationMessage = new Intent(this, BaseLocationService.class);
        IntentFilter receiverFilter = new IntentFilter();
        receiverFilter.addAction("com.BaseLocationReceiver");
        registerReceiver(localMessageReceiver,receiverFilter);
        startService(getLocationMessage);
    }

    @Override
    protected void onStop() {
        super.onStop();
            unregisterReceiver(localMessageReceiver);
        if(null != getLocationMessage){
            stopService(getLocationMessage);
        }
    }

    private LatLonPoint fromPoint;
    private LatLonPoint toPoint;
    Intent pathShowIntent;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Initialize.REQUEST_CODE_UP&& resultCode == Initialize.RESULT_CODE_UP){
            if(data.getStringExtra(Initialize.PLACE_NAME).equals("我的位置")){
                if(belowText.getText().toString().equals("我的位置")){
                    upText.setText("我的位置");
                    belowText.setText("输入终点");
                    upImage.setImageResource(R.drawable.route_location);
                    belowImage.setImageResource(R.drawable.route_target);
                    myLocationIsUp = myLocationIsUp?false:true;
                }else{
                    upText.setText(data.getStringExtra(Initialize.PLACE_NAME));
                    upImage.setImageResource(R.drawable.route_location);
                }
            }else{
                upText.setText(data.getStringExtra(Initialize.PLACE_NAME));
                upImage.setImageResource(R.drawable.target_blue);
            }
            fromPoint = data.getParcelableExtra(Initialize.PLACE_POINT);
        }else if(requestCode == Initialize.REQUEST_CODE_BELOW && resultCode == Initialize.RESULT_CODE_BELOW){
            if(data.getStringExtra(Initialize.PLACE_NAME).equals("我的位置")){
                if(upText.getText().toString().equals("我的位置")){
                    belowText.setText("我的位置");
                    upText.setText("输入起点");
                    upImage.setImageResource(R.drawable.target_blue);
                    belowImage.setImageResource(R.drawable.route_location);
                    myLocationIsUp = myLocationIsUp?false:true;
                }else{
                    belowText.setText(data.getStringExtra(Initialize.PLACE_NAME));
                    belowImage.setImageResource(R.drawable.route_location);
                }
            }else{
                belowText.setText(data.getStringExtra(Initialize.PLACE_NAME));
                belowImage.setImageResource(R.drawable.route_target);
            }
            toPoint = data.getParcelableExtra(Initialize.PLACE_POINT);
        }

        if(!upText.getText().toString().substring(0,2).equals("输入") &&
                !belowText.getText().toString().substring(0,2).equals("输入")){
            searchBtn.setTextColor(Initialize.BLUE_TEXT);
            searchBtn.setEnabled(true);
            searchBtn.setOnClickListener(this);
            handleMessageTo();
        }
    }

    public void handleMessageTo(){
        pathShowIntent = new Intent(this,BusPathActivity.class);
        if(null == fromPoint){
            fromPoint = new LatLonPoint(locationMessage.getLatitude(),locationMessage.getLongitude());
        }else if(null == toPoint){
            toPoint = new LatLonPoint(locationMessage.getLatitude(),locationMessage.getLongitude());
        }

        pathShowIntent.putExtra(Initialize.LOCATION,locationMessage);
        pathShowIntent.putExtra("FromName",upText.getText().toString());
        pathShowIntent.putExtra("ToName",belowText.getText().toString());
        pathShowIntent.putExtra("From",fromPoint);
        pathShowIntent.putExtra("To",toPoint);
        startActivity(pathShowIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver localMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                locationMessage = intent.getParcelableExtra(Initialize.LOCATION);
                if(null != locationMessage){
                    mGetLocalMessageListener.getLocalMessage(locationMessage);
                }else{
                    /**
                     *
                     * 错误处理逻辑
                     *
                     * **/
                }
            }
    };

    private GetLocalMessageListener mGetLocalMessageListener;
    public void setGetLocalMessageListener(GetLocalMessageListener listener){
        this.mGetLocalMessageListener = listener;
    }
}
