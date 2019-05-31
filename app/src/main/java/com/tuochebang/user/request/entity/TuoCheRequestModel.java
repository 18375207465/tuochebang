package com.tuochebang.user.request.entity;

import java.io.Serializable;

public class TuoCheRequestModel implements Serializable {
    private String begin;
    private String car;
    private String corporate;
    private String driveId;
    private double e_latitude;
    private double e_longitude;
    private String end;
    private String gearboxId;
    private int isCrane;
    private int isReturn;
    private double latitude;
    private double longitude;
    private String mobile;
    private String modelId;
    private double money;
    private String otherCar;
    private String payId;
    private String picture0;
    private String picture1;
    private String picture2;
    private String picture3;
    private String requestId;
    private String time;
    private String typeId;

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

    public double getE_longitude() {
        return this.e_longitude;
    }

    public void setE_longitude(double e_longitude) {
        this.e_longitude = e_longitude;
    }

    public double getE_latitude() {
        return this.e_latitude;
    }

    public void setE_latitude(double e_latitude) {
        this.e_latitude = e_latitude;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getTypeId() {
        return this.typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPayId() {
        return this.payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public int getIsReturn() {
        return this.isReturn;
    }

    public void setIsReturn(int isReturn) {
        this.isReturn = isReturn;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getCar() {
        return this.car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getModelId() {
        return this.modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getGearboxId() {
        return this.gearboxId;
    }

    public void setGearboxId(String gearboxId) {
        this.gearboxId = gearboxId;
    }

    public String getDriveId() {
        return this.driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public String getPicture0() {
        return this.picture0;
    }

    public void setPicture0(String picture0) {
        this.picture0 = picture0;
    }

    public String getPicture1() {
        return this.picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture2() {
        return this.picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return this.picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String toString() {
        return "TuoCheRequestModel{corporate='" + this.corporate + '\'' + ", mobile='" + this.mobile + '\'' + ", time='" + this.time + '\'' + ", begin='" + this.begin + '\'' + ", end='" + this.end + '\'' + ", typeId='" + this.typeId + '\'' + ", payId='" + this.payId + '\'' + ", isReturn=" + this.isReturn + ", money=" + this.money + ", car='" + this.car + '\'' + ", modelId='" + this.modelId + '\'' + ", gearboxId='" + this.gearboxId + '\'' + ", driveId='" + this.driveId + '\'' + ", picture0='" + this.picture0 + '\'' + ", picture1='" + this.picture1 + '\'' + ", picture2='" + this.picture2 + '\'' + ", picture3='" + this.picture3 + '\'' + ", requestId='" + this.requestId + '\'' + ", longitude=" + this.longitude + ", e_longitude=" + this.e_longitude + ", latitude=" + this.latitude + ", e_latitude=" + this.e_latitude + '}';
    }
}
