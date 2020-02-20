package com.example.test2;

public class ListViewItemCenter {
    private String numStr;
    private String userStr;
    private String titleStr ;
    private String dateStr ;
    private String stateStr ;

    public void setUser(String user) {
        userStr = user ;
    }
    public void setNum(String num) {
        numStr = num ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDate(String date) {
        dateStr = date ;
    }
    public void setState(String state) {
        stateStr = state ;
    }

    public String getUser() {
        return this.userStr ;
    }
    public String getNum() {
        return this.numStr ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDate() {
        return this.dateStr ;
    }
    public String getState() {
        return this.stateStr ;
    }
}