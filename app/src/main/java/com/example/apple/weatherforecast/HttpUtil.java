package com.example.apple.weatherforecast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rawght Steven on 7/28/16, 16.
 * Email:rawghtsteven@gmail.com
 */
public class HttpUtil {

    public static String HttpGet(String urls,String method){

        HttpURLConnection connection = null;
        InputStream in = null;
        StringBuffer response = new StringBuffer();

        try {
            URL url = new URL(urls);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("apikey","0089e54ddc9caab4f66b665d0242ef59");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode == 200){
                in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while((line = reader.readLine())!=null){
                    response.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                connection.disconnect();
            }
        }
        return response.toString();
    }
}
