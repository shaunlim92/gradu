package com.example.user.first;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;

public class File_Read extends AppCompatActivity {
    TextView textView;
    File DataFile = null;
    Intent intent1=null;
    String Root_Folder;
    String FileName;
    File Root_File1=null;
    String File_Path="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file__read);
        //이전파일 삭제와 현재 새롭게 만드는 파일 두가지가 동시에 진행됨. 쓰레드 때문인지 뭔지... 같이 진행 안되게 할 방법이 필요.
        Intent intent = getIntent();
        //파일에 대한 모든 경로가 들어있음. (파일명까지)
        File_Path = intent.getStringExtra("File_Path");
        //최상위 폴더
        String Root_Path = intent.getStringExtra("Root_Path");
        //읽어온 파일의 이름
        String File_Name = intent.getStringExtra("File_Name");
        //가져온 파일이름만 남기기
        FileName = File_Name.substring(0,File_Name.lastIndexOf("."));
        textView = (TextView)findViewById(R.id.textView2);
        Root_Folder = Root_Path+"E_Dot";
        //경로에 있는 파일을 가져옴.

        TMPAsyncTask asyncTask = new TMPAsyncTask();
        asyncTask.execute();
    }
    public void ConvertFile()
    {
        FileInputStream fileInputStream = null;
        int line;
        int jong,jung,cho;
        try{
            BufferedWriter bfw = new BufferedWriter(new FileWriter(Root_File1+"/"+FileName+".dat"));
            //텍스트 파일 읽기.
            fileInputStream = new FileInputStream(File_Path);
            Reader in = new InputStreamReader(fileInputStream,"euc-kr");
            BufferedReader reader = new BufferedReader(in);
            while((line = reader.read()) != -1){
                if(line==32)    //띄어쓰기 일 경우 어떻게 처리할지 생각 해봐야할듯
                    bfw.write(0b0);
                else if(line == 13 || line == 10) //엔터인 경우인데 둘이 같이 붙어다님. 생각해봐야할듯.
                    bfw.write(0b0);
                else {
                    line = line - 0xAC00;
                    jong = line % 28;
                    jung = ((line - jong) / 28) % 21;
                    cho = (((line - jong) / 28) - jung) / 21;
                    Dot dot= new Dot(cho, jung, jong);

                    //초성일 경우 쓰기
                    switch(dot.whatcase / 6){
                        case 0: bfw.write(dot.cb_cho1); break;
                        case 1: break;
                        default: bfw.write(dot.cb_cho1); bfw.write(dot.cb_cho2); break;
                    }

                    //중성일 경우 쓰기
                    switch ( (dot.whatcase%6) / 3){
                        case 0: bfw.write(dot.cb_jung1); break;
                        default: bfw.write(dot.cb_jung1); bfw.write(dot.cb_jung2); break;
                    }

                    switch( dot.whatcase % 3) {
                        case 0: bfw.write(dot.cb_jong1); bfw.write(dot.cb_jong2); break;
                        case 1: bfw.write(dot.cb_jong1); break;
                        default: break;
                    }
                }
            }
            bfw.flush();
            bfw.close();
            reader.close();

            AlertDialog.Builder alert = new AlertDialog.Builder(File_Read.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //intent1 = new Intent(File_Read.this, BlueTooth.class);        //밑에꺼 지우고 이걸로 실행 해야됨. 잠깐 주석.
                    intent1 = new Intent(File_Read.this, Dot_Show.class);
                    intent1.putExtra("File_Name",Root_Folder+"/"+FileName+".dat");
                    intent1.putExtra("File_Path",File_Path);
                    startActivity(intent1);
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

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getDir() {
        //폴더가 없으면 생성
        try {

            Root_File1 = new File(Root_Folder);
            if (!Root_File1.exists())
                Root_File1.mkdir();

            DataFile = new File(Root_Folder+"/" + FileName + ".dat");
            if (DataFile.exists()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(File_Read.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (DataFile.delete())
                            Toast.makeText(getApplicationContext(), "이전 " + FileName + " 파일 삭제 완료.", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "이전 " + FileName + " 파일 삭제 실패.", Toast.LENGTH_LONG).show();
                        ConvertFile();
                    }
                }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //취소 버튼 눌렀을 때, 무슨일이 있어야할까..?

                            }
                        });
                alert.setMessage("이전 파일이 존재합니다. 삭제해도 되겠습니까?");
                alert.setCancelable(false);
                alert.show();
            }
            else
                ConvertFile();

        }catch(Exception e){
        }

    }

    public class TMPAsyncTask extends AsyncTask<String,Void,String>{
        public String result;
        //background 스레드를 실행하기 전 준비단계.
        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
            getDir();
        }
        //해당 메서드가 background 스레드로 일처리를 해주는 곳.
        @Override
        protected String doInBackground(String... params){
            return result;
        }
        //doinbackground 메서드에서 중간중간에 UI 스레드에게 일처리를 맡겨야 하는 상황일 때
        //매개변수로 Void를 받으므로, 실제 인자가 없이, publishProgress() 메서드를 호출하면 backgroundThread 중간에
        //메인 스레드 에게 일을 시킬수 있음.
        @Override
        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }
        //background thread 가 일을 끝마치고 리턴값으로 result를 넘겨준다.
        // 그 값을 지금 보고 있는 해당 메서드가 매개변수로 받은 후 받은 데이터를 토대로
        //ui 스레드에 일처리를 시킬 때 쓰는 메서드.
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
        }
    }
}