package com.example.test2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listview;
    Bitmap bitimg1, bitimg2, bitmap;
    TextView clear;
    int stu_cnt;
    String stu_no, result15;

    int mg_cnt;
    Bitmap bitmap2;
    //firebase auth object
    private FirebaseAuth firebaseAuth;

    int rev_cnt;
    GridView gridview;
    String review_num;
    String result6, state;

    Button fav;

    CarouselView localCarouselView;
    int[] localImages = {R.drawable.ca1, R.drawable.ca2, R.drawable.midlabel};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();

        initView();


        //main

        ImageView sinimg1 = findViewById(R.id.sinimg1);
        ImageView sinimg2 = findViewById(R.id.sinimg2);
        ImageView sinimg3 = findViewById(R.id.sinimg3);
        ImageView sinimg4 = findViewById(R.id.sinimg4);

        ImageView hotimg1 = findViewById(R.id.hotimg1);
        ImageView hotimg2 = findViewById(R.id.hotimg2);
        ImageView hotimg3 = findViewById(R.id.hotimg3);
        ImageView hotimg4 = findViewById(R.id.hotimg4);

        ImageView pickimg1 = findViewById(R.id.pickimg1);
        ImageView pickimg2 = findViewById(R.id.pickimg2);
        ImageView pickimg3 = findViewById(R.id.pickimg3);
        ImageView pickimg4 = findViewById(R.id.pickimg4);

        sinimg1.setOnClickListener(this);
        sinimg2.setOnClickListener(this);
        sinimg3.setOnClickListener(this);
        sinimg4.setOnClickListener(this);

        hotimg1.setOnClickListener(this);
        hotimg2.setOnClickListener(this);
        hotimg3.setOnClickListener(this);
        hotimg4.setOnClickListener(this);

        pickimg1.setOnClickListener(this);
        pickimg2.setOnClickListener(this);
        pickimg3.setOnClickListener(this);
        pickimg4.setOnClickListener(this);

        /*sinimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Detail.class);
                intent.putExtra("stu","10001");
                startActivity(intent);
            }
        });*/


        /* STUDIO TAB START */

//      studioList START
        final ListViewAdapterStudio adapter;
        adapter = new ListViewAdapterStudio();
        listview = (ListView) findViewById(R.id.stu_list);
        listview.setAdapter(adapter);
        bitimg1 = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.p5);

//      studio 개수 START
        try {
            String result2;
            CustomTask2 task2 = new CustomTask2();
            result2 = task2.execute().get();
            stu_cnt = Integer.parseInt(result2);
        } catch (Exception e) {

        }
//      studio 개수 END

