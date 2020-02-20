package com.example.test2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import java.util.concurrent.ExecutionException;

public class Detail extends AppCompatActivity {

    Bitmap bitmap, bitimg, bitmap2;
    String url_add, stu_no, rm_no, stu_name, rm_name, text, state;
    int rev_cnt = 0;
    ListView listview, listview2;
    float _rate = 0.0f;
    TextView add;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

//      getIntent START
        Intent ii = getIntent();
        stu_no = ii.getExtras().getString("stu");
//      getIntent END

        TextView main = findViewById(R.id.main);
        main.setText("상세정보");

//      좋아요 START
        final ImageView heart = findViewById(R.id.heart);
        try {
            String result7;
            CustomTask7 task7 = new CustomTask7();
            result7 = task7.execute(user.getEmail(), stu_no, stu_no, stu_no).get();

            if (result7.equals("O")) {
                heart.setImageResource(R.drawable.heart2);
                state = "O";
            } else if (result7.equals("X")) {
                state = "X";
            }
        } catch (Exception e) {

        }
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equals("O")) {
                    try {
                        String result8;
                        CustomTask8 task8 = new CustomTask8();
                        result8 = task8.execute(user.getEmail(), stu_no, stu_no, stu_no).get();

                        heart.setImageResource(R.drawable.heart1);
                        state = "X";

                    } catch (Exception e) {

                    }
                } else if (state.equals("X")) {
                    try {
                        String result9;
                        CustomTask9 task9 = new CustomTask9();
                        result9 = task9.execute(stu_no, user.getEmail(), stu_no, stu_no).get();

                        heart.setImageResource(R.drawable.heart2);
                        state = "O";

                    } catch (Exception e) {

                    }
                }
            }
        });
//      좋아요 END


//      스튜디오 정보 START
        TextView name = findViewById(R.id.stuname);
        TextView desc = findViewById(R.id.desc2);
        add = findViewById(R.id.wherein);
        ImageView img = findViewById(R.id.studio);

        try {
            String result3;
            CustomTask3 task3 = new CustomTask3();
            result3 = task3.execute(stu_no).get();

            for (int i = 0; i < 2; i++) {
                int ss = result3.indexOf(";");
                String mail1 = result3.substring(0, ss);
                String mail2 = result3.substring(ss + 1);
                int ss2 = mail2.indexOf(";");
                final String bottom = mail2.substring(0, ss2);
                String rest = mail2.substring(ss2 + 1);

                result3 = rest;

                if (i == 0) {
                    name.setText(mail1);
                    stu_name = mail1;

                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(bottom);

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
                        img.setImageBitmap(bitmap);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else if (i == 1) {
                    add.setText(mail1);
                    text = bottom.replace("<br>", "\n");
                    desc.setText(text);
                }
            }
        } catch (Exception e) {

        }
//      스튜디오 정보 END

//      룸 START
        final ListViewAdapterRoom adapter;
        adapter = new ListViewAdapterRoom();
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        try {
            String result4;
            CustomTask4 task4 = new CustomTask4();
            result4 = task4.execute(stu_no).get();

            for (int i = 0; i < 3; i++) {
                int idx = result4.indexOf("a;");
                final String mail1 = result4.substring(0, idx);
                adapter.addItem(bitimg, "", "", "");

                Thread mThread2 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(mail1);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitimg = BitmapFactory.decodeStream(is);
                        } catch (IOException ex) {
                        }
                    }
                };
                mThread2.start();
                try {
                    mThread2.join();
                    ((ListViewItemRoom) adapter.getItem(i)).setIcon(bitimg);
                    adapter.notifyDataSetChanged();
                } catch (InterruptedException e) {

                }

                String mail2 = result4.substring(idx + 2);
                int idx2 = mail2.indexOf("b;");
                String bottom = mail2.substring(0, idx2);
                rm_no = bottom;
                ((ListViewItemRoom) adapter.getItem(i)).setNum(bottom);
                adapter.notifyDataSetChanged();

                String mail3 = mail2.substring(idx2 + 2);
                int idx3 = mail3.indexOf("c;");
                String hh2 = mail3.substring(0, idx3);
                rm_name = hh2;
                ((ListViewItemRoom) adapter.getItem(i)).setTitle(hh2);
                adapter.notifyDataSetChanged();


                String mail4 = mail3.substring(idx3 + 2);
                int idx4 = mail4.indexOf("d;");
                String hh4 = mail4.substring(0, idx4);
                ((ListViewItemRoom) adapter.getItem(i)).setMoney(hh4 + "원 / 1hour");
                adapter.notifyDataSetChanged();

                String rest = mail4.substring(idx4 + 2);
                result4 = rest;
            }
        } catch (Exception e) {

        }

        setListViewHeightBasedOnChildren(listview);
//      룸 END


//      Intent_Room START
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String room = ((ListViewItemRoom) adapter.getItem(i)).getNum();

                Intent intent = new Intent(getApplicationContext(), Room.class);
                intent.putExtra("stu", stu_no);
                intent.putExtra("rm", room);
                intent.putExtra("stu_name", stu_name);
                intent.putExtra("rm_name", rm_name);
                startActivity(intent);

            }
        });
//      Intent_Room END

//      Review_List START
        /*LinearLayout review_list = findViewById(R.id.review_list);
        review_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReviewList.class);
                intent.putExtra("stu2", stu_no);
                startActivity(intent);
            }
        });*/
//      Review_List END


