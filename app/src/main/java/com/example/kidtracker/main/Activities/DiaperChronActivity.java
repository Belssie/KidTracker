package com.example.kidtracker.main.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.kidtracker.R;

import com.example.kidtracker.main.Adapters.DiaperAdapter;
import com.example.kidtracker.main.Data.DiaperData;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DiaperChronActivity extends AppCompatActivity {
    Button btnBack, btnAdd;
    RecyclerView diaperView;
    DiaperAdapter diaperAdapter;

    String id, kidName;

    DatabaseReference dbr, dbl;
    FirebaseAuth fbAuth;

    FirebaseRecyclerOptions<DiaperData> options;

    @Override
    protected void onStop() {
        super.onStop();
        diaperAdapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper_chron);

        btnAdd = findViewById(R.id.btn_add_diaper);
        btnBack = findViewById(R.id.btn_back_info_diaper);
        diaperView = findViewById(R.id.rv_diaper);
        diaperView.setLayoutManager(new LinearLayoutManager(this));

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        dbl = FirebaseDatabase.getInstance().getReference("accounts").child(id).child("last");
        dbl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kidName = snapshot.getValue(String.class);
                options = new FirebaseRecyclerOptions.Builder<DiaperData>().setQuery(dbr.child(id).child("kids").child(kidName).child("diaper"), DiaperData.class).build();
                diaperAdapter = new DiaperAdapter(options);// Connecting Adapter class with the Recycler view*/
                diaperAdapter.startListening();
                diaperView.setAdapter(diaperAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAdd.setOnClickListener(v -> {
            Intent intentDiaper = new Intent(getApplicationContext(), DiaperActivity.class);
            startActivity(intentDiaper);
            finish();
        });

        btnBack.setOnClickListener(v -> finish());
    }
}