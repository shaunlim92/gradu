package com.example.user.first;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TimerTask mTask;
    private Timer mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Request_Permission.class );
                startActivity(intent);
                finish();
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 3000);
    }
    @Override
    protected void onDestroy(){
        mTimer.cancel();
        super.onDestroy();
    }
}
