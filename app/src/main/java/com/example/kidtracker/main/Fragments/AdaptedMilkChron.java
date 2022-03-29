package com.example.kidtracker.main.Fragments;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kidtracker.R;
import com.example.kidtracker.main.Adapters.AdaptedMilkAdapter;
import com.example.kidtracker.main.Data.AdaptedMilkData;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdaptedMilkChron extends Fragment {
    RecyclerView adaptedMilkView;
    AdaptedMilkAdapter adaptedMilkAdapter;
    TextView tvName;

    String id, kidName;

    DatabaseReference dbr, dbl;
    FirebaseAuth fbAuth;

    FirebaseRecyclerOptions<AdaptedMilkData> options;

    Context context;

    @Override
    public void onStop() {
        super.onStop();
        adaptedMilkAdapter.stopListening();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_adapted_milk_chron, container, false);

        context = v.getContext();

        adaptedMilkView = v.findViewById(R.id.rv_am);

        tvName = v.findViewById(R.id.tv_name_am);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");

        dbr.keepSynced(true);

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        dbl = FirebaseDatabase.getInstance().getReference("accounts").child(id).child("last");

        dbl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kidName = snapshot.getValue(String.class);
                options = new FirebaseRecyclerOptions.Builder<AdaptedMilkData>().setQuery(dbr.child(id).child("kids").child(kidName).child("feeding").child("adapted_milk"), AdaptedMilkData.class).build();
                adaptedMilkAdapter = new AdaptedMilkAdapter(options);// Connecting Adapter class with the Recycler view*/
                adaptedMilkAdapter.startListening();
                adaptedMilkView.setAdapter(adaptedMilkAdapter);
                adaptedMilkView.setLayoutManager(new LinearLayoutManager(context));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}