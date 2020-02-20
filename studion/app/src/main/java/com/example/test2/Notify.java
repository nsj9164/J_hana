package com.example.test2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Notify extends AppCompatActivity {

    int no_cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        ListView listview;
        final ListViewAdapterNotify adapter;

        adapter = new ListViewAdapterNotify();

        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);


        try {
            String result2;
            CustomTask2 task2 = new CustomTask2();
            result2 = task2.execute().get();
            no_cnt = Integer.parseInt(result2) + 1;
            Toast.makeText(this, no_cnt, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }

        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute().get();

            for (int i = 0; i < no_cnt; i++) {
                int idx = result.indexOf("a;");
                String mail1 = result.substring(0, idx);
                adapter.addItem( "", "","");
                ((ListViewItemNotify) adapter.getItem(i)).setNo(mail1);
                adapter.notifyDataSetChanged();

                String mail2 = result.substring(idx + 2);
                int idx2 = mail2.indexOf("b;");
                String bottom = mail2.substring(0, idx2);
                ((ListViewItemNotify) adapter.getItem(i)).setTitle(bottom);
                adapter.notifyDataSetChanged();

                String mail3 = mail2.substring(idx2 + 2);
                int idx3 = mail3.indexOf("c;");
                final String hh2 = mail3.substring(0, idx3);
                ((ListViewItemNotify) adapter.getItem(i)).setDate(hh2);
                adapter.notifyDataSetChanged();

                String rest = mail3.substring(idx3 + 2);
                result = rest;
            };

        } catch (Exception e) {

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String no = ((ListViewItemNotify) adapter.getItem(i)).getNo();
                Intent ii = new Intent(getApplicationContext(), NotifyDetail.class);
                ii.putExtra("q2_no", no);
                startActivity(ii);
            }
        });
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_notify.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setRequestProperty("Content-Type",
//                        "application/x-www-form-urlencoded;charset=UTF-8");
                conn.setRequestMethod("POST");
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

    class CustomTask2 extends AsyncTask<String, Void, String> {
        String sendMsg2, receiveMsg2;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_notify_count.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setRequestProperty("Content-Type",
//                        "application/x-www-form-urlencoded;charset=UTF-8");
                conn.setRequestMethod("POST");
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
