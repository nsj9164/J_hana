package com.example.test2;

public class ListViewItemReview {
    private String re_no;
    private String stu_no;
    private String user_id;
    private String rm_name;
    private Float rate;
    private String date;
    private String text;


    public void setRe(String re) {re_no = re;}
    public void setStu(String num) {
        stu_no = num ;
    }
    public void setUser(String id) {
        user_id = id ;
    }
    public void setRoom(String room) {
        rm_name = room ;
    }
        public void setStar(Float star) {
        rate = star ;
    }
    public void setDate(String cal) {
        date = cal;
    }
    public void setText(String txt) {
        text = txt;
    }


    public String getRe() {
        return this.re_no ;
    }
    public String getStu() {
        return this.stu_no ;
    }
    public String getUser() {
        return this.user_id ;
    }
    public String getRoom() {
        return this.rm_name ;
    }
       public Float getStar() {
        return this.rate ;
    }
    public String getDate() {
        return this.date;
    }
    public String getText() {
        return this.text;
    }
}
