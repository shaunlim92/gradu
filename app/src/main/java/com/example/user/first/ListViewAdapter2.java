package com.example.user.first;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 명윤 on 2017-12-27.
 */

public class ListViewAdapter2 extends BaseAdapter{
    private ArrayList<ListViewItem2> listViewItemList = new ArrayList<ListViewItem2>();

    public ListViewAdapter2(){
    }
    //Adapter에 사용 되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount(){return listViewItemList.size();}

    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //외부 XML 파일에 있는 resource로 부터 가져옴.
            convertView = inflater.inflate(R.layout.file_find,parent, false);
        }

        //화면에 표시될 View(Layout 이 inflate된)으로 부터 위젯에 대한 참조 획득
        ImageView iconImageView = ViewHolderHelper.get(convertView, R.id.imageV1);
        TextView titleTextView = ViewHolderHelper.get(convertView, R.id.TextV1);

        //Data Set(ListViewItemList) 에서 position 에 위치한 데이터 참조 획득
        ListViewItem2 listViewItem = listViewItemList.get(position);

        //아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getTitle());

        return convertView;
    }
    //지정한 위치에 있는 데이터와 관계된 아이템의 ID를 리턴 : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    //지정한 위치에 있는 데이터 리턴 : 필수 구현
    @Override
    public ListViewItem2 getItem(int position) {
        return listViewItemList.get(position);
    }

    //아이템 데이터 추가를 위한 함수, 개발자가 원하는대로 작성 가능
    public void addItem(Drawable icon, String title, String desc) {
        ListViewItem2 item = new ListViewItem2();

        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);
        listViewItemList.add(item);
    }
}
