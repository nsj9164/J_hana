package com.example.test2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class NotifyDetail extends AppCompatActivity {

    String q2_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_detail);

        Intent intent = getIntent();
        q2_no = intent.getExtras().getString("q2_no");
        Toast.makeText(this, q2_no, Toast.LENGTH_SHORT).show();

        TextView title = findViewById(R.id.title);
        TextView date = findViewById(R.id.date);
        TextView answer = findViewById(R.id.answer);
//no+"a;"+title+"b;"+desc+"c;"+date+"d;"
        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute(q2_no).get();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

            int idx = result.indexOf("a;");
            final String mail1 = result.substring(0, idx);

            String mail2 = result.substring(idx + 2);
            int idx2 = mail2.indexOf("b;");
            final String bottom = mail2.substring(0, idx2);
            title.setText(bottom);

            String mail3 = mail2.substring(idx2 + 2);
            int idx3 = mail3.indexOf("c;");
            final String hh2 = mail3.substring(0, idx3);
            answer.setText(hh2);

            String mail4 = mail3.substring(idx3 + 2);
            int idx4 = mail4.indexOf("d;");
            String hh4 = mail4.substring(0, idx4);
            date.setText(hh4);

        } catch (Exception e) {

        }
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_notify2.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "no_no=" + strings[0];
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
