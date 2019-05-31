package com.tuochebang.user.request.entity;

import java.io.Serializable;

public class ReturnCarInfo implements Serializable {
    private String begin;
    private String corporate;
    private String e_latitude;
    private String e_longitude;
    private String end;
    private String latitude;
    private String longitude;
    private String mobile;
    private double money;
    private String name;
    private String returnId;
    private int status;
    private String time;
    private String type;

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getE_latitude() {
        return this.e_latitude;
    }

    public void setE_latitude(String e_latitude) {
        this.e_latitude = e_latitude;
    }

    public String getE_longitude() {
        return this.e_longitude;
    }

    public void setE_longitude(String e_longitude) {
        this.e_longitude = e_longitude;
    }

    public String getReturnId() {
        return this.returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "ReturnCarInfo{returnId='" + this.returnId + '\'' + ", status=" + this.status + ", corporate='" + this.corporate + '\'' + ", mobile='" + this.mobile + '\'' + ", time='" + this.time + '\'' + ", begin='" + this.begin + '\'' + ", end='" + this.end + '\'' + ", money=" + this.money + ", name='" + this.name + '\'' + ", type='" + this.type + '\'' + '}';
    }
}
