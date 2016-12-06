package com.example.apple.weatherforecast;

/**
 * Created by Rawght Steven on 7/28/16, 15.
 * Email:rawghtsteven@gmail.com
 */
public class WeatherBean {

    private String Province;
    private String City;
    private String County;
    private String Weather;
    private String temperature;
    private String max_tem;
    private String min_tem;
    private int areaId;
    private String WindDirection;
    private String WindStrength;
    private String Time;
    private int PM2_5;

    private String yesType;
    private String yesHighTemp;
    private String yesLowTemp;
    private String tomType;
    private String tomHighTemp;
    private String tomLowTemp;
    private String tdatType;
    private String tdatHighTemp;
    private String tdatLowTemp;

    public int getPM2_5() {
        return PM2_5;
    }

    public void setPM2_5(int PM2_5) {
        this.PM2_5 = PM2_5;
    }

    public String getYesHighTemp() {
        return yesHighTemp;
    }

    public void setYesHighTemp(String yesHighTemp) {
        this.yesHighTemp = yesHighTemp;
    }

    public String getYesType() {
        return yesType;
    }

    public void setYesType(String yesType) {
        this.yesType = yesType;
    }

    public String getYesLowTemp() {
        return yesLowTemp;
    }

    public void setYesLowTemp(String yesLowTemp) {
        this.yesLowTemp = yesLowTemp;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTomType() {
        return tomType;
    }

    public void setTomType(String tomType) {
        this.tomType = tomType;
    }

    public String getTomHighTemp() {
        return tomHighTemp;
    }

    public String getTomLowTemp() {
        return tomLowTemp;
    }

    public void setTomLowTemp(String tomLowTemp) {
        this.tomLowTemp = tomLowTemp;
    }

    public String getTdatType() {
        return tdatType;
    }

    public void setTdatType(String tdatType) {
        this.tdatType = tdatType;
    }

    public String getTdatHighTemp() {
        return tdatHighTemp;
    }

    public void setTdatHighTemp(String tdatHighTemp) {
        this.tdatHighTemp = tdatHighTemp;
    }

    public String getTdatLowTemp() {
        return tdatLowTemp;
    }

    public void setTdatLowTemp(String tdatLowTemp) {
        this.tdatLowTemp = tdatLowTemp;
    }

    public void setTomHighTemp(String tomHighTemp) {
        this.tomHighTemp = tomHighTemp;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getWeather() {
        return Weather;
    }

    public void setWeather(String weather) {
        Weather = weather;
    }

    public String getMax_tem() {
        return max_tem;
    }

    public void setMax_tem(String max_tem) {
        this.max_tem = max_tem;
    }

    public String getMin_tem() {
        return min_tem;
    }

    public void setMin_tem(String min_tem) {
        this.min_tem = min_tem;
    }

    public String getWindDirection() {
        return WindDirection;
    }

    public void setWindDirection(String windDirection) {
        WindDirection = windDirection;
    }

    public String getWindStrength() {
        return WindStrength;
    }

    public void setWindStrength(String windStrength) {
        WindStrength = windStrength;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }
}
