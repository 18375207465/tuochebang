package com.tuochebang.user.request.entity;

import java.io.Serializable;
import java.util.List;

public class TuocheRequestInfo implements Serializable {
    private String begin;
    private CarInfo car;
    private String corporate;
    private DriverInfo driverInfo;
    private double e_latitude;
    private double e_longitude;
    private String end;
    private int isCrane;
    private int isReturn;
    private double latitude;
    private double longitude;
    private String mobile;
    private double money;
    private String otherCar;
    private String payName;
    private List<String> picture;
    private String remark;
    private String requestId;
    private int status;
    private String time;
    private String typeName;

    public class DriverInfo implements Serializable {
        private String mobile;
        private String nickName;
        private String picture;

        public String getNickName() {
            return this.nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPicture() {
            return this.picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getMobile() {
            return this.mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }

    public int getIsCrane() {
        return this.isCrane;
    }

    public void setIsCrane(int isCrane) {
        this.isCrane = isCrane;
    }

    public String getOtherCar() {
        return this.otherCar;
    }

    public void setOtherCar(String otherCar) {
        this.otherCar = otherCar;
    }

    public DriverInfo getDriverInfo() {
        return this.driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getE_latitude() {
        return this.e_latitude;
    }

    public void setE_latitude(double e_latitude) {
        this.e_latitude = e_latitude;
    }

    public double getE_longitude() {
        return this.e_longitude;
    }

    public void setE_longitude(double e_longitude) {
        this.e_longitude = e_longitude;
    }

    public int getIsReturn() {
        return this.isReturn;
    }

    public void setIsReturn(int isReturn) {
        this.isReturn = isReturn;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCorporate() {
        return this.corporate;
    }

    public void setCorporate(String corporate) {
        this.corporate = corporate;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBegin() {
        return this.begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return this.end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPayName() {
        return this.payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public CarInfo getCar() {
        return this.car;
    }

    public void setCar(CarInfo car) {
        this.car = car;
    }

    public List<String> getPicture() {
        return this.picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public String toString() {
        return "TuocheRequestInfo{status=" + this.status + ", corporate='" + this.corporate + '\'' + ", mobile='" + this.mobile + '\'' + ", time='" + this.time + '\'' + ", begin='" + this.begin + '\'' + ", end='" + this.end + '\'' + ", money=" + this.money + ", typeName='" + this.typeName + '\'' + ", payName='" + this.payName + '\'' + ", isReturn=" + this.isReturn + ", car=" + this.car + ", picture=" + this.picture + ", remark='" + this.remark + '\'' + ", requestId='" + this.requestId + '\'' + ", latitude=" + this.latitude + ", longitude=" + this.longitude + ", e_latitude=" + this.e_latitude + ", e_longitude=" + this.e_longitude + ", driverInfo=" + this.driverInfo + ", otherCar='" + this.otherCar + '\'' + '}';
    }
}
