package com.example.apple.weatherforecast;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmga.metroloading.MetroLoadingView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Rawght Steven on 7/31/16, 13.
 * Email:rawghtsteven@gmail.com
 */
@SuppressLint("ValidFragment")
public class MainFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.tType) TextView TomType;
    @BindView(R.id.tTemperature) TextView TomTemp;
    @BindView(R.id.tdatType) TextView TDATType;
    @BindView(R.id.tdatTemp) TextView TDATTemp;
    @BindView(R.id.twodatType) TextView TwoDATType;
    @BindView(R.id.twodatTemp) TextView TwoDATTemp;
    @BindView(R.id.twodat)TextView TwoDAT;

    @BindView(R.id.city) TextView city;
    @BindView(R.id.temperature) TextView temperature;
    @BindView(R.id.air) TextView air;
    @BindView(R.id.condition) TextView condition;
    @BindView(R.id.wind) TextView wind;

    @BindView(R.id.weather) ImageView weatherImage;
    @BindView(R.id.tImage) ImageView TomImage;
    @BindView(R.id.tdatImage) ImageView TDATImage;
    @BindView(R.id.twodatImage) ImageView TwoDATImage;

    private WeatherBean weatherBean;
    private Unbinder unbinder;

    private static final String QING = "晴";
    private static final String DUOYUN = "多云";
    private static final String XIAOYU = "小雨";
    private static final String ZHENYU = "阵雨";
    private static final String YIN = "阴";
    private static final String DAYU = "大雨";
    private static final String LEIZHENYU = "雷阵雨";
    private static final String ZHONGYU = "中雨";
    private static final String MAI = "霾";
    private static final String BAOYU = "暴雨";
    private static final String XIAOXUE = "小雪";
    private static final String ZHONGXUE = "中雪";
    private static final String DAXUE = "大雪";
    private static final String BINGBAO = "冰雹";
    private static final String WU = "雾";
    private static final String SHACHENBAO = "沙尘暴";
    private static final String YUJIAXUE = "雨夹雪";
    private static final String TYPHOON = "台风";


    public MainFragment(WeatherBean bean) {
        this.weatherBean = bean;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        unbinder = ButterKnife.bind(this,view);

        Typeface SemiLight =Typeface.createFromAsset(getActivity().getAssets(),"fonts/SegoeSemiLight.TTF");
        Typeface Segoe = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Segoe.TTF");
        Typeface Light = Typeface.createFromAsset(getActivity().getAssets(),"fonts/SegoeLight.TTF");

        city.setTypeface(Segoe);
        temperature.setTypeface(Light);
        air.setTypeface(SemiLight);
        condition.setTypeface(SemiLight);
        wind.setTypeface(SemiLight);

        TomType.setTypeface(Light);
        TomTemp.setTypeface(Light);
        TDATType.setTypeface(Light);
        TDATTemp.setTypeface(Light);
        TwoDATType.setTypeface(Light);
        TwoDATTemp.setTypeface(Light);

        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        TwoDAT.setText(String.valueOf(date+3)+"号");

        city.setText(weatherBean.getShowapi_res_body().getCityInfo().getC5());
        String temp = weatherBean.getShowapi_res_body().getNow().getTemperature();
        String cond = weatherBean.getShowapi_res_body().getNow().getWeather();
        temperature.setText(temp+"°");
        condition.setText(cond);
        wind.setText(weatherBean.getShowapi_res_body().getNow().getWind_direction()+weatherBean.getShowapi_res_body().getNow().getWind_power());
        air.setText("空气质量："+weatherBean.getShowapi_res_body().getNow().getAqiDetail().getQuality()+
                "\n"+"相对湿度："+weatherBean.getShowapi_res_body().getNow().getSd()+
                "\n"+"PM2.5指数："+weatherBean.getShowapi_res_body().getNow().getAqiDetail().getPm2_5()
        );
        TomType.setText(weatherBean.getShowapi_res_body().getF2().getDay_weather());
        TomTemp.setText(weatherBean.getShowapi_res_body().getF2().getDay_air_temperature()+"°~"+weatherBean.getShowapi_res_body().getF2().getNight_air_temperature()+"°");
        TDATType.setText(weatherBean.getShowapi_res_body().getF3().getDay_weather());
        TDATTemp.setText(weatherBean.getShowapi_res_body().getF3().getDay_air_temperature()+"°~"+weatherBean.getShowapi_res_body().getF3().getNight_air_temperature()+"°");
        TwoDATType.setText(weatherBean.getShowapi_res_body().getF4().getDay_weather());
        TwoDATTemp.setText(weatherBean.getShowapi_res_body().getF4().getDay_air_temperature()+"°~"+weatherBean.getShowapi_res_body().getF4().getNight_air_temperature()+"°");

        setWeatherPic();
        return view;
    }

    private void setWeatherPic() {

        switch (weatherBean.getShowapi_res_body().getNow().getWeather()){
            case QING:
                weatherImage.setImageResource(R.drawable.sunny);
                break;
            case DUOYUN:
                weatherImage.setImageResource(R.drawable.duoyun);
                break;
            case XIAOYU:
                weatherImage.setImageResource(R.drawable.xiaoyu);
                break;
            case ZHENYU:
                weatherImage.setImageResource(R.drawable.zhenyu);
                break;
            case YIN:
                weatherImage.setImageResource(R.drawable.yin);
                break;
            case DAYU:
                weatherImage.setImageResource(R.drawable.dayu);
                break;
            case LEIZHENYU:
                weatherImage.setImageResource(R.drawable.leizhenyu);
                break;
            case ZHONGYU:
                weatherImage.setImageResource(R.drawable.zhongyu);
                break;
            case MAI:
                weatherImage.setImageResource(R.drawable.mai);
                break;
            case BAOYU:
                weatherImage.setImageResource(R.drawable.baoyu);
                break;
            case XIAOXUE:
                weatherImage.setImageResource(R.drawable.xiaoxue);
                break;
            case ZHONGXUE:
                weatherImage.setImageResource(R.drawable.zhongxue);
                break;
            case DAXUE:
                weatherImage.setImageResource(R.drawable.daxue);
                break;
            case YUJIAXUE:
                weatherImage.setImageResource(R.drawable.yujiaxue);
                break;
            case WU:
                weatherImage.setImageResource(R.drawable.wu);
                break;
            case BINGBAO:
                weatherImage.setImageResource(R.drawable.bingbao);
                break;
            case SHACHENBAO:
                weatherImage.setImageResource(R.drawable.shachenbao);
                break;
            case TYPHOON:
                weatherImage.setImageResource(R.drawable.typhoon);
                break;
        }
        switch (weatherBean.getShowapi_res_body().getF2().getDay_weather()){
            case QING:
                TomImage.setImageResource(R.drawable.sunny);
                break;
            case DUOYUN:
                TomImage.setImageResource(R.drawable.duoyun);
                break;
            case XIAOYU:
                TomImage.setImageResource(R.drawable.xiaoyu);
                break;
            case ZHENYU:
                TomImage.setImageResource(R.drawable.zhenyu);
                break;
            case YIN:
                TomImage.setImageResource(R.drawable.yin);
                break;
            case DAYU:
                TomImage.setImageResource(R.drawable.dayu);
                break;
            case LEIZHENYU:
                TomImage.setImageResource(R.drawable.leizhenyu);
                break;
            case ZHONGYU:
                TomImage.setImageResource(R.drawable.zhongyu);
                break;
            case MAI:
                TomImage.setImageResource(R.drawable.mai);
                break;
            case BAOYU:
                TomImage.setImageResource(R.drawable.baoyu);
                break;
            case XIAOXUE:
                TomImage.setImageResource(R.drawable.xiaoxue);
                break;
            case ZHONGXUE:
                TomImage.setImageResource(R.drawable.zhongxue);
                break;
            case DAXUE:
                TomImage.setImageResource(R.drawable.daxue);
                break;
            case YUJIAXUE:
                TomImage.setImageResource(R.drawable.yujiaxue);
                break;
            case WU:
                TomImage.setImageResource(R.drawable.wu);
                break;
            case BINGBAO:
                TomImage.setImageResource(R.drawable.bingbao);
                break;
            case SHACHENBAO:
                TomImage.setImageResource(R.drawable.shachenbao);
                break;
            case TYPHOON:
                TomImage.setImageResource(R.drawable.typhoon);
                break;
        }
        switch (weatherBean.getShowapi_res_body().getF3().getDay_weather()){
            case QING:
                TDATImage.setImageResource(R.drawable.sunny);
                break;
            case DUOYUN:
                TDATImage.setImageResource(R.drawable.duoyun);
                break;
            case XIAOYU:
                TDATImage.setImageResource(R.drawable.xiaoyu);
                break;
            case ZHENYU:
                TDATImage.setImageResource(R.drawable.zhenyu);
                break;
            case YIN:
                TDATImage.setImageResource(R.drawable.yin);
                break;
            case DAYU:
                TDATImage.setImageResource(R.drawable.dayu);
                break;
            case LEIZHENYU:
                TDATImage.setImageResource(R.drawable.leizhenyu);
                break;
            case ZHONGYU:
                TDATImage.setImageResource(R.drawable.zhongyu);
                break;
            case MAI:
                TDATImage.setImageResource(R.drawable.mai);
                break;
            case BAOYU:
                TDATImage.setImageResource(R.drawable.baoyu);
                break;
            case XIAOXUE:
                TDATImage.setImageResource(R.drawable.xiaoxue);
                break;
            case ZHONGXUE:
                TDATImage.setImageResource(R.drawable.zhongxue);
                break;
            case DAXUE:
                TDATImage.setImageResource(R.drawable.daxue);
                break;
            case YUJIAXUE:
                TDATImage.setImageResource(R.drawable.yujiaxue);
                break;
            case WU:
                TDATImage.setImageResource(R.drawable.wu);
                break;
            case BINGBAO:
                TDATImage.setImageResource(R.drawable.bingbao);
                break;
            case SHACHENBAO:
                TDATImage.setImageResource(R.drawable.shachenbao);
                break;
            case TYPHOON:
                TDATImage.setImageResource(R.drawable.typhoon);
                break;
        }
        switch (weatherBean.getShowapi_res_body().getF4().getDay_weather()){
            case QING:
                TwoDATImage.setImageResource(R.drawable.sunny);
                break;
            case DUOYUN:
                TwoDATImage.setImageResource(R.drawable.duoyun);
                break;
            case XIAOYU:
                TwoDATImage.setImageResource(R.drawable.xiaoyu);
                break;
            case ZHENYU:
                TwoDATImage.setImageResource(R.drawable.zhenyu);
                break;
            case YIN:
                TwoDATImage.setImageResource(R.drawable.yin);
                break;
            case DAYU:
                TwoDATImage.setImageResource(R.drawable.dayu);
                break;
            case LEIZHENYU:
                TwoDATImage.setImageResource(R.drawable.leizhenyu);
                break;
            case ZHONGYU:
                TwoDATImage.setImageResource(R.drawable.zhongyu);
                break;
            case MAI:
                TwoDATImage.setImageResource(R.drawable.mai);
                break;
            case BAOYU:
                TwoDATImage.setImageResource(R.drawable.baoyu);
                break;
            case XIAOXUE:
                TwoDATImage.setImageResource(R.drawable.xiaoxue);
                break;
            case ZHONGXUE:
                TwoDATImage.setImageResource(R.drawable.zhongxue);
                break;
            case DAXUE:
                TwoDATImage.setImageResource(R.drawable.daxue);
                break;
            case YUJIAXUE:
                TwoDATImage.setImageResource(R.drawable.yujiaxue);
                break;
            case WU:
                TwoDATImage.setImageResource(R.drawable.wu);
                break;
            case BINGBAO:
                TwoDATImage.setImageResource(R.drawable.bingbao);
                break;
            case SHACHENBAO:
                TwoDATImage.setImageResource(R.drawable.shachenbao);
                break;
            case TYPHOON:
                TwoDATImage.setImageResource(R.drawable.typhoon);
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
