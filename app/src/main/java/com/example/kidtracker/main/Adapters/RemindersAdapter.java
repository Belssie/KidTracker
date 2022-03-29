package com.example.kidtracker.main.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kidtracker.main.Data.ReminderData;
import com.example.kidtracker.R;
import com.example.kidtracker.main.PopUps.RemindersPopUp;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RemindersAdapter extends FirebaseRecyclerAdapter<ReminderData, RemindersAdapter.alarmViewHolder> {
    private final Context context;
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("accounts");
    String id = fbAuth.getUid();

    public RemindersAdapter(@NonNull FirebaseRecyclerOptions<ReminderData> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RemindersAdapter.alarmViewHolder holder, int position, @NonNull ReminderData model) {
        holder.tvTitle.setText(model.getTitle());
        holder.tvTime.setText(model.getTime());

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm");

        try {
            Date itemViewDate = sdf.parse(holder.tvTime.getText().toString());
            if (itemViewDate.equals(date)){
                holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else if(itemViewDate.before(date)){
                holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RemindersAdapter.alarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm, parent, false);
        return new alarmViewHolder(view);
    }

    class alarmViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvTime, deleteAlarm;
        ConstraintLayout clAlarms;

        String kid;

        public alarmViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            deleteAlarm = itemView.findViewById(R.id.tv_delete_alarms);
            clAlarms = itemView.findViewById(R.id.cl_alarm);

            Context contextH = itemView.getContext();
            clAlarms.setOnClickListener(v -> {
                ValueEventListener kidListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        kid = snapshot.getValue(String.class);
                        dbr.child(id).child("kids").child(kid).child("last_reminder").setValue(tvTime.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };dbr.child(id).child("last").addValueEventListener(kidListener);

                Intent alarmIntent = new Intent(contextH, RemindersPopUp.class);
                contextH.startActivity(alarmIntent);
            });
        }
    }
}
