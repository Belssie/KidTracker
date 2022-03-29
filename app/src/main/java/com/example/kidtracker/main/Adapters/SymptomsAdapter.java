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
import com.example.kidtracker.main.Data.SymptomsData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SymptomsAdapter extends FirebaseRecyclerAdapter<SymptomsData, SymptomsAdapter.symptomsViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public SymptomsAdapter(@NonNull FirebaseRecyclerOptions<SymptomsData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SymptomsAdapter.symptomsViewHolder holder, int position, @NonNull SymptomsData model) {
        holder.ietDateSymptoms.setText(model.getDateTimeSymptom());
        holder.ietTitleSymptoms.setText(model.getTitleSymptom());
    }

    @NonNull
    @Override
    public SymptomsAdapter.symptomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.symptoms_info, parent, false);
        return new symptomsViewHolder(view);
    }

    class symptomsViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietDateSymptoms, ietTitleSymptoms;
        TextView tvDelete;
        ConstraintLayout clSymptoms;

        String id = fbAuth.getUid();
        public symptomsViewHolder(@NonNull View itemView) {
            super(itemView);

            ietDateSymptoms = itemView.findViewById(R.id.iet_date_symptoms_chron);
            ietTitleSymptoms = itemView.findViewById(R.id.iet_title_symptoms_chron);

            tvDelete = itemView.findViewById(R.id.tv_delete_symptoms);
            clSymptoms = itemView.findViewById(R.id.cl_symptoms);

            ietDateSymptoms.setFocusable(false);
            ietTitleSymptoms.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        String date = ietDateSymptoms.getText().toString();
                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("important").child("symptom").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "Symptoms was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
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

