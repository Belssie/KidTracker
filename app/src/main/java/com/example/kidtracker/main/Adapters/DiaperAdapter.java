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
import com.example.kidtracker.main.Data.DiaperData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DiaperAdapter extends FirebaseRecyclerAdapter<DiaperData, DiaperAdapter.diaperViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public DiaperAdapter(@NonNull FirebaseRecyclerOptions<DiaperData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DiaperAdapter.diaperViewHolder holder, int position, @NonNull DiaperData model) {
        holder.ietDateDiaper.setText(model.getDateTimeDiaper());
        holder.ietTypeDiaper.setText(model.getTypeDiaper());
    }

    @NonNull
    @Override
    public DiaperAdapter.diaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diaper_info, parent, false);
        return new diaperViewHolder(view);
    }

    class diaperViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText ietDateDiaper, ietTypeDiaper;
        TextView tvDelete;
        ConstraintLayout clDiaper;

        String id = fbAuth.getUid();
        public diaperViewHolder(@NonNull View itemView) {
            super(itemView);
            ietDateDiaper = itemView.findViewById(R.id.iet_date_diaper_chron);
            ietTypeDiaper = itemView.findViewById(R.id.iet_type_diaper_chron);
            tvDelete = itemView.findViewById(R.id.tv_delete_diaper);
            clDiaper = itemView.findViewById(R.id.cl_diaper);

            ietDateDiaper.setFocusable(false);
            ietTypeDiaper.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        Log.d("KID", kid);
                        String date = ietDateDiaper.getText().toString();
                        Log.d("DATE", date);

                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("diaper").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "Diaper was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
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