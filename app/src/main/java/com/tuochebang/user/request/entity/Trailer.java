package com.tuochebang.user.request.entity;

import java.io.Serializable;

public class Trailer implements Serializable {
    private String mobile;
    private String name;
    private String picture;
    private int trailerId;
    private String type;

    public int getTrailerId() {
        return this.trailerId;
    }

    public void setTrailerId(int trailerId) {
        this.trailerId = trailerId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String toString() {
        return "Trailer{trailerId=" + this.trailerId + ", type='" + this.type + '\'' + ", name='" + this.name + '\'' + ", mobile='" + this.mobile + '\'' + ", picture='" + this.picture + '\'' + '}';
    }
}
