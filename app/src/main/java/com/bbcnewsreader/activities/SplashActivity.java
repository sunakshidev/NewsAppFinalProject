package com.bbcnewsreader.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.bbcnewsreader.MainActivity;
import com.bbcnewsreader.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("news", MODE_PRIVATE);

        if (!sharedPreferences.contains("first")){
            SharedPreferences.Editor editor = getSharedPreferences("news", MODE_PRIVATE).edit();
            editor.putBoolean("first", true);
            editor.apply();
            Toast.makeText(this, "Welcome to BBC News Reader", Toast.LENGTH_SHORT).show();
        }



        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        },  2000);
    }
}