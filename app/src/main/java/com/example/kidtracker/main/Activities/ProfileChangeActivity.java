package com.example.kidtracker.main.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.kidtracker.main.Data.KidsData;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Adapters.KidAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileChangeActivity extends AppCompatActivity {
    Button btnAddOne, btnBack;
    RecyclerView kidView;
    private KidAdapter kidAdapter;

    String id;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    @Override
    protected void onStart() {
        super.onStart();
        kidAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        kidAdapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);
        setContentView(R.layout.activity_profile_change);
        btnAddOne = findViewById(R.id.btn_add);
        btnBack = findViewById(R.id.btn_profiles_back);
        kidView = findViewById(R.id.rv_kids);
        kidView.setLayoutManager(new LinearLayoutManager(this));

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        assert id != null;
        FirebaseRecyclerOptions<KidsData> options = new FirebaseRecyclerOptions.Builder<KidsData>().setQuery(dbr.child(id).child("kids"), KidsData.class).build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        kidAdapter = new KidAdapter(options, getApplicationContext());// Connecting Adapter class with the Recycler view*/
        kidView.setAdapter(kidAdapter);

        btnAddOne.setOnClickListener(v -> {
            dbr.child(id).child("last").setValue("none");
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v ->{
            Intent intentHome = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intentHome);
        });
    }
}