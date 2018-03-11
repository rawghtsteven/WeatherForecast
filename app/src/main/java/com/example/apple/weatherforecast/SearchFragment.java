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
import android.widget.Toast;

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

    public static final String TAG = "SEARCH FRAGMENT";
    private String INPUT;
    private Unbinder unbinder;
    CallBackValue callBackValue;
    MyAdapter myAdapter;
    List<CityBean.showapi_res_body.City> beans = new ArrayList<>();

    public void setINPUT(String INPUT) {
        this.INPUT = INPUT;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityBean.showapi_res_body.City mbean = (CityBean.showapi_res_body.City) parent.getItemAtPosition(position);
                callBackValue.sendValue(mbean.getAreaid());
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
        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder().retryOnConnectionFailure(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.base_url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        service.getCity(MainActivity.API_KEY,INPUT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CityBean>() {
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
                    public void onNext(CityBean cityBean) {
                        if (!cityBean.getShowapi_res_body().getRet_code().equals("0")){
                            Toast.makeText(getActivity(),"您输入的城市有误",Toast.LENGTH_SHORT).show();
                        }else {
                            myAdapter = new MyAdapter(cityBean.getShowapi_res_body().getList());
                            listView.setAdapter(myAdapter);
                        }
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

        private List<CityBean.showapi_res_body.City> cityList = new ArrayList<>();

        public MyAdapter(List<CityBean.showapi_res_body.City> cityList) {
            this.cityList = cityList;
        }

        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public Object getItem(int position) {
            CityBean.showapi_res_body.City city = cityList.get(position);
            return city;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            CityBean.showapi_res_body.City bean = cityList.get(position);
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

            viewHolder.County.setText(bean.getArea());
            viewHolder.PCC.setText(bean.getProv()+"省 "+
                    bean.getDistric()+"市 "+
                    bean.getArea()+"区"
            );

            return convertView;
        }
    }

    static class ViewHolder{
        TextView County, PCC;
    }

    public interface CallBackValue {
        void sendValue(String cityId);
    }
}
