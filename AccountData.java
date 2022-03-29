package com.example.kidtracker;

public class AccountData {
    private String email;
    private String pw;

    public AccountData(String email, String pw){
        this.email = email;
        this.pw = pw;
    }

    public AccountData(){}

    @Override
    public String toString() {
        return "AccountData{" +
                "email='" + email + '\'' +
                ", pw='" + pw + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
