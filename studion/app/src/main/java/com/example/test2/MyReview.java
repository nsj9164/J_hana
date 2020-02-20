package com.example.test2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyReview extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    int mr_cnt;
    ListView listview;
    float _rate = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        try {
            String result2;
            CustomTask2 task2 = new CustomTask2();
            result2 = task2.execute(user.getEmail()).get();
            mr_cnt = Integer.parseInt(result2);
            Toast.makeText(this, result2, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }

        ListViewAdapterReview adapter;

        adapter = new ListViewAdapterReview();
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        TextView nothing = findViewById(R.id.nothing);
        nothing.setVisibility(View.INVISIBLE);

        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute(user.getEmail()).get();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

            if (result.startsWith(null)) {
                nothing.setVisibility(View.VISIBLE);
            } else {

                for (int i = 0; i < mr_cnt; i++) {
                    int idx = result.indexOf("a;");
                    String mail1 = result.substring(0, idx);
                    adapter.addItem("", "", "", "", _rate, "", "");

                    ((ListViewItemReview) adapter.getItem(i)).setRe(mail1);
                    adapter.notifyDataSetChanged();

                    String mail2 = result.substring(idx + 2);
                    int idx2 = mail2.indexOf("b;");
                    final String bottom = mail2.substring(0, idx2);
                    float star = Float.parseFloat(bottom);
                    ((ListViewItemReview) adapter.getItem(i)).setStar(star);
                    adapter.notifyDataSetChanged();

                    String mail3 = mail2.substring(idx2 + 2);
                    int idx3 = mail3.indexOf("c;");
                    final String hh2 = mail3.substring(0, idx3);
                    ((ListViewItemReview) adapter.getItem(i)).setText(hh2);
                    adapter.notifyDataSetChanged();

                    String mail4 = mail3.substring(idx3 + 2);
                    int idx4 = mail4.indexOf("d;");
                    String hh4 = mail4.substring(0, idx4);
                    ((ListViewItemReview) adapter.getItem(i)).setDate(hh4);
                    adapter.notifyDataSetChanged();

                    String mail5 = mail4.substring(idx4 + 2);
                    int idx5 = mail5.indexOf("e;");
                    String hh5 = mail5.substring(0, idx5);
                    ((ListViewItemReview) adapter.getItem(i)).setStu(hh5);
                    adapter.notifyDataSetChanged();

                    String mail6 = mail5.substring(idx5 + 2);
                    int idx6 = mail6.indexOf("f;");
                    String hh6 = mail6.substring(0, idx6);
                    ((ListViewItemReview) adapter.getItem(i)).setRoom(hh6);
                    adapter.notifyDataSetChanged();

                    String mail7 = mail6.substring(idx6 + 2);
                    int idx7 = mail7.indexOf("g;");
                    String hh7 = mail7.substring(0, idx7);
                    ((ListViewItemReview) adapter.getItem(i)).setUser(hh7);
                    adapter.notifyDataSetChanged();

                    String rest = mail7.substring(idx7 + 2);
                    result = rest;
                }
            }

        } catch (Exception e) {

        }
    }

    //  스튜디오 이름
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_review_mine.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "user_id=" + strings[0];
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


    //  스튜디오 이름
    class CustomTask2 extends AsyncTask<String, Void, String> {
        String sendMsg2, receiveMsg2;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_review_mine_count.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg2 = "user_id=" + strings[0];
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
}
