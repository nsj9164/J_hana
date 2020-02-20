package com.example.test2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Modify3 extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    String result2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify3);

        final EditText _pw = findViewById(R.id.pw);
        final EditText _newPw = findViewById(R.id.new_pw);
        final EditText _pwCheck = findViewById(R.id.pwCheck);

        final FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();

        Button cancel = findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Modify3.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button modify = findViewById(R.id.modifyBtn);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = _pw.getText().toString();
                String newPw = _newPw.getText().toString();
                String pwCheck = _pwCheck.getText().toString();

                if(TextUtils.isEmpty(pw) || TextUtils.isEmpty(newPw) || TextUtils.isEmpty(pwCheck)){
                    Toast.makeText(getApplicationContext(), "모든 항목을 정확하게 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                try {
                    Modify3.CustomTask2 task2 = new Modify3.CustomTask2();
                    result2 = task2.execute(user.getEmail()).get();
                } catch (Exception e) {

                }
                if (result2.equals(pw) && newPw.equals(pwCheck)) {
                    try {
                        String result;
                        Modify3.CustomTask task = new Modify3.CustomTask();
                        result = task.execute(newPw,user.getEmail(), pw).get();
                    } catch (Exception e) {

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "잘못 입력하셨습니다.\\다시 한번 입력해주세요.", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_pwchange.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "user_pw" + strings[0] + "user_id=" + strings[1] + "&user_pw=" + strings[2];
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
                URL url = new URL("http://studionkorea.com/stu_pwcheck.jsp");
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
}
