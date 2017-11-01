package com.azwallpaper;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int KEEP_TIME = 2000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----------onCreate----------");

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getSupportActionBar().hide();

        setContentView(R.layout.activity_splash);
        handler = new Handler();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "----------startActivityIntent----------");
                Intent intent = new Intent(SplashActivity.this,  DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }, KEEP_TIME);
    }
}
