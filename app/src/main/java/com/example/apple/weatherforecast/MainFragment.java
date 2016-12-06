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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Rawght Steven on 7/31/16, 13.
 * Email:rawghtsteven@gmail.com
 */
@SuppressLint("ValidFragment")
public class MainFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.yesType) TextView YesType;
    @BindView(R.id.yesTemp) TextView YesTemp;
    @BindView(R.id.tomType) TextView TomType;
    @BindView(R.id.tomTemp) TextView TomTemp;
    @BindView(R.id.tdatType) TextView TdatType;
    @BindView(R.id.tdatTemp) TextView TdatTemp;

    @BindView(R.id.city) TextView city;
    @BindView(R.id.temperature) TextView temperature;
    @BindView(R.id.air) TextView air;
    @BindView(R.id.condition) TextView condition;
    @BindView(R.id.wind) TextView wind;

    @BindView(R.id.weather) ImageView weatherImage;
    @BindView(R.id.yesImage) ImageView yesImage;
    @BindView(R.id.tomImage) ImageView tomImage;
    @BindView(R.id.tdatImage) ImageView tdatImage;

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

        YesType.setTypeface(Light);
        YesTemp.setTypeface(Light);
        TomType.setTypeface(Light);
        TomTemp.setTypeface(Light);
        TdatType.setTypeface(Light);
        TdatTemp.setTypeface(Light);

        city.setText(weatherBean.getCity());
        String temp = weatherBean.getTemperature();
        StringBuilder builder = new StringBuilder(temp);
        builder.reverse().deleteCharAt(0).reverse();
        String mintemp = weatherBean.getMin_tem();
        String maxtemp = weatherBean.getMax_tem();
        StringBuilder builder1 = new StringBuilder(mintemp);
        builder1.reverse().deleteCharAt(0).reverse();
        StringBuilder builder2 = new StringBuilder(maxtemp);
        builder2.reverse().deleteCharAt(0).reverse();
        temperature.setText(builder.toString()+"°");
        condition.setText(weatherBean.getWeather()+"  "+builder1+"°"+"~"+builder2+"°");
        wind.setText(weatherBean.getWindDirection()+"  "+weatherBean.getWindStrength());
        air.setText("PM2.5  "+weatherBean.getPM2_5());

        String yesType = weatherBean.getYesType();
        String yesLowTemp = weatherBean.getYesLowTemp();
        String yesHighTemp = weatherBean.getYesHighTemp();
        String tomType = weatherBean.getTomType();
        String tomLowTemp = weatherBean.getTomLowTemp();
        String tomHighTemp = weatherBean.getTomHighTemp();
        String tdatType = weatherBean.getTdatType();
        String tdatLowTemp = weatherBean.getTdatLowTemp();
        String tdatHighTemp = weatherBean.getTdatHighTemp();

        StringBuilder builder3 = new StringBuilder(yesLowTemp);
        builder3.reverse().deleteCharAt(0).reverse();
        StringBuilder builder4 = new StringBuilder(yesHighTemp);
        builder4.reverse().deleteCharAt(0).reverse();
        StringBuilder builder5 = new StringBuilder(tomLowTemp);
        builder5.reverse().deleteCharAt(0).reverse();
        StringBuilder builder6 = new StringBuilder(tomHighTemp);
        builder6.reverse().deleteCharAt(0).reverse();
        StringBuilder builder7 = new StringBuilder(tdatLowTemp);
        builder7.reverse().deleteCharAt(0).reverse();
        StringBuilder builder8 = new StringBuilder(tdatHighTemp);
        builder8.reverse().deleteCharAt(0).reverse();

        YesType.setText(yesType);
        YesTemp.setText(builder3+"°"+"~"+builder4+"°");
        TomType.setText(tomType);
        TomTemp.setText(builder5+"°"+"~"+builder6+"°");
        TdatType.setText(tdatType);
        TdatTemp.setText(builder7+"°"+"~"+builder8+"°");

        setWeatherPic();
        return view;
    }

    private void setWeatherPic() {

        switch (weatherBean.getWeather()){
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
        switch (weatherBean.getYesType()){
            case QING:
                yesImage.setImageResource(R.drawable.sunny);
                break;
            case DUOYUN:
                yesImage.setImageResource(R.drawable.duoyun);
                break;
            case XIAOYU:
                yesImage.setImageResource(R.drawable.xiaoyu);
                break;
            case ZHENYU:
                yesImage.setImageResource(R.drawable.zhenyu);
                break;
            case YIN:
                yesImage.setImageResource(R.drawable.yin);
                break;
            case DAYU:
                yesImage.setImageResource(R.drawable.dayu);
                break;
            case LEIZHENYU:
                yesImage.setImageResource(R.drawable.leizhenyu);
                break;
            case ZHONGYU:
                yesImage.setImageResource(R.drawable.zhongyu);
                break;
            case MAI:
                yesImage.setImageResource(R.drawable.mai);
                break;
            case BAOYU:
                yesImage.setImageResource(R.drawable.baoyu);
                break;
            case XIAOXUE:
                yesImage.setImageResource(R.drawable.xiaoxue);
                break;
            case ZHONGXUE:
                yesImage.setImageResource(R.drawable.zhongxue);
                break;
            case DAXUE:
                yesImage.setImageResource(R.drawable.daxue);
                break;
            case YUJIAXUE:
                yesImage.setImageResource(R.drawable.yujiaxue);
                break;
            case WU:
                yesImage.setImageResource(R.drawable.wu);
                break;
            case BINGBAO:
                yesImage.setImageResource(R.drawable.bingbao);
                break;
            case SHACHENBAO:
                yesImage.setImageResource(R.drawable.shachenbao);
                break;
            case TYPHOON:
                yesImage.setImageResource(R.drawable.typhoon);
                break;
        }
        switch (weatherBean.getTomType()){
            case QING:
                tomImage.setImageResource(R.drawable.sunny);
                break;
            case DUOYUN:
                tomImage.setImageResource(R.drawable.duoyun);
                break;
            case XIAOYU:
                tomImage.setImageResource(R.drawable.xiaoyu);
                break;
            case ZHENYU:
                tomImage.setImageResource(R.drawable.zhenyu);
                break;
            case YIN:
                tomImage.setImageResource(R.drawable.yin);
                break;
            case DAYU:
                tomImage.setImageResource(R.drawable.dayu);
                break;
            case LEIZHENYU:
                tomImage.setImageResource(R.drawable.leizhenyu);
                break;
            case ZHONGYU:
                tomImage.setImageResource(R.drawable.zhongyu);
                break;
            case MAI:
                tomImage.setImageResource(R.drawable.mai);
                break;
            case BAOYU:
                tomImage.setImageResource(R.drawable.baoyu);
                break;
            case XIAOXUE:
                tomImage.setImageResource(R.drawable.xiaoxue);
                break;
            case ZHONGXUE:
                tomImage.setImageResource(R.drawable.zhongxue);
                break;
            case DAXUE:
                tomImage.setImageResource(R.drawable.daxue);
                break;
            case YUJIAXUE:
                tomImage.setImageResource(R.drawable.yujiaxue);
                break;
            case WU:
                tomImage.setImageResource(R.drawable.wu);
                break;
            case BINGBAO:
                tomImage.setImageResource(R.drawable.bingbao);
                break;
            case SHACHENBAO:
                tomImage.setImageResource(R.drawable.shachenbao);
                break;
            case TYPHOON:
                tomImage.setImageResource(R.drawable.typhoon);
                break;
        }
        switch (weatherBean.getTdatType()){
            case QING:
                tdatImage.setImageResource(R.drawable.sunny);
                break;
            case DUOYUN:
                tdatImage.setImageResource(R.drawable.duoyun);
                break;
            case XIAOYU:
                tdatImage.setImageResource(R.drawable.xiaoyu);
                break;
            case ZHENYU:
                tdatImage.setImageResource(R.drawable.zhenyu);
                break;
            case YIN:
                tdatImage.setImageResource(R.drawable.yin);
                break;
            case DAYU:
                tdatImage.setImageResource(R.drawable.dayu);
                break;
            case LEIZHENYU:
                tdatImage.setImageResource(R.drawable.leizhenyu);
                break;
            case ZHONGYU:
                tdatImage.setImageResource(R.drawable.zhongyu);
                break;
            case MAI:
                tdatImage.setImageResource(R.drawable.mai);
                break;
            case BAOYU:
                tdatImage.setImageResource(R.drawable.baoyu);
                break;
            case XIAOXUE:
                tdatImage.setImageResource(R.drawable.xiaoxue);
                break;
            case ZHONGXUE:
                tdatImage.setImageResource(R.drawable.zhongxue);
                break;
            case DAXUE:
                tdatImage.setImageResource(R.drawable.daxue);
                break;
            case YUJIAXUE:
                tdatImage.setImageResource(R.drawable.yujiaxue);
                break;
            case WU:
                tdatImage.setImageResource(R.drawable.wu);
                break;
            case BINGBAO:
                tdatImage.setImageResource(R.drawable.bingbao);
                break;
            case SHACHENBAO:
                tdatImage.setImageResource(R.drawable.shachenbao);
                break;
            case TYPHOON:
                tdatImage.setImageResource(R.drawable.typhoon);
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
