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

public class Center extends AppCompatActivity {

    int cen_cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        ListView listview;
        final ListViewAdapterCenter adapter;

        adapter = new ListViewAdapterCenter();

        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        try {
            String result2;
            CustomTask2 task2 = new CustomTask2();
            result2 = task2.execute().get();
            cen_cnt = Integer.parseInt(result2) + 1;
        } catch (Exception e) {

        }


        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute().get();

            for (int i = 0; i < cen_cnt; i++) {
                int idx = result.indexOf("a;");
                final String mail1 = result.substring(0, idx);
                adapter.addItem( "", "","","", "");
                ((ListViewItemCenter) adapter.getItem(i)).setUser(mail1);
                adapter.notifyDataSetChanged();

                String mail2 = result.substring(idx + 2);
                int idx2 = mail2.indexOf("b;");
                final String bottom = mail2.substring(0, idx2);
                ((ListViewItemCenter) adapter.getItem(i)).setDate(bottom);
                adapter.notifyDataSetChanged();

                String mail3 = mail2.substring(idx2 + 2);
                int idx3 = mail3.indexOf("c;");
                final String hh2 = mail3.substring(0, idx3);
                ((ListViewItemCenter) adapter.getItem(i)).setTitle(hh2);
                adapter.notifyDataSetChanged();

                String mail4 = mail3.substring(idx3 + 2);
                int idx4 = mail4.indexOf("d;");
                String hh4 = mail4.substring(0, idx4);
                ((ListViewItemCenter) adapter.getItem(i)).setState(hh4);
                adapter.notifyDataSetChanged();

                String mail5 = mail4.substring(idx4 + 2);
                int idx5 = mail5.indexOf("e;");
                String hh5 = mail5.substring(0, idx5);
                ((ListViewItemCenter) adapter.getItem(i)).setNum(hh5);
                adapter.notifyDataSetChanged();

                String rest = mail5.substring(idx5 + 2);
                result = rest;
            }

        } catch (Exception e) {

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String center = ((ListViewItemCenter) adapter.getItem(i)).getNum();
                Intent ii = new Intent(getApplicationContext(), CenterDetail.class);
                ii.putExtra("center", center);
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
                URL url = new URL("http://studionkorea.com/stu_center.jsp");
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
                URL url = new URL("http://studionkorea.com/stu_center_count.jsp");
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