//      평점 START
        TextView rbnum = findViewById(R.id.rbnum);
        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute(stu_no).get();

            rbnum.setText(result);

        } catch (Exception e) {

        }
//      평점 END

//      ReviewList START
        ListViewAdapterReview adapter2;
        adapter2 = new ListViewAdapterReview();
        listview2 = (ListView) findViewById(R.id.listview2);
        listview2.setAdapter(adapter2);

        TextView nothing = findViewById(R.id.nothing);
        nothing.setVisibility(View.INVISIBLE);

        // 후기개수
        try {
            String result2;
            CustomTask2 task2 = new CustomTask2();
            result2 = task2.execute(stu_no).get();
            rev_cnt = Integer.parseInt(result2);
            Toast.makeText(this, rev_cnt, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

        }

        //후기리스트
        if (rev_cnt == 0) {
            nothing.setVisibility(View.VISIBLE);
        } else {
            try {
                String result6;
                CustomTask6 task6 = new CustomTask6();
                result6 = task6.execute(stu_no).get();

                for (int i = 0; i < rev_cnt; i++) {
                    int idx = result6.indexOf("a;");
                    String mail1 = result6.substring(0, idx);
                    adapter2.addItem("", "", "", "", _rate, "", "");

                    ((ListViewItemReview) adapter2.getItem(i)).setRe(mail1);
                    adapter2.notifyDataSetChanged();

                    String mail2 = result6.substring(idx + 2);
                    int idx2 = mail2.indexOf("b;");
                    final String bottom = mail2.substring(0, idx2);
                    float star = Float.parseFloat(bottom);
                    ((ListViewItemReview) adapter2.getItem(i)).setStar(star);
                    adapter2.notifyDataSetChanged();

                    String mail3 = mail2.substring(idx2 + 2);
                    int idx3 = mail3.indexOf("c;");
                    final String hh2 = mail3.substring(0, idx3);
                    ((ListViewItemReview) adapter2.getItem(i)).setText(hh2);
                    adapter2.notifyDataSetChanged();

                    String mail4 = mail3.substring(idx3 + 2);
                    int idx4 = mail4.indexOf("d;");
                    String hh4 = mail4.substring(0, idx4);
                    ((ListViewItemReview) adapter2.getItem(i)).setDate(hh4);
                    adapter2.notifyDataSetChanged();

                    String mail5 = mail4.substring(idx4 + 2);
                    int idx5 = mail5.indexOf("e;");
                    String hh5 = mail5.substring(0, idx5);
                    ((ListViewItemReview) adapter2.getItem(i)).setStu(hh5);
                    adapter2.notifyDataSetChanged();

                    String mail6 = mail5.substring(idx5 + 2);
                    int idx6 = mail6.indexOf("f;");
                    String hh6 = mail6.substring(0, idx6);
                    ((ListViewItemReview) adapter2.getItem(i)).setRoom(hh6);
                    adapter2.notifyDataSetChanged();

                    String mail7 = mail6.substring(idx6 + 2);
                    int idx7 = mail7.indexOf("g;");
                    String hh7 = mail7.substring(0, idx7);
                    ((ListViewItemReview) adapter2.getItem(i)).setUser(hh7);
                    adapter2.notifyDataSetChanged();

                    String rest = mail7.substring(idx7 + 2);
                    result6 = rest;
                }

            } catch (Exception e) {

            }
        }

        setListViewHeightBasedOnChildren(listview2);
//      ReviewList END

    }

    //  listview height 자동 설정 START
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
//  listview height 자동 설정 END

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_review4.jsp");
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

    class CustomTask2 extends AsyncTask<String, Void, String> {
        String sendMsg2, receiveMsg2;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_review_count.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg2 = "stu_no=" + strings[0];
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
                URL url = new URL("http://studionkorea.com/stu_studio_detail.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg3 = "stu_no=" + strings[0];
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

    class CustomTask4 extends AsyncTask<String, Void, String> {
        String sendMsg4, receiveMsg4;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_room.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg4 = "stu_no=" + strings[0];
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

    class CustomTask6 extends AsyncTask<String, Void, String> {
        String sendMsg6, receiveMsg6;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_review6.jsp");
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

    class CustomTask7 extends AsyncTask<String, Void, String> {
        String sendMsg7, receiveMsg7;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_heart_yes.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg7 = "user_id=" + strings[0] + "&stu_no=" + strings[1] + "&stu_no=" + strings[2] + "&stu_no=" + strings[3];
                osw.write(sendMsg7);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg7 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "통신 결과" + conn.getResponseCode() + "에러", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg7;
        }
    }

    class CustomTask8 extends AsyncTask<String, Void, String> {
        String sendMsg8, receiveMsg8;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_heart_delete.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg8 = "user_id=" + strings[0] + "&stu_no=" + strings[1] + "&stu_no=" + strings[2] + "&stu_no=" + strings[3];
                osw.write(sendMsg8);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg8 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "통신 결과" + conn.getResponseCode() + "에러", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg8;
        }
    }

    class CustomTask9 extends AsyncTask<String, Void, String> {
        String sendMsg9, receiveMsg9;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_heart_insert.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg9 = "stu_no=" + strings[0] + "&user_id=" + strings[1] + "&stu_no=" + strings[2] + "&stu_no=" + strings[3];
                osw.write(sendMsg9);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg9 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "통신 결과" + conn.getResponseCode() + "에러", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg9;
        }
    }
}