//      StudioList START
        try {
            String result;
            CustomTask task = new CustomTask();
            result = task.execute().get();

            for (int i = 0; i < stu_cnt; i++) {
                int idx = result.indexOf("a;");
                String mail1 = result.substring(0, idx);
                stu_no = mail1;
                adapter.addItem(bitimg1, "", "", "", "", "", "", "");
                ((ListViewItemStudio) adapter.getItem(i)).setNum(mail1);
                adapter.notifyDataSetChanged();

                String mail2 = result.substring(idx + 2);
                int idx2 = mail2.indexOf("b;");
                String bottom = mail2.substring(0, idx2);
                ((ListViewItemStudio) adapter.getItem(i)).setName(bottom);
                adapter.notifyDataSetChanged();

                String mail3 = mail2.substring(idx2 + 2);
                int idx3 = mail3.indexOf("c;");
                final String hh2 = mail3.substring(0, idx3);

                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(hh2);
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
                    ((ListViewItemStudio) adapter.getItem(i)).setIcon(bitmap);
                    adapter.notifyDataSetChanged();
                } catch (InterruptedException e) {

                }

                String mail4 = mail3.substring(idx3 + 2);
                int idx4 = mail4.indexOf("d;");
                String hh4 = mail4.substring(0, idx4);
                ((ListViewItemStudio) adapter.getItem(i)).setRate(hh4);
                adapter.notifyDataSetChanged();

                String mail5 = mail4.substring(idx4 + 2);
                int idx5 = mail5.indexOf("e;");
                String hh5 = mail5.substring(0, idx5);
                ((ListViewItemStudio) adapter.getItem(i)).setTag(hh5);
                adapter.notifyDataSetChanged();

                String mail6 = mail5.substring(idx5 + 2);
                int idx6 = mail6.indexOf("f;");
                String hh6 = mail6.substring(0, idx6);
                ((ListViewItemStudio) adapter.getItem(i)).setPrice(hh6 + "원 / 1hour");
                adapter.notifyDataSetChanged();

                try {
                    String result9;
                    CustomTask9 task9 = new CustomTask9();
                    result9 = task9.execute(mail1).get();
                    ((ListViewItemStudio) adapter.getItem(i)).setReview(result9);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }

                String mail7 = mail6.substring(idx6 + 2);
                int idx7 = mail7.indexOf("g;");
                String hh7 = mail7.substring(0, idx7);
                ((ListViewItemStudio) adapter.getItem(i)).setMap(hh7);
                adapter.notifyDataSetChanged();

                String rest = mail7.substring(idx7 + 2);
                result = rest;
            }
            ;

        } catch (Exception e) {

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String studio = ((ListViewItemStudio) adapter.getItem(i)).getNum();
                Intent ii = new Intent(getApplicationContext(), Detail.class);
                ii.putExtra("stu", studio);
                startActivity(ii);
            }
        });
//      studioList END

        clear = findViewById(R.id.clear);
        clear.setVisibility(View.GONE);

        // 태그검색
        ImageButton tag = findViewById(R.id.tag);
        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_search();

            }
        });

//      studioSearch START
        final EditText editTextFilter = (EditText) findViewById(R.id.editSearch);
        editTextFilter.setText("");

        editTextFilter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 동작
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString();
                ((ListViewAdapterStudio) listview.getAdapter()).getFilter().filter(filterText);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        final EditText editTextFilter2 = (EditText) findViewById(R.id.editSearch2);

        editTextFilter2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 동작
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

        editTextFilter2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText2 = edit.toString();
                ((ListViewAdapterStudio) listview.getAdapter()).getFilter().filter(filterText2);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        final EditText editTextFilter3 = (EditText) findViewById(R.id.editSearch3);

        editTextFilter3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 동작
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

        editTextFilter3.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText3 = edit.toString();
                ((ListViewAdapterStudio) listview.getAdapter()).getFilter().filter(filterText3);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        final EditText editTextFilter4 = (EditText) findViewById(R.id.editSearch4);

        editTextFilter4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 동작
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

        editTextFilter4.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText4 = edit.toString();
                ((ListViewAdapterStudio) listview.getAdapter()).getFilter().filter(filterText4);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        //검색 초기화
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextFilter.setText("");
                editTextFilter2.setText("");
                editTextFilter3.setText("");
                editTextFilter4.setText("");
            }
        });

//      studioSearch END

        /* STUDIO TAB END */


        //magazine

//      magazine 개수 START
        try {
            String result5;
            CustomTask5 task5 = new CustomTask5();
            result5 = task5.execute().get();
            mg_cnt = Integer.parseInt(result5);
        } catch (Exception e) {

        }
//      magazine 개수 END


