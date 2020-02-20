package com.example.test2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ResList extends AppCompatActivity {

    Bitmap bitmap;
    int res_cnt;
    String res_no, stu_no, stu_name, state, date, time, today_date, today_time;
    FirebaseAuth firebaseAuth;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_list);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final ListViewAdapterRes adapter;

        adapter = new ListViewAdapterRes();
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        Bitmap bitimg1 = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.stu5);

        TextView main = findViewById(R.id.main);
        main.setText("예약내역");
        ImageView heart = findViewById(R.id.heart);
        heart.setVisibility(View.INVISIBLE);

//      예약 갯수 START
        try {
            String result2;
            CustomTask2 task2 = new CustomTask2();
            result2 = task2.execute(user.getEmail()).get();
            res_cnt = Integer.parseInt(result2) + 1;
        } catch (Exception e) {

        }
//      예약 갯수 END

//      ResList START
        TextView nothing = findViewById(R.id.nothing);
        nothing.setVisibility(View.INVISIBLE);
        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute(user.getEmail()).get();

            if (result == null) {
                nothing.setVisibility(View.VISIBLE);
            } else {
                for (int i = 0; i < res_cnt; i++) {
                    int idx = result.indexOf("a;");
                    final String mail1 = result.substring(0, idx);
                    adapter.addItem(bitimg1, "", "", "", "", "", "", "");

                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(mail1);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setDoInput(true);
                                conn.connect();
                                InputStream is = conn.getInputStream();
                                bitmap = BitmapFactory.decodeStream(is);
                            } catch (IOException ex) {
                            }
                        }
                    };
                    mThread.start();
                    try {
                        mThread.join();
                        ((ListViewItemRes) adapter.getItem(i)).setIcon(bitmap);
                        adapter.notifyDataSetChanged();
                    } catch (InterruptedException e) {

                    }

                    String mail2 = result.substring(idx + 2);
                    int idx2 = mail2.indexOf("b;");
                    String bottom = mail2.substring(0, idx2);
                    ((ListViewItemRes) adapter.getItem(i)).setNum(bottom);
                    adapter.notifyDataSetChanged();

                    String mail3 = mail2.substring(idx2 + 2);
                    int idx3 = mail3.indexOf("c;");
                    final String hh2 = mail3.substring(0, idx3);
                    ((ListViewItemRes) adapter.getItem(i)).setSname(hh2);
                    adapter.notifyDataSetChanged();

                    String mail4 = mail3.substring(idx3 + 2);
                    int idx4 = mail4.indexOf("d;");
                    String hh4 = mail4.substring(0, idx4);
                    ((ListViewItemRes) adapter.getItem(i)).setRname(hh4);
                    adapter.notifyDataSetChanged();

                    String mail5 = mail4.substring(idx4 + 2);
                    int idx5 = mail5.indexOf("e;");
                    String hh5 = mail5.substring(0, idx5);
                    ((ListViewItemRes) adapter.getItem(i)).setTime(hh5);
                    adapter.notifyDataSetChanged();

                    String mail6 = mail5.substring(idx5 + 2);
                    int idx6 = mail6.indexOf("f;");
                    String hh6 = mail6.substring(0, idx6);
                    ((ListViewItemRes) adapter.getItem(i)).setDate(hh6);
                    adapter.notifyDataSetChanged();

                    String mail7 = mail6.substring(idx6 + 2);
                    int idx7 = mail7.indexOf("g;");
                    String hh7 = mail7.substring(0, idx7);
                    ((ListViewItemRes) adapter.getItem(i)).setPrice(hh7);
                    adapter.notifyDataSetChanged();

                    String mail8 = mail7.substring(idx7 + 2);
                    int idx8 = mail8.indexOf("h;");
                    String hh8 = mail8.substring(0, idx8);
                    ((ListViewItemRes) adapter.getItem(i)).setSnum(hh8);
                    adapter.notifyDataSetChanged();

                    String mail9 = mail8.substring(idx8 + 2);
                    int idx9 = mail9.indexOf("i;");
                    String hh9 = mail9.substring(0, idx9);
                    ((ListViewItemRes) adapter.getItem(i)).setRnum(hh9);
                    adapter.notifyDataSetChanged();

                    String mail10 = mail9.substring(idx9 + 2);
                    int idx10 = mail10.indexOf("j;");
                    String hh10 = mail10.substring(0, idx10);
                    ((ListViewItemRes) adapter.getItem(i)).setState(hh10);
                    adapter.notifyDataSetChanged();

                    String rest = mail10.substring(idx10 + 2);
                    result = rest;
                };
            }

        } catch (Exception e) {

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                res_no = ((ListViewItemRes) adapter.getItem(i)).getNum();
                stu_name = ((ListViewItemRes) adapter.getItem(i)).getSname();
                stu_no = ((ListViewItemRes) adapter.getItem(i)).getSnum();
                state = ((ListViewItemRes) adapter.getItem(i)).getState();
                Intent i4 = new Intent(getApplicationContext(), ResDetail.class);
                i4.putExtra("res_no", res_no);
                i4.putExtra("stu_name", stu_name);
                i4.putExtra("stu_no", stu_no);
                i4.putExtra("state", state);
                startActivity(i4);
            }
        });
    }

    //  예약 내용
    class CustomTask extends AsyncTask<String, Void, String> {
        String receiveMsg, sendMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;

                URL url = new URL("http://studionkorea.com/stu_reslist.jsp");
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

    //  예약 개수
    class CustomTask2 extends AsyncTask<String, Void, String> {
        String receiveMsg2, sendMsg2;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;

                URL url = new URL("http://studionkorea.com/stu_reslist_count.jsp");
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
