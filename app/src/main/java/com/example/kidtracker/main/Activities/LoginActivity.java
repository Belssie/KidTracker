package com.example.kidtracker.main.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.kidtracker.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText et_login_email, et_login_password;
    Button btn_login, btn_sign_up;
    ProgressBar pbLogin;
    TextView tvForgottenPw;

    private FirebaseAuth fbAuth;
    DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initViews();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();

        if (fbAuth.getCurrentUser() != null) {
            Intent homeIntent = new Intent(getApplicationContext(), LauncherActivity.class);
            startActivity(homeIntent);

            finish();
        }

        btn_login.setOnClickListener(view -> {
            userLogin();
        });

        btn_sign_up.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(i);
        });

        tvForgottenPw.setOnClickListener(v -> {
            Intent forgotPwIntent = new Intent(getApplicationContext(), ForgottenPassword.class);
            startActivity(forgotPwIntent);
        });
    }

    public void initViews() {
        et_login_email = findViewById(R.id.et_lgn_email);
        et_login_password = findViewById(R.id.et_lgn_pw);
        btn_login = findViewById(R.id.btn_login);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        pbLogin = findViewById(R.id.pb_login);
        tvForgottenPw = findViewById(R.id.tv_forgotten_pw);
    }

    public void userLogin() {
        String email = et_login_email.getText().toString();
        String password = et_login_password.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fulfill your registration form!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_login_email.setError("Please enter a valid email!");
            et_login_email.requestFocus();
        } else if (password.length() < 6) {
            et_login_password.setError("Minimum password length is 6 characters!");
            et_login_password.requestFocus();
        } else {
            pbLogin.setVisibility(View.VISIBLE);

            fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        if(user.isEmailVerified()) {
                            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(homeIntent);
                            finish();

                        } else {
                            pbLogin.setVisibility(View.GONE);
                            user.sendEmailVerification();
                            Toast.makeText(getApplicationContext(), "Sent a verification email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to login! Please check your credentials!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}

