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
import com.example.kidtracker.main.Data.PumpingBreastFeedingData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PumpingAdapter extends FirebaseRecyclerAdapter<PumpingBreastFeedingData, PumpingAdapter.pumpingViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public PumpingAdapter(@NonNull FirebaseRecyclerOptions<PumpingBreastFeedingData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PumpingAdapter.pumpingViewHolder holder, int position, @NonNull PumpingBreastFeedingData model) {
       holder.ietDatePumping.setText(model.getDateTimeStarted());
       holder.ietAmountPumping.setText(model.getAmountPBF());
       holder.ietLeftDuration.setText(model.getLeftDur());
       holder.ietRightDuration.setText(model.getRightDur());
    }

    @NonNull
    @Override
    public PumpingAdapter.pumpingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pumping_info, parent, false);
        return new pumpingViewHolder(view);
    }

    class pumpingViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietDatePumping, ietAmountPumping, ietLeftDuration, ietRightDuration;
        TextView tvDelete;
        ConstraintLayout clPumping;

        String id = fbAuth.getUid();
        public pumpingViewHolder(@NonNull View itemView) {
            super(itemView);
            ietDatePumping = itemView.findViewById(R.id.iet_date_pumping_chron);
            ietAmountPumping = itemView.findViewById(R.id.iet_ml_pumping_chron);
            ietLeftDuration = itemView.findViewById(R.id.iet_left_pumping_chron);
            ietRightDuration = itemView.findViewById(R.id.iet_right_pumping_chron);
            tvDelete = itemView.findViewById(R.id.tv_delete_pumping);
            clPumping = itemView.findViewById(R.id.cl_pumping);

            ietDatePumping.setFocusable(false);
            ietAmountPumping.setFocusable(false);
            ietLeftDuration.setFocusable(false);
            ietRightDuration.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        String date = ietDatePumping.getText().toString();
                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("pumping").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "Pumping was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }; dbr.child(id).child("last").addValueEventListener(kidListener);
            });
        }
    }
}