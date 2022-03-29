package com.example.kidtracker.main.PopUps;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kidtracker.main.Data.ReminderData;
import com.example.kidtracker.main.Workers.AlertReceiver;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Workers.TimeCalculator;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
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

public class RemindersPopUp extends AppCompatActivity {
    TextInputEditText etTitle, etTime;
    Button saveAlarm, deleteAlarm, cancel;
    ProgressBar pbAlarm;

    String id, kid, title, time, reminder, toBeDeleted, strDateTime;
    int hour, minute, year, month, day, broadCastId, bcId;

    Calendar c, currentDate;
    Date date;
    LocalDate localDate;
    LocalTime localTime;
    LocalDateTime localDateTime;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    Intent intent;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);
        setContentView(R.layout.reminders_popup);
        loadPopUp();
        initViews();

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();
        dbr = FirebaseDatabase.getInstance().getReference("accounts");

        getFromFirebase();

        date = new Date();
        strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date);
        etTime.setText(strDateTime);

        currentDate = Calendar.getInstance();
        c = Calendar.getInstance();
        hour = currentDate.get(Calendar.HOUR_OF_DAY);
        minute = currentDate.get(Calendar.MINUTE);
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        month = currentDate.get(Calendar.MONTH);
        year = currentDate.get(Calendar.YEAR);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);

        intent = new Intent(getApplicationContext(), AlertReceiver.class);
        broadCastId = (int) System.currentTimeMillis();
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), broadCastId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        etTime.setOnClickListener(v -> {
            Calendar currentDate = Calendar.getInstance();
            year = currentDate.get(Calendar.YEAR);
            month = currentDate.get(Calendar.MONTH);
            day = currentDate.get(Calendar.DAY_OF_MONTH);

            new SingleDateAndTimePickerDialog.Builder(RemindersPopUp.this)
                    .backgroundColor(getResources().getColor(R.color.whitish_purple))
                    .mainColor(getResources().getColor(R.color.deep_purple))
                    .backgroundColor(getResources().getColor(R.color.whitish_purple))
                    .titleTextColor(getResources().getColor(R.color.whitish_purple))
                    .title("Time and date")
                    .curved()
                    .minutesStep(1)
                    .listener(date -> {
                        String newDate = date.toString();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter f = DateTimeFormatter.ofPattern( "EEE MMM dd HH:mm:ss zzz uuuu" ).withLocale( Locale.US );
                            ZonedDateTime zdt = ZonedDateTime.parse(newDate, f);

                            localDate = zdt.toLocalDate();
                            localTime = zdt.toLocalTime();
                            localDateTime = zdt.toLocalDateTime();

                            c.set(Calendar.YEAR, zdt.getYear());
                            c.set(Calendar.MONTH, zdt.getMonthValue());
                            c.set(Calendar.DATE, zdt.getDayOfMonth());
                            c.set(Calendar.HOUR_OF_DAY, zdt.getHour());
                            c.set(Calendar.MINUTE, zdt.getMinute());

                            etTime.setText(localDate.toString() + ", " + localTime.toString());
                        }
                    })
                    .display();
        });

        saveAlarm.setOnClickListener(v -> {
            if(!etTitle.getText().toString().isEmpty()) {
                TimeCalculator timeCalculator = new TimeCalculator(strDateTime, etTime.getText().toString());
                String isTrue = null;
                try {
                    isTrue = timeCalculator.isDateBefore(strDateTime, etTime.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (isTrue.equals("yes") || etTime.getText().toString().equals(strDateTime)){
                    try {
                        setSaveAlarm();
                        finish();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    etTime.requestFocus();
                    etTime.setError("Set proper time in the future");
                }

            } else if (etTitle.getText().toString().isEmpty()){
                etTitle.requestFocus();
                etTitle.setError("Add title");
            }
        });

        cancel.setOnClickListener(v -> finish());
    }

    public void loadPopUp(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setLayout((int) (width * .87), (int) (height * .50));
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void startAlarm() throws ParseException {
        String datetime = etTime.getText().toString();
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd, hh:mm");
        Date date = formatter.parse(datetime);
        if (date != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
        }
    }

    public void setSaveAlarm() throws ParseException {
        startAlarm();
        time = etTime.getText().toString();
        title = etTitle.getText().toString();
        ReminderData reminderData = new ReminderData(title, time, broadCastId);
        dbr.child(id).child("kids").child(kid).child("reminders").child(time).setValue(reminderData);
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void setDeleteAlarm(int idb){
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idb, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        toBeDeleted =  etTime.getText().toString();
        dbr.child(id).child("kids").child(kid).child("reminders").child(toBeDeleted).removeValue().addOnSuccessListener(unused -> {
            Toast.makeText(getApplicationContext(), "Alarm was deleted", Toast.LENGTH_SHORT).show();
            dbr.child(id).child("kids").child(kid).child("last_reminder_added").setValue("none");
            finish();
        });
    }

    public void getFromFirebase(){
        ValueEventListener kidListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
                ValueEventListener lastReminderListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reminder = snapshot.getValue(String.class);
                        if (reminder == null) {
                            etTime.setText(strDateTime);
                            etTitle.setText("");
                        } else if (reminder.equals("none")){
                            etTime.setText(strDateTime);
                            etTitle.setText("");
                        } else {
                            ValueEventListener reminderListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ReminderData reminderData = snapshot.getValue(ReminderData.class);
                                    if (reminderData != null) {
                                        bcId = reminderData.getBroadCastId();
                                        etTime.setText(reminderData.getTime());
                                        etTitle.setText(reminderData.getTitle());
                                        saveAlarm.setVisibility(View.GONE);
                                        deleteAlarm.setOnClickListener(v -> setDeleteAlarm(bcId));
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }; dbr.child(id).child("kids").child(kid).child("reminders").child(reminder).addValueEventListener(reminderListener);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }; dbr.child(id).child("kids").child(kid).child("last_reminder").addValueEventListener(lastReminderListener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidListener);
    }

    public void initViews(){
        etTitle = findViewById(R.id.iet_title_reminder);
        etTime = findViewById(R.id.iet_date_reminder);
        saveAlarm = findViewById(R.id.btn_save_alarm);
        deleteAlarm = findViewById(R.id.btn_delete);
        cancel = findViewById(R.id.btn_cancel_alarm);
        pbAlarm = findViewById(R.id.pb_alarms);
    }
}


