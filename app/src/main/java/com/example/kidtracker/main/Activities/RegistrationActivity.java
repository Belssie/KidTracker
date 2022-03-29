package com.example.kidtracker.main.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.kidtracker.main.Data.AccountData;
import com.example.kidtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private EditText regEmail, regPw, regPwConfirm;
    private Button btn_register, btn_back;
    private ProgressBar pbRegistration;

    String id, email, password, passwordConfirm;

    private FirebaseAuth fbAuth;
    DatabaseReference dbr;

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
        setTheme(R.style.Theme_Hardtest);
        setContentView(R.layout.activity_registration);
        initViews();

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(view -> {
            registerAcc();
        });

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void initViews() {
        regEmail = findViewById(R.id.et_reg_email);
        regPw = findViewById(R.id.et_reg_pw);
        regPwConfirm = findViewById(R.id.et_reg_pw_confirm);
        btn_register = findViewById(R.id.btn_reg);
        btn_back = findViewById(R.id.btn_reg_back);
        pbRegistration = findViewById(R.id.pb_register);
    }


    public void registerAcc() {
        email = regEmail.getText().toString();
        password = regPw.getText().toString();
        passwordConfirm = regPwConfirm.getText().toString();

        if (email.isEmpty()) {
            regEmail.setError("Please, enter email");
            regEmail.requestFocus();
        } else if (!email_address.matcher(email).matches()) {
            regEmail.setError("Please, enter a valid email!");
            regEmail.requestFocus();
        } else if (password.isEmpty()) {
            regPw.setError("Please enter a password!");
            regPw.requestFocus();
        } else if (password.length() < 6) {
            regPw.setError("Password must contain 6 or more characters!");
            regPw.requestFocus();
        } else if (password.equals(passwordConfirm)) {
            pbRegistration.setVisibility(View.VISIBLE);
            fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    id = fbAuth.getUid();
                    AccountData accountData = new AccountData(id, email, password);
                    dbr.child(id).setValue(accountData).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Your account has been made successfully", Toast.LENGTH_LONG).show();
                            pbRegistration.setVisibility(View.GONE);

                            fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task11 -> {
                                if (task11.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        if(user.isEmailVerified()) {
                                            Intent launcherIntent = new Intent(getApplicationContext(), LauncherActivity.class);
                                            startActivity(launcherIntent);
                                            finish();
                                        } else {
                                            user.sendEmailVerification();
                                            Toast.makeText(getApplicationContext(), "Sent a verification email!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to register! Please check your credentials!", Toast.LENGTH_LONG).show();
                                }
                            });
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            pbRegistration.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    pbRegistration.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
            regPwConfirm.setError("Passwords do not match!");
            regPwConfirm.requestFocus();
        }
    }
}
