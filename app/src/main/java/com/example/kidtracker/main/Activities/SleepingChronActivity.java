package com.example.kidtracker.main.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Adapters.SleepingAdapter;
import com.example.kidtracker.main.Data.SleepingData;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SleepingChronActivity extends AppCompatActivity {
    Button btnBack, btnAdd;
    RecyclerView sleepView;
    SleepingAdapter sleepAdapter;

    String id, kidName;

    DatabaseReference dbr, dbl;
    FirebaseAuth fbAuth;

    FirebaseRecyclerOptions<SleepingData> options;

    @Override
    protected void onStop() {
        super.onStop();
        sleepAdapter.stopListening();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeping_chron);

        btnAdd = findViewById(R.id.btn_add_sleep);
        btnBack = findViewById(R.id.btn_back_info_sleep);
        sleepView = findViewById(R.id.rv_sleep);
        sleepView.setLayoutManager(new LinearLayoutManager(this));

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        dbl = FirebaseDatabase.getInstance().getReference("accounts").child(id).child("last");

        dbl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kidName = snapshot.getValue(String.class);
                options = new FirebaseRecyclerOptions.Builder<SleepingData>().setQuery(dbr.child(id).child("kids").child(kidName).child("sleep"), SleepingData.class).build();
                sleepAdapter = new SleepingAdapter(options);// Connecting Adapter class with the Recycler view*/
                sleepAdapter.startListening();
                sleepView.setAdapter(sleepAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAdd.setOnClickListener(v -> {
            Intent intentSleeping = new Intent(getApplicationContext(), SleepingActivity.class);
            startActivity(intentSleeping);
            finish();
        });

        btnBack.setOnClickListener(v -> {
            Intent intentSleep = new Intent(getApplicationContext(), SleepingActivity.class);
            startActivity(intentSleep);
        });
    }
}