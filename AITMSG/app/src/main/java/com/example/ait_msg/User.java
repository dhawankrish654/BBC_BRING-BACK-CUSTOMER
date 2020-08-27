package com.example.ait_msg;

public class User {
    private String DOB;
    private String name;
    private String phone;
    private String DateOFJoin;

    public User(String DOB, String name, String phone, String dateOFJoin) {
        this.DOB = DOB;
        this.name = name;
        this.phone = phone;
        DateOFJoin = dateOFJoin;
    }

    public User() {
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOFJoin() {
        return DateOFJoin;
    }

    public void setDateOFJoin(String dateOFJoin) {
        DateOFJoin = dateOFJoin;
    }
}
