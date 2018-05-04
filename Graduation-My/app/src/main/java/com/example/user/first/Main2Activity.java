package com.example.user.first;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageButton b = (ImageButton)findViewById(R.id.imageButton);
        ImageButton b2 = (ImageButton)findViewById(R.id.imageButton2);
        ImageButton b3= (ImageButton)findViewById(R.id.imageButton3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Main2Activity.this, File_Find2.class);
                startActivity(intent);
            }
        });
        //버튼 클릭하면 Dat 파일로 변환 된 것들만 보여주어 읽을 수 있는 부분을 만들 예정.
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Main2Activity.this, DotFile_Show.class);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Main2Activity.this, Image_Search.class);
                startActivity(intent);
            }
        });
    }
}
