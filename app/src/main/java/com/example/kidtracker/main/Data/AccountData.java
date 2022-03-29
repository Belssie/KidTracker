package com.example.kidtracker.main.Data;

public class AccountData {
    private String id;
    private String email;
    private String pw;

    public AccountData(String id, String email, String pw) {
        this.id = id;
        this.email = email;
        this.pw = pw;
    }

    public AccountData() {}

    @Override
    public String toString() {
        return "AccountData{" +
                "email='" + email + '\'' +
                ", pw='" + pw + '\'' +
                '}';
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

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