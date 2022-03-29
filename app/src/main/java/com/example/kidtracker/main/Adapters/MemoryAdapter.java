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
import com.example.kidtracker.main.Data.MemoryData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemoryAdapter extends FirebaseRecyclerAdapter<MemoryData, MemoryAdapter.memoryViewHolder> {
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    public MemoryAdapter(@NonNull FirebaseRecyclerOptions<MemoryData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MemoryAdapter.memoryViewHolder holder, int position, @NonNull MemoryData model) {
        holder.ietDateMemory.setText(model.getDateTimeMemory());
        holder.ietTitleMemory.setText(model.getTitleMemory());
    }

    @NonNull
    @Override
    public MemoryAdapter.memoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memory_info, parent, false);
        return new memoryViewHolder(view);
    }

    class memoryViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText ietDateMemory, ietTitleMemory;
        TextView tvDelete;
        ConstraintLayout clMemory;

        String id = fbAuth.getUid();
        public memoryViewHolder(@NonNull View itemView) {
            super(itemView);

            ietDateMemory = itemView.findViewById(R.id.iet_date_memory_chron);
            ietTitleMemory = itemView.findViewById(R.id.iet_title_memory_chron);

            tvDelete = itemView.findViewById(R.id.tv_delete_memory);
            clMemory = itemView.findViewById(R.id.cl_memory);

            ietDateMemory.setFocusable(false);
            ietTitleMemory.setFocusable(false);

            Context contextH = itemView.getContext();

            tvDelete.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String kid = snapshot.getValue(String.class);
                        String date = ietDateMemory.getText().toString();
                        if (kid != null) {
                            dbr.child(id).child("kids").child(kid).child("important").child("memory").child(date).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "Memory was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
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
