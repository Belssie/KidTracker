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
import com.example.kidtracker.main.Data.FoodData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodAdapter extends FirebaseRecyclerAdapter<FoodData, FoodAdapter.foodViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public FoodAdapter(@NonNull FirebaseRecyclerOptions<FoodData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodAdapter.foodViewHolder holder, int position, @NonNull FoodData model) {
        holder.ietDateFood.setText(model.getDateTimeFood());
        holder.ietTypeFood.setText(model.getTypeFood());
        holder.ietAmountFood.setText(model.getAmountFood());
    }

    @NonNull
    @Override
    public FoodAdapter.foodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_info, parent, false);
        return new foodViewHolder(view);
    }

    class foodViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietDateFood, ietTypeFood, ietAmountFood;
        TextView tvDelete;
        ConstraintLayout clFood;

        String id = fbAuth.getUid();
        public foodViewHolder(@NonNull View itemView) {
            super(itemView);

            ietDateFood = itemView.findViewById(R.id.iet_date_food_chron);
            ietTypeFood = itemView.findViewById(R.id.iet_type_food_chron);
            ietAmountFood = itemView.findViewById(R.id.iet_amount_food_chron);
            tvDelete = itemView.findViewById(R.id.tv_delete_food);
            clFood = itemView.findViewById(R.id.cl_food);

            ietDateFood.setFocusable(false);
            ietTypeFood.setFocusable(false);
            ietAmountFood.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        String date = ietDateFood.getText().toString();
                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("feeding").child("food").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "Food was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
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
