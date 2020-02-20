package com.example.test2;


import android.graphics.Bitmap;

public class ListViewItem {
    private Bitmap iconDrawable ;
    private String numStr ;
    private String titleStr ;
    private String descStr ;

    public void setIcon(Bitmap icon) {
        iconDrawable = icon ;
    }
    public void setNum(String num) {
        numStr = num ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public Bitmap getIcon() {
        return this.iconDrawable ;
    }
    public String getNum() {
        return this.numStr ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}