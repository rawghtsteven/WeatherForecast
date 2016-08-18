package com.example.apple.weatherforecast;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.mmga.metroloading.MetroLoadingView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements SearchFragment.CallBackValue, EmptyFragment.InternetStateChanged {

    private final static String APIKEY = "60b80bce0e96df9c323876a356fb89e6";
    RelativeLayout layout;

    private ViewPager pager;
    private FragmentPagerAdapter adapter;
    List<android.support.v4.app.Fragment> fragmentList = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SearchFragment searchFragment;
    EmptyFragment fragment;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout) findViewById(R.id.relative_layout);

        sharedPreferences = getSharedPreferences("CURRENTCITY",MODE_PRIVATE);
        editor = sharedPreferences.edit();

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
                bean.setCounty(sharedPreferences.getString("CITY","北京"));
                bean.setAreaId(sharedPreferences.getInt("CITYCODE",101010100));
                RequestWeatherTask task = new RequestWeatherTask(bean);
                task.execute();
            }else {
                WeatherBean bean = new WeatherBean();
                bean.setCounty("青岛");
                bean.setTime("11:00");
                bean.setMin_tem("25º");
                bean.setWindStrength("2级");
                bean.setWindDirection("东南风");
                bean.setAreaId(10111100);
                bean.setMax_tem("32º");
                bean.setTemperature("30º");
                bean.setWeather("多云");
                bean.setProvince("山东省");
                bean.setYesType("多云");
                bean.setYesLowTemp("25度");
                bean.setYesHighTemp("32度");
                bean.setTomType("中雨");
                bean.setTomLowTemp("23度");
                bean.setTomHighTemp("28度");
                bean.setTdatType("小雨");
                bean.setTdatLowTemp("25度");
                bean.setTdatHighTemp("30度");
                MainFragment main = new MainFragment(bean);
                fragmentList.add(main);
            }
        }

        pager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        pager.setAdapter(adapter);

        initSystemBar(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                break;
            case R.id.action_search:
                break;
            case R.id.share:
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
        editor.putString("CITY",bean.getCounty());
        editor.putInt("CITYCODE",bean.getAreaId());
        editor.commit();
        RequestWeatherTask task = new RequestWeatherTask(bean);
        task.execute();
    }

    @Override
    public void stateChangeListener(boolean InternetState) {
        if (InternetState){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            sharedPreferences = getSharedPreferences("CURRENTCITY",MODE_PRIVATE);
            editor = sharedPreferences.edit();
            if (sharedPreferences.contains("CITY")){
                WeatherBean bean = new WeatherBean();
                bean.setCounty(sharedPreferences.getString("CITY","北京"));
                bean.setAreaId(sharedPreferences.getInt("CITYCODE",101010100));
                RequestWeatherTask task = new RequestWeatherTask(bean);
                task.execute();
            }else {
                WeatherBean bean = new WeatherBean();
                bean.setCounty("青岛");
                bean.setTime("11:00");
                bean.setMin_tem("25º");
                bean.setWindStrength("2级");
                bean.setWindDirection("东南风");
                bean.setAreaId(10111100);
                bean.setMax_tem("32º");
                bean.setTemperature("30º");
                bean.setWeather("多云");
                bean.setProvince("山东省");
                bean.setYesType("多云");
                bean.setYesLowTemp("25度");
                bean.setYesHighTemp("32度");
                bean.setTomType("中雨");
                bean.setTomLowTemp("23度");
                bean.setTomHighTemp("28度");
                bean.setTdatType("小雨");
                bean.setTdatLowTemp("25度");
                bean.setTdatHighTemp("30度");
                MainFragment main = new MainFragment(bean);
                fragmentList.add(main);
            }
        }
    }

    private class RequestWeatherTask extends AsyncTask<Void,Void,WeatherBean> {

        private WeatherBean weatherBean;
        private int i = 0;

        public RequestWeatherTask(WeatherBean bean) {
            this.weatherBean = bean;
        }

        @Override
        protected WeatherBean doInBackground(Void... params) {
            String countyName = weatherBean.getCounty();
            int ID = weatherBean.getAreaId();
            String httpUrl = "http://apis.baidu.com/apistore/weatherservice/recentweathers?";
            String httpArg = "cityname="+URLEncoder.encode(countyName)+"&cityid="+ID;
            String url = httpUrl+httpArg;
            String response = HttpUtil.HttpGet(url,"GET");
            return parseJSON(response);
        }

        @Override
        protected void onPostExecute(WeatherBean bean) {
            super.onPostExecute(bean);
            MainFragment fragment = new MainFragment(bean);
            fragmentList.add(fragment);
            adapter.notifyDataSetChanged();
            i++;
            pager.setCurrentItem(i);
            /*temperature.setText(weatherBean.getTemperatue()+"º");
            condition.setText(weatherBean.getWeather()+" "+weatherBean.getMin_tem()+"º~"+weatherBean.getMax_tem()+"º");
            wind.setText(weatherBean.getWindDirection()+" "+weatherBean.getWindStrength());
            air.setText(weatherBean.getTime()+"发布");*/
        }

        private WeatherBean parseJSON(String response) {
            WeatherBean weatherBean = new WeatherBean();
            try {
                JSONObject jsonObject = new JSONObject(response);
                int errNum = jsonObject.getInt("errNum");
                String errMsg = jsonObject.getString("errMsg");
                Log.e("ERROR IN PARSEJSON",errMsg+" "+errNum);

                JSONObject retData = jsonObject.getJSONObject("retData");
                String county = retData.getString("city");
                JSONObject today = retData.getJSONObject("today");
                //今天的天气信息
                String curTemp = today.getString("curTemp");
                //int PM2_5 = today.getInt("aqi");
                String fengxiang = today.getString("fengxiang");
                String fengli = today.getString("fengli");
                String hightemp = today.getString("hightemp");
                String lowtemp = today.getString("lowtemp");
                String type = today.getString("type");
                //明天和后天的天气信息
                JSONArray forecast = retData.getJSONArray("forecast");
                JSONObject tomObject = (JSONObject) forecast.get(1);
                String tomType = tomObject.getString("type");
                String tomHighTemp = tomObject.getString("hightemp");
                String tomLowTemp = tomObject.getString("lowtemp");
                JSONObject tdatObject = (JSONObject) forecast.get(2);
                String tdatType = tdatObject.getString("type");
                String tdatHighTemp = tdatObject.getString("hightemp");
                String tdatLowTemp = tdatObject.getString("lowtemp");
                //昨天的天气信息
                JSONArray history = retData.getJSONArray("history");
                JSONObject yesterday = history.getJSONObject(6);
                String yesType = yesterday.getString("type");
                String yesHighTemp = yesterday.getString("hightemp");
                String yesLowTemp = yesterday.getString("lowtemp");

                weatherBean.setCounty(county);
                weatherBean.setTemperature(curTemp);
                //weatherBean.setPM2_5(PM2_5);
                weatherBean.setWindDirection(fengxiang);
                weatherBean.setWindStrength(fengli);
                weatherBean.setMax_tem(hightemp);
                weatherBean.setMin_tem(lowtemp);
                weatherBean.setWeather(type);
                weatherBean.setYesType(yesType);
                weatherBean.setYesHighTemp(yesHighTemp);
                weatherBean.setYesLowTemp(yesLowTemp);
                weatherBean.setTomType(tomType);
                weatherBean.setTomHighTemp(tomHighTemp);
                weatherBean.setTomLowTemp(tomLowTemp);
                weatherBean.setTdatType(tdatType);
                weatherBean.setTdatHighTemp(tdatHighTemp);
                weatherBean.setTdatLowTemp(tdatLowTemp);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weatherBean;
        }
    }
}
