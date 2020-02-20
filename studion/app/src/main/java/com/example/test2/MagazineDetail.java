package com.example.test2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MagazineDetail extends AppCompatActivity {

    Bitmap bitmap;

    String mg_no, stu_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine_detail);

        Intent intent = getIntent();
        mg_no = intent.getExtras().getString("mg_no");

        TextView nothing = findViewById(R.id.nothing);
        nothing.setVisibility(View.INVISIBLE);
        ImageView img = findViewById(R.id.img);
        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute(mg_no).get();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

            int idx = result.indexOf("a;");
            final String mail1 = result.substring(0, idx);

            if (mail1 == null) {
                img.setImageResource(R.drawable.notfound);
                nothing.setVisibility(View.VISIBLE);
            } else {

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
                    img.setImageBitmap(bitmap);

                } catch (InterruptedException e) {

                }
            }

            String mail2 = result.substring(idx + 2);
            int idx2 = mail2.indexOf("b;");
            String bottom = mail2.substring(0, idx2);
            stu_no = bottom;

            String rest = mail2.substring(idx2 + 2);
            result = rest;

        } catch (Exception e) {

        }

        /*Button mg = findViewById(R.id.mg);
        mg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), Detail.class);
                intent1.putExtra("stu", stu_no);
                startActivity(intent1);
            }
        });*/
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_magazine_detail.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "mg_no=" + strings[0];
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
}
