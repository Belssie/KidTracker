package com.example.kidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et_login_email, et_login_password;
    Button btn_login, btn_sign_up;

    DataBaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_login_email = (EditText) findViewById(R.id.et_lgn_email);
        et_login_password = (EditText) findViewById(R.id.et_lgn_pw);
        btn_login = findViewById(R.id.btn_login);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        db = new DataBaseHelper(MainActivity.this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_login_email.getText().toString();
                String password = et_login_password.getText().toString();

                    if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fulfill your registration form!", Toast.LENGTH_SHORT).show();
                } else {
                        Boolean checkPw = db.checkPassword(email, password);
                        if(checkPw) {
                            Toast.makeText(MainActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    }
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (MainActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });
    }
}