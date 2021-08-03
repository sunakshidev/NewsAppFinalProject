package com.bbcnewsreader.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.bbcnewsreader.R;

public class AboutActivity extends AppCompatActivity {

    private Button thanksB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        thanksB = findViewById(R.id.thanksB);

        thanksB.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}