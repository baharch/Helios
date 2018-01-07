package org.bahar.helios;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import org.bahar.helios.weatherentity.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_launcher);



        //Launcher Intent Code
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WeatherActivity.start(LauncherActivity.this);
                finish();
            }
        }, 3000);
        //Launcher Intent Codes



    }


}
