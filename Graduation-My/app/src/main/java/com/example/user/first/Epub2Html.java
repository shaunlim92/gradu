package com.example.user.first;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

//import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Epub2Html extends AppCompatActivity {
    String Root_Folder;
    String Zip_Folder;
    String Root_Path;
    File FolderFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epub2_html);

        Intent intent = getIntent();
        //파일에 대한 모든 경로가 들어있음. (파일명까지)
        String File_Path = intent.getStringExtra("File_Path");
        //최상위 폴더
        String Root_Path = intent.getStringExtra("Root_Path");
        //읽어온 파일의 이름
        String File_Name = intent.getStringExtra("File_Name");
        //가져온 파일이름만 남기기

        //파일 이름 +.epub 파일명
        String name1 = File_Path.substring(File_Path.lastIndexOf("/") + 1);
        //파일 주소.
        String name2 = File_Path.substring(0, File_Path.lastIndexOf("/"));
        //파일 이름만 따옴.
        String name3 = name1.substring(0, name1.lastIndexOf("."));
        //E_dot폴더 경로
        Root_Folder = Root_Path + "/E_Dot";
        //E_dot폴더 아래에있는 zip폴더 경로
        Zip_Folder = Root_Folder + "/Zip";

        //폴더가 없으면 생성
        File Root_File = new File(Root_Folder);
        if (!Root_File.exists())
            Root_File.mkdir();

        //zip폴더 생성
        File Zip_File = new File(Zip_Folder);
        if (!Zip_File.exists())
            Zip_File.mkdir();
        //이 위로는 이상 없음.

        File RootInName = new File(Root_Folder+"/"+name3+".dat");
        if(RootInName.exists())
            File_Delete(RootInName);
        RootInName = new File(Root_Folder+"/"+name3+".txt");
        if(RootInName.exists())
            File_Delete(RootInName);





        File filePre = new File(Zip_Folder + "/", name1);
        File fileNow = new File(Zip_Folder + "/", name3 + ".zip");

        copyFile(File_Path, Zip_Folder + "/" + name3 + ".epub");
        //파일 확장자 변경
        if (filePre.renameTo(fileNow))
        {

        }
        else{

        }


           /* 박종수
              18.01.05 15:00
              epub파일을 zip파일로 바꾼후 zip파일의 압축을 해제
            */
        String Foldername = Zip_Folder + "/" + name3;
        Foldername = makeFolder(Foldername);
        String unzipfilepath = fileNow.getAbsolutePath();
        try {
            unZipZipfile(unzipfilepath, Foldername, name3, Root_Path);
            Toast.makeText(getApplicationContext(), "압축성공", Toast.LENGTH_LONG).show();
        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "압축실패", Toast.LENGTH_LONG).show();
        }


    }

    //inFilePath 에 있는 것을 outFilePath 로 복사.
    //이 메소드를 한 이유는, 확장자를 그냥 변경할 경우 오리지널 파일이 사라짐.
    //채널을 사용한 이유는 일반 버퍼스트림을 이용할 경우 너무 오래걸림.
    public synchronized void copyFile(String inFilePath, String outFilePath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fcin = null;
        FileChannel fcout = null;

        try {
            //스트림 생성
            fis = new FileInputStream(inFilePath);
            fos = new FileOutputStream(outFilePath);

            fcin = fis.getChannel();
            fcout = fos.getChannel();

            long size = fcin.size();

            fcin.transferTo(0, size, fcout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //자원 해제
        try {
            fcout.close();
        } catch (IOException ioe) {
        }
        try {
            fcin.close();
        } catch (IOException ioe) {
        }
        try {
            fos.close();
        } catch (IOException ioe) {
        }
        try {
            fis.close();
        } catch (IOException ioe) {
        }
    }

    /* 박종수
       18.01.05 12:02
       폴더경로+이름을 받고
       폴더가 있으면 폴더를 삭제할것인지 물어보고, 폴더가 없으면 폴더를 만든다.
     */
    public String makeFolder(final String foldername) {
        try {

            FolderFile = new File(foldername);

            //폴더가 존재하지 않으면 폴더생성
            if (FolderFile.exists())
                File_Delete(FolderFile);


            FolderFile.mkdir();
                //폴더가존재하면 폴더 생성


        } catch (Exception e) {
        }
        return FolderFile.getPath();

    }

    /*
    박종수
    18.01.05 15:00
    위에서 압축된 zip파일을 해제하기 위해 만듬
    http://nowonbun.tistory.com/321
     */
    public void unZipZipfile(String zipFileName, String unZipdirectory, String file_name, String RootPath) throws Throwable {
        File zipfile = new File(zipFileName);
        File unZipFileNCXFILE = null;
        FileInputStream unZipFileInputStream = null;
        ZipInputStream unZipZipInputStream = null;
        ZipEntry unZipZipEntry = null;
        String unZipCopyfileName = null;
        try {
            //파일스트림
            unZipFileInputStream = new FileInputStream(zipfile);
            //Zip 파일 스트림
            unZipZipInputStream = new ZipInputStream(unZipFileInputStream);
            while ((unZipZipEntry = unZipZipInputStream.getNextEntry()) != null) {
                String inzipfilename = unZipZipEntry.getName();
                File file = new File(unZipdirectory, inzipfilename);
                //entiry가 폴더면 폴더 생성
                if (unZipZipEntry.isDirectory()) {
                    file.mkdirs();
                }
                // 폴더가 아니면 파일 생성
                else {
                    if (file.getName().endsWith(".ncx")) {
                        createFile(file, unZipZipInputStream);
                        //String NcxFolder= makeFolder(file.getParent()+"/ncx");
                        //unZipCopyfileName=NcxFolder+"/ncx.xml";
                        unZipCopyfileName = file.getParent() + "/ncx.xml";
                        copyFile(file.getPath(), unZipCopyfileName);
                    } else {
                        //파일이면 파일 만들기
                        createFile(file, unZipZipInputStream);
                    }
                }
            }
            //압축이 끝나면
            unZipFileNCXFILE = new File(unZipCopyfileName);
            //String filenameama = Root_Folder+name3+".xml";
            xmlParse(unZipFileNCXFILE, file_name, RootPath);
        } catch (Throwable e) {
            throw e;
        } finally {
            if (unZipZipInputStream != null)
                unZipZipInputStream.close();
            if (unZipZipInputStream != null)
                unZipZipInputStream.close();
        }
    }

    /*
    박종수
    180107 22:32
    위의 UNZIP 클래스에서 압축을 해제할때 파일을 생성하는 클래스
    참조 http://nowonbun.tistory.com/321
     */
    private static void createFile(File file, ZipInputStream zis) throws Throwable {
        //디렉토리 확인
        File parentDir = new File(file.getParent());
        //디렉토리가 없으면 생성하자
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        FileOutputStream fos = null;
        fos = new FileOutputStream(file);
        //파일 스트림 선언
        try {
            byte[] buffer = new byte[256];
            int size = 0;
            //Zip스트림으로부터 byte뽑아내기
            while ((size = zis.read(buffer)) > 0) {
                //byte로 파일 만들기
                fos.write(buffer, 0, size);
            }
        } catch (Throwable e) {
            throw e;
        }
    }

    /*
    박종수
    180108 17:57
    xml파일을 파싱하기 위한 클래스
    ncx파일을 xml로 바꾼후 바꾼 xml파일을 text와 content부분만 파싱한다.
     */
    public void xmlParse(File file, String a, String RootPath) throws Throwable {
        ArrayList<String> xmlParse_htmllist = new ArrayList<String>();
        String fileName = file.getPath();
        BufferedReader xmlParseIn = new BufferedReader(new FileReader(fileName));
        BufferedWriter xmlParseWriter = new BufferedWriter(new FileWriter(Root_Folder + "/" + a+ ".txt", true));
        String line = "", data = "";
        try {

            while ((line = xmlParseIn.readLine()) != null) {

                if (line.contains("<docTitle>")) {
                    //while (!line.contains("</docTitle>"))
                    //  line = xmlParseIn.readLine();
                } /*else if (line.contains("<text")) {
                    data = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<"));
                    xmlParseWriter.write(data);
                    data = null;
                }*/ else if (line.contains("<content src=")) {
                    String contentPath = file.getParent() + "/" + line.substring(line.indexOf("=") + 2, line.lastIndexOf("/") - 1);
                    if (contentPath.contains(".html#") || contentPath.contains(".xml#")) {
                        contentPath = contentPath.substring(0, contentPath.lastIndexOf("#"));
                    }
                    if (!xmlParse_htmllist.contains(contentPath)) {
                        File fff = new File(contentPath);
                        if (fff.exists()) {
                            Document d = Jsoup.parse(fff, "UTF-8");
                            //data=d.text();
                            Elements e = d.select("p");
                            for (Element x : e) {
                                data = x.text();
                                xmlParseWriter.write(data);

                            }
                            d = null;
                            xmlParse_htmllist.add(contentPath);
                        }
                    }
                }
            }


        } catch (Throwable e) {
            throw e;
        }


        File Zip_File = new File (Root_Folder + "/Zip");
        File_Delete(Zip_File);
        Log.d("Path" ,Zip_File.getPath());


        xmlParseWriter.flush();
        xmlParseWriter.close();
        xmlParseIn.close();

        File epubtxtfile = new File (Root_Folder + "/" + a+ ".txt");

        Intent intent = new Intent(
                getApplicationContext(), File_Read.class);
        //파일 경로 전송.

        Log.d("Path" ,epubtxtfile.getPath());
        Log.d("root" ,RootPath);
        Log.d("name" ,epubtxtfile.getName());

        intent.putExtra("File_Path", epubtxtfile.getPath());
        intent.putExtra("Root_Path", RootPath);
        intent.putExtra("File_Name", epubtxtfile.getName());
        startActivity(intent);

        finish();

    }

    /* 박종수 180325 22:30
    폴더 삭제시 안의 있는 파일도 삭제해야 폴더가 삭제 되므로 삭제부분을 따로 만듬
     */

    public void File_Delete(File deleteFile){


        if( deleteFile.exists() ){ //파일존재여부확인
            if(deleteFile.isDirectory()){ //파일이 디렉토리인지 확인
                File[] files = deleteFile.listFiles();

                for( int i=0; i<files.length; i++){
                    File filei=files[i];
                    File_Delete(filei);
                }
            }
            if(deleteFile.delete()){
            }else{
                Toast.makeText(getApplicationContext(), deleteFile.getName()+"파일 삭제 실패.", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getApplicationContext(), deleteFile.getName()+"파일이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
        }

    }



}
