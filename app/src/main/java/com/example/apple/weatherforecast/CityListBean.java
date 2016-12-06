package com.example.apple.weatherforecast;

import java.util.List;

/**
 * Created by Rawght Steven on 04/12/2016, 19.
 * Email:rawghtsteven@gmail.com
 */

public class CityListBean {

    private int errNum;
    private String errMsg;
    private List<RetData> retData;

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public List<RetData> getRetData() {
        return retData;
    }

    public void setRetData(List<RetData> retData) {
        this.retData = retData;
    }

    public static class RetData{
        private String province_cn;
        private String district_cn;
        private String name_cn;
        private String name_en;
        private int area_id;

        public String getProvince_cn() {
            return province_cn;
        }

        public void setProvince_cn(String province_cn) {
            this.province_cn = province_cn;
        }

        public String getDistrict_cn() {
            return district_cn;
        }

        public void setDistrict_cn(String district_cn) {
            this.district_cn = district_cn;
        }

        public String getName_cn() {
            return name_cn;
        }

        public void setName_cn(String name_cn) {
            this.name_cn = name_cn;
        }

        public String getName_en() {
            return name_en;
        }

        public void setName_en(String name_en) {
            this.name_en = name_en;
        }

        public int getArea_id() {
            return area_id;
        }

        public void setArea_id(int area_id) {
            this.area_id = area_id;
        }
    }
}
