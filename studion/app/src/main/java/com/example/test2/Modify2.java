package com.example.test2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Modify2 extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify2);

        final TextView _id = findViewById(R.id.id);
        final TextView _pw = findViewById(R.id.pw);
        final TextView _name = findViewById(R.id.name);
        final TextView _birth = findViewById(R.id.birth);
        final EditText _phone = findViewById(R.id.phone);
        final EditText _add = findViewById(R.id.add);

        final FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute(user.getEmail()).get();

            for (int i = 0; i < 3; i++) {
                int ss = result.indexOf(";");
                String mail1 = result.substring(0, ss);
                String mail2 = result.substring(ss + 1);
                int ss2 = mail2.indexOf(";");
                String bottom = mail2.substring(0, ss2);
                String rest = mail2.substring(ss2 + 1);

                result = rest;

                if (i == 0) {
                    _id.setText(mail1);
                    _pw.setText(bottom);
                } else if (i == 1) {
                    _name.setText(mail1);
                    _birth.setText(bottom);
                } else if (i == 2) {
                    int ss3 = rest.indexOf(";");
                    String end = rest.substring(0, ss3);
                    _phone.setText(mail1);
                    _add.setText(end);
                }
            }

        } catch (Exception e) {

        }


        Button modify = findViewById(R.id.modifyBtn);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = _phone.getText().toString();
                String add = _add.getText().toString();

                /*try {
                    String result2;
                    Modify2.CustomTask2 task2 = new Modify2.CustomTask2();
                    result2 = task2.execute(phone, email, add, user.getEmail()).get();
                    Toast.makeText(getApplicationContext(), result2, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }*/

            }
        });

        Button change = findViewById(R.id.change_pw);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Modify2.this, Modify3.class);
                startActivity(intent);
            }
        });
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_userlist.jsp");
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
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_update.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "user_phone=" + strings[0] + "&user_email=" + strings[1] + "&user_add=" + strings[2] + "&user_id=" + strings[3];
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
