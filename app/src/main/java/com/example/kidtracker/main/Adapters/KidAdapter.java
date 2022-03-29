package com.example.kidtracker.main.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.kidtracker.main.Activities.HomeActivity;
import com.example.kidtracker.main.Data.KidsData;
import com.example.kidtracker.main.Activities.ProfileActivity;
import com.example.kidtracker.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class KidAdapter extends FirebaseRecyclerAdapter<KidsData, KidAdapter.kidViewHolder> {
    private final Context context;
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");

    public KidAdapter(@NonNull FirebaseRecyclerOptions<KidsData> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull KidAdapter.kidViewHolder holder, int position, @NonNull KidsData model) {
        Glide.with(context).load(model.getPicture()).transform(new CircleCrop()).placeholder(R.drawable.circle_image_button1).into(holder.ivKid);
        holder.name.setText(model.getName());
        holder.birthDate.setText(model.getBirthDate());

        holder.itemView.setOnClickListener(v -> {
            Intent homeIntent = new Intent(v.getContext(), HomeActivity.class);
            context.startActivity(homeIntent);
        });
    }

    @NonNull
    @Override
    public KidAdapter.kidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kid, parent, false);
        return new kidViewHolder(view);
    }

    class kidViewHolder extends RecyclerView.ViewHolder{
        ImageView ivKid;
        TextView name, birthDate, deleteKid, editKid;
        SwipeRevealLayout swipeRevealLayout;
        ConstraintLayout clKid;

        String id = fbAuth.getUid();

        public kidViewHolder(@NonNull View itemView) {
            super(itemView);

            ivKid = itemView.findViewById(R.id.iv_kid);
            name = itemView.findViewById(R.id.tv_name);
            birthDate = itemView.findViewById(R.id.tv_birthdate);
            editKid = itemView.findViewById(R.id.txt_edit_alarms);
            deleteKid = itemView.findViewById(R.id.tv_delete_alarms);
            swipeRevealLayout = itemView.findViewById(R.id.sw_layout);
            clKid = itemView.findViewById(R.id.cl_alarm);

            ivKid.setFocusable(false);

            Context contextH = itemView.getContext();

            clKid.setOnClickListener(v -> {
                dbr.child(id).child("last").setValue(name.getText().toString());
                Intent homeIntent = new Intent(contextH, HomeActivity.class);
                contextH.startActivity(homeIntent);
            });

            editKid.setOnClickListener(v -> {
                dbr.child(id).child("last").setValue(name.getText().toString());
                Intent profileIntent = new Intent(contextH, ProfileActivity.class);
                contextH.startActivity(profileIntent);
            });

            deleteKid.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        KidsData kidsData = snapshot.getValue(KidsData.class);
                        if (kidsData != null) {
                            String picUrl = kidsData.getPicture();
                            if (picUrl != null) {
                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(picUrl);
                                storageReference.delete().addOnSuccessListener(aVoid -> Log.e("Picture", "#deleted"));
                            }
                        } dbr.child(id).child("kids").child(name.getText().toString()).removeValue().addOnSuccessListener(unused -> Toast.makeText(contextH, "Kid was deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(contextH, "error", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }; dbr.child(id).child("kids").child(name.getText().toString()).addValueEventListener(kidListener);
                dbr.child(id).child("last").setValue(null);
            });
        }
    }
}
