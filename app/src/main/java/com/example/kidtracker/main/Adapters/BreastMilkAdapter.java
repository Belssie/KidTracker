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

public class BreastMilkAdapter extends FirebaseRecyclerAdapter<PumpingBreastFeedingData, BreastMilkAdapter.breastMilkViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public BreastMilkAdapter(@NonNull FirebaseRecyclerOptions<PumpingBreastFeedingData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BreastMilkAdapter.breastMilkViewHolder holder, int position, @NonNull PumpingBreastFeedingData model) {
        holder.ietDateBreastMilk.setText(model.getDateTimeStarted());
        holder.ietStartedBreastMilk.setText(model.getBreastStarted());
        holder.ietAmountBreastMilk.setText(model.getAmountPBF());
        holder.ietLbreastMilk.setText(model.getLeftDur());
        holder.ietRbreastMilk.setText(model.getRightDur());
    }

    @NonNull
    @Override
    public BreastMilkAdapter.breastMilkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.breast_milk_info, parent, false);
        return new breastMilkViewHolder(view);
    }

    class breastMilkViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietDateBreastMilk, ietStartedBreastMilk, ietAmountBreastMilk, ietLbreastMilk, ietRbreastMilk;
        TextView tvDelete;
        ConstraintLayout clBreastMilk;



        String id = fbAuth.getUid();
        public breastMilkViewHolder(@NonNull View itemView) {
            super(itemView);

            ietDateBreastMilk = itemView.findViewById(R.id.iet_date_breast_milk_chron);
            ietStartedBreastMilk = itemView.findViewById(R.id.iet_started_breast_milk_chron);
            ietAmountBreastMilk = itemView.findViewById(R.id.iet_amount_breast_milk_chron);
            ietLbreastMilk = itemView.findViewById(R.id.iet_l_breast_milk_chron);
            ietRbreastMilk = itemView.findViewById(R.id.iet_r_breast_milk_chron);
            tvDelete = itemView.findViewById(R.id.tv_delete_breast_milk);
            clBreastMilk = itemView.findViewById(R.id.cl_breast_milk);

            ietDateBreastMilk.setFocusable(false);
            ietStartedBreastMilk.setFocusable(false);
            ietAmountBreastMilk.setFocusable(false);
            ietLbreastMilk.setFocusable(false);
            ietRbreastMilk.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        Log.d("KID", kid);
                        String date = ietDateBreastMilk.getText().toString();
                        Log.d("DATE", date);

                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("feeding").child("breast_feeding").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "BreastMilk was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
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
