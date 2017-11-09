package com.gsccs.smart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.gsccs.smart.R;
import com.gsccs.smart.model.LocationMessage;
import com.gsccs.smart.network.BaseConst;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 *  周边服务
 */
public class RoundActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;
    @Bind(R.id.webView)
    WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_map_activity);
        ButterKnife.bind(this);

        String titleName = getIntent().getStringExtra(BaseConst.TOOLBAR_TITLE);
        if (null==titleName || titleName.equals("")){
            mHeadTextView.setText("周边服务");
        }else{
            mHeadTextView.setText(titleName);
        }
        mHeadImageView.setOnClickListener(this);

        WebSettings setting = mWebView.getSettings();
        //  自适应屏幕
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setLoadWithOverviewMode(true);
        //  支持javascript
        setting.setJavaScriptEnabled(true);
        /*mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/
        mWebView.loadUrl("https://m.amap.com/nearby/index/");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_ico:
                finish();
                break;
        }
    }
}
