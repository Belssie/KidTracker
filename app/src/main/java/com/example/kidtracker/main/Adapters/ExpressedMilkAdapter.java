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
import com.example.kidtracker.main.Data.ExpressedMilkData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExpressedMilkAdapter extends FirebaseRecyclerAdapter<ExpressedMilkData, ExpressedMilkAdapter.expressedMilkViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public ExpressedMilkAdapter(@NonNull FirebaseRecyclerOptions<ExpressedMilkData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExpressedMilkAdapter.expressedMilkViewHolder holder, int position, @NonNull ExpressedMilkData model) {
        holder.ietDateExpressedMilk.setText(model.getDateTimeExpressed());
        holder.ietAmountExpressedMilk.setText(model.getAmountExpressed());
    }

    @NonNull
    @Override
    public ExpressedMilkAdapter.expressedMilkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expressed_milk_info, parent, false);
        return new expressedMilkViewHolder(view);
    }

    class expressedMilkViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietDateExpressedMilk, ietAmountExpressedMilk;
        TextView tvDelete;
        ConstraintLayout clExpressedMilk;

        String id = fbAuth.getUid();
        public expressedMilkViewHolder(@NonNull View itemView) {
            super(itemView);

            ietDateExpressedMilk = itemView.findViewById(R.id.iet_date_expressed_milk_chron);
            ietAmountExpressedMilk = itemView.findViewById(R.id.iet_amount_expressed_milk_chron);
            tvDelete = itemView.findViewById(R.id.tv_delete_expressed_milk);
            clExpressedMilk = itemView.findViewById(R.id.cl_expressed_milk);

            ietDateExpressedMilk.setFocusable(false);
            ietAmountExpressedMilk.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        String date = ietDateExpressedMilk.getText().toString();
                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("feeding").child("expressed_milk").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "ExpressedMilk was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
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
