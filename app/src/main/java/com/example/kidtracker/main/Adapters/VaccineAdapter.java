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
import com.example.kidtracker.main.Data.VaccineData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VaccineAdapter extends FirebaseRecyclerAdapter<VaccineData, VaccineAdapter.vaccineViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public VaccineAdapter(@NonNull FirebaseRecyclerOptions<VaccineData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VaccineAdapter.vaccineViewHolder holder, int position, @NonNull VaccineData model) {
        holder.ietDateVaccine.setText(model.getDateTimeVaccine());
        holder.ietTitleVaccine.setText(model.getTitleVaccine());
    }

    @NonNull
    @Override
    public VaccineAdapter.vaccineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_info, parent, false);
        return new vaccineViewHolder(view);
    }

    class vaccineViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietDateVaccine, ietTitleVaccine;
        TextView tvDelete;
        ConstraintLayout clVaccine;

        String id = fbAuth.getUid();
        public vaccineViewHolder(@NonNull View itemView) {
            super(itemView);

            ietDateVaccine = itemView.findViewById(R.id.iet_date_vaccine_chron);
            ietTitleVaccine = itemView.findViewById(R.id.iet_title_vaccine_chron);

            tvDelete = itemView.findViewById(R.id.tv_delete_vaccine);
            clVaccine = itemView.findViewById(R.id.cl_vaccine);

            ietDateVaccine.setFocusable(false);
            ietTitleVaccine.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        String date = ietDateVaccine.getText().toString();
                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("important").child("vaccine").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "Vaccine was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
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

