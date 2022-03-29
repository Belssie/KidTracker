package com.example.kidtracker.main.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class LauncherActivity extends AppCompatActivity {
    String langOn, id;
    FirebaseAuth fbAuth;
    DatabaseReference dbr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();
        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);

        ValueEventListener languageListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                langOn = snapshot.getValue(String.class);
                if (langOn != null) {
                    setLocale(langOn);
                    Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(home);
                } else {
                    setLocale("en");
                    Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(home);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("language").addValueEventListener(languageListener);
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
