package com.example.user.first;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class DotFile_Show extends AppCompatActivity {
    private String root = Environment.getExternalStorageDirectory().getAbsolutePath();  //최상위 폴더
    private ArrayList<String> itemFiles = new ArrayList<String>();  //display 되는 파일이나 폴더이름
    private ArrayList<String> pathFiles = new ArrayList<String>();  // 화면에 display 되는 list의 경로와 이름이 붙어있는 목록
    private ListViewAdapter adapter=null;
    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_file__show);

        adapter = new ListViewAdapter();
        listview = (ListView) findViewById(R.id.DotFile_View);
        listview.setAdapter(adapter);
        //listview 아이템 눌렀을 경우 실행 될 행동.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //눌린 위치의 정보를 가져옴
                ListViewItem ob = adapter.getItem(position);
                //눌렀을 때 폴더 이동 함수를 호출
                itemClick(ob.getTitle(),ob.getDesc());
            }
        });
        getDir(root+"/E_Dot");
    }
    private void getDir(String dirPath)
    {
        if(!dirPath.endsWith("/")) dirPath = dirPath+"/";
        File file = new File(dirPath);
        File[] files = file.listFiles();
        if( files == null) {
            Toast.makeText(getApplicationContext(), dirPath, Toast.LENGTH_LONG).show();
            return;
        }
        //ArrayList 내용을 다시 채우기 위하여 비움. (보여주는 리스트 지우는 거라 보면 됨)
        itemFiles.clear();
        pathFiles.clear();

        if( !root.endsWith("/")) root = root+"/";
        //찾아낸 파일 및 폴더 리스트를 배열에다가 정리.
        for(int i=0; i<files.length;i++) {
            File f = files[i];
            if (f.isDirectory())
            {
                //폴더일 경우 아무것도 안함.
            }
            else {
                //Toast.makeText(getApplicationContext(), f.getName(), Toast.LENGTH_LONG).show();
                itemFiles.add(f.getName());
                pathFiles.add(f.getPath());
            }
        }
        Move_folder();
    }
    private void itemClick(String name, String path)
    {
        File file = new File(path);
        if(file.isDirectory())
        {
        }
        //파일 일 때
        else{
            final String Name = file.getName();
            final String Path = file.getPath();
            //누른 파일이 Dat파일이면.
            if(file.getName().endsWith(".dat"))
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(DotFile_Show.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //확인 버튼 누르면 이 텍스트 파일을 읽어 와야됨.
                        //다른 액티비티에서 읽어 올려고 함.
                        Intent intent = new Intent(
                                getApplicationContext(), Dot_Show2.class);
                        //파일 경로 전송.
                        intent.putExtra("File_Path", Path);
                        intent.putExtra("Root_Path",root);
                        intent.putExtra("File_Name",Name);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //취소 버튼 눌렀을 때, 아무일도 없어도 됨.
                            }
                        });
                alert.setMessage("이 파일을 읽어 오시겠습니까?");
                alert.show();
            }
            else {
                new AlertDialog.Builder(this)
                        .setTitle("[" + file.getName() + "]")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        }
    }
    private void Move_folder()
    {
        ListViewAdapter adapter2 = new ListViewAdapter();
        adapter = adapter2;
        listview.setAdapter(adapter);
        if(itemFiles.size() == 0)
            adapter.addItem(ContextCompat.getDrawable(this,R.drawable.etc),"파일이 존재하지 않습니다.","");
        for(int i=0;i<itemFiles.size();i++)
        {
            // 여기서 listview 에 넣음
                adapter.addItem(ContextCompat.getDrawable(this,R.drawable.text),itemFiles.get(i),pathFiles.get(i));
        }
        adapter.notifyDataSetChanged();
    }
}
