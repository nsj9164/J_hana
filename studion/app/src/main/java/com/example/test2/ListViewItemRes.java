package com.example.test2;


import android.graphics.Bitmap;


public class ListViewItemRes {
    private Bitmap iconDrawable ;
    private String numStr ;
    private String snameStr ;
    private String rnameStr ;
    private String timeStr ;
    private String dateStr ;
    private String priceStr ;
    private String snumStr ;
    private String rnumStr ;
    private String stateStr ;

    public void setIcon(Bitmap icon) {
        iconDrawable = icon ;
    }
    public void setNum(String num) {
        numStr = num ;
    }
    public void setSname(String sname) {
        snameStr = sname ;
    }
    public void setRname(String rname) {
        rnameStr = rname ;
    }
    public void setTime(String time) {
        timeStr = time ;
    }
    public void setDate(String date) { dateStr = date ; }
    public void setPrice(String price) { priceStr = price ; }
    public void setSnum(String snum) { snumStr = snum ; }
    public void setRnum(String rnum) {
        rnumStr = rnum ;
    }
    public void setState(String state) {
        stateStr = state ;
    }

    public Bitmap getIcon() {
        return this.iconDrawable ;
    }
    public String getNum() {
        return this.numStr ;
    }
    public String getSname() {
        return this.snameStr ;
    }
    public String getRname() {
        return this.rnameStr ;
    }
    public String getTime() {
        return this.timeStr ;
    }
    public String getDate() {
        return this.dateStr ;
    }
    public String getPrice() {
        return this.priceStr ;
    }
    public String getSnum() {
        return this.snumStr ;
    }
    public String getRnum() {
        return this.rnumStr ;
    }
    public String getState() {
        return this.stateStr ;
    }
}
