package com.example.kidtracker.main.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Data.SleepingData;
import com.example.kidtracker.main.Workers.TimeCalculator;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SleepingActivity extends AppCompatActivity {
    Button saveSleep, backSleep, cancelSleep, btnChron;
    TextInputEditText ietFell, ietWoke, ietNote, ietSlept;
    ProgressBar pbSleeping;

    String id, kid, note, fellAsleep, wokeUp, timeFell, timeWoke, strDateTime;
    int yearWoke, monthWoke, dayWoke;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    Date date;
    LocalDate localDateWake, localDateFell;
    LocalTime localTimeWake, localTimeFell;
    LocalDateTime localDateTimeWake, localDateTimeFell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);
        setContentView(R.layout.activity_sleeping);
        initViews();

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        date = new Date();
        strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date);
        ietFell.setText(strDateTime);



        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
                if (kid!=null) {
                    Query querySleep = dbr.child(id).child("kids").child(kid).child("sleep").limitToLast(1);

                    querySleep.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot sleepSnapshot : snapshot.getChildren()) {
                                SleepingData sleepingData = sleepSnapshot.getValue(SleepingData.class);
                                if (sleepingData != null) {
                                    fellAsleep = sleepingData.getFellAsleep();
                                    wokeUp = sleepingData.getWokeUp();

                                    if (fellAsleep != null && (wokeUp == null || wokeUp.equals("none"))) {
                                        ietFell.setText(fellAsleep);
                                        ietWoke.setText(strDateTime);
                                        if (!ietWoke.getText().toString().isEmpty()) {
                                            try {
                                                TimeCalculator calculator = new TimeCalculator(ietFell.getText().toString(), ietWoke.getText().toString());
                                                String difference = calculator.findDifference(ietFell.getText().toString(), ietWoke.getText().toString());
                                                ietSlept.setText(difference);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else if (ietFell.getText().toString().equals(fellAsleep)) {
                                        ietWoke.setText(strDateTime);
                                    } else {
                                        ietFell.setText(strDateTime);
                                        ietWoke.setText("");
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                /* ValueEventListener fellAsleepListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        fellAsleep = snapshot.getValue(String.class);

                        if (fellAsleep != null && ietWoke.getText().toString().isEmpty()) {
                            ietFell.setText(fellAsleep);
                            ietWoke.setText("");
                            ValueEventListener wokeUpListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    wokeUp = snapshot.getValue(String.class);
                                    if (wokeUp == null || wokeUp.equals("none")) {
                                        ietWoke.setText(strDateTime);
                                        if(!ietWoke.getText().toString().isEmpty()){
                                            try {
                                                TimeCalculator calculator = new TimeCalculator(ietFell.getText().toString(), ietWoke.getText().toString());
                                                String difference = calculator.findDifference(ietFell.getText().toString(), ietWoke.getText().toString());
                                                ietSlept.setText(difference);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        ietFell.setText(strDateTime);
                                        ietWoke.setText("");
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }; dbr.child(id).child("kids").child(kid).child("last_sleep").child("last_time_wake").addValueEventListener(wokeUpListener);
                        } else if (ietFell.getText().toString().equals(fellAsleep)) {
                            ietWoke.setText(strDateTime);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }; dbr.child(id).child("kids").child(kid).child("last_sleep").child("last_time_fell").addValueEventListener(fellAsleepListener); */
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);

        ietFell.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            new SingleDateAndTimePickerDialog.Builder(SleepingActivity.this)
                    .bottomSheet()
                    .bottomSheetHeight(500)
                    .curved()
                    .backgroundColor(getResources().getColor(R.color.whitish_purple))
                    .mainColor(getResources().getColor(R.color.deep_purple))
                    .backgroundColor(getResources().getColor(R.color.whitish_purple))
                    .titleTextColor(getResources().getColor(R.color.whitish_purple))
                    .title("Time and date")
                    .curved()
                    .minutesStep(1)
                    .listener(date1 -> {
                        String newDate = date1.toString();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz uuuu").withLocale(Locale.US);
                            ZonedDateTime zdt = ZonedDateTime.parse(newDate, f);

                            localDateFell = zdt.toLocalDate();
                            localTimeFell = zdt.toLocalTime();
                            localDateTimeFell = zdt.toLocalDateTime();

                            ietFell.setText(localDateFell.toString() + ", " + localTimeFell.toString());


                            if (ietWoke.getText().toString().isEmpty()) {
                                try {
                                    TimeCalculator calculator = new TimeCalculator(ietFell.getText().toString(), strDateTime);
                                    String difference = calculator.findDifference(ietFell.getText().toString(), strDateTime);
                                    ietSlept.setText(difference);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else if (!ietFell.getText().toString().isEmpty() && !ietWoke.getText().toString().isEmpty()){
                                try {
                                    TimeCalculator calculator = new TimeCalculator(ietFell.getText().toString(), ietWoke.getText().toString());
                                    String difference = null;
                                    difference = calculator.findDifference(ietFell.getText().toString(), ietWoke.getText().toString());
                                    ietSlept.setText(difference);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else if (ietWoke.getText().toString().equals(ietFell.getText().toString())){
                                ietSlept.setText("a few seconds");
                            }
                        }
                    })
                    .display();
        });

        ietWoke.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            Calendar currentDate = Calendar.getInstance();
            yearWoke = currentDate.get(Calendar.YEAR);
            monthWoke = currentDate.get(Calendar.MONTH);
            dayWoke = currentDate.get(Calendar.DAY_OF_MONTH);

            new SingleDateAndTimePickerDialog.Builder(SleepingActivity.this)
                    .bottomSheet()
                    .bottomSheetHeight(500)
                    .curved()
                    .backgroundColor(getResources().getColor(R.color.whitish_purple))
                    .mainColor(getResources().getColor(R.color.deep_purple))
                    .backgroundColor(getResources().getColor(R.color.whitish_purple))
                    .titleTextColor(getResources().getColor(R.color.whitish_purple))
                    .title("Time and date")
                    .curved()
                    .minutesStep(1)
                    .listener(date12 -> {
                        String newDate = date12.toString();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz uuuu").withLocale(Locale.US);
                            ZonedDateTime zdt = ZonedDateTime.parse(newDate, f);

                            localDateWake = zdt.toLocalDate();
                            localTimeWake = zdt.toLocalTime();
                            localDateTimeWake = zdt.toLocalDateTime();

                            ietWoke.setText(localDateWake.toString() + ", " + localTimeWake.toString());

                            if (!ietWoke.getText().toString().isEmpty()) {
                                if (ietWoke.getText().toString().equals(ietFell.getText().toString())){
                                    ietSlept.setText("a few seconds");
                                } else {
                                    TimeCalculator tc = new TimeCalculator(ietFell.getText().toString(), ietWoke.getText().toString());
                                    try {
                                        String result = tc.isDateBefore(ietFell.getText().toString(), ietWoke.getText().toString());
                                        if (result.equals("yes")) {
                                            try {
                                                TimeCalculator calculator = new TimeCalculator(ietFell.getText().toString(), ietWoke.getText().toString());
                                                String difference = calculator.findDifference(ietFell.getText().toString(), ietWoke.getText().toString());
                                                ietSlept.setText(difference);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            ietWoke.setText(strDateTime);
                                            ietWoke.requestFocus();
                                            ietWoke.setError("Put a real time/date");
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
                    })
                    .display();
        });

        saveSleep.setOnClickListener(v -> {
            try {
                setSaveSleep();
                Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeIntent);
                finish();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        btnChron.setOnClickListener(v -> {
            Intent sleepChronIntent = new Intent(getApplicationContext(), SleepingChronActivity.class);
            startActivity(sleepChronIntent);
            finish();
        });

        backSleep.setOnClickListener(v -> {
            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(homeIntent);
            finish();
        });

        cancelSleep.setOnClickListener(v -> {
            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(homeIntent);
            finish();
        });
    }

    public void initViews(){
        saveSleep = findViewById(R.id.btn_save_sleeping);
        backSleep = findViewById(R.id.btn_back_sleep);
        cancelSleep = findViewById(R.id.btn_cancel_sleeping);
        btnChron = findViewById(R.id.btn_chron_sleeping);
        ietFell = findViewById(R.id.txt_iet_fell_asleep_at);
        ietWoke = findViewById(R.id.txt_iet_woke_up_at);
        ietNote = findViewById(R.id.iet_note_sleeping);
        ietSlept = findViewById(R.id.iet_slept);
        pbSleeping = findViewById(R.id.pb_sleeping);
    }

    public void setSaveSleep() throws ParseException {
        timeFell = ietFell.getText().toString();
        timeWoke = ietWoke.getText().toString();

        if (timeWoke.isEmpty()){
            timeWoke = "none";
        } else {
            TimeCalculator calculator = new TimeCalculator(timeFell, timeWoke);
            String difference = calculator.findDifference(timeFell, timeWoke);
            ietSlept.setText(difference);
        }

        if (ietNote.getText().toString().isEmpty()){
            note = "none";
        } else {
            note = ietNote.getText().toString();
        }

        SleepingData sleepingData = new SleepingData(timeFell, timeWoke, note);
        dbr.child(id).child("kids").child(kid).child("sleep").child(timeFell).setValue(sleepingData);
    }
}