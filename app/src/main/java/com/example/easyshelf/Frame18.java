package com.example.easyshelf;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Frame18 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame18);



        Button btnSw = findViewById(R.id.btn_sw);
        btnSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent для запуска активности activity_frame32
                Intent intent = new Intent(Frame18.this, Frame32.class);
                startActivity(intent);
            }
        });
    }
}
