package com.example.test2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class Reservation extends AppCompatActivity {

    String stu, rm, date, time, name, price;
    String pay, res_way="신용카드";
    String at, bt, _at, _bt;
    String stu_name, rm_name;
    FirebaseAuth firebaseAuth;
    String user_id;
    String ssname;
    String res_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        user_id = user.getEmail();

        Intent ic = getIntent();
        stu = ic.getExtras().getString("stu");
        rm = ic.getExtras().getString("rm");
        name = ic.getExtras().getString("name");
        date = ic.getExtras().getString("date");
        time = ic.getExtras().getString("time");
        price = ic.getExtras().getString("price");
        at = ic.getExtras().getString("at");
        bt = ic.getExtras().getString("bt");

        TextView sname = findViewById(R.id.s_name);
        TextView rname = findViewById(R.id.r_name);
        TextView _date = findViewById(R.id.date);
        TextView _time = findViewById(R.id.time);
        TextView _price1 = findViewById(R.id.price);
        TextView _price2 = findViewById(R.id.res_price);
        TextView _price3 = findViewById(R.id.dc_price);
        TextView _price4 = findViewById(R.id.pay_price);

        try {
            String result;
            Reservation.CustomTask task = new Reservation.CustomTask();
            result = task.execute(stu, rm).get();

            int idx = result.indexOf(";");
            final String mail1 = result.substring(0, idx);
            sname.setText(mail1);
            stu_name = mail1;

            String mail2 = result.substring(idx + 1);
            rname.setText(mail2);
        } catch (Exception e) {

        }

        String[] data22 = date.split("-");
        String date2 = data22[0] + "년" + data22[1] + "월" + data22[2] + "일";
        _date.setText(date2);

        int att = Integer.parseInt(at);
        int btt = Integer.parseInt(bt);
        int total_t = btt - att;
        String total = Integer.toString(total_t);
        _time.setText(time + "(" + total + "시간 )");
        _price1.setText(price);
        int pp = price.indexOf("원");
        String price_txt = price.substring(0, pp);
        int price2 = Integer.parseInt(price_txt);
        int total_p = price2 * total_t;
        String total_pp = Integer.toString(total_p);
        _price2.setText(total_pp);

        radio();

        checkText();

        TextView dc = findViewById(R.id.dc);
        try {
            String result4;
            Reservation.CustomTask4 task4 = new Reservation.CustomTask4();
            result4 = task4.execute(stu, rm, user_id).get();

            String[] data = result4.split("/");

            dc.setText(data[0]);
            _price3.setText(data[1]);
        } catch (Exception e) {

        }

        int p2 = Integer.parseInt(_price3.getText().toString());
        int p3 = total_p - p2;
        pay = Integer.toString(p3);
        _price4.setText(pay);
    }

    private void radio() {
        RadioGroup rg = findViewById(R.id.paymentGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.method1:
                        res_way = "신용카드";
                        break;
                    case R.id.method2:
                        res_way = "계좌이체";
                        break;
                    case R.id.method3:
                        res_way = "가상계좌";
                        break;
                    case R.id.method4:
                        res_way = "카카오페이";
                        break;
                }
            }
        });
    }

    private void checkText() {

        final Button res = (Button) findViewById(R.id.btn_reservation);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText _name = (EditText) findViewById(R.id.name);
                final EditText _phone = (EditText) findViewById(R.id.phone);
                final String res_name = _name.getText().toString();
                res_phone = _phone.getText().toString();

                if (res_name.equals("")) {
                    Toast.makeText(getApplicationContext(), "예약자 명을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (res_phone.equals("")) {
                    Toast.makeText(getApplicationContext(), "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {

                    if (at.length() == 1) {
                        _at = "0" + at + ":00:00";
                    } else {
                        _at = at + ":00:00";
                    }

                    if (bt.length() == 1) {
                        _bt = "0" + bt + ":00:00";
                    } else {
                        _bt = bt + ":00:00";
                    }

                    int b2 = Integer.parseInt(bt) - 1;
                    String bt2 = Integer.toString(b2);

                    try {
                        String result5;
                        Reservation.CustomTask5 task5 = new Reservation.CustomTask5();
                        result5 = task5.execute(at, bt2, user_id, rm, stu, date, rm, stu, date, rm, stu, date, rm, stu, date).get();
                    } catch (Exception e) {

                    }

                    try {
                        String result6;
                        Reservation.CustomTask6 task6 = new Reservation.CustomTask6();
                        result6 = task6.execute(res_name, res_phone, pay, res_way, time, "결제완료", user_id, stu, rm, date, _at, _bt).get();
                    } catch (Exception e) {

                    }

                    Intent intent = new Intent(getApplicationContext(), Paydone.class);
                    intent.putExtra("res_name", res_name);
                    intent.putExtra("rm_name", name);
                    intent.putExtra("price", price);
                    intent.putExtra("phone", res_phone);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("stu_name", stu_name);
                    startActivity(intent);
                }
            }
        });
    }


    //  스튜디오 이름
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_studio_name.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "studio.stu_no=" + strings[0] + "&rm_no=" + strings[1];
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

    // 예약 체크
    class CustomTask3 extends AsyncTask<String, Void, String> {
        String sendMsg3, receiveMsg3;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_res_check.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg3 = "res_name=" + strings[0] + "&res_phone=" + strings[1] + "&res_price=" + strings[2] + "&res_time=" + strings[3]
                        + "&res_way=" + strings[4] + "&user_id=" + strings[5] + "&stu_no=" + strings[6] + "&rm_no=" + strings[7]
                        + "&res_date=" + strings[8];
                osw.write(sendMsg3);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg3 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "통신 결과" + conn.getResponseCode() + "에러", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg3;
        }
    }

    // 할인 정보
    class CustomTask4 extends AsyncTask<String, Void, String> {
        String sendMsg4, receiveMsg4;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_discount.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg4 = "stu_no=" + strings[0] + "&rm_no=" + strings[1] + "&user_id=" + strings[2];
                osw.write(sendMsg4);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg4 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "통신 결과" + conn.getResponseCode() + "에러", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg4;
        }
    }

    // 스케줄 추가
    class CustomTask5 extends AsyncTask<String, Void, String> {
        String sendMsg5, receiveMsg5;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_res_insert.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg5 = "a1=" + strings[0] + "&b1=" + strings[1] + "&name=" + strings[2]
                        + "&rm_no=" + strings[3] + "&stu_no=" + strings[4] + "&sc_date=" + strings[5]
                        + "&rm_no=" + strings[6] + "&stu_no=" + strings[7] + "&sc_date=" + strings[8]
                        + "&rm_no=" + strings[9] + "&stu_no=" + strings[10] + "&sc_date=" + strings[11]
                        + "&rm_no=" + strings[12] + "&stu_no=" + strings[13] + "&sc_date=" + strings[14];
                osw.write(sendMsg5);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg5 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "통신 결과" + conn.getResponseCode() + "에러", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg5;
        }
    }

    // 예약내역 추가
    class CustomTask6 extends AsyncTask<String, Void, String> {
        String sendMsg6, receiveMsg6;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_res_insert2.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//                res_name, res_phone, res_price, res_way, res_time, res_state, user_id, stu_no, rm_no, res_date
                sendMsg6 = "res_name=" + strings[0] + "&res_phone=" + strings[1] + "&res_price=" + strings[2] + "&res_way=" + strings[3]
                        + "&res_time=" + strings[4] + "&res_state=" + strings[5] + "&user_id=" + strings[6] + "&stu_no=" + strings[7]
                        + "&rm_no=" + strings[8] + "&res_date=" + strings[9] + "&res_start=" + strings[10] + "&res_end=" + strings[11];
                osw.write(sendMsg6);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg6 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "통신 결과" + conn.getResponseCode() + "에러", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg6;
        }
    }
}
