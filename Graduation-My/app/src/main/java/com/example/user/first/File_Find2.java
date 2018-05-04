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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class File_Find2 extends AppCompatActivity {
    private String root = Environment.getExternalStorageDirectory().getAbsolutePath();  //최상위 폴더
    private String CurPath = Environment.getExternalStorageDirectory().getAbsolutePath();   //현재 탐색하는 폴더
    private ArrayList<String> itemFiles = new ArrayList<String>();  //display 되는 파일이나 폴더이름
    private ArrayList<String> pathFiles = new ArrayList<String>();  // 화면에 display 되는 list의 경로와 이름이 붙어있는 목록
    private ListViewAdapter2 adapter=null;
    private GridView girdview;
    private TextView Path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file__find2);
        adapter = new ListViewAdapter2();

        girdview = (GridView) findViewById(R.id.File_View2);
        Path=(TextView)findViewById(R.id.File_FindName);
        girdview.setAdapter(adapter);
        getDir(root);

        //girdview 아이템 눌렀을 경우 실행 될 행동.
        girdview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //눌린 위치의 정보를 가져옴
                ListViewItem2 ob = adapter.getItem(position);
                //눌렀을 때 폴더 이동 함수를 호출
                itemClick(ob.getTitle(),ob.getDesc());
            }
        });
    }
    @Override
    public void onBackPressed(){
        String parent = CurPath.substring(0,CurPath.lastIndexOf("/"));
        if(root.equals(CurPath+"/")  )
            super.onBackPressed();
        else
            itemClick("../",parent);
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
        if( !root.equals(dirPath)){
            itemFiles.add("../");
            pathFiles.add(file.getParent());
        }
        //찾아낸 파일 및 폴더 리스트를 배열에다가 정리.
        for(int i=0; i<files.length;i++) {
            File f = files[i];
            pathFiles.add(f.getPath());

            if (f.isDirectory())
                itemFiles.add(f.getName() + "/");
            else {
                //Toast.makeText(getApplicationContext(), f.getName(), Toast.LENGTH_LONG).show();
                itemFiles.add(f.getName());
            }
        }
        Move_folder();
    }
    private void itemClick(String name, String path)
    {
        //epub 파일의 경우 zip 파일로 바꾸고 해부해서 html 파일 찾아내기.
        File file = new File(path);
        if(file.isDirectory())
        {
            if(file.canRead()){
                CurPath = path;
                //데이터 정보가 바뀔시 리스트 뷰 다시 재정비.
                getDir(CurPath);

            }else{  //요 부분 없어도 되는 부분.
                new AlertDialog.Builder(this)
                        .setTitle("["+file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        }
        //파일 일 때
        else{
            final String Name = file.getName();
            final String Path = file.getPath();
            //누른 파일이 텍스트 파일이면
            if(file.getName().endsWith(".txt"))
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(File_Find2.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //확인 버튼 누르면 이 텍스트 파일을 읽어 와야됨.
                        //다른 액티비티에서 읽어 올려고 함.
                        Intent intent = new Intent(
                                getApplicationContext(), File_Read.class);
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
                alert.setMessage("이 텍스트 파일을 변환 하시겠습니까?");
                alert.show();
            }
            //누른 파일이 epub 파일이면
            else if(file.getName().endsWith(".epub"))
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(File_Find2.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //확인 버튼 누르면 이 epbu 파일을 읽어 와야됨.
                        //다른 액티비티에서 읽어 올려고 함.
                        Intent intent = new Intent(
                                getApplicationContext(), Epub2Html.class);
                        //파일 경로 전송.
                        intent.putExtra("File_Path", Path);
                        intent.putExtra("Root_Path",root);
                        intent.putExtra("File_Name",Name);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //취소 버튼 눌렀을 때, 아무일도 없어도 됨.
                    }
                });
                alert.setMessage("이 전자책 파일을 변환 하시겠습니까?");
                alert.show();
            }
            // 180103 21시 dat파일을 누르면 dot_show가 실행되서 dat파일이 바뀜
            else if(file.getName().endsWith(".dat"))
            {
                Intent intent = new Intent(getApplicationContext(), Dot_Show.class);
                intent.putExtra("File_Name",Path);
                startActivity(intent);
                finish();
            }
            //적용 되지 않는 파일 선택시.. 인데 없어도 되는 부분일 듯.
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
    private String File_Path_Rename(String Folder_Path){
        String Path=Folder_Path;
        String result="내장메모리";
        String tmp[] = Path.split("/");
        for(int i=0;i<tmp.length;i++)
        {
            if(i>3)
                result=result+"▶"+tmp[i];
        }
        return result;
    }
    private void Move_folder()
    {
        //girdview 초기화 및 다시 그리기.
        ListViewAdapter2 adapter2 = new ListViewAdapter2();
        adapter = adapter2;
        girdview.setAdapter(adapter);
        for(int i=0;i<itemFiles.size();i++)
        {
            // 여기서 listview 에 넣는데 확장자 명에 따라 그림을 구분 해줘야됨.
            if(itemFiles.get(i).endsWith(".txt"))
                adapter.addItem(ContextCompat.getDrawable(this,R.drawable.text),itemFiles.get(i),pathFiles.get(i));
            else if(itemFiles.get(i).endsWith(".png") || itemFiles.get(i).endsWith(".jpg") || itemFiles.get(i).endsWith(".dat"))
                adapter.addItem(ContextCompat.getDrawable(this,R.drawable.etc),itemFiles.get(i),pathFiles.get(i));
            else if(itemFiles.get(i).endsWith(".epub"))
                adapter.addItem(ContextCompat.getDrawable(this,R.drawable.ic_action_name),itemFiles.get(i),pathFiles.get(i));
            else
                adapter.addItem(ContextCompat.getDrawable(this,R.drawable.folder),itemFiles.get(i),pathFiles.get(i));
        }
        String tmp = File_Path_Rename(CurPath);
        Path.setText(tmp);
        adapter.notifyDataSetChanged();
    }

}
