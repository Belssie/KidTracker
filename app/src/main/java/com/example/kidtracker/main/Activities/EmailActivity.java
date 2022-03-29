package com.example.kidtracker.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kidtracker.R;
import com.google.android.material.textfield.TextInputEditText;

public class EmailActivity extends AppCompatActivity {
    TextInputEditText etEmailTo, etSubject, etMessage;
    Button btnSend, btnCancel;
    ProgressBar pbEmail;

    String emailTo, subject, message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);
        setContentView(R.layout.send_email);
        loadPopUp();
        initViews();

        emailTo = "belssielicious@gmail.com";
        etEmailTo.setText(emailTo);
        etEmailTo.setFocusable(false);

        btnSend.setOnClickListener(v -> {

            pbEmail.setVisibility(View.VISIBLE);
            sendMail();
        });

        btnCancel.setOnClickListener(v -> finish());

    }

    public void loadPopUp(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setLayout((int) (width * .87), (int) (height * .85));
    }

    public void initViews(){
        etEmailTo = findViewById(R.id.iet_to_email);
        etSubject = findViewById(R.id.iet_subject_email);
        etMessage = findViewById(R.id.iet_message_email);
        btnSend = findViewById(R.id.btn_send_email);
        btnCancel = findViewById(R.id.btn_cancel_email);
        pbEmail = findViewById(R.id.pb_email);
    }

    public void sendMail(){
        subject = etSubject.getText().toString();
        message =  etMessage.getText().toString();

        Intent intentMail = new Intent(Intent.ACTION_SEND);
        intentMail.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        intentMail.putExtra(Intent.EXTRA_SUBJECT, subject);
        intentMail.putExtra(Intent.EXTRA_TEXT, message);

        intentMail.setType("message/rfc822");
        startActivity(Intent.createChooser(intentMail, "Choose an email client"));

        pbEmail.setVisibility(View.GONE);
    }
}
