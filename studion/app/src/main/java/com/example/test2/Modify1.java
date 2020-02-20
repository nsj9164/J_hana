package com.example.test2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

public class Modify1 extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    EditText pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify1);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        TextView id = findViewById(R.id.id);
        id.setText(user.getEmail());
        pw = findViewById(R.id.pw);

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _pw = pw.getText().toString();
                try {
                    String result;
                    CustomTask task = new CustomTask();
                    result = task.execute(user.getEmail()).get();

                    if (result.equals(_pw)) {
                        Intent i = new Intent(getApplicationContext(), Modify4.class);
                        startActivity(i);
                    } else {
                        show();
                    }

                } catch (Exception e) {

                }
            }
        });

    }

    //  비밀번호 불일치
    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인 정보 확인");
        String text = "비밀번호가 일치하지 않습니다.\n\n다시 한번 입력해주세요.";
        builder.setMessage(text);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    //  비밀번호 체크
    class CustomTask extends AsyncTask<String, Void, String> {
        String receiveMsg;
        String sendMsg;

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