//      magazine START
        ListView listview3;
        final ListViewAdapterMagazine adapter3;

        adapter3 = new ListViewAdapterMagazine();

        listview3 = (ListView) findViewById(R.id.listview3);
        listview3.setAdapter(adapter3);

        try {
            String result4;
            CustomTask4 task4 = new CustomTask4();
            result4 = task4.execute().get();

            for (int i = 0; i < mg_cnt; i++) {
                int idx = result4.indexOf("a;");
                final String mail1 = result4.substring(0, idx);
                adapter3.addItem(bitimg1, "", "", "");

                Thread mThread2 = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url2 = new URL(mail1);
                            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                            conn2.setDoInput(true);
                            conn2.connect();
                            InputStream is2 = conn2.getInputStream();
                            bitmap2 = BitmapFactory.decodeStream(is2);
                        } catch (IOException ex) {
                        }
                    }
                };
                mThread2.start();
                try {
                    mThread2.join();
                    ((ListViewItemMagazine) adapter3.getItem(i)).setIcon(bitmap2);
                    adapter3.notifyDataSetChanged();
                } catch (InterruptedException e) {

                }

                String mail2 = result4.substring(idx + 2);
                int idx2 = mail2.indexOf("b;");
                final String bottom = mail2.substring(0, idx2);
                ((ListViewItemMagazine) adapter3.getItem(i)).setNum(bottom);
                adapter3.notifyDataSetChanged();

                String mail3 = mail2.substring(idx2 + 2);
                int idx3 = mail3.indexOf("c;");
                final String hh2 = mail3.substring(0, idx3);
                ((ListViewItemMagazine) adapter3.getItem(i)).setStart(hh2);
                adapter3.notifyDataSetChanged();

                String mail4 = mail3.substring(idx3 + 2);
                int idx4 = mail4.indexOf("d;");
                String hh4 = mail4.substring(0, idx4);
                ((ListViewItemMagazine) adapter3.getItem(i)).setEnd(hh4);
                adapter3.notifyDataSetChanged();

                String rest = mail4.substring(idx4 + 2);
                result4 = rest;
            }
            ;

        } catch (Exception e) {

        }

        listview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String mg_no = ((ListViewItemMagazine) adapter3.getItem(i)).getNum();
                Intent ii2 = new Intent(getApplicationContext(), MagazineDetail.class);
                ii2.putExtra("mg_no", mg_no);
                startActivity(ii2);
            }
        });
//      magazine END

        //magazine


        //photo


//      photo START

        try {
            String result14;
            CustomTask14 task14 = new CustomTask14();
            result14 = task14.execute().get();
            rev_cnt = Integer.parseInt(result14) + 1;
        } catch (Exception e) {

        }

        final gridAdapter adapter5;
        adapter5 = new gridAdapter();
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter5);

//        ImageView heart = findViewById(R.id.heart1);
        ImageView like = findViewById(R.id.heart1);
        try {
            String result13;
            CustomTask13 task13 = new CustomTask13();
            result13 = task13.execute().get();

            for (int i = 0; i < rev_cnt; i++) {
                int idx = result13.indexOf("a;");
                final String mail1 = result13.substring(0, idx);
                adapter5.addItem(bitmap2, "", "", "", "", "", 0);

                review_num = mail1;
                ((gridItem) adapter5.getItem(i)).setNum(mail1);
                adapter5.notifyDataSetChanged();

                try {
                    CustomTask6 task6 = new CustomTask6();
                    result6 = task6.execute(user.getEmail(), mail1, mail1, mail1).get();

                    if (result6.equals("O")) {
                        ((gridItem) adapter5.getItem(i)).setLike(R.drawable.heart2);
                        adapter5.notifyDataSetChanged();
                        state = "O";
                    } else if (result6.equals("X")) {
                        ((gridItem) adapter5.getItem(i)).setLike(R.drawable.heart1);
                        adapter5.notifyDataSetChanged();
                        state = "X";
                    }
                } catch (Exception e) {

                }

                String mail2 = result13.substring(idx + 2);
                int idx2 = mail2.indexOf("b;");
                final String bottom = mail2.substring(0, idx2);

                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(bottom);
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
                    ((gridItem) adapter5.getItem(i)).setIcon(bitmap);
                    adapter5.notifyDataSetChanged();
                } catch (InterruptedException e) {

                }

                String mail3 = mail2.substring(idx2 + 2);
                int idx3 = mail3.indexOf("c;");
                final String hh2 = mail3.substring(0, idx3);
                ((gridItem) adapter5.getItem(i)).setStu(hh2);
                adapter5.notifyDataSetChanged();

                String mail4 = mail3.substring(idx3 + 2);
                int idx4 = mail4.indexOf("d;");
                String hh4 = mail4.substring(0, idx4);
                ((gridItem) adapter5.getItem(i)).setTitle(hh4);
                adapter5.notifyDataSetChanged();


                String mail5 = mail4.substring(idx4 + 2);
                int idx5 = mail5.indexOf("e;");
                String hh5 = mail5.substring(0, idx5);
                ((gridItem) adapter5.getItem(i)).setUser(hh5);
                adapter5.notifyDataSetChanged();

                String mail6 = mail5.substring(idx5 + 2);
                int idx6 = mail6.indexOf("f;");
                String hh6 = mail6.substring(0, idx6);
                ((gridItem) adapter5.getItem(i)).setFavor(hh6);
                adapter5.notifyDataSetChanged();

                String rest = mail6.substring(idx6 + 2);
                result13 = rest;
            }
        } catch (Exception e) {

        }

