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
import com.example.kidtracker.main.Data.GrowthData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GrowthAdapter extends FirebaseRecyclerAdapter<GrowthData, GrowthAdapter.growthViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public GrowthAdapter(@NonNull FirebaseRecyclerOptions<GrowthData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GrowthAdapter.growthViewHolder holder, int position, @NonNull GrowthData model) {
        holder.ietDateGrowth.setText(model.getDateTimeGrowth());
        holder.ietLengthGrowth.setText(model.getLengthGrowth());
        holder.ietWeightGrowth.setText(model.getWeightGrowth());
    }

    @NonNull
    @Override
    public GrowthAdapter.growthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.growth_info, parent, false);
        return new growthViewHolder(view);
    }

    class growthViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietDateGrowth, ietLengthGrowth, ietWeightGrowth;
        TextView tvDelete;
        ConstraintLayout clGrowth;

        String id = fbAuth.getUid();
        public growthViewHolder(@NonNull View itemView) {
            super(itemView);

            ietDateGrowth = itemView.findViewById(R.id.iet_date_growth_chron);
            ietLengthGrowth = itemView.findViewById(R.id.iet_length_growth_chron);
            ietWeightGrowth = itemView.findViewById(R.id.iet_weight_growth_chron);
            tvDelete = itemView.findViewById(R.id.tv_delete_growth);
            clGrowth = itemView.findViewById(R.id.cl_growth);

            ietDateGrowth.setFocusable(false);
            ietLengthGrowth.setFocusable(false);
            ietWeightGrowth.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        String date = ietDateGrowth.getText().toString();
                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("important").child("growth").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "Growth was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
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
