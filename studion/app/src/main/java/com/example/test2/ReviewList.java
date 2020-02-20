package com.example.test2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReviewList extends AppCompatActivity {

    String stu_no;
    String date, rm_no, rm_name, user_id, text;
    Bitmap bitimg;
    int rev_cnt;
    float _rate = 0.0f;

    ListView listview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        Intent intent = getIntent();
        stu_no = intent.getExtras().getString("stu2");

//      평점 START
        RatingBar rb = findViewById(R.id.ratingBar);
        TextView rb_num = findViewById(R.id.rb_num12);
        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute(stu_no).get();

            if (result == null) {
                rb_num.setText("0");
            } else {
                rb_num.setText(result);
            }

        } catch (Exception e) {

        }

        TextView rb_num1 = findViewById(R.id.rb_num1);
        TextView rb_num2 = findViewById(R.id.rb_num2);
        TextView rb_num3 = findViewById(R.id.rb_num3);
        TextView rb_num4 = findViewById(R.id.rb_num4);
        RatingBar rb1 = findViewById(R.id.ratingBar1);
        RatingBar rb2 = findViewById(R.id.ratingBar2);
        RatingBar rb3 = findViewById(R.id.ratingBar3);
        RatingBar rb4 = findViewById(R.id.ratingBar4);

        try {
            String result2;
            ReviewList.CustomTask2 task2 = new ReviewList.CustomTask2();
            result2 = task2.execute(stu_no).get();
            Toast.makeText(this, stu_no, Toast.LENGTH_SHORT).show();

            for (int i = 0; i < 2; i++) {
                int ss = result2.indexOf(";");
                String mail1 = result2.substring(0, ss);
                String mail2 = result2.substring(ss + 1);
                int ss2 = mail2.indexOf(";");
                String bottom = mail2.substring(0, ss2);
                String rest = mail2.substring(ss2 + 1);

                result2 = rest;

                if (i == 0) {
                    rb_num1.setText(mail1);
                    float rate1 = Float.parseFloat(mail1);
                    rb1.setRating(rate1);

                    rb_num2.setText(bottom);
                    float rate2 = Float.parseFloat(bottom);
                    rb2.setRating(rate2);
                } else if (i == 1) {
                    rb_num3.setText(mail1);
                    float rate3 = Float.parseFloat(mail1);
                    rb3.setRating(rate3);

                    rb_num4.setText(bottom);
                    float rate4 = Float.parseFloat(bottom);
                    rb4.setRating(rate4);
                }
            }
//            }

        } catch (Exception e) {

        }
//      평점 END

        TextView nothing = findViewById(R.id.nothing);
        nothing.setVisibility(View.INVISIBLE);

//      리뷰 갯수 START
//error
        try {
            String result5;
            ReviewList.CustomTask5 task5 = new ReviewList.CustomTask5();
            result5 = task5.execute(stu_no).get();
            rev_cnt = Integer.parseInt(result5) + 1;
        } catch (Exception e) {

        }
//      리뷰 갯수 END

//      ReviewList START

        ListViewAdapterReview adapter2;

        adapter2 = new ListViewAdapterReview();
        listview2 = (ListView) findViewById(R.id.listview2);
        listview2.setAdapter(adapter2);

        try {
            String result3;
            CustomTask3 task3 = new CustomTask3();
            result3 = task3.execute(stu_no).get();

            Toast.makeText(this, rev_cnt, Toast.LENGTH_SHORT).show();
            for (int i = 0; i < rev_cnt; i++) {
                int idx = result3.indexOf("a;");
                String mail1 = result3.substring(0, idx);
                adapter2.addItem("", "", "", "", _rate, "", "");

                ((ListViewItemReview) adapter2.getItem(i)).setRe(mail1);
                adapter2.notifyDataSetChanged();

                String mail2 = result3.substring(idx + 2);
                int idx2 = mail2.indexOf("b;");
                final String bottom = mail2.substring(0, idx2);
                float star = Float.parseFloat(bottom);
                ((ListViewItemReview) adapter2.getItem(i)).setStar(star);
                adapter2.notifyDataSetChanged();

                String mail3 = mail2.substring(idx2 + 2);
                int idx3 = mail3.indexOf("c;");
                final String hh2 = mail3.substring(0, idx3);
                ((ListViewItemReview) adapter2.getItem(i)).setText(hh2);
                adapter2.notifyDataSetChanged();

                String mail4 = mail3.substring(idx3 + 2);
                int idx4 = mail4.indexOf("d;");
                String hh4 = mail4.substring(0, idx4);
                ((ListViewItemReview) adapter2.getItem(i)).setDate(hh4);
                adapter2.notifyDataSetChanged();

                String mail5 = mail4.substring(idx4 + 2);
                int idx5 = mail5.indexOf("e;");
                String hh5 = mail5.substring(0, idx5);
                ((ListViewItemReview) adapter2.getItem(i)).setStu(hh5);
                adapter2.notifyDataSetChanged();

                String mail6 = mail5.substring(idx5 + 2);
                int idx6 = mail6.indexOf("f;");
                String hh6 = mail6.substring(0, idx6);
                ((ListViewItemReview) adapter2.getItem(i)).setRoom(hh6);
                adapter2.notifyDataSetChanged();

                String mail7 = mail6.substring(idx6 + 2);
                int idx7 = mail7.indexOf("g;");
                String hh7 = mail7.substring(0, idx7);
                ((ListViewItemReview) adapter2.getItem(i)).setUser(hh7);
                adapter2.notifyDataSetChanged();

                String rest = mail7.substring(idx7 + 2);
                result3 = rest;
            }

        } catch (Exception e) {

        }
//      ReviewList END

    }

    //  총 별점
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_review4.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "stu_no=" + strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "통신 결과" + conn.getResponseCode() + "에러", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }


    //  분야별 별점
    class CustomTask2 extends AsyncTask<String, Void, String> {
        String sendMsg2, receiveMsg2;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_review5.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg2 = "stu_no=" + strings[0];
                osw.write(sendMsg2);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg2 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "통신 결과" + conn.getResponseCode() + "에러", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg2;
        }
    }


    //  listview_review
    class CustomTask3 extends AsyncTask<String, Void, String> {
        String sendMsg3, receiveMsg3;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_review6.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg3 = "stu_no=" + strings[0];
                osw.write(sendMsg3);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg3 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "통신 결과" + conn.getResponseCode() + "에러", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg3;
        }
    }

    //  후기 개수
    class CustomTask5 extends AsyncTask<String, Void, String> {
        String receiveMsg5, sendMsg5;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;

                URL url = new URL("http://studionkorea.com/stu_review_count.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg5 = "stu_no=" + strings[0];
                osw.write(sendMsg5);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {

                        buffer.append(str);

                    }
                    receiveMsg5 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "??? ???" + conn.getResponseCode() + "????", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg5;
        }
    }
}
