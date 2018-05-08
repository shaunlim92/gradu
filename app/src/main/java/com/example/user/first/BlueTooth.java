package com.example.user.first;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BlueTooth extends AppCompatActivity {
    //사용자 정의 함수로 블루투스 활성 상태의 변경 결과를 App으로 알려줄 때 식별자로 사용됨(0보다 커야됨)
    static final int REQUEST_ENABLE_BT = 10;
    //폰의 블루투스 모듈을 사용하기 위한 오브젝트
    BluetoothAdapter mBluetoothAdapter;
    //폰의 블루투스 모듈을 사용하기 위한 오브젝트
    Set<BluetoothDevice> mDevices;
    int mPariedDeviceCount = 0;
    /**
     BluetoothDevice 로 기기의 장치정보를 알아낼 수 있는 자세한 메소드 및 상태값을 알아낼 수 있다.
     연결하고자 하는 다른 블루투스 기기의 이름, 주소, 연결 상태 등의 정보를 조회할 수 있는 클래스.
     현재 기기가 아닌 다른 블루투스 기기와의 연결 및 정보를 알아낼 때 사용.
     */
    BluetoothDevice mRemoteDevie;
    //스마트폰과 페어링 된 디바이스 간 통신 채널에 대응하는 BluetoothSocket
    BluetoothSocket mSocket = null;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    //전송할 Dat파일
    String File_Name;
    //Dat파일 불러올 것
    File Send_File;
    String File_Path;   //변환할 파일.
    private FileReader fr = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        Intent intent = getIntent();
        //파일에 대한 모든 경로가 들어있음. (파일명까지)
        File_Path = intent.getStringExtra("File_Path");
        File_Name = intent.getStringExtra("File_Name");
        //아직 방법을 몰라서 그러는데 블루투스 목록을 띄워줄 때 연결 안된 블루투스는 목록에 안뜸.
        //사용 방법을 알려 주던지, 아니면 한번 등록을 하면은 계속해서 자동으로 연결 해주는 방법을 찾아야할듯.

        checkBluetooth();
    }
    void checkBluetooth(){
        /*
        getDefaultAdapter() : 만일 폰에 블루투스 모듈이 없으면 null을 리턴한다.
        이경우 Toast를 사용해 에러메시지를 표시하고 앱으 종료.
         */
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter== null){   //블루투스 모듈이 없을 경우
            Toast.makeText(getApplicationContext(),"기기가 블루투스를 지원하지 않습니다.",Toast.LENGTH_LONG);
            //뭘 해야할지 생각 해봐야 할듯.
        }
        else{//블루투스 지원
            /*
            isEnable() : 블루투스 모듈이 활성화 되어 있는지 확인,
            true : 지원, false : 미지원
             */
            if(!mBluetoothAdapter.isEnabled()){ //블루투스 지원하며 비활성 상태인 경우
                //블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요청 부분.
                Toast.makeText(getApplicationContext(),"현재 블루투스가 비활성 상태입니다.", Toast.LENGTH_LONG);
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //REQUEST_ENABLE_BT : 블루투스 활성 상태의 변경 결과를 App으로 알려줄 때 식별자로 사용(0이상)
                startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
            }
            else    //블루투스 지원하며 활성 상태인 경우.
            //페어링된 기기 목록을 보여주고 연결할 장치를 선택.
                selectDevice();
        }
    }
    void selectDevice(){
        //블루투스 디바이스는 연결해서 사용하기 전에 먼저 페어링 되어야만 한다.
        //getBondedDevices() : 페어링된 장치 목록 얻어오는 함수.
        mDevices = mBluetoothAdapter.getBondedDevices();
        mPariedDeviceCount = mDevices.size();

        if(mPariedDeviceCount == 0){    //페어링된 장치가 없는 경우
            Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            //페어링된 장치가 없을 경우 뭘 해야할지 생각해봐야할듯.
        }
        //페어링된 장치가 있는 경우
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 선택");

        //각 디바이스는 이름과 (서로 다른) 주소를 가진다. 페어링 된 디바이스들을 표시한다.
        List<String> listItems = new ArrayList<String>();
        for(BluetoothDevice device : mDevices){
            //device.getName() : 단말기의 Bluetooth Adpater 이름을 반환
            listItems.add(device.getName());
        }
        listItems.add("취소"); //취소 항목 추가

        //CharSequence : 변경 가능한 문자열
        // toArray : List형태로 넘어온 것 배열로 바꿔서 처리하기 위한 toArray() 함수.
        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
            //toArray함수를 이용해서 size만큼 배열이 생성
        listItems.toArray(new CharSequence[listItems.size()]);
        System.out.println("확인");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if(item == mPariedDeviceCount){ //연결할 장치를 선택하지 않고 '취소'를 누른경우,
                    Toast.makeText(getApplicationContext(),"연결할 장치를 선택하지 않았습니다.",Toast.LENGTH_LONG);
                    finish();
                    //선택 안할 경우 뭘 해야할지 생각해봐야 할듯.
                }
                else{   //연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도.
                    Toast.makeText(getApplicationContext(),"연결할 장치와 연결을 시도합니다.",Toast.LENGTH_LONG);
                    connectToSelectedDevice(items[item].toString());
                }
            }
        });
        builder.setCancelable(false); //뒤로가기 버튼 사용 금지.
        AlertDialog alert = builder.create();
        alert.show();
    }
    //connectToSelectedDevice() : 원격 장치와 연결하는 과정을 나타냄
    // 실제 데이터 송수신을 위해서는 소켓으로부터 입출력 스트림을 얻고 이용하여 이루어진다.
    void connectToSelectedDevice(String selectedDeviceName){
        //BluetoothDevice 원격 블루투스 기기를 나타냄.
        System.out.println("확인1");
        mRemoteDevie = getDeviceFromBondedList(selectedDeviceName);
        //java.util.UUID.fromString : 자바에서 중복되지 않는 Unique 키 생성.
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        try{
            System.out.println("확인2");
            // 소켓 생성, RFCOMM 채널을 통한 연결.
            // createRfcommSocketToServiceRecord(uuid) : 이 함수를 사용하여 원격 블루투스 장치와 통신할 수 있는 소켓을 생성함.
            // 이 메소드가 성공하면 스마트폰과 페어링 된 디바이스간 통신 채널에 대응하는 BluetoothSocket 오브젝트를 리턴함.
            mSocket = mRemoteDevie.createRfcommSocketToServiceRecord(uuid);
            System.out.println("확인3");
            mSocket.connect(); // 소켓이 생성 되면 connect() 함수를 호출함으로써 두기기의 연결은 완료된다.

            System.out.println("확인4");
            // 데이터 송수신을 위한 스트림 얻기.
            // BluetoothSocket 오브젝트는 두개의 Stream을 제공한다.
            // 1. 데이터를 보내기 위한 OutputStrem
            // 2. 데이터를 받기 위한 InputStream
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();

            //요 부분에다가 데이터 송신 하는거 넣어야 됨.
            int data;
            //전송할 파일 읽어오기
            Log.d("Naem 값.", "ItemAdd: Name : "+File_Name);
            Log.d("Path 값.", "ItemAdd: Name : "+File_Path);

            Send_File = new File(File_Name);
            try {
                Toast.makeText(getApplicationContext(),"블루투스 연결 완료했습니다.",Toast.LENGTH_LONG);
                // 전송을 시작합니다.
                System.out.println("확인5");
                data=0b10000000;    //요거가 데이터 전송 시작.
                mOutputStream.write((byte)data);

                fr = new FileReader(Send_File);
                int j = 0;
                while ((data = fr.read()) != -1) {
                    mOutputStream.write((byte)data);
                    Toast.makeText(getApplicationContext(),"전송중",Toast.LENGTH_LONG);
                    System.out.println("확인7");
                }
            }catch(Exception e)
            {            }

            // 전송을 끝.
            try{
                data=0b01000000;    //요거가 데이터 전송 끝.
                System.out.println("확인8");
                mOutputStream.write((byte)data);
            }catch(Exception e){

            }
            Intent intent1 = new Intent(BlueTooth.this, Dot_Show.class);
            intent1.putExtra("File_Name",File_Name);
            intent1.putExtra("File_Path",File_Path);
            startActivity(intent1);
            finish();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"블루투스 연결 중 오류가 발생했습니다.",Toast.LENGTH_LONG);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    selectDevice();
                }
                else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(getApplicationContext(),"현재 블루투스가 비활성 상태로 이용 할 수 없습니다..", Toast.LENGTH_LONG);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // 블루투스 장치의 이름이 주어졌을때 해당 블루투스 장치 객체를 페어링 된 장치 목록에서 찾아내는 코드.
    BluetoothDevice getDeviceFromBondedList(String name) {
        // BluetoothDevice : 페어링 된 기기 목록을 얻어옴.
        BluetoothDevice selectedDevice = null;
        // getBondedDevices 함수가 반환하는 페어링 된 기기 목록은 Set 형식이며,
        // Set 형식에서는 n 번째 원소를 얻어오는 방법이 없으므로 주어진 이름과 비교해서 찾는다.
        for(BluetoothDevice deivce : mDevices) {
            // getName() : 단말기의 Bluetooth Adapter 이름을 반환
            if(name.equals(deivce.getName())) {
                selectedDevice = deivce;
                break;
            }
        }
        return selectedDevice;
    }
    @Override
    protected void onDestroy(){
        try{
            mSocket.close();
            mOutputStream.close();
            mInputStream.close();
        }catch(Exception e){}
        super.onDestroy();
    }
}