/*
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ImageView img = new ImageView(getApplicationContext());
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("예약 취소");
                String text = "정말 예약을 취소하시겠습니까?.";
                builder.setMessage(text);
                builder.setView(img);
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
            }
        });*/

//      photo END
//6 yes 8 insert
//      좋아요 START

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

       /*
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equals("O")) {
                    try {
                        String result7;
                        CustomTask7 task7 = new CustomTask7();
                        result7 = task7.execute(review_num, user.getEmail(), review_num, review_num).get();

                        heart.setImageResource(R.drawable.heart1);
                        state = "X";

                    } catch (Exception e) {

                    }
                } else if (state.equals("X")) {
                    try {
                        String result8;
                        CustomTask8 task8 = new CustomTask8();
                        result8 = task8.execute(review_num, sdf.format(new Date()), user.getEmail(), review_num, review_num).get();

                        heart.setImageResource(R.drawable.heart2);
                        state = "O";

                    } catch (Exception e) {

                    }
                }
            }
        });*/
//      좋아요 END

        //photo


        //user

//      사용자id 표시
        TextView name = findViewById(R.id.name);
        name.setText(user.getEmail());
//      사용자 등급 표시
        TextView rank = findViewById(R.id.rank);
        try {
            String result11;
            CustomTask11 task11 = new CustomTask11();
            result11 = task11.execute(user.getEmail()).get();
            rank.setText(result11);
        } catch (Exception e) {

        }
//      계정
        RelativeLayout re = findViewById(R.id.re);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i8 = new Intent(getApplicationContext(), Modify1.class);
                startActivity(i8);
            }
        });
//      예약
        Button res = findViewById(R.id.res);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i7 = new Intent(getApplicationContext(), ResList.class);
                startActivity(i7);
            }
        });
//      관심
        fav = findViewById(R.id.favorite);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Favorite.class));
            }
        });
//      후기
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i8 = new Intent(getApplicationContext(), MyReview.class);
                startActivity(i8);
            }
        });
//      공지사항
        RelativeLayout r3 = findViewById(R.id.r3);
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Notify.class);
                startActivity(intent);
            }
        });
//      고객센터
        RelativeLayout r4 = findViewById(R.id.r4);
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Center.class);
                startActivity(intent);
            }
        });

        //user


        //tab
        LinearLayout hidetab = (LinearLayout) findViewById(R.id.hidetab);
        hidetab.setVisibility(View.GONE);

        final TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("TAB 1");
        tabHost1.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("TAB 2");
        tabHost1.addTab(ts2);

        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.content3);
        ts3.setIndicator("TAB 3");
        tabHost1.addTab(ts3);

        TabHost.TabSpec ts4 = tabHost1.newTabSpec("Tab Spec 4");
        ts4.setContent(R.id.content4);
        ts4.setIndicator("TAB 4");
        tabHost1.addTab(ts4);

        TabHost.TabSpec ts5 = tabHost1.newTabSpec("Tab Spec 5");
        ts5.setContent(R.id.content5);
        ts5.setIndicator("TAB 5");
        tabHost1.addTab(ts5);


        final Button tab1 = (Button) findViewById(R.id.tab1);
        tab1.setTextColor(Color.BLACK);
        tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home2_black_24dp, 0, 0);
        final Button tab2 = (Button) findViewById(R.id.tab2);
        tab2.setTextColor(Color.GRAY);
        tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_camera_alt_black_24dp, 0, 0);
        final Button tab3 = (Button) findViewById(R.id.tab3);
        tab3.setTextColor(Color.GRAY);
        tab3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_featured_play_list_black_24dp, 0, 0);
        final Button tab4 = (Button) findViewById(R.id.tab4);
        tab4.setTextColor(Color.GRAY);
        tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_photo_filter_black_24dp, 0, 0);
        final Button tab5 = (Button) findViewById(R.id.tab5);
        tab5.setTextColor(Color.GRAY);
        tab5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_person2_black_24dp, 0, 0);

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tab1:
                        tabHost1.setCurrentTab(0);
                        tab1.setTextColor(Color.BLACK);
                        tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home2_black_24dp, 0, 0);
                        tab2.setTextColor(Color.GRAY);
                        tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_camera_alt_black_24dp, 0, 0);
                        tab3.setTextColor(Color.GRAY);
                        tab3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_featured_play_list_black_24dp, 0, 0);
                        tab4.setTextColor(Color.GRAY);
                        tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_photo_filter_black_24dp, 0, 0);
                        tab5.setTextColor(Color.GRAY);
                        tab5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_person2_black_24dp, 0, 0);
                        break;
                    case R.id.tab2:
                        tabHost1.setCurrentTab(1);
                        tab1.setTextColor(Color.GRAY);
                        tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_black_24dp, 0, 0);
                        tab2.setTextColor(Color.BLACK);
                        tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_camera2_alt_black_24dp, 0, 0);
                        tab3.setTextColor(Color.GRAY);
                        tab3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_featured_play_list_black_24dp, 0, 0);
                        tab4.setTextColor(Color.GRAY);
                        tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_photo_filter_black_24dp, 0, 0);
                        tab5.setTextColor(Color.GRAY);
                        tab5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_person2_black_24dp, 0, 0);
                        break;
                    case R.id.tab3:
                        tabHost1.setCurrentTab(2);
                        tab1.setTextColor(Color.GRAY);
                        tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_black_24dp, 0, 0);
                        tab2.setTextColor(Color.GRAY);
                        tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_camera_alt_black_24dp, 0, 0);
                        tab3.setTextColor(Color.BLACK);
                        tab3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_featured2_play_list_black_24dp, 0, 0);
                        tab4.setTextColor(Color.GRAY);
                        tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_photo_filter_black_24dp, 0, 0);
                        tab5.setTextColor(Color.GRAY);
                        tab5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_person2_black_24dp, 0, 0);
                        break;
                    case R.id.tab4:
                        tabHost1.setCurrentTab(3);
                        tab1.setTextColor(Color.GRAY);
                        tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_black_24dp, 0, 0);
                        tab2.setTextColor(Color.GRAY);
                        tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_camera_alt_black_24dp, 0, 0);
                        tab3.setTextColor(Color.GRAY);
                        tab3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_featured_play_list_black_24dp, 0, 0);
                        tab4.setTextColor(Color.BLACK);
                        tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_photo2_filter_black_24dp, 0, 0);
                        tab5.setTextColor(Color.GRAY);
                        tab5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_person2_black_24dp, 0, 0);
                        break;
                    case R.id.tab5:
                        tabHost1.setCurrentTab(4);
                        tab1.setTextColor(Color.GRAY);
                        tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_black_24dp, 0, 0);
                        tab2.setTextColor(Color.GRAY);
                        tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_camera_alt_black_24dp, 0, 0);
                        tab3.setTextColor(Color.GRAY);
                        tab3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_featured_play_list_black_24dp, 0, 0);
                        tab4.setTextColor(Color.GRAY);
                        tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_photo_filter_black_24dp, 0, 0);
                        tab5.setTextColor(Color.BLACK);
                        tab5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_person4_black_24dp, 0, 0);
                        break;
                }
            }
        };
        tab1.setOnClickListener(onClickListener);
        tab2.setOnClickListener(onClickListener);
        tab3.setOnClickListener(onClickListener);
        tab4.setOnClickListener(onClickListener);
        tab5.setOnClickListener(onClickListener);
        //tab
    }

    private void show_search() {

        try {
            CustomTask15 task15 = new CustomTask15();
            result15 = task15.execute().get();
        } catch (Exception e) {

        }

        result15 = result15.replaceAll("\\[", "");
        result15 = result15.replaceAll("\\]", "");


        final List<String> arr = new ArrayList<>();

        String[] data = result15.split(", ");
        for (int k = 0; k < data.length; k++) {
            arr.add(data[k]);
        }

        HashSet<String> arr2 = new HashSet<>(arr); // HashSet에 arr데이터 삽입
        final ArrayList<String> ListItems = new ArrayList<>(arr2); // 중복이 제거된 HashSet을 다시 ArrayList에 삽입

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        final List SelectedItems = new ArrayList();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("원하는 검색어를 선택해주세요.");
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
                        Toast.makeText(getApplicationContext(),
                                "Total " + SelectedItems.size() + " Items Selected.\n" + msg, Toast.LENGTH_LONG)
                                .show();

                        String[] ts = msg.split(" ");
                        for (int m = 0; m < SelectedItems.size(); m++) {
                            if (SelectedItems.size() == 1) {
                                ((ListViewAdapterStudio) listview.getAdapter()).getFilter().filter(ts[0]);
                            } else if (SelectedItems.size() == 2) {
                                ((ListViewAdapterStudio) listview.getAdapter()).getFilter().filter(ts[0]);
                                ((ListViewAdapterStudio) listview.getAdapter()).getFilter().filter(ts[1]);
                            } else if (SelectedItems.size() == 3) {
                                ((ListViewAdapterStudio) listview.getAdapter()).getFilter().filter(ts[0]);
                                ((ListViewAdapterStudio) listview.getAdapter()).getFilter().filter(ts[1]);
                                ((ListViewAdapterStudio) listview.getAdapter()).getFilter().filter(ts[2]);
                            }
                            clear.setVisibility(View.VISIBLE);

                        }
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }

    private void initView() {

        // drawable 이미지 롤링배너 보여주기
        localCarouselView = (CarouselView) findViewById(R.id.localCarouselView);
        localCarouselView.setPageCount(localImages.length);
        localCarouselView.setSlideInterval(4000);
        localCarouselView.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                View customView = getLayoutInflater().inflate(R.layout.view_custom, null);

                TextView labelTextView = (TextView) customView.findViewById(R.id.labelTextView);
                ImageView fruitImageView = (ImageView) customView.findViewById(R.id.fruitImageView);

                fruitImageView.setImageResource(localImages[position]);
//                labelTextView.setText(TitleNames[position]);

                localCarouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

                return customView;
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), Detail.class);
        switch (view.getId()) {
            case R.id.sinimg1:
                intent.putExtra("stu", "10001");
                break;
            case R.id.sinimg2:
                intent.putExtra("stu", "10001");
                break;
            case R.id.sinimg3:
                intent.putExtra("stu", "10001");
                break;
            case R.id.sinimg4:
                intent.putExtra("stu", "10001");
                break;
            case R.id.hotimg1:
                intent.putExtra("stu", "10001");
                break;
            case R.id.hotimg2:
                intent.putExtra("stu", "10001");
                break;
            case R.id.hotimg3:
                intent.putExtra("stu", "10001");
                break;
            case R.id.hotimg4:
                intent.putExtra("stu", "10001");
                break;
            case R.id.pickimg1:
                intent.putExtra("stu", "10001");
                break;
            case R.id.pickimg2:
                intent.putExtra("stu", "10001");
                break;
            case R.id.pickimg3:
                intent.putExtra("stu", "10001");
                break;
            case R.id.pickimg4:
                intent.putExtra("stu", "10001");
                break;
        }
        startActivity(intent);
    }

    //  스튜디오 목록
    class CustomTask extends AsyncTask<String, Void, String> {
        String receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;

                URL url = new URL("http://studionkorea.com/stu_studio.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {

                        buffer.append(str);

                    }
                    receiveMsg = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "??? ???" + conn.getResponseCode() + "????", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }

    //  스튜디오 개수
    class CustomTask2 extends AsyncTask<String, Void, String> {
        String receiveMsg2;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;

                URL url = new URL("http://studionkorea.com/stu_studio_count.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {

                        buffer.append(str);

                    }
                    receiveMsg2 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "??? ???" + conn.getResponseCode() + "????", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg2;
        }
    }

    //  후기 개수
    class CustomTask9 extends AsyncTask<String, Void, String> {
        String sendMsg9, receiveMsg9;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_review_cnt.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg9 = "studio.stu_no=" + strings[0];
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
                    Toast.makeText(getApplicationContext(), "??? ???" + conn.getResponseCode() + "????", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg9;
        }
    }

    class CustomTask15 extends AsyncTask<String, Void, String> {
        String receiveMsg15, sendMsg15;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;

                URL url = new URL("http://studionkorea.com/stu_tag.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {

                        buffer.append(str);

                    }
                    receiveMsg15 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "??? ???" + conn.getResponseCode() + "????", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg15;
        }
    }


    //  매거진
    class CustomTask4 extends AsyncTask<String, Void, String> {
        String sendMsg4, receiveMsg4;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_magazine.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {

                        buffer.append(str);

                    }
                    receiveMsg4 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "??? ???" + conn.getResponseCode() + "????", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg4;
        }
    }

    //  매거진 개수
    class CustomTask5 extends AsyncTask<String, Void, String> {
        String sendMsg5, receiveMsg5;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_magazine_count.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {

                        buffer.append(str);

                    }
                    receiveMsg5 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "??? ???" + conn.getResponseCode() + "????", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg5;
        }
    }


    class CustomTask13 extends AsyncTask<String, Void, String> {
        String receiveMsg13, sendMsg13;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;

                URL url = new URL("http://studionkorea.com/stu_photo.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {

                        buffer.append(str);

                    }
                    receiveMsg13 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "??? ???" + conn.getResponseCode() + "????", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg13;
        }
    }

    class CustomTask14 extends AsyncTask<String, Void, String> {
        String receiveMsg14, sendMsg14;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;

                URL url = new URL("http://studionkorea.com/stu_photo_count.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {

                        buffer.append(str);

                    }
                    receiveMsg14 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "??? ???" + conn.getResponseCode() + "????", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg14;
        }

    }

    class CustomTask6 extends AsyncTask<String, Void, String> {
        String sendMsg6, receiveMsg6;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://studionkorea.com/stu_like_yes2.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg6 = "user_id=" + strings[0] + "&review_num=" + strings[1] + "&review_num=" + strings[2] + "&review_num=" + strings[3];
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
                URL url = new URL("http://studionkorea.com/stu_like_delete2.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg7 = "review_num=" + strings[0] + "&user_id=" + strings[1] + "&review_num=" + strings[2] + "&review_num=" + strings[3];
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
                URL url = new URL("http://studionkorea.com/stu_like_insert2.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg8 = "review_num=" + strings[0] + "&li_date=" + strings[1] + "&user_id=" + strings[2]
                        + "&review_num=" + strings[3] + "&review_num=" + strings[4];
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

    //  사용자 등급 표시
    class CustomTask11 extends AsyncTask<String, Void, String> {
        String receiveMsg11, sendMsg11;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;

                URL url = new URL("http://studionkorea.com/stu_user_rank.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg11 = "user_id=" + strings[0];
                osw.write(sendMsg11);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "utf-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {

                        buffer.append(str);

                    }
                    receiveMsg11 = buffer.toString();

                } else {
                    Toast.makeText(getApplicationContext(), "??? ???" + conn.getResponseCode() + "????", Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg11;
        }
    }
}
