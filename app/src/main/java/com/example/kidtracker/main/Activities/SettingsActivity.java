package com.example.kidtracker.main.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kidtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    Button btnBack;
    TextView tvLogout, tvLanguage, tvEmailSupport;
    ProgressBar pbSettings;


    FirebaseAuth fbAuth;
    DatabaseReference dbr;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        initViews();

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        btnBack.setOnClickListener(v -> {
            Intent intentHome = new Intent (getApplicationContext(), HomeActivity.class);
            startActivity(intentHome);
            finish();
        });

        tvLanguage.setOnClickListener(v -> showLanguageDialog());

        tvEmailSupport.setOnClickListener(v -> {
            Intent emailSupport = new Intent(getApplicationContext(), EmailActivity.class);
            startActivity(emailSupport);
        });


        tvLogout.setOnClickListener(v -> showPopup());
    }

    public void initViews(){
        tvLogout = findViewById(R.id.tv_logout);
        tvLanguage = findViewById(R.id.tv_language);
        tvEmailSupport = findViewById(R.id.tv_support);
        btnBack = findViewById(R.id.btn_back_settings);
        pbSettings = findViewById(R.id.pb_settings);
    }

    private void showLanguageDialog() {
        final String[] listLanguages = {"English", "български", "Deutsch", "français"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Choose language...");
        builder.setSingleChoiceItems(listLanguages, -1, (dialog, which) -> {
            if (which == 0) {
                pbSettings.setVisibility(View.VISIBLE);
                dbr.child(id).child("language").setValue("en");
                setLocale("en");
                recreate();
            } else if (which == 1) {
                pbSettings.setVisibility(View.VISIBLE);
                dbr.child(id).child("language").setValue("bg");
                setLocale("bg");
                recreate();
            } else if (which == 2) {
                pbSettings.setVisibility(View.VISIBLE);
                dbr.child(id).child("language").setValue("de");
                setLocale("de");
                recreate();
            } else if (which == 3) {
                pbSettings.setVisibility(View.VISIBLE);
                dbr.child(id).child("language").setValue("fr");
                setLocale("fr");
                recreate();
            }
            dialog.dismiss();
            pbSettings.setVisibility(View.GONE);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void showPopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
        alert.setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Sign out", (dialog, which) -> {

                    pbSettings.setVisibility(View.VISIBLE);
                    signOut(); // Last step. Logout function

                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent logoutIntent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(logoutIntent);
        pbSettings.setVisibility(View.GONE);
    }
}