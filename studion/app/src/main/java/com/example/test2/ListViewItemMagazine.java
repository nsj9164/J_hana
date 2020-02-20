package com.example.test2;

import android.graphics.Bitmap;

public class ListViewItemMagazine {
    private Bitmap iconDrawable ;
    private String numStr;
    private String startStr ;
    private String endStr ;

    public void setIcon(Bitmap icon) {
        iconDrawable = icon ;
    }
    public void setNum(String num) {
        numStr = num ;
    }
    public void setStart(String start) {
        startStr = start ;
    }
    public void setEnd(String end) {
        endStr = end ;
    }

    public Bitmap getIcon() {
        return this.iconDrawable ;
    }
    public String getNum() {
        return this.numStr ;
    }
    public String getStart() {
        return this.startStr ;
    }
    public String getEnd() {
        return this.endStr ;
    }
}