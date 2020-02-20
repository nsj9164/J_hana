package com.example.test2;

import android.graphics.Bitmap;

public class ListViewItemStudio {
    private Bitmap iconDrawable;
    private String numStr;
    private String nameStr;
    private String rateStr;
    private String reviewStr;
    private String priceStr;
    private String tagStr;
    private String mapStr;

    public void setIcon(Bitmap icon) {
        iconDrawable = icon ;
    }
    public void setNum(String num) {
        numStr = num ;
    }
    public void setName(String name) {
        nameStr = name ;
    }
    public void setRate(String rate) {
        rateStr = rate ;
    }
    public void setReview(String review) {
        reviewStr = review ;
    }
    public void setPrice(String price) {
        priceStr = price ;
    }
    public void setTag(String tag) {
        tagStr = tag ;
    }
    public void setMap(String map) {mapStr = map;}

    public Bitmap getIcon() { return this.iconDrawable ; }
    public String getNum() { return this.numStr ; }
    public String getName() { return this.nameStr ; }
    public String getRate() { return this.rateStr ; }
    public String getReview() { return this.reviewStr ; }
    public String getPrice() { return this.priceStr ; }
    public String getTag() { return this.tagStr ; }
    public String getMap() { return this.mapStr ; }
}