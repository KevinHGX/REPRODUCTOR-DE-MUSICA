package com.example.navegacion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class InitApplication extends AppCompatActivity {

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_aplication);

        handler = new Handler();
        handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent intent = new Intent(InitApplication.this,MainActivity.class);
                startActivity(intent);
            }
        }, 3000);

    }
}