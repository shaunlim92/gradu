package com.example.user.first;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

public class Dot_Show2 extends AppCompatActivity {
    private FileReader fr = null;
    private GridView girdview;
    private GridLayout gridLayout;
    private DotShowAdapter adapter=null;
    FileInputStream fileInputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot__show2);
        adapter = new DotShowAdapter();

        girdview = (GridView) findViewById(R.id.Dot_Show2);
        //gridLayout =(GridLayout) findViewById(R.id.DotLayout2);
        //gridLayout =(GridLayout) findViewById(R.id.Dot);
        //Toast.makeText(getApplicationContext(), "읽는 것.", Toast.LENGTH_LONG).show();


        Intent intent = getIntent();
        //파일에 대한 모든 경로가 들어있음. (파일명까지)
        String File_Path = intent.getStringExtra("File_Path");
        //그리드 뷰 어댑터 붙여주기.
        girdview.setAdapter(adapter);

        int line;

        try {
            fileInputStream = new FileInputStream(File_Path);
            Reader in = new InputStreamReader(fileInputStream, "euc-kr");
            BufferedReader reader = new BufferedReader(in);

            while((line = reader.read()) != -1) {
                char A = (char)line;
                /*
                String Name="a";
                for(int i=5;i>=0;i--)
                {
                    if((( A >> i) & 0b1) == 1)
                        Name = Name+"1";
                    else if((( A >> i) & 0b1)==0)
                        Name = Name+"0";
                }
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dot_show,null);
                ImageView img = (ImageView)view.findViewById(R.id.Dot_img);
                Drawable tmp1 = ContextCompat.getDrawable(this,getResources().getIdentifier(Name, "drawable", this.getPackageName()));
                img.setImageDrawable(tmp1);
                gridLayout.addView(view);
                */
                ItemAdd((A));
            }
            reader.close();
        }catch(Exception e){}
    }
    public void ItemAdd(char A){
        String Name="a";
        for(int i=5;i>=0;i--)
        {
            if((( A >> i) & 0b1) == 1)
                Name = Name+"1";
            else if((( A >> i) & 0b1)==0)
                Name = Name+"0";
        }

        Log.d("Name 값.", "ItemAdd: Name : "+Name);
        Drawable tmp1 = ContextCompat.getDrawable(this,getResources().getIdentifier(Name, "drawable", this.getPackageName()));
        Log.d("Drawable 값.", "ItemAdd: Name : "+tmp1);
        adapter.addItem(tmp1,null, "",0);
    }
}
