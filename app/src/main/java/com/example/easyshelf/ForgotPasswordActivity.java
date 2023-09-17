package com.example.easyshelf;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText forEmail;
    private Button forgot;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        forEmail = findViewById(R.id.forgot_email);
        forgot = findViewById(R.id.btn_forgot_pw);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = forEmail.getText().toString();


                if (email.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this, "Please provide your email", Toast.LENGTH_SHORT).show();
                } else {
                    forgotPassword();
                }
            }
        });
    }

    private void forgotPassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, "Проверьте почту", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            finish();
                        }else {
                            Toast.makeText(ForgotPasswordActivity.this, "Ошибка: " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}