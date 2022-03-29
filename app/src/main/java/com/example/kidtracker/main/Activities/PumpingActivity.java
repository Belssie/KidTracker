package com.example.kidtracker.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Data.PumpingBreastFeedingData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PumpingActivity extends AppCompatActivity {
    Button left, right, leftPause, rightPause, stop, backPumping, savePumping, reset, simultaneous, cancel, btnChronPumping;
    TextInputEditText etPumpingNote, etPumpingAmount, startedTime, startedWith;
    Chronometer chronoL, chronoR;
    ProgressBar pbPumping;

    String id, kid, note, startedBreast, startedAt, amount, leftDur, rightDur, endAt;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    private long pauseOffSetL, pauseOffSetR;
    private boolean runningLeft, runningRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);
        setContentView(R.layout.activity_pumping);
        initViews();

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
        };
        dbr.child(id).child("last").addValueEventListener(kidNameListener);

        simultaneous.setOnClickListener(v -> {
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

        left.setOnClickListener(v -> {
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
            }
        });

        leftPause.setOnClickListener(v -> {
            if (runningLeft) {
                chronoL.stop();
                chronoR.stop();
                pauseOffSetL = SystemClock.elapsedRealtime() - chronoL.getBase();
                runningLeft = false;
                runningRight = false;
                left.setVisibility(View.VISIBLE);
                leftPause.setVisibility(View.GONE);
            }
        });

        right.setOnClickListener(v -> {
            if (runningLeft || pauseOffSetL != 0 && pauseOffSetR != 0){
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
            }
        });

        rightPause.setOnClickListener(v -> {
            if (runningRight) {
                chronoL.stop();
                chronoR.stop();
                pauseOffSetR = SystemClock.elapsedRealtime() - chronoR.getBase();
                runningRight = false;
                runningLeft = false;
                right.setVisibility(View.VISIBLE);
                rightPause.setVisibility(View.GONE);
            }
        });

        stop.setOnClickListener(v -> {
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
            String strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date);
            endAt = strDateTime;
        });

        reset.setOnClickListener(v -> {
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
            savePumping.setEnabled(false);
        });

        cancel.setOnClickListener(v -> {
            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(homeIntent);
            finish();
        });

        backPumping.setOnClickListener(v -> {
            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(homeIntent);
            finish();
        });

        savePumping.setOnClickListener(v -> {
            if (!startedTime.getText().toString().isEmpty() && !etPumpingAmount.getText().toString().isEmpty()){
                try {
                    setSavePumping();
                    finish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (startedTime.getText().toString().isEmpty()){
                startedTime.requestFocus();
                startedTime.setError("No information given");
            } else if (etPumpingAmount.getText().toString().isEmpty()){
                etPumpingAmount.requestFocus();
                etPumpingAmount.setError("Add amount");
            }
        });

        btnChronPumping.setOnClickListener(v -> {
            Intent pumpingChronIntent = new Intent(getApplicationContext(), PumpingChronActivity.class);
            pumpingChronIntent.putExtra("NAME", kid);
            startActivity(pumpingChronIntent);
            finish();
        });
    }

    public void initViews(){
        simultaneous = findViewById(R.id.btn_simultaneous_pumping);
        left = findViewById(R.id.btn_left_pumping);
        leftPause = findViewById(R.id.btn_left_pumping_pause);
        right = findViewById(R.id.btn_right_pumping);
        rightPause = findViewById(R.id.btn_right_pumping_pause);
        stop = findViewById(R.id.btn_stop_pumping);
        reset = findViewById(R.id.btn_reset_pumping);
        chronoL = findViewById(R.id.chrono_left_pumping);
        chronoR = findViewById(R.id.chrono_right_pumping);
        startedTime = findViewById(R.id.iet_started_at_pumping);
        startedWith = findViewById(R.id.iet_started_with_pumping);
        etPumpingAmount = findViewById(R.id.iet_amount_pumping);
        etPumpingNote = findViewById(R.id.iet_note_pumping);
        backPumping = findViewById(R.id.btn_back_pumping);
        savePumping = findViewById(R.id.btn_save_pumping);
        cancel = findViewById(R.id.btn_cancel_pumping);
        pbPumping = findViewById(R.id.pb_pumping);
        btnChronPumping = findViewById(R.id.btn_chron_pumping);

        left.setVisibility(View.VISIBLE);
        right.setVisibility(View.VISIBLE);
        stop.setVisibility(View.VISIBLE);
    }

    public void setSavePumping() throws ParseException {
        leftDur = chronoL.getText().toString();
        rightDur = chronoR.getText().toString();
        startedBreast = startedWith.getText().toString();
        startedAt = startedTime.getText().toString();
        amount = etPumpingAmount.getText().toString();

        if(etPumpingNote.getText().toString().isEmpty()){
            note = "none";
        } else {
            note = etPumpingNote.getText().toString();
        }

        PumpingBreastFeedingData pumpingBreastFeedingData = new PumpingBreastFeedingData(startedAt, startedBreast, amount, note, endAt, leftDur, rightDur);
        dbr.child(id).child("kids").child(kid).child("pumping").child(startedAt).setValue(pumpingBreastFeedingData);
        dbr.child(id).child("kids").child(kid).child("last_pumping").child("last_breast_started_pumping").setValue(startedBreast);
        dbr.child(id).child("kids").child(kid).child("last_pumping").child("last_amount_pumping").setValue(amount);
        dbr.child(id).child("kids").child(kid).child("last_pumping").child("last_time_started_pumping").setValue(startedAt);
        dbr.child(id).child("kids").child(kid).child("last_pumping").child("last_time_ended_pumping").setValue(endAt);
    }
}