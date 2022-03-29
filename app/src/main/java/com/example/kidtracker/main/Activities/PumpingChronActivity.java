package com.example.kidtracker.main.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.kidtracker.R;
import com.example.kidtracker.main.Adapters.PumpingAdapter;
import com.example.kidtracker.main.Data.PumpingBreastFeedingData;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PumpingChronActivity extends AppCompatActivity {
    Button btnBack, btnAdd;
    RecyclerView pumpView;
    PumpingAdapter pumpingAdapter;

    String id, kidName;

    DatabaseReference dbr, dbl;
    FirebaseAuth fbAuth;

    FirebaseRecyclerOptions<PumpingBreastFeedingData> options;

    @Override
    protected void onStop() {
        super.onStop();
        pumpingAdapter.stopListening();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pumping_chron);

        btnAdd = findViewById(R.id.btn_add_pump);
        btnBack = findViewById(R.id.btn_back_info_pump);
        pumpView = findViewById(R.id.rv_pump);
        pumpView.setLayoutManager(new LinearLayoutManager(this));

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        dbl = FirebaseDatabase.getInstance().getReference("accounts").child(id).child("last");
        dbl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kidName = snapshot.getValue(String.class);
                options = new FirebaseRecyclerOptions.Builder<PumpingBreastFeedingData>().setQuery(dbr.child(id).child("kids").child(kidName).child("pumping"), PumpingBreastFeedingData.class).build();
                pumpingAdapter = new PumpingAdapter(options);// Connecting Adapter class with the Recycler view*/
                pumpingAdapter.startListening();
                pumpView.setAdapter(pumpingAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnAdd.setOnClickListener(v -> {
            Intent intentPumping = new Intent(getApplicationContext(), PumpingActivity.class);
            startActivity(intentPumping);
            finish();
        });

        btnBack.setOnClickListener(v -> {
            Intent intentPump = new Intent(getApplicationContext(), PumpingActivity.class);
            startActivity(intentPump);
        });
    }
}