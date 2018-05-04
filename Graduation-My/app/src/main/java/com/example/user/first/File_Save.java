package com.example.user.first;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class File_Save extends AppCompatActivity {
    //저장위치 temp폴더
    private String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp/";

    protected void dataSaveLog(String _log, String _fileName)
    {
        File file = new File(dirPath);

        // 일치하는 폴더가 없으면 생성
        if (!file.exists())
            file.mkdirs();

        // txt 파일 생성
        String testStr = "예시";
        File savefile = new File(dirPath+"/test.txt");
        try{
            FileOutputStream fos = new FileOutputStream(savefile);
            fos.write(testStr.getBytes());
            fos.close();
            Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show();
        } catch(IOException e){}


    }

}
// 오류 수정중