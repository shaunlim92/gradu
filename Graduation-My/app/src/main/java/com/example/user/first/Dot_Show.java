package com.example.user.first;

import android.content.Context;
import android.content.Intent;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

// 외부 Xml 가져오는 법.
//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.listview_form,null);

public class Dot_Show extends AppCompatActivity {
    private FileReader fr = null;
    private LinearLayout linearLayout;
    FileInputStream fileInputStream;
    private LinearLayout parentLL;
    private LinearLayout parentLL2;
    private LayoutInflater inflater;
    private int x = 0;
    private int Width;
    private int cols = 6;
    String File_Path;
    String File_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot__show);

        //출처: http://qits.tistory.com/entry/안드로이드-디바이스-화면-크기-구하기 [Quiet, In The Storm...]
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int pixels = dm.widthPixels;
        //float width = convertPixelsToDp(pixels,this);
        Width = (int)(pixels/cols);
        //Log.d("Name 값.", "ItemAdd: Name : "+width +" Name2 : "+Width);

        Intent intent = getIntent();
        //파일에 대한 모든 경로가 들어있음. (파일명까지)
        File_Path = intent.getStringExtra("File_Path");
        File_Name = intent.getStringExtra("File_Name");

        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout = (LinearLayout)findViewById(R.id.Dot_Show);
        Button b = (Button)findViewById(R.id.Button);
        //Connect 버튼 클릭 시 블루투스로 전송.
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Dot_Show.this, BlueTooth.class);
                intent.putExtra("File_Path",File_Path);
                intent.putExtra("File_Name",File_Name);
                startActivity(intent);
            }
        });


        int line;
        int jong,jung,cho;
        try {
           // fileInputStream = new FileInputStream(File_Path);
            //Reader in = new InputStreamReader(fileInputStream, "euc-kr");
            FileReader in = new FileReader(File_Path);
            BufferedReader reader = new BufferedReader(in);
            while((line = reader.read()) != -1) {
                //Log.d("읽는중", "읽는중");
                if ((x % cols) == 0)    //만약 6칸 꽉찼다면 초기화.
                {
                    Liner_Setting();
                    x = 0;
                }
                if (line == 32){    //띄어쓰기 일 경우 어떻게 처리할지 생각 해봐야할듯
                    x++;
                    ItemAdd((char) 0b0, (char) 0b0, " ", 0);
                }
                else if (line == 13 || line == 10) { //엔터인 경우인데 둘이 같이 붙어다님. 생각해봐야할듯.
                    x++;
                    ItemAdd((char) 0b0, (char) 0b0, " ", 0);
                }
                // 18.04.16 16:12 영어일 경우 분리
                else if (line >= 65 && line <= 122){


                }
                else if(line >= 0xAC00 && line <= 0xD800) {
                    line = line - 0xAC00;
                    jong = line % 28;
                    jung = ((line - jong) / 28) % 21;
                    cho = (((line - jong) / 28) - jung) / 21;
                    Dot dot = new Dot(cho, jung, jong);

                    //초성일 경우 쓰기
                    switch (dot.whatcase / 6) {
                        case 0:
                            x = x + 1;
                            ItemAdd((char) dot.cb_cho1, (char) 0b0, dot.ch_cho, 0);
                            break;
                        case 1:
                            break;
                        default:
                            if(x == (cols-1)) {
                                linearLayout.addView(parentLL);
                                Liner_Setting();
                                x=0;
                            }
                            // 18.02.01 13:43 박종수 dot.ch_cho-> string: " "로 하여 빈칸이 나오게
                            x = x + 2;
                            ItemAdd((char) dot.cb_cho1, (char) dot.cb_cho2, dot.ch_cho, 1);
                            break;
                    }
                    //중성일 경우 쓰기
                    switch ((dot.whatcase % 6) / 3) {
                        case 0:
                            x = x + 1;
                            ItemAdd((char) dot.cb_jung1, (char) 0b0, dot.ch_jung, 0);
                            break;
                        default:
                            if(x == (cols-1)) {
                                linearLayout.addView(parentLL);
                                Liner_Setting();
                                x=0;
                            }
                            x = x + 2;
                            ItemAdd((char) dot.cb_jung1, (char) dot.cb_jung2, dot.ch_jung, 1);
                            break;
                    }
                    //종성일 경우 쓰기.
                    switch (dot.whatcase % 3) {
                        case 0:
                            if(x == (cols-1)) {
                                linearLayout.addView(parentLL);
                                Liner_Setting();
                                x=0;
                            }
                            x = x + 2;
                            ItemAdd((char) dot.cb_jong1, (char) dot.cb_jong2, dot.ch_jong, 1);
                            break;
                        case 1:
                            x = x + 1;
                            ItemAdd((char) dot.cb_jong1, (char) 0b0, dot.ch_jong, 0);

                            break;
                        default:
                            break;
                    }
                }

            }
            linearLayout.addView(parentLL);

            reader.close();
        }catch(Exception e){}
    }
    public void Liner_Setting(){
        //가로 줄 리니어 레이어
        parentLL = new LinearLayout(this);
        LinearLayout.LayoutParams plControl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        plControl.setMargins(5,5,5,5);
        parentLL.setLayoutParams(plControl);
        parentLL.setOrientation(LinearLayout.HORIZONTAL);
    }
    public void ItemAdd(char A, char B, String Hangul,int type){
        String name="a";
        String name2="a";
        for(int i=5;i>=0;i--)
        {
            if((( A >> i) & 0b1) == 1)
                name = name+"1";
            else if((( A >> i) & 0b1)==0)
                name = name+"0";
        }

        for(int i=5;i>=0;i--)
        {
            if((( B >> i) & 0b1) == 1)
                name2 = name2+"1";
            else if((( B >> i) & 0b1)==0)
                name2 = name2+"0";
        }

        //Log.d("Name 값.", "ItemAdd: Name : "+name +" Name2 : "+name2);
        Drawable tmp1 = ContextCompat.getDrawable(this,getResources().getIdentifier(name, "drawable", this.getPackageName()));
        Drawable tmp2 = ContextCompat.getDrawable(this,getResources().getIdentifier(name2, "drawable", this.getPackageName()));
        //Log.d("Drawable 값.", "ItemAdd: Name : "+tmp1 +" Name2 : "+tmp2);
        if(type == 0) {
            // 설명.
            //image_view.getLayoutParams().height = 20; 이런식으로 설정하면 될거에요.
            //중요한건 height나 width를 설정하고 레이아웃에 image_view.requestLayout()라고 이미지뷰 변경을 적용해달라고 요청해야해요.
            View tmpView = inflater.inflate(R.layout.dot_show, null);
            ImageView Img = (ImageView) tmpView.findViewById(R.id.Dot_img);
            Img.getLayoutParams().width = Width;
            Img.setImageDrawable(tmp1);
            Img.requestLayout();
            TextView text = (TextView) tmpView.findViewById(R.id.Dot_txt);
            text.getLayoutParams().width = Width;
            text.setText(Hangul);
            text.requestLayout();
            parentLL.addView(tmpView);
        }
        if(type == 1)
        {
            View tmpView = inflater.inflate(R.layout.dot_show2, null);
            ImageView Img = (ImageView) tmpView.findViewById(R.id.Dot_img2);
            Img.setImageDrawable(tmp1);
            Img.getLayoutParams().width = Width;
            Img.requestLayout();

            ImageView Img2 = (ImageView) tmpView.findViewById(R.id.Dot_img3);
            Img2.setImageDrawable(tmp2);
            Img2.getLayoutParams().width = Width;
            Img2.requestLayout();

            TextView text = (TextView) tmpView.findViewById(R.id.dot_txt2);
            text.getLayoutParams().width = Width;
            text.setText(Hangul);
            text.requestLayout();
            parentLL.addView(tmpView);
        }

        if(x==cols) {
            linearLayout.addView(parentLL);
            Liner_Setting();
            x = 0;
        }
    }
    //출처: http://neoroid.tistory.com/388 [그린 블로그]
    public static float convertPixelsToDp(float px, Context context){

        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        float dp = px / (metrics.densityDpi / 160f);

        return dp;

    }
}