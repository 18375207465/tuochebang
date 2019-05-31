package com.tuochebang.user.request.entity;

public class IdCard {
    private String address;
    private String birthday;
    private String sex;

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        return "IdCard{sex='" + this.sex + '\'' + ", birthday='" + this.birthday + '\'' + ", address='" + this.address + '\'' + '}';
    }
}
