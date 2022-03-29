package com.example.kidtracker.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kidtracker.main.Data.ReminderData;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Adapters.RemindersAdapter;
import com.example.kidtracker.main.PopUps.RemindersPopUp;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RemindersActivity extends AppCompatActivity {
    Button btnBack, btnAdd;
    RecyclerView alarmView;
    RemindersAdapter remindersAdapter;

    DatabaseReference dbr, dbl;
    FirebaseAuth fbAuth;

    String id, kidName;

    @Override
    protected void onStop() {
        super.onStop();
        remindersAdapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);
        setContentView(R.layout.activity_alarms);
        initViews();

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        dbl = FirebaseDatabase.getInstance().getReference("accounts").child(id).child("last");

        dbl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kidName = snapshot.getValue(String.class);
                FirebaseRecyclerOptions<ReminderData> options = new FirebaseRecyclerOptions.Builder<ReminderData>().setQuery(dbr.child(id).child("kids").child(kidName).child("reminders"), ReminderData.class).build();
                remindersAdapter = new RemindersAdapter(options, getApplicationContext());
                remindersAdapter.startListening();
                alarmView.setLayoutManager(new LinearLayoutManager(RemindersActivity.this));
                alarmView.setAdapter(remindersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAdd.setOnClickListener(v -> {
            dbr.child(id).child("kids").child(kidName).child("last_reminder").setValue("none");
            Intent intentAlarms = new Intent(getApplicationContext(), RemindersPopUp.class);
            startActivity(intentAlarms);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    public void initViews(){
        alarmView = findViewById(R.id.rv_alarms);
        btnBack = findViewById(R.id.btn_alarms_back);
        btnAdd = findViewById(R.id.btn_add_alarms);
    }
}