package com.example.test2;

import android.graphics.Bitmap;

public class ListViewItemHeart {
    private Bitmap iconDrawable;
    private String numStr;
    private String nameStr;
    private String priceStr;

    public void setIcon(Bitmap icon) {
        iconDrawable = icon ;
    }
    public void setNum(String num) {
        numStr = num ;
    }
    public void setName(String name) {
        nameStr = name ;
    }
    public void setPrice(String price) {
        priceStr = price ;
    }

    public Bitmap getIcon() { return this.iconDrawable ; }
    public String getNum() { return this.numStr ; }
    public String getName() { return this.nameStr ; }
    public String getPrice() { return this.priceStr ; }
}