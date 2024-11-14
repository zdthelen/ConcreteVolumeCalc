package com.zachthelen.concretevolumecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonFooting = findViewById(R.id.buttonFooting);
        Button buttonWall = findViewById(R.id.buttonWall);

        buttonFooting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FootingActivity.class);
                startActivity(intent);
            }
        });

        buttonWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WallActivity.class);
                startActivity(intent);
            }
        });
    }
}