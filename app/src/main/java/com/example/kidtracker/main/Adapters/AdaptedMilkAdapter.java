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
import com.example.kidtracker.main.Data.AdaptedMilkData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdaptedMilkAdapter extends FirebaseRecyclerAdapter<AdaptedMilkData, AdaptedMilkAdapter.adaptedMilkViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public AdaptedMilkAdapter(@NonNull FirebaseRecyclerOptions<AdaptedMilkData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdaptedMilkAdapter.adaptedMilkViewHolder holder, int position, @NonNull AdaptedMilkData model) {
        holder.ietDateAdaptedMilk.setText(model.getDateTimeAm());
        holder.ietTypeAdaptedMilk.setText(model.getTypeAm());
        holder.ietAmountAdaptedMilk.setText(model.getAmountAm());
    }

    @NonNull
    @Override
    public AdaptedMilkAdapter.adaptedMilkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapted_milk_info, parent, false);
        return new adaptedMilkViewHolder(view);
    }

    class adaptedMilkViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietDateAdaptedMilk, ietTypeAdaptedMilk, ietAmountAdaptedMilk;
        TextView tvDelete;
        ConstraintLayout clAdaptedMilk;

        String id = fbAuth.getUid();
        public adaptedMilkViewHolder(@NonNull View itemView) {
            super(itemView);

            ietDateAdaptedMilk = itemView.findViewById(R.id.iet_date_am_chron);
            ietTypeAdaptedMilk = itemView.findViewById(R.id.iet_type_am_chron);
            ietAmountAdaptedMilk = itemView.findViewById(R.id.iet_amount_am_chron);
            tvDelete = itemView.findViewById(R.id.tv_delete_am);
            clAdaptedMilk = itemView.findViewById(R.id.cl_am);

            Context contextH = itemView.getContext();

            ietDateAdaptedMilk.setFocusable(false);
            ietTypeAdaptedMilk.setFocusable(false);
            ietAmountAdaptedMilk.setFocusable(false);

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        String date = ietDateAdaptedMilk.getText().toString();

                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("feeding").child("adapted_milk").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "AdaptedMilk was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
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
