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

public class Favorite extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    ListView listview;
    int heart_cnt;
    String stu_no;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        TextView main = findViewById(R.id.main);
        main.setText("좋아요 내역");
        ImageView heart = findViewById(R.id.heart);
        heart.setVisibility(View.INVISIBLE);

//      좋아요 누른 갯수
        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute(user.getEmail()).get();
            heart_cnt = Integer.parseInt(result);
        } catch (Exception e) {

        }

        final ListViewAdapterHeart adapter;
        adapter = new ListViewAdapterHeart();
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        Bitmap bitimg1 = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.notfound);

        TextView nothing = findViewById(R.id.nothing);
        nothing.setVisibility(View.INVISIBLE);

        if (heart_cnt == 0) {
            nothing.setVisibility(View.VISIBLE);
        } else {
            try {
                String result2;
                CustomTask2 task2 = new CustomTask2();
                result2 = task2.execute(user.getEmail()).get();

                if (result2 == null) {
                    nothing.setVisibility(View.VISIBLE);
                } else {

                    for (int i = 0; i < heart_cnt; i++) {
                        int idx = result2.indexOf("a;");
                        String mail1 = result2.substring(0, idx);
                        adapter.addItem(bitimg1, "", "", "");

                        ((ListViewItemHeart) adapter.getItem(i)).setNum(mail1);
                        adapter.notifyDataSetChanged();

                        String mail2 = result2.substring(idx + 2);
                        int idx2 = mail2.indexOf("b;");
                        String bottom = mail2.substring(0, idx2);
                        ((ListViewItemHeart) adapter.getItem(i)).setName(bottom);
                        adapter.notifyDataSetChanged();

                        String mail3 = mail2.substring(idx2 + 2);
                        int idx3 = mail3.indexOf("c;");
                        String hh2 = mail3.substring(0, idx3);
                        ((ListViewItemHeart) adapter.getItem(i)).setPrice(hh2+"원 / 1hour");
                        adapter.notifyDataSetChanged();

                        String mail4 = mail3.substring(idx3 + 2);
                        int idx4 = mail4.indexOf("d;");
                        final String hh4 = mail4.substring(0, idx4);

                        if (hh4.equals("")) {
                            ((ListViewItemHeart) adapter.getItem(i)).setIcon(bitimg1);
                            adapter.notifyDataSetChanged();
                        } else {
                            Thread mThread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        URL url = new URL(hh4);
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
                                ((ListViewItemHeart) adapter.getItem(i)).setIcon(bitmap);
                                adapter.notifyDataSetChanged();
                            } catch (InterruptedException e) {

                            }
                        }
                        String rest = mail4.substring(idx4 + 2);
                        result2 = rest;
                    }
                }

            } catch (Exception e) {

            }
        }
//      좋아요 누른 스튜디오

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String studio = ((ListViewItemHeart) adapter.getItem(i)).getNum();
                Intent ii = new Intent(getApplicationContext(), Detail.class);
                ii.putExtra("stu", studio);
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
                URL url = new URL("http://studionkorea.com/stu_heart_all_count.jsp");
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

    class CustomTask2 extends AsyncTask<String, Void, String> {
        String sendMsg2, receiveMsg2;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_heart_all.jsp");
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
