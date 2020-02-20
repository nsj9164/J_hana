package com.example.test2;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class gridItem {
    private Bitmap iconDrawable ;
    private String numStr;
    private String stuStr;
    private String titleStr ;
    private String userStr ;
    private String favorStr;
    private int likeDrawable;

    public void setIcon(Bitmap icon) { iconDrawable = icon ; }
    public void setNum(String num) {
        numStr = num ;
    }
    public void setStu(String stu) {
        stuStr = stu ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setUser(String user) {
        userStr = user ;
    }
    public void setFavor(String favor) {
        favorStr = favor ;
    }
    public void setLike(int like) { likeDrawable = like ; }

    public Bitmap getIcon() {
        return this.iconDrawable ;
    }
    public String getNum() {
        return this.numStr ;
    }
    public String getStu() {
        return this.stuStr ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getUser() {
        return this.userStr ;
    }
    public String getFavor() {
        return this.favorStr ;
    }
    public int getLike() {
        return this.likeDrawable ;
    }
}