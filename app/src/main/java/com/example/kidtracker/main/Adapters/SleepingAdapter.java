package com.example.kidtracker.main.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Data.SleepingData;
import com.example.kidtracker.main.Workers.TimeCalculator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;

public class SleepingAdapter extends FirebaseRecyclerAdapter<SleepingData, SleepingAdapter.sleepViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public SleepingAdapter(@NonNull FirebaseRecyclerOptions<SleepingData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SleepingAdapter.sleepViewHolder holder, int position, @NonNull SleepingData model) {
        holder.ietFellAsleep.setText(model.getFellAsleep());
        holder.ietWokeUp.setText(model.getWokeUp());
        TimeCalculator tc = new TimeCalculator(model.getFellAsleep(), model.getWokeUp());
        try {
            String duration = tc.findDifference(model.getFellAsleep(), model.getWokeUp());
            holder.ietDuration.setText(duration);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public SleepingAdapter.sleepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sleep_info, parent, false);
        return new sleepViewHolder(view);
    }

    class sleepViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietFellAsleep, ietWokeUp, ietDuration;
        TextView tvDelete;
        ConstraintLayout clSleep;

        String id = fbAuth.getUid();
        public sleepViewHolder(@NonNull View itemView) {
            super(itemView);

            ietFellAsleep = itemView.findViewById(R.id.iet_fell_asleep_chron);
            ietWokeUp = itemView.findViewById(R.id.iet_woke_up_chron);
            ietDuration = itemView.findViewById(R.id.iet_duration_chron);
            tvDelete = itemView.findViewById(R.id.tv_delete_sleep);
            clSleep = itemView.findViewById(R.id.cl_sleep);

            ietFellAsleep.setFocusable(false);
            ietWokeUp.setFocusable(false);
            ietDuration.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kid = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kidname = snapshot.getValue(String.class);
                        if (kidname != null) {
                            dbr.child(id).child("kids").child(kidname).child("sleep").child(ietFellAsleep.getText().toString()).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "Sleep was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }; dbr.child(id).child("last").addValueEventListener(kid);
            });
        }
    }
}
