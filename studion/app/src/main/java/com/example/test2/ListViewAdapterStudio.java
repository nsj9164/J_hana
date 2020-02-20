package com.example.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapterStudio extends BaseAdapter implements Filterable {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItemStudio> listViewItemList = new ArrayList<ListViewItemStudio>() ;
    private ArrayList<ListViewItemStudio> filteredItemList = listViewItemList;

    Filter listFilter;

    // ListViewAdapter의 생성자
    public ListViewAdapterStudio() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return filteredItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_studio, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView img = (ImageView) convertView.findViewById(R.id.img) ;
        TextView name = (TextView) convertView.findViewById(R.id.name) ;
        TextView rate = (TextView) convertView.findViewById(R.id.rate) ;
        TextView review = (TextView) convertView.findViewById(R.id.review) ;
        TextView tag = (TextView) convertView.findViewById(R.id.tag) ;
        TextView price = (TextView) convertView.findViewById(R.id.price) ;
        TextView map = (TextView) convertView.findViewById(R.id.map) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItemStudio listViewItem = filteredItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        img.setImageBitmap(listViewItem.getIcon());
        name.setText(listViewItem.getName());
        rate.setText(listViewItem.getRate());
        review.setText(listViewItem.getReview());
        tag.setText(listViewItem.getTag());
        price.setText(listViewItem.getPrice());
        map.setText(listViewItem.getMap());

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
        return filteredItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Bitmap icon, String num, String name, String rate, String review, String price, String tag, String map) {
        ListViewItemStudio item = new ListViewItemStudio();

        item.setIcon(icon);
        item.setNum(num);
        item.setName(name);
        item.setRate(rate);
        item.setReview(review);
        item.setPrice(price);
        item.setTag(tag);
        item.setMap(map);

        listViewItemList.add(item);
    }

    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter();
        }

        return listFilter;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            if (constraint == null || constraint.length() == 0) {
                results.values = listViewItemList ;
                results.count = listViewItemList.size() ;
            } else {
                ArrayList<ListViewItemStudio> itemList = new ArrayList<ListViewItemStudio>() ;

                for (ListViewItemStudio item : listViewItemList) {
                    if (item.getName().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            item.getTag().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            item.getMap().toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        itemList.add(item) ;
                    }
                }

                results.values = itemList ;
                results.count = itemList.size() ;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // update listview by filtered data list.
            filteredItemList = (ArrayList<ListViewItemStudio>) results.values ;

            // notify
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }
}
