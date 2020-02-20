package com.example.test2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner;
    ArrayAdapter adapter;

    String sex="W";
    String age;

    String idcheck_success;
    String result;

    EditText _id;
    EditText _pw;
    EditText _pwCheck;
    EditText _name;
    EditText _birth;
    EditText _phone;
    EditText _add;
    TextView reason;
    Button registerBtn;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner();

        radio();

        firebaseAuth = FirebaseAuth.getInstance();

        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);

        _id = (EditText) findViewById(R.id.id);
        _pw = (EditText) findViewById(R.id.pw);
        _pwCheck = (EditText) findViewById(R.id.pwCheck);
        _name = (EditText) findViewById(R.id.name);
        _birth = (EditText) findViewById(R.id.birth);
        _phone = (EditText) findViewById(R.id.phone);
        _add = (EditText) findViewById(R.id.add);

        final Button idCheck = findViewById(R.id.idCheck);

        idCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = _id.getText().toString();
                try {
                    Register.CustomTask task = new Register.CustomTask();
                    result = task.execute(id).get();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }
        });
    }

    private void spinner() {
        Spinner spinner = findViewById(R.id.age);
        adapter = ArrayAdapter.createFromResource(this, R.array.age, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void radio() {
        RadioGroup rg = findViewById(R.id.genderGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.genderW:
                        sex = "W";
                        break;
                    case R.id.genderM:
                        sex = "M";
                        break;
                }
            }
        });
    }

    private void registerUser() {
        //사용자가 입력하는 email, password를 가져온다.
        final String email = _id.getText().toString().trim();
        final String password = _pw.getText().toString().trim();
        //기타데이타
        String pw2 = _pwCheck.getText().toString();
        final String name = _name.getText().toString();
        final String birth = _birth.getText().toString();
        final String phone = _phone.getText().toString();
        final String add = _add.getText().toString();

        //email과 password가 비었는지 아닌지를 체크 한다.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(pw2)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.\n\n다시 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }


        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            try {
                                String result2;
                                CustomTask2 task2 = new CustomTask2();
                                result2 = task2.execute(email, password, name, birth, phone, add, sex, age).get();
                                Toast.makeText(getApplicationContext(), result2, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {

                            }
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else {
                            //에러발생시
                            reason.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                            Toast.makeText(getApplicationContext(), "등록 에러!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == registerBtn) {
            //TODO
            registerUser();
        }
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_idcheck.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setRequestProperty("Content-Type",
//                        "application/x-www-form-urlencoded;charset=UTF-8");
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
                URL url = new URL("http://studionkorea.com/stu_register.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg2 = "user_id=" + strings[0] + "&user_pw=" + strings[1] + "&user_name=" + strings[2] + "&user_birth=" + strings[3]
                        + "&user_phone=" + strings[4] + "&user_add=" + strings[5] + "&user_sex=" + strings[6] + "&user_age=" + strings[7];
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
