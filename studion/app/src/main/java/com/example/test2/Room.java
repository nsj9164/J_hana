package com.example.test2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Room extends AppCompatActivity {
    DatePicker mDate, mDate2;
    TextView mTxtDate, mTime, price;

    String at, bt;

    ImageView roomimg1, roomimg2, roomimg3, roomimg4, roomimg5;
    ImageView iv;

    Button res;
    Bitmap bitmap;
    String stu_no, rm_no, stu_name, rm_name;
    String date, time;
    String d, t;
    String result4, result6;

    int yy = 0, mm = 0, dd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        stu_no = intent.getExtras().getString("stu");
        rm_no = intent.getExtras().getString("rm");
        stu_name = intent.getExtras().getString("stu_name");
        rm_name = intent.getExtras().getString("rm_name");

        TextView stu = findViewById(R.id.stu);
        stu.setText("[" + stu_name + "]");

        mDate = (DatePicker) findViewById(R.id.datepicker);
        mTxtDate = (TextView) findViewById(R.id.txtdate);
        mTime = (TextView) findViewById(R.id.txttime);

        //처음 DatePicker를 오늘 날짜로 초기화한다.
        //그리고 리스너를 등록한다.
        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {

                    //값이 바뀔때마다 텍스트뷰의 값을 바꿔준다.
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                        // TODO Auto-generated method stub

                        //monthOfYear는 0값이 1월을 뜻하므로 1을 더해줌 나머지는 같다.
                        mTxtDate.setText(String.format("%d년 %d월 %d일", year, monthOfYear + 1
                                , dayOfMonth));

                    }
                });

        //선택기로부터 날짜 조사
        findViewById(R.id.btnnow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String result = String.format("%d년 %d월 %d일", mDate.getYear(),
                        mDate.getMonth() + 1, mDate.getDayOfMonth());
                date = String.format("%d-%d-%d", mDate.getYear(),
                        mDate.getMonth() + 1, mDate.getDayOfMonth());

                if (mTxtDate.equals("date")) {
                    Toast.makeText(Room.this, "날짜를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    date_check();
                }
            }
        });

//      달력보기 START
        Button cal = findViewById(R.id.cal);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
            }
        });
//      달력보기 END

