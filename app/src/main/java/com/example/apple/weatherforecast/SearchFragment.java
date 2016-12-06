package com.example.apple.weatherforecast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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

/**
 * Created by Rawght Steven on 7/29/16, 11.
 * Email:rawghtsteven@gmail.com
 */
public class SearchFragment extends DialogFragment {

    @BindView(R.id.list_view) ListView listView;

    public static final String URL = "http://apis.baidu.com/apistore/weatherservice/";
    public static final String TAG = "SEARCH CITY";
    private String INPUT;
    private Unbinder unbinder;
    CallBackValue callBackValue;
    MyAdapter myAdapter;
    List<WeatherBean> beans = new ArrayList<>();

    public void setINPUT(String INPUT) {
        this.INPUT = INPUT;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);
        myAdapter = new MyAdapter(beans);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherBean mbean = (WeatherBean) parent.getItemAtPosition(position);
                callBackValue.sendValue(mbean);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
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
                .baseUrl(URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        CityListService service = retrofit.create(CityListService.class);
        service.getCityList(INPUT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Subscriber<CityListBean>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG,"Search completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"Search failed");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CityListBean cityListBean) {
                        Log.e(TAG,cityListBean.getRetData().get(0).getName_cn()+"  "+cityListBean.getRetData().get(0).getArea_id());
                        for (int i=0;i<cityListBean.getRetData().size();i++){
                            WeatherBean bean = new WeatherBean();
                            bean.setCounty(cityListBean.getRetData().get(i).getName_cn());
                            bean.setCity(cityListBean.getRetData().get(i).getDistrict_cn());
                            bean.setProvince(cityListBean.getRetData().get(i).getProvince_cn());
                            bean.setAreaId(cityListBean.getRetData().get(i).getArea_id());
                            beans.add(bean);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBackValue = (CallBackValue) activity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class MyAdapter extends BaseAdapter {

        private List<WeatherBean> weatherBeanList = new ArrayList<>();

        public MyAdapter(List<WeatherBean> weatherBeenList) {
            this.weatherBeanList = weatherBeenList;
        }

        @Override
        public int getCount() {
            return weatherBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            WeatherBean weatherBean = weatherBeanList.get(position);
            return weatherBean;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            WeatherBean bean = weatherBeanList.get(position);
            if (convertView==null){
                convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.search_fragment_listview,null);
                viewHolder = new ViewHolder();
                TextView county = (TextView) convertView.findViewById(R.id.county);
                TextView PCC = (TextView) convertView.findViewById(R.id.PCC);
                viewHolder.County = county;
                viewHolder.PCC = PCC;
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Typeface Segoe = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Segoe.TTF");
            Typeface SemiLight =Typeface.createFromAsset(getActivity().getAssets(),"fonts/SegoeSemiLight.TTF");

            viewHolder.County.setTypeface(Segoe);
            viewHolder.PCC.setTypeface(SemiLight);
            viewHolder.County.setText(bean.getCounty());
            viewHolder.PCC.setText(bean.getProvince()+"省 "+bean.getCity()+"市 ");

            return convertView;
        }
    }

    static class ViewHolder{
        TextView County, PCC;
    }

    public interface CallBackValue {
        void sendValue(WeatherBean bean);
    }

    public interface CityListService{
        @GET("citylist")
        Observable<CityListBean> getCityList(@Query("cityname")String cityname);
    }
}
