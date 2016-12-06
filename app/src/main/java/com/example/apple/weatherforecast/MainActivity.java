package com.example.apple.weatherforecast;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
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
import com.readystatesoftware.systembartint.SystemBarTintManager;

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

        initSystemBar(this);
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

            if (sharedPreferences.contains("CITY")){
                WeatherBean bean = new WeatherBean();
                bean.setCity(sharedPreferences.getString("CITY","北京"));
                bean.setAreaId(sharedPreferences.getInt("CITYCODE",101010100));
                loadData(bean);
            }
        }
    }

    private void loadData(WeatherBean bean) {

        String baseUrl = "http://apis.baidu.com/apistore/weatherservice/";

        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        okhttp3.Request request = chain.request();
                        okhttp3.Request.Builder builder1 = request.newBuilder();
                        okhttp3.Request.Builder builder2 = builder1.addHeader("apikey","0089e54ddc9caab4f66b665d0242ef59");
                        return chain.proceed(builder2.build());
                    }
                })
                .retryOnConnectionFailure(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        service.getWeather(bean.getCity(),bean.getAreaId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bean>() {
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
                    public void onNext(Bean response) {
                        Log.e(TAG,response.getRetData().getCity()+" "+
                                response.getRetData().getToday().getCurTemp()+" "+
                                response.getRetData().getToday().getType()
                        );
                        weatherBean = new WeatherBean();
                        weatherBean.setCity(response.getRetData().getCity());
                        weatherBean.setAreaId(response.getRetData().getCityid());
                        weatherBean.setWeather(response.getRetData().getToday().getType());
                        weatherBean.setMax_tem(response.getRetData().getToday().getHightemp());
                        weatherBean.setMin_tem(response.getRetData().getToday().getLowtemp());
                        weatherBean.setTemperature(response.getRetData().getToday().getCurTemp());
                        weatherBean.setWindDirection(response.getRetData().getToday().getFengxiang());
                        weatherBean.setWindStrength(response.getRetData().getToday().getFengli());
                        weatherBean.setPM2_5(response.getRetData().getToday().getAqi());
                        weatherBean.setYesType(response.getRetData().getHistory().get(1).getType());
                        weatherBean.setYesHighTemp(response.getRetData().getHistory().get(1).getHightemp());
                        weatherBean.setYesLowTemp(response.getRetData().getHistory().get(1).getLowtemp());
                        weatherBean.setTomType(response.getRetData().getForecast().get(0).getType());
                        weatherBean.setTomHighTemp(response.getRetData().getForecast().get(0).getHightemp());
                        weatherBean.setTomLowTemp(response.getRetData().getForecast().get(0).getLowtemp());
                        weatherBean.setTdatType(response.getRetData().getForecast().get(1).getType());
                        weatherBean.setTdatLowTemp(response.getRetData().getForecast().get(1).getLowtemp());
                        weatherBean.setTdatHighTemp(response.getRetData().getForecast().get(1).getHightemp());
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
                        searchFragment = new SearchFragment();
                        searchFragment.setINPUT(newText);
                        getFragmentManager().beginTransaction().replace(R.id.relative_layout,searchFragment).commit();
                        return true;
                    }
                });
                break;
            case R.id.share:
                Intent shareIntent = new Intent();
                String shareText = "今天"+ weatherBean.getCity()+"的天气是"+
                        weatherBean.getWeather() +" "+weatherBean.getTemperature()+
                        "\n"+weatherBean.getWindDirection()+weatherBean.getWindStrength()+
                        "\n"+"PM 2.5是"+weatherBean.getPM2_5()+
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

    public static void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);}
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(R.color.black);
    }

    @TargetApi(19)

    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void sendValue(WeatherBean bean) {
        Log.e("RECIEVED!", String.valueOf(bean));
        editor.putString("CITY",bean.getCity());
        editor.putInt("CITYCODE",bean.getAreaId());
        editor.commit();
        loadData(bean);
    }

    @Override
    public void stateChangeListener(boolean InternetState) {
        if (InternetState){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            sharedPreferences = getSharedPreferences("CURRENTCITY",MODE_PRIVATE);
            editor = sharedPreferences.edit();
            if (sharedPreferences.contains("CITY")){
                WeatherBean bean = new WeatherBean();
                bean.setCity(sharedPreferences.getString("CITY","北京"));
                bean.setAreaId(sharedPreferences.getInt("CITYCODE",101010100));
                loadData(bean);
            }
        }
    }

    public interface WeatherService{
        @GET("recentweathers")
        Observable<Bean> getWeather(@Query("cityname")String cityname, @Query("cityid")int cityid);
    }
}
