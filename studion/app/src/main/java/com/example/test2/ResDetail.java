package com.example.test2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResDetail extends AppCompatActivity {

    String res_no, stu, stu_name, state;
    String stu_no, rm_no, date, time;
    String result2;
    String start, end;
    String at, bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_detail);

        TextView main = findViewById(R.id.main);
        main.setText("상세 예약내역");

        Intent i4 = getIntent();
        res_no = i4.getExtras().getString("res_no");
        stu = i4.getExtras().getString("stu_no");
        stu_name = i4.getExtras().getString("stu_name");
        state = i4.getExtras().getString("state");

        TextView studio2 = findViewById(R.id.studio2);
        TextView room2 = findViewById(R.id.room2);
        TextView date2 = findViewById(R.id.date2);
        TextView time2 = findViewById(R.id.time2);
        TextView price2 = findViewById(R.id.price2);
        TextView client2 = findViewById(R.id.client2);
        TextView phone2 = findViewById(R.id.phone2);
        TextView payway2 = findViewById(R.id.payway2);
        TextView payprice2 = findViewById(R.id.payprice2);

        try {
            String result3;
            CustomTask3 task3 = new CustomTask3();
            result3 = task3.execute(res_no).get();

            int idx = result3.indexOf("a;");
            final String mail1 = result3.substring(0, idx);
            studio2.setText(mail1);

            String mail2 = result3.substring(idx + 2);
            int idx2 = mail2.indexOf("b;");
            String bottom = mail2.substring(0, idx2);
            room2.setText(bottom);

            String mail3 = mail2.substring(idx2 + 2);
            int idx3 = mail3.indexOf("c;");
            final String hh2 = mail3.substring(0, idx3);
            date2.setText(hh2);
            date = hh2;

            String mail4 = mail3.substring(idx3 + 2);
            int idx4 = mail4.indexOf("d;");
            String hh4 = mail4.substring(0, idx4);
            time2.setText(hh4);
            time = hh4;

            String mail5 = mail4.substring(idx4 + 2);
            int idx5 = mail5.indexOf("e;");
            String hh5 = mail5.substring(0, idx5);
            price2.setText(hh5);

            String mail6 = mail5.substring(idx5 + 2);
            int idx6 = mail6.indexOf("f;");
            String hh6 = mail6.substring(0, idx6);
            client2.setText(hh6);

            String mail7 = mail6.substring(idx6 + 2);
            int idx7 = mail7.indexOf("g;");
            String hh7 = mail7.substring(0, idx7);
            phone2.setText(hh7);

            String mail8 = mail7.substring(idx7 + 2);
            int idx8 = mail8.indexOf("h;");
            String hh8 = mail8.substring(0, idx8);
            payway2.setText(hh8);

            String mail9 = mail8.substring(idx8 + 2);
            int idx9 = mail9.indexOf("i;");
            String hh9 = mail9.substring(0, idx9);
            payprice2.setText(hh9);

            String mail10 = mail9.substring(idx9 + 2);
            int idx10 = mail10.indexOf("j;");
            String hh10 = mail10.substring(0, idx10);
            stu_no = hh10;

            String mail11 = mail10.substring(idx10 + 2);
            int idx11 = mail11.indexOf("k;");
            String hh11 = mail11.substring(0, idx11);
            rm_no = hh11;

            String mail12 = mail11.substring(idx11 + 2);
            int idx12 = mail12.indexOf("l;");
            String hh12 = mail12.substring(0, idx12);
            start = hh12;

            String mail13 = mail12.substring(idx12 + 2);
            int idx13 = mail13.indexOf("m;");
            String hh13 = mail13.substring(0, idx13);
            end = hh13;

        } catch (Exception e) {

        }

//      스튜디오 정보
        Button studio = findViewById(R.id.studio);
        studio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Detail.class);
//                intent.putExtra("stu", stu);
//                startActivity(intent);
            }
        });
//      후기작성
        Button review = findViewById(R.id.review);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date day1 = format.parse(date);
                    Date day2 = format.parse(sdf.format(new Date()));

                    int compare = day1.compareTo(day2);
                    if (compare >= 0) {
                        Toast.makeText(ResDetail.this, "후기는 사용완료 후 작성하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(getApplicationContext(), Review.class);
                        i.putExtra("res_no", res_no);
                        i.putExtra("stu_name", stu_name);
                        i.putExtra("stu_no", stu);
                        startActivity(i);
                    }
                } catch (ParseException e) {

                }
            }
        });
//      예약취소
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (state.equals("예약취소")) {
                    Toast.makeText(ResDetail.this, "이미 예약취소가 완료된 스튜디오입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    show();
                }

            }
        });
    }

    void show() {

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("예약 취소");
        builder.setMessage("정말로 예약을 취소하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String[] data1 = start.split(":");
                        if (data1[0].startsWith("0")) {
                            at = data1[0].substring(1);
                        } else {
                            at = data1[0];
                        }

                        String[] data2 = end.split(":");
                        if (data2[0].startsWith("0")) {
                            bt = data2[0].substring(1);
                        } else {
                            bt = data2[0];
                        }

//                      예약 삭제
                        try {
                            CustomTask2 task2 = new CustomTask2();
                            result2 = task2.execute(sdf.format(new Date()), res_no, res_no).get();
                        } catch (Exception e) {

                        }

                        if (result2.equals("O")) {
//                              스케줄 삭제
                            try {
                                String result;
                                CustomTask task = new CustomTask();
                                result = task.execute(at, bt, rm_no, stu_no, date).get();
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                                if (result.equals("O")) {
                                    startActivity(new Intent(getApplicationContext(), ResList.class));
                                }
                            } catch (Exception e) {

                            }
                        } else {
                            Toast.makeText(ResDetail.this, "예약취소가 불가한 상태입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }


    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_res_delete.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "a1=" + strings[0] + "&b1=" + strings[1] + "&rm_no=" + strings[2] + "&stu_no=" + strings[3] + "&sc_date=" + strings[4];
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
                URL url = new URL("http://studionkorea.com/stu_res_delete2.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg2 = "today=" + strings[0] + "&res_no=" + strings[1] + "&res_no=" + strings[2];
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

    class CustomTask3 extends AsyncTask<String, Void, String> {
        String sendMsg3, receiveMsg3;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_res_detail.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg3 = "res_no=" + strings[0];
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
}
