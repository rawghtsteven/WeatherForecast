package com.example.apple.weatherforecast;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.mmga.metroloading.MetroLoadingView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements SearchFragment.CallBackValue, EmptyFragment.InternetStateChanged {

    @BindView(R.id.relative_layout) RelativeLayout layout;
    @BindView(R.id.view_pager) ViewPager pager;
    @BindView(R.id.metroLoading)
    MetroLoadingView loadingView;

    public static final String TAG = "MAIN ACTIVITY";
    public static final String API_KEY = "f405d25941d842408d2e2c8d44248e28";
    public static final String base_url = "http://api.shujuzhihui.cn/api/weatherForecast/";
    public static final String NEED_MORE_DAY = String.valueOf(1);

    private MyFragmentPagerAdapter adapter;
    List<android.support.v4.app.Fragment> fragmentList = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SearchFragment searchFragment;
    EmptyFragment fragment;
    WeatherBean weatherBean;

    @SuppressLint("CommitPrefEdits")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadingView.start();

        sharedPreferences = getSharedPreferences("CURRENTCITY",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.setFragmentList(fragmentList);
        pager.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();

        if (!wifi&&!internet){
            fragment = new EmptyFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.relative_layout,fragment).commit();
            Snackbar snackbar = Snackbar.make(layout,"您的网络出现了问题",Snackbar.LENGTH_LONG).setAction("去设置", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            });
            snackbar.show();

        }else {
            if (sharedPreferences.contains("CityID")){
                String cityID = sharedPreferences.getString("CityID","101010100");
                loadWeatherForNow(cityID);
            }else {
                loadWeatherForNow("101010100");
            }
        }
    }

    private void loadWeatherForNow(String cityID) {

        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder().retryOnConnectionFailure(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        service.getWeather(API_KEY,cityID,NEED_MORE_DAY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherBean>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG,"Get weather completed");
                        loadingView.stop();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"Get weather failed");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(WeatherBean weatherBean) {
                        //Log.e(TAG, weatherBean.getShowapi_res_body().getRet_code());
                        Log.e(TAG, weatherBean.getShowapi_res_body().getCityInfo().getC5()+" "+
                                weatherBean.getShowapi_res_body().getNow().getWeather()+" "+
                                weatherBean.getShowapi_res_body().getNow().getTemperature());
                        MainFragment fragment = new MainFragment(weatherBean);
                        fragmentList.add(fragment);
                        adapter.setFragmentList(fragmentList);
                        adapter.notifyDataSetChanged();
                        pager.setCurrentItem(fragmentList.size()-1);
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_search:
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setQueryHint("请输入要查找的城市名");
                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        return false;
                    }
                });
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchFragment = new SearchFragment();
                        searchFragment.setINPUT(query);
                        getFragmentManager().beginTransaction().replace(R.id.relative_layout,searchFragment).commit();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return true;
                    }
                });
                break;
            case R.id.share:
                Intent shareIntent = new Intent();
                String shareText = "今天"+ weatherBean.getShowapi_res_body().getCityInfo().getC5()+"的天气是"+
                        weatherBean.getShowapi_res_body().getNow().getWeather() +" "+weatherBean.getShowapi_res_body().getNow().getTemperature()+
                        "\n"+weatherBean.getShowapi_res_body().getNow().getWind_direction()+weatherBean.getShowapi_res_body().getNow().getWind_power()+
                        "\n"+"空气湿度"+weatherBean.getShowapi_res_body().getNow().getSd()+
                        "\n"+"空气质量为"+weatherBean.getShowapi_res_body().getNow().getAqiDetail().getQuality()+
                        "\n"+"PM2.5指数为"+weatherBean.getShowapi_res_body().getNow().getAqiDetail().getPm2_5()+
                        "\n"+"看天气就用天气预报，简单又好用的天气软件，点击下面的地址即可下载："+
                        "\n"+"http://fir.im/5d3j";
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent,"分享到"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void stateChangeListener(boolean InternetState) {
        if (InternetState){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            sharedPreferences = getSharedPreferences("CURRENTCITY",MODE_PRIVATE);
            editor = sharedPreferences.edit();
            if (sharedPreferences.contains("CITY")){
                String cityName = sharedPreferences.getString("CITY","北京");
                loadWeatherForNow(cityName);
            }
        }
    }

    @Override
    public void sendValue(String cityId) {
        Log.e(TAG, "CITY ID: " + String.valueOf(cityId));
        editor.putString("CityID",cityId);
        editor.commit();
        loadWeatherForNow(cityId);
    }
}
