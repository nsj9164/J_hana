package com.example.test2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

public class CenterDetail extends AppCompatActivity {

    String center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_detail);

        Intent intent = getIntent();
        center = intent.getExtras().getString("center");

        TextView title = findViewById(R.id.title);
        TextView user = findViewById(R.id.user);
        TextView date = findViewById(R.id.date);
        TextView answer = findViewById(R.id.answer);
        TextView a = findViewById(R.id.a);

        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute(center).get();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

            int idx = result.indexOf("a;");
            final String mail1 = result.substring(0, idx);
            user.setText(mail1);

            String mail2 = result.substring(idx + 2);
            int idx2 = mail2.indexOf("b;");
            final String bottom = mail2.substring(0, idx2);
            date.setText(bottom);

            String mail3 = mail2.substring(idx2 + 2);
            int idx3 = mail3.indexOf("c;");
            final String hh2 = mail3.substring(0, idx3);
            if (hh2 == null) {
                a.setVisibility(View.INVISIBLE);
                answer.setText("아직 문의에 대한 답변이 달리지 않았습니다.\n조금만 기다려주세요.");
            } else {
                answer.setText(hh2);
            }

            String mail4 = mail3.substring(idx3 + 2);
            int idx4 = mail4.indexOf("d;");
            String hh4 = mail4.substring(0, idx4);
            title.setText(hh4);

            String mail5 = mail4.substring(idx4 + 2);
            int idx5 = mail5.indexOf("e;");
            String hh5 = mail5.substring(0, idx5);

            String rest = mail5.substring(idx5 + 2);
            result = rest;

        } catch (Exception e) {

        }
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_center_detail.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setRequestProperty("Content-Type",
//                        "application/x-www-form-urlencoded;charset=UTF-8");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "q2_no=" + strings[0];
                osw.write(sendMsg);
                osw.flush();
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
}
