package com.example.kidtracker.main.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Data.MedicationData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedicationAdapter extends FirebaseRecyclerAdapter<MedicationData, MedicationAdapter.medicationViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public MedicationAdapter(@NonNull FirebaseRecyclerOptions<MedicationData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MedicationAdapter.medicationViewHolder holder, int position, @NonNull MedicationData model) {
        holder.ietDateMedication.setText(model.getDateTimeMedication());
        holder.ietTitleMedication.setText(model.getTitleMedication());
        holder.ietAmountMedication.setText(model.getAmountMedication());
    }

    @NonNull
    @Override
    public MedicationAdapter.medicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medication_info, parent, false);
        return new medicationViewHolder(view);
    }

    class medicationViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietDateMedication, ietTitleMedication, ietAmountMedication;
        TextView tvDelete;
        ConstraintLayout clMedication;

        String id = fbAuth.getUid();
        public medicationViewHolder(@NonNull View itemView) {
            super(itemView);

            ietDateMedication = itemView.findViewById(R.id.iet_date_medication_chron);
            ietTitleMedication = itemView.findViewById(R.id.iet_title_medication_chron);
            ietAmountMedication = itemView.findViewById(R.id.iet_amount_medication_chron);
            tvDelete = itemView.findViewById(R.id.tv_delete_medication);
            clMedication = itemView.findViewById(R.id.cl_medication);

            ietDateMedication.setFocusable(false);
            ietTitleMedication.setFocusable(false);
            ietAmountMedication.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        Log.d("KID", kid);
                        String date = ietDateMedication.getText().toString();
                        Log.d("DATE", date);

                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("important").child("medication").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "Medication was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
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

