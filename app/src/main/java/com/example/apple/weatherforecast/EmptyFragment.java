package com.example.apple.weatherforecast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Rawght Steven on 8/18/16, 15.
 * Email:rawghtsteven@gmail.com
 */
public class EmptyFragment extends Fragment{

    @BindView(R.id.refresh) ImageButton refresh;

    private Unbinder unbinder;
    InternetStateChanged changed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager con=(ConnectivityManager)getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
                boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
                boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
                if (!wifi&&!internet){
                    Snackbar snackbar = Snackbar.make(refresh,"您的网络出现了问题",Snackbar.LENGTH_LONG).setAction("去设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    });
                    snackbar.show();
                }else {
                    changed.stateChangeListener(true);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changed = (InternetStateChanged) activity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface InternetStateChanged{
        void stateChangeListener(boolean InternetState);
    }
}
