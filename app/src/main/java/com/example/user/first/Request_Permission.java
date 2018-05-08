package com.example.user.first;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class Request_Permission extends AppCompatActivity {
private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__permission);
        RequestPermission();
    }
    public void RequestPermission(){
      //check for permission
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_DENIED ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(Request_Permission.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    //권한 획득에 대한 설명 보여주기
                    //그후 권한 요청을 수행
                }
                else
                {   //필요성을 설명할 필요가 없는 경우, 권한 획득 여부 요청
                    ActivityCompat.requestPermissions(Request_Permission.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                Intent intent = new Intent(Request_Permission.this, Main2Activity.class);
                startActivity(intent);
                finish();
            }
        }
        else
        {
            Intent intent = new Intent(Request_Permission.this, Main2Activity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Request_Permission.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                        finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
