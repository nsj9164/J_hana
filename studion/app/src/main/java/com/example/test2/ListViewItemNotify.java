package com.example.test2;

public class ListViewItemNotify {
    private String noStr;
    private String titleStr ;
    private String dateStr ;

    public void setNo(String no) {
        noStr = no ;
    }
    public void setDate(String date) {
        dateStr = date ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }

    public String getNo() {
        return this.noStr ;
    }
    public String getDate() {
        return this.dateStr ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
}