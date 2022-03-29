package com.example.kidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    EditText regEmail, regPw, regPwConfirm;
    Button btn_register, btn_back;

    DataBaseHelper dbh;

    public static final Pattern email_address = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z][a-zA-Z\\-]{1,25}" +
                    ")+"
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regEmail = (EditText) findViewById(R.id.et_reg_email);
        regPw = (EditText) findViewById(R.id.et_reg_pw);
        regPwConfirm = (EditText) findViewById(R.id.et_reg_pw_confirm);
        btn_register = (Button) findViewById(R.id.btn_reg);
        btn_back = (Button) findViewById(R.id.btn_reg_back);
        dbh = new DataBaseHelper(RegistrationActivity.this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = regEmail.getText().toString();
                String password = regPw.getText().toString();
                String passwordConfirm = regPwConfirm.getText().toString();

                if(email.isEmpty())
                    Toast.makeText(getApplicationContext(),"Please, enter email", Toast.LENGTH_SHORT).show();
                else if (!email_address.matcher(email).matches())
                    Toast.makeText(getApplicationContext(), "Please, enter a valid email!", Toast.LENGTH_SHORT).show();
                else if (password.isEmpty())
                    Toast.makeText(getApplicationContext(),"Please enter a password!", Toast.LENGTH_SHORT).show();
                else if (password.length()<6)
                    Toast.makeText(getApplicationContext(), "Password must contain 6 or more characters!", Toast.LENGTH_SHORT).show();
                else if (password.equals(passwordConfirm)){
                    AccountData acc = new AccountData(email, password);
                    acc.setEmail(email);
                    acc.setPw(password);

                    dbh.addNewAcc(acc);
                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    regEmail.setText("");
                    regPw.setText("");
                    regPwConfirm.setText("");
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}