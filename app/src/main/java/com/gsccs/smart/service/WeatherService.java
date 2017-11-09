package com.gsccs.smart.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.gsccs.smart.model.ErrorStatus;
import com.gsccs.smart.utils.Initialize;

/**
 *
 */
public class WeatherService extends Service implements WeatherSearch.OnWeatherSearchListener{

    private String search;

    private int MESSAGE_FLAG = 0;
    private ErrorStatus errorStatus;

    private String cityCode = "酒泉";// 城市区号
    private LocalWeatherLiveResult localWeatherLiveResult;// 天气查询返回的结果
    private WeatherSearchQuery weatherSearchQuery;// 天气查询的查询类
    private Intent weatherBroadcast;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("testWeather","onStartCommand*******");
        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        WeatherSearchQuery weatherQuery = new WeatherSearchQuery(
                cityCode,
                WeatherSearchQuery.WEATHER_TYPE_LIVE);
        WeatherSearch weatherSearch = new WeatherSearch(this);
        weatherSearch.setQuery(weatherQuery);
        weatherSearch.setOnWeatherSearchListener(this);
        weatherSearch.searchWeatherAsyn();
        return super.onStartCommand(intent, flags, startId);

    }


    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult ,int rCode) {
        Log.i("testWeather","WeatherSearch*******" + weatherLiveResult.getLiveResult().getCity());
        weatherBroadcast = new Intent("com.WeatherBroadcast");
        errorStatus = new ErrorStatus();
        if (rCode == 1000) {
            errorStatus.setReturnCode(rCode);
            errorStatus.setIsError(false);
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();
                weatherBroadcast.putExtra("weatherlive",weatherlive);
                //reporttime1.setText(weatherlive.getReportTime()+"发布");
                //weather.setText(weatherlive.getWeather());
                //Temperature.setText(weatherlive.getTemperature()+"°");
                //wind.setText(weatherlive.getWindDirection()+"风     "+weatherlive.getWindPower()+"级");
                //humidity.setText("湿度         "+weatherlive.getHumidity()+"%");
            }else {
                //ToastUtil.show(MainActivity.this, R.string.no_result);
                errorStatus.setReturnCode(Initialize.NO_RESULT_CODE);
                errorStatus.setIsError(true);
            }
        }else {
            //ToastUtil.showerror(MainActivity.this, rCode);
            errorStatus.setReturnCode(Initialize.NO_RESULT_CODE);
            errorStatus.setIsError(true);
        }
        weatherBroadcast.putExtra("ErrorStatus",errorStatus);
        sendBroadcast(weatherBroadcast);
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
        Log.d("testWeather","WeatherSearch*******" + localWeatherForecastResult.getForecastResult().getCity());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
