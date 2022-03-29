package com.example.kidtracker.main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Activities.HomeActivity;
import com.example.kidtracker.main.Data.FeedingData;
import com.example.kidtracker.main.Data.PumpingBreastFeedingData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BreastMilkFragment extends Fragment {
    Button left, right, leftPause, rightPause, stop, saveBreast, reset, simultaneous, cancelBm;
    ProgressBar pbBm;
    TextInputEditText etBreastNote, etBreastAmount, startedTime, startedWith;
    Chronometer chronoL, chronoR;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    String id, kid, note, startedBreast, startedAt, amount, leftDur, rightDur, endAt;

    private long pauseOffSetL, pauseOffSetR;
    private boolean runningLeft, runningRight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_breast_milk, container, false);
        simultaneous = v.findViewById(R.id.btn_simultaneous_bm);
        left = v.findViewById(R.id.btn_left_bm);
        leftPause = v.findViewById(R.id.btn_left_bm_pause);
        right = v.findViewById(R.id.btn_right_bm);
        rightPause = v.findViewById(R.id.btn_right_bm_pause);
        stop = v.findViewById(R.id.btn_stop_bm);
        reset = v.findViewById(R.id.btn_reset_bm);
        chronoL = v.findViewById(R.id.chrono_left_bm);
        chronoR = v.findViewById(R.id.chrono_right_bm);
        startedTime = v.findViewById(R.id.iet_started_at_bm);
        startedWith = v.findViewById(R.id.iet_started_with_bm);
        etBreastAmount = v.findViewById(R.id.iet_amount_bm);
        etBreastNote = v.findViewById(R.id.iet_note_bm);
        saveBreast = v.findViewById(R.id.btn_save_bm);
        cancelBm = v.findViewById(R.id.btn_cancel_bm);
        pbBm = v.findViewById(R.id.pb_bm);

        left.setVisibility(View.VISIBLE);
        right.setVisibility(View.VISIBLE);
        stop.setVisibility(View.VISIBLE);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);

        simultaneous.setOnClickListener(v19 -> {
            Date date = new Date();
            String strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date);
            startedTime.setText(strDateTime);
            startedWith.setText("simultaneous");
            chronoL.setBase(SystemClock.elapsedRealtime() - pauseOffSetL);
            chronoR.setBase(SystemClock.elapsedRealtime() - pauseOffSetR);
            chronoL.start();
            chronoR.start();
            pauseOffSetL = SystemClock.elapsedRealtime() - chronoL.getBase();
            pauseOffSetR = SystemClock.elapsedRealtime() - chronoR.getBase();
            runningLeft = true;
            runningRight = true;
            reset.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            left.setVisibility(View.GONE);
            leftPause.setVisibility(View.GONE);
            rightPause.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        });

        left.setOnClickListener(v18 -> {
            if (runningRight || (pauseOffSetL!=0 && pauseOffSetR!=0)){
                chronoL.setBase(SystemClock.elapsedRealtime() - pauseOffSetL);
                chronoL.start();
                chronoR.stop();
                pauseOffSetL = SystemClock.elapsedRealtime() - chronoL.getBase();
                runningLeft = true;
                reset.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                left.setVisibility(View.GONE);
                leftPause.setVisibility(View.VISIBLE);
                rightPause.setVisibility(View.GONE);
                right.setVisibility(View.VISIBLE);
                saveBreast.setEnabled(true);
            } else if (!runningLeft && pauseOffSetR==0) {
                Date date = new Date();
                String strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date);
                startedTime.setText(strDateTime);
                startedWith.setText("left");
                chronoL.setBase(SystemClock.elapsedRealtime() - pauseOffSetL);
                chronoL.start();
                chronoR.stop();
                pauseOffSetL = SystemClock.elapsedRealtime() - chronoL.getBase();
                pauseOffSetR = SystemClock.elapsedRealtime() - chronoR.getBase();
                runningLeft = true;
                reset.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                left.setVisibility(View.GONE);
                leftPause.setVisibility(View.VISIBLE);
                rightPause.setVisibility(View.GONE);
                right.setVisibility(View.VISIBLE);
                saveBreast.setEnabled(true);
            }
        });

        leftPause.setOnClickListener(v17 -> {
            if (runningLeft) {
                chronoL.stop();
                pauseOffSetL = SystemClock.elapsedRealtime() - chronoL.getBase();
                runningLeft = false;
                runningRight = false;
                left.setVisibility(View.VISIBLE);
                leftPause.setVisibility(View.GONE);
            }
        });

        right.setOnClickListener(v16 -> {
            if (runningLeft || (pauseOffSetL!=0 && pauseOffSetR!=0)){
                chronoR.setBase(SystemClock.elapsedRealtime() - pauseOffSetR);
                chronoR.start();
                chronoL.stop();
                pauseOffSetR = SystemClock.elapsedRealtime() - chronoR.getBase();
                runningRight = true;
                reset.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                right.setVisibility(View.GONE);
                rightPause.setVisibility(View.VISIBLE);
                leftPause.setVisibility(View.GONE);
                left.setVisibility(View.VISIBLE);
                saveBreast.setEnabled(true);
            } else if (!runningRight && pauseOffSetL == 0) {
                Date date = new Date();
                String strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date);
                startedTime.setText(strDateTime);
                startedWith.setText("right");
                chronoR.setBase(SystemClock.elapsedRealtime() - pauseOffSetR);
                chronoR.start();
                chronoL.stop();
                pauseOffSetL = SystemClock.elapsedRealtime() - chronoL.getBase();
                pauseOffSetR = SystemClock.elapsedRealtime() - chronoR.getBase();
                runningRight = true;
                reset.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                right.setVisibility(View.GONE);
                rightPause.setVisibility(View.VISIBLE);
                leftPause.setVisibility(View.GONE);
                left.setVisibility(View.VISIBLE);
                saveBreast.setEnabled(true);
            }
        });

        rightPause.setOnClickListener(v15 -> {
            if (runningRight) {
                chronoR.stop();
                pauseOffSetR = SystemClock.elapsedRealtime() - chronoR.getBase();
                runningRight = false;
                runningLeft = false;
                right.setVisibility(View.VISIBLE);
                rightPause.setVisibility(View.GONE);
            }
        });

        stop.setOnClickListener(v14 -> {
            runningLeft = false;
            runningRight = false;
            chronoL.stop();
            chronoR.stop();
            pauseOffSetL = SystemClock.elapsedRealtime() - chronoL.getBase();
            pauseOffSetR = SystemClock.elapsedRealtime() - chronoR.getBase();
            reset.setVisibility(View.VISIBLE);
            stop.setVisibility(View.GONE);
            left.setVisibility(View.GONE);
            leftPause.setVisibility(View.GONE);
            rightPause.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
            Date date = new Date();
            String strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss").format(date);
            endAt = strDateTime;
            saveBreast.setEnabled(true);
        });

        reset.setOnClickListener(v13 -> {
            runningLeft = false;
            runningRight = false;
            chronoL.setBase(SystemClock.elapsedRealtime());
            chronoR.setBase(SystemClock.elapsedRealtime());
            pauseOffSetL = 0;
            pauseOffSetR = 0;
            startedTime.setText("");
            startedWith.setText("");
            reset.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            left.setVisibility(View.VISIBLE);
            leftPause.setVisibility(View.GONE);
            rightPause.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
            saveBreast.setEnabled(false);
        });

        saveBreast.setOnClickListener(v12 -> {
            if(!startedTime.getText().toString().isEmpty() && !etBreastAmount.getText().toString().isEmpty()) {
                setSaveBreast();
                Intent intentHome = new Intent(getContext(), HomeActivity.class);
                startActivity(intentHome);
            } else if (startedTime.getText().toString().isEmpty()){
                startedTime.requestFocus();
                startedTime.setError("No information given");
            } else if (etBreastAmount.getText().toString().isEmpty()){
                etBreastAmount.requestFocus();
                etBreastAmount.setError("Add amount");
            }
        });

        cancelBm.setOnClickListener(v1 -> {
            Intent intentHome = new Intent(getContext(), HomeActivity.class);
            startActivity(intentHome);
        });

        onStop();
        onDestroyView();
        return v;
    }

    public void setSaveBreast(){
        leftDur = chronoL.getText().toString();
        rightDur = chronoR.getText().toString();
        startedBreast = startedWith.getText().toString();
        startedAt = startedTime.getText().toString();
        amount = etBreastAmount.getText().toString();

        if(etBreastNote.getText().toString().isEmpty()){
            note = "none";
        } else {
            note = etBreastNote.getText().toString();
        }

        PumpingBreastFeedingData pumpingBreastFeedingData = new PumpingBreastFeedingData(startedAt, startedBreast, amount, note, endAt, leftDur, rightDur);
        FeedingData feedingData = new FeedingData(startedAt, "breast milk", startedBreast);
        dbr.child(id).child("kids").child(kid).child("feeding").child("breast_feeding").child(startedAt).setValue(pumpingBreastFeedingData);
        dbr.child(id).child("kids").child(kid).child("feeding_all").child(startedAt).setValue(feedingData);

    }

    @Override
    public void onStop() {
        super.onStop();
        startedWith.setText("");
        startedTime.setText("");
        etBreastNote.setText("");
        etBreastAmount.setText("");
        runningLeft = false;
        runningRight = false;
        pauseOffSetL = 0;
        pauseOffSetR = 0;
        left.setVisibility(View.VISIBLE);
        right.setVisibility(View.VISIBLE);
        stop.setVisibility(View.VISIBLE);
    }
}