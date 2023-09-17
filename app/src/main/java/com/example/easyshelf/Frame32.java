package com.example.easyshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Frame32 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame32);

        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(2500);
                    startActivity(new Intent(Frame32.this, Frame24.class));
                    finish();

                }catch (Exception e){

                }
            }
        }; thread.start();


    }
}