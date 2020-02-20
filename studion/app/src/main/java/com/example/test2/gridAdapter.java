package com.example.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class gridAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<gridItem> GridViewItemList = new ArrayList<gridItem>() ;

    // ListViewAdapter의 생성자
    public gridAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return GridViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_photo, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView img1 = (ImageView) convertView.findViewById(R.id.img1) ;
        TextView txt1 = (TextView) convertView.findViewById(R.id.txt1) ;
        TextView txt2 = (TextView) convertView.findViewById(R.id.txt2) ;
        TextView cnt1 = (TextView) convertView.findViewById(R.id.cnt1) ;
        ImageView like = (ImageView) convertView.findViewById(R.id.heart1);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        gridItem gridItem = GridViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        img1.setImageBitmap(gridItem.getIcon());
        txt1.setText(gridItem.getTitle());
        txt2.setText(gridItem.getUser());
        cnt1.setText(gridItem.getFavor());
        like.setImageResource(gridItem.getLike());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return GridViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Bitmap icon, String num, String stu, String title, String user, String favor, int like) {
        gridItem item = new gridItem();

        item.setIcon(icon);
        item.setNum(num);
        item.setStu(stu);
        item.setTitle(title);
        item.setUser(user);
        item.setFavor(favor);
        item.setLike(like);

        GridViewItemList.add(item);
    }
}
