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

import com.example.kidtracker.R;
import com.example.kidtracker.main.Adapters.MemoryAdapter;
import com.example.kidtracker.main.Data.MemoryData;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemoryChron extends Fragment {
    RecyclerView memoryView;
    MemoryAdapter memoryAdapter;

    String id, kidName;

    DatabaseReference dbr, dbl;
    FirebaseAuth fbAuth;
    FirebaseRecyclerOptions<MemoryData> options;

    Context context;

    @Override
    public void onStop() {
        super.onStop();
        memoryAdapter.stopListening();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_memory_chron, container, false);

        context = v.getContext();

        memoryView = v.findViewById(R.id.rv_memory);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        dbl = FirebaseDatabase.getInstance().getReference("accounts").child(id).child("last");
        dbl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kidName = snapshot.getValue(String.class);
                options = new FirebaseRecyclerOptions.Builder<MemoryData>().setQuery(dbr.child(id).child("kids").child(kidName).child("important").child("memory"), MemoryData.class).build();
                memoryAdapter = new MemoryAdapter(options);// Connecting Adapter class with the Recycler view*/
                memoryAdapter.startListening();
                memoryView.setAdapter(memoryAdapter);
                memoryView.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
}