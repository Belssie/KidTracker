package com.example.kidtracker.main.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.kidtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPassword extends AppCompatActivity {
    private EditText emailPwReset;
    private Button btnPwReset, btnFpwBack;
    private ProgressBar pbPwReset;

    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);
        setContentView(R.layout.activity_forgotten_password);
        initViews();

        fbAuth = FirebaseAuth.getInstance();

        btnPwReset.setOnClickListener(v -> resetPassword());

        btnFpwBack.setOnClickListener(v -> finish());
    }

    private void resetPassword(){
        String email = emailPwReset.getText().toString();

        if(email.isEmpty()){
            emailPwReset.setError("Email is required!");
            emailPwReset.requestFocus();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailPwReset.setError("Please provide valid email!");
            emailPwReset.requestFocus();
        } else {
            pbPwReset.setVisibility(View.VISIBLE);
            fbAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                    pbPwReset.setVisibility(View.GONE);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_LONG).show();
                    emailPwReset.requestFocus();
                    pbPwReset.setVisibility(View.GONE);
                }
            });
        }
    }

    private void initViews(){
        emailPwReset = findViewById(R.id.et_email_pw_reset);
        btnPwReset = findViewById(R.id.btn_reset_pw);
        btnFpwBack = findViewById(R.id.btn_fpw_back);
        pbPwReset = findViewById(R.id.pb_reset_pw);
    }
}