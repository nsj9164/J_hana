package com.example.test2;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapterRes extends BaseAdapter {

    Context context;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItemRes> listViewItemList = new ArrayList<ListViewItemRes>();

    // ListViewAdapter의 생성자
    public ListViewAdapterRes() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_res, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        TextView res_no = (TextView) convertView.findViewById(R.id.res_no);
        TextView stu_name = (TextView) convertView.findViewById(R.id.stu_name);
        TextView rm_name = (TextView) convertView.findViewById(R.id.rm_name);
        TextView res_time = (TextView) convertView.findViewById(R.id.res_time);
        TextView res_date = (TextView) convertView.findViewById(R.id.res_date);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView state = (TextView) convertView.findViewById(R.id.state);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListViewItemRes listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        img.setImageBitmap(listViewItem.getIcon());
        res_no.setText(listViewItem.getNum());
        stu_name.setText(listViewItem.getSname());
        rm_name.setText(listViewItem.getRname());
        res_time.setText(listViewItem.getTime());
        res_date.setText(listViewItem.getDate());
        price.setText(listViewItem.getPrice());
        state.setText(listViewItem.getState());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Bitmap icon, String num, String sname, String rname, String time, String date, String price, String state) {
        ListViewItemRes item = new ListViewItemRes();

        item.setIcon(icon);
        item.setNum(num);
        item.setSname(sname);
        item.setRname(rname);
        item.setTime(time);
        item.setDate(date);
        item.setPrice(price);
        item.setState(state);

        listViewItemList.add(item);
    }

}