//      룸정보 설정 START
        final TextView name = findViewById(R.id.name);
        final TextView price = findViewById(R.id.price);
        TextView desc = findViewById(R.id.desc);
        ImageView rm_img = findViewById(R.id.rm_img);

        try {
            String result2;
            Room.CustomTask2 task2 = new Room.CustomTask2();
            result2 = task2.execute(stu_no, rm_no).get();

            for (int i = 0; i < 3; i++) {
                int idx = result2.indexOf("a;");
                final String mail1 = result2.substring(0, idx);

                String mail2 = result2.substring(idx + 2);
                int idx2 = mail2.indexOf("b;");
                String bottom = mail2.substring(0, idx2);
                name.setText(bottom);

                String mail3 = mail2.substring(idx2 + 2);
                int idx3 = mail3.indexOf("c;");
                String hh2 = mail3.substring(0, idx3);
                price.setText(hh2+"원 / 1hour");

                String mail4 = mail3.substring(idx3 + 2);
                int idx4 = mail4.indexOf("d;");
                String hh4 = mail4.substring(0, idx4);
                String hh_4 = hh4.replaceAll("<br>","\n");
                desc.setText(hh_4);

                String mail5 = mail4.substring(idx4 + 2);
                int idx5 = mail5.indexOf("d;");
                final String hh5 = mail5.substring(0, idx5);

                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(hh5);

                            // Web에서 이미지를 가져온 뒤
                            // ImageView에 지정할 Bitmap을 만든다
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true); // 서버로 부터 응답 수신
                            conn.connect();

                            InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                            bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환
                        } catch (MalformedURLException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mThread.start(); // Thread 실행

                try {
                    mThread.join();
                    rm_img.setImageBitmap(bitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String rest = mail5.substring(idx5 + 2);
                result2 = rest;
            }


        } catch (Exception e) {

        }
//      룸정보 설정 END


//      예약정보확인 START
        final String p = price.getText().toString();
        res = (Button) findViewById(R.id.res_btn);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ic = new Intent(getApplicationContext(), Reservation.class);
                ic.putExtra("stu", stu_no);
                ic.putExtra("rm", rm_no);
                ic.putExtra("name", name.getText().toString());
                ic.putExtra("date", date);
                ic.putExtra("time", time);
                ic.putExtra("at", at);
                ic.putExtra("bt", bt);
                ic.putExtra("price", p);
                startActivity(ic);

                try {
                    String result3;
                    Room.CustomTask3 task3 = new Room.CustomTask3();
                    result3 = task3.execute(rm_no, stu_no, date, at, bt).get();

                    if (result3.equals("O")) {

                    } else if (result3.equals("X")) {
                        Toast.makeText(getApplication(), "예약이 불가합니다. 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                }
            }
        });
//      예약정보확인 END

    }
    void showDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yy = year;
                mm = month;
                dd = dayOfMonth;

                mTxtDate.setText(String.format("%d년 %d월 %d일", year, month
                        , dayOfMonth));

                mDate.updateDate(year, month, dayOfMonth);

            }
        }, mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth());

        datePickerDialog.show();
    }

    int n, m;

    void date_check() {
        try {
            String result5;
            Room.CustomTask5 task5 = new Room.CustomTask5();
            result5 = task5.execute(date, stu_no).get();

            String[] data = result5.split(";");

            if (data[0].equals("영업가능")) {
                date_check2();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("예약시간을 선택해주세요.");
                builder.setMessage("영업기간이 아닙니다. 다시 선택해주세요.\n영업기간은 " + data[1] + " ~ " + data[2] + " 입니다.");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
            }
        } catch (Exception e) {

        }
    }

    void date_check2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String y = mTxtDate.getText().toString().substring(0, 4);
        String m = mTxtDate.getText().toString().substring(6, 8);
        String d = mTxtDate.getText().toString().substring(10, 12);

        String s_date = y + "-" + m + "-" + d;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date day1 = format.parse(s_date);
            Date day2 = format.parse(sdf.format(new Date()));

            int compare = day1.compareTo(day2);

            if (compare < 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("예약시간을 선택해주세요.");
                builder.setMessage("영업이 종료된 날짜입니다.\n날짜를 다시 선택해주세요.");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mTxtDate.setText("date");
                            }
                        });
                builder.show();
            } else {
                date_check3();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    void date_check3() throws ParseException {
        String y = mTxtDate.getText().toString().substring(0, 4);
        String m = mTxtDate.getText().toString().substring(6, 8);
        String d = mTxtDate.getText().toString().substring(10, 12);

        String s_date = y + "-" + m + "-" + d;

        String day = "";

        String inputDate = s_date;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(inputDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;

        }

        try {
            Room.CustomTask6 task6 = new Room.CustomTask6();
            result6 = task6.execute(stu_no).get();

            if (result6.contains(day)) {
                show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("예약시간을 선택해주세요.");
                builder.setMessage("휴무로 영업을 하지 않습니다. 날짜를 다시 선택해주세요.\n스튜디오 영업요일은 "+result6+"요일 입니다.");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mTxtDate.setText("date");
                            }
                        });
                builder.show();
            }
        } catch (Exception e) {

        }
    }

    void show() {
        try {
            Room.CustomTask4 task4 = new Room.CustomTask4();
            result4 = task4.execute(date, stu_no, rm_no, stu_no, date).get();
        } catch (Exception e) {

        }

        if (result4 == "") {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("예약시간을 선택해주세요.");
            builder.setMessage("예약이 마감되었습니다.");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
        } else {
            final List<String> ListItems = new ArrayList<>();

            String[] data = result4.split(";");
            for (int k = 0; k < data.length; k++) {
                ListItems.add(data[k] + "시");
            }

            final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

            final List SelectedItems = new ArrayList();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("예약시간을 선택해주세요.");
            builder.setMultiChoiceItems(items, null,
                    new DialogInterface.OnMultiChoiceClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            if (isChecked) {
                                //사용자가 체크한 경우 리스트에 추가
                                SelectedItems.add(which);
                            } else if (SelectedItems.contains(which)) {
                                //이미 리스트에 들어있던 아이템이면 제거
                                SelectedItems.remove(Integer.valueOf(which));
                            }
                        }
                    });
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String msg = "";
                            for (int i = 0; i < SelectedItems.size(); i++) {
                                int index = (int) SelectedItems.get(i);

                                msg = msg + ListItems.get(index) + " ";

                            }
//                            Toast.makeText(getApplicationContext(),
//                                    "Total " + SelectedItems.size() + " Items Selected.\n" + msg, Toast.LENGTH_LONG).show();

                            String[] tt = msg.split("시 ");
                            for (int k=0; k<tt.length; k++) {
                                if (tt[k].length() == 1) {
                                    tt[k] = "0"+tt[k];
                                }
                            }
                            Arrays.sort(tt);
                            at = tt[0];
                            String btt = tt[tt.length - 1];
                            int btt2 = Integer.parseInt(btt) + 1;
                            bt = Integer.toString(btt2);
                            mTime.setText(at + "시 ~ " + bt + "시");
                            time = mTime.getText().toString();

                        }
                    });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }
    }

    //  예약가능시간 개수
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_time.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "stu_no=" + strings[0];
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

    //  룸 이름, 가격
    class CustomTask2 extends AsyncTask<String, Void, String> {
        String sendMsg2, receiveMsg2;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_room_detail.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg2 = "stu_no=" + strings[0] + "&rm_no=" + strings[1];
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

    //  예약정보확인
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
//                sendMsg3 = "a1=" + strings[0] + "&b1=" + strings[1];
                sendMsg3 = "rm_no=" + strings[0] + "&stu_no=" + strings[1] + "&sc_date=" + strings[2] + "&at=" + strings[3] + "&bt=" + strings[4];
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

    //  예약가능시간
    class CustomTask4 extends AsyncTask<String, Void, String> {
        String sendMsg4, receiveMsg4;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_res_exception.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg4 = "sc_date2=" + strings[0] + "&stu_no=" + strings[1] + "&rm_no=" + strings[2] + "&stu_no=" + strings[3]
                        + "&sc_date=" + strings[4];
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

    //  영업날짜
    class CustomTask5 extends AsyncTask<String, Void, String> {
        String sendMsg5, receiveMsg5;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_res_open.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg5 = "date=" + strings[0] + "&stu_no=" + strings[1];
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

    class CustomTask6 extends AsyncTask<String, Void, String> {
        String sendMsg6, receiveMsg6;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_res_day.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg6 = "stu_no=" + strings[0];
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
