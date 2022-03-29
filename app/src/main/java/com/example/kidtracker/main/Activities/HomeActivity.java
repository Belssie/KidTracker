package com.example.kidtracker.main.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.kidtracker.main.Data.DiaperData;
import com.example.kidtracker.main.Data.FeedingData;
import com.example.kidtracker.main.Data.ImportantData;
import com.example.kidtracker.main.Data.KidsData;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Data.PumpingBreastFeedingData;
import com.example.kidtracker.main.Data.ReminderData;
import com.example.kidtracker.main.Data.SleepingData;
import com.example.kidtracker.main.Workers.TimeCalculator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    Button btnFeeding, btnSleeping, btnDiaper, btnPumping, btnImportant, btnAlarms, btnSettings, btnAdd;
    ImageView profilePic;
    TextView tvHome, tvSleep, tvDiaper, tvFeeding, tvPumping, tvImportant, tvReminders, tvPic;
    ProgressBar pbHome;

    String id, name, age, kid, bd;

    FirebaseAuth fbAuth;
    DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        initViews();

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();
        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);

        getFromFirebase();

        if (tvHome.getText().toString().isEmpty()){
            removeVisibility();
        } else {
            restoreVisibility();
        }

        tvHome.setOnClickListener(v -> {
                    Intent changeProfile = new Intent(getApplicationContext(), ProfileChangeActivity.class);
                    startActivity(changeProfile);
                    finish();
        });

        profilePic.setOnClickListener(view -> {
            Intent iProfile = new Intent (getApplicationContext(), ProfileActivity.class);
            startActivity(iProfile);
        });

        btnFeeding.setOnClickListener(v -> {
            Intent intentFeeding = new Intent(getApplicationContext(), FeedingActivity.class);
            startActivity(intentFeeding);
        });

        btnSleeping.setOnClickListener(v -> {
            Intent intentSleeping = new Intent(getApplicationContext(), SleepingActivity.class);
            startActivity(intentSleeping);
        });

        btnDiaper.setOnClickListener(v -> {
            Intent intentDiaper = new Intent(getApplicationContext(), DiaperActivity.class);
            startActivity(intentDiaper);
        });

        btnPumping.setOnClickListener(v -> {
            Intent intentPumping = new Intent(getApplicationContext(), PumpingActivity.class);
            startActivity(intentPumping);
        });

        btnAlarms.setOnClickListener(v -> {
            Intent intentAlarms = new Intent(getApplicationContext(), RemindersActivity.class);
            startActivity(intentAlarms);
        });

        btnImportant.setOnClickListener(v -> {
            Intent intentImportant = new Intent(getApplicationContext(), ImportantActivity.class);
            startActivity(intentImportant);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intentSettings);
            finish();
        });

        btnAdd.setOnClickListener(v -> {
            Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intentProfile);
        });
    }

    public void getFromFirebase(){
        pbHome.setVisibility(View.VISIBLE);

        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
                if (kid==null){
                    Query queryKids = dbr.child(id).child("kids").limitToLast(1);
                    queryKids.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot kidsSnapshot : snapshot.getChildren()) {
                                KidsData kidsData = kidsSnapshot.getValue(KidsData.class);
                                if (kidsData != null) {
                                    try {
                                        String lastKidName = kidsData.getName();
                                        String bDate = kidsData.getBirthDate();
                                        dbr.child(id).child("last").setValue(lastKidName);

                                        Date date = new Date();
                                        String strDateTime = new SimpleDateFormat("yyyy-MM-dd").format(date);

                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date d1 = sdf.parse(bDate);
                                        Date d2 = sdf.parse(strDateTime);
                                        assert d1 != null;
                                        assert d2 != null;

                                        String days, years, months;
                                        age = "";

                                        long difference_In_Time = d2.getTime() - d1.getTime();

                                        long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

                                        long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

                                        long difference_In_Months = (difference_In_Days / 30);

                                        if (difference_In_Years > 0) {
                                            if (difference_In_Years == 1) {
                                                years = ", " + difference_In_Years + " " + getResources().getText(R.string.year_old);
                                            } else {
                                                years = ", " + difference_In_Years + " " + getResources().getText(R.string.years_old);
                                            }
                                            age += years;
                                        } else if (difference_In_Months > 0) {
                                            if (difference_In_Months == 1) {
                                                months = ", " + difference_In_Months + " " + getResources().getText(R.string.month_old);
                                            } else {
                                                months = ", " + difference_In_Months + " " + getResources().getText(R.string.months_old);
                                            }
                                            age += months;
                                        } else if (difference_In_Days > 0) {
                                            if (difference_In_Days == 1) {
                                                days = ", " + difference_In_Days + " " + getResources().getText(R.string.day_old);
                                            } else {
                                                days = ", " + difference_In_Days + " " + getResources().getText(R.string.days_old);
                                            }
                                            age += days;
                                        } else {
                                            age += ", " + getResources().getText(R.string.few_hours_old);
                                        }

                                        tvHome.setText(String.format("%s%s", lastKidName, age));
                                        restoreVisibility();
                                    } catch (ParseException e) {
                                        name = "";
                                        age = "";
                                        tvHome.setText(String.format("%s%s", name, age));
                                        tvPumping.setText("");
                                        tvDiaper.setText("");
                                        tvSleep.setText("");
                                        tvFeeding.setText("");
                                        removeVisibility();
                                        tvPic.setVisibility(View.VISIBLE);
                                        Glide.with(getApplicationContext()).load(R.drawable.circle_image_button1).transform(new CircleCrop()).into(profilePic);
                                    }
                                } else {
                                    tvHome.setText("");
                                    Glide.with(getApplicationContext()).load(R.drawable.circle_image_button1).transform(new CircleCrop()).into(profilePic);
                                }

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    ValueEventListener kidListener = new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            KidsData kidsData = snapshot.getValue(KidsData.class);
                            try {
                                name = kidsData.getName();
                                bd = kidsData.getBirthDate();
                                Date date = new Date();
                                String strDateTime = new SimpleDateFormat("yyyy-MM-dd").format(date);

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date d1 = sdf.parse(bd);
                                Date d2 = sdf.parse(strDateTime);
                                assert d1 != null;
                                assert d2 != null;

                                String days, years, months;
                                age = "";

                                long difference_In_Time = d2.getTime() - d1.getTime();

                                long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

                                long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

                                long difference_In_Months = (difference_In_Days / 30);

                                if (difference_In_Years>0){
                                    if (difference_In_Years == 1){
                                        years = ", " + difference_In_Years + " " + getResources().getText(R.string.year_old);
                                    } else {
                                        years = ", " + difference_In_Years + " " + getResources().getText(R.string.years_old);
                                    }
                                    age += years;
                                } else if (difference_In_Months>0) {
                                    if (difference_In_Months == 1){
                                        months = ", " + difference_In_Months + " " + getResources().getText(R.string.month_old);
                                    } else {
                                        months = ", " + difference_In_Months + " " + getResources().getText(R.string.months_old);
                                    }
                                    age += months;
                                } else if (difference_In_Days>0) {
                                    if (difference_In_Days == 1){
                                        days = ", " + difference_In_Days + " " + getResources().getText(R.string.day_old);
                                    } else {
                                        days = ", " + difference_In_Days + " " + getResources().getText(R.string.days_old);
                                    }
                                    age += days;
                                } else {
                                    age += ", " + getResources().getText(R.string.few_hours_old);
                                }

                                tvHome.setText(String.format("%s%s", name, age));


                                Query querySleep = dbr.child(id).child("kids").child(kid).child("sleep").limitToLast(1);
                                querySleep.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot sleepSnapshot : snapshot.getChildren()) {
                                            SleepingData sleepingData = sleepSnapshot.getValue(SleepingData.class);
                                            if (sleepingData != null) {
                                                String fellAsleep = sleepingData.getFellAsleep();
                                                String wokeUp = sleepingData.getWokeUp();
                                                if (wokeUp == null) {
                                                    tvSleep.setText("");
                                                } else if (fellAsleep != null && !wokeUp.equals("none")) {
                                                    if (fellAsleep.equals(wokeUp)) {
                                                        tvSleep.setText(getResources().getText(R.string.last_slept_few_seconds));
                                                    } else {
                                                        TimeCalculator calculator = new TimeCalculator(fellAsleep, wokeUp);
                                                        String difference;
                                                        try {
                                                            difference = calculator.findDifference(fellAsleep, wokeUp);
                                                            tvSleep.setText(getResources().getText(R.string.last_slept_for) + " " + difference);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                } else if (fellAsleep != null) {
                                                    Date date = new Date();
                                                    String strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date);
                                                    if (fellAsleep.equals(strDateTime)) {
                                                        tvSleep.setText(getResources().getText(R.string.fell_asleep_home) + " " + getResources().getText(R.string.a_few_seconds) + " " + getResources().getText(R.string.ago));
                                                    } else {
                                                        try {
                                                            TimeCalculator calculator = new TimeCalculator(fellAsleep, strDateTime);
                                                            String difference = calculator.findDifference(fellAsleep, strDateTime);
                                                            tvSleep.setText(getResources().getText(R.string.fell_asleep_home) + " " + difference + getResources().getText(R.string.ago));
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Query queryDiaper = dbr.child(id).child("kids").child(kid).child("diaper").limitToLast(1);
                                queryDiaper.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot diaperSnapshot : snapshot.getChildren()) {
                                            DiaperData diaperData = diaperSnapshot.getValue(DiaperData.class);
                                            if (diaperData != null) {
                                                String diaperTime = diaperData.getDateTimeDiaper();
                                                String diaperType = diaperData.getTypeDiaper();
                                                if(diaperType.equals("wet")) {
                                                    tvDiaper.setText(getResources().getText(R.string.last_changed) + " " + getResources().getText(R.string.wet) + " " + getResources().getText(R.string.at) + " " + diaperTime);
                                                } else if (diaperType.equals("dry")){
                                                    tvDiaper.setText(getResources().getText(R.string.last_changed) + " " + getResources().getText(R.string.dry) + " " + getResources().getText(R.string.at) + " " + diaperTime);
                                                } else if (diaperType.equals("dirty")){
                                                    tvDiaper.setText(getResources().getText(R.string.last_changed) + " " + getResources().getText(R.string.dirty) + " " + getResources().getText(R.string.at) + " " + diaperTime);
                                                } else if (diaperType.equals("mixed")){
                                                    tvDiaper.setText(getResources().getText(R.string.last_changed) + " " + getResources().getText(R.string.mixed) + " " + getResources().getText(R.string.at) + " " + diaperTime);
                                                }
                                            }

                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Query queryFeeding = dbr.child(id).child("kids").child(kid).child("feeding_all").limitToLast(1);
                                queryFeeding.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot feedingSnapshot : snapshot.getChildren()) {
                                            FeedingData feedingData = feedingSnapshot.getValue(FeedingData.class);
                                            if (feedingData != null) {
                                                String feedingTime = feedingData.getFeedingDateTime();
                                                String feedingType = feedingData.getFeedingType();
                                                if (feedingType.equals("breast milk")) {
                                                    String breast = feedingData.getBreastStarted();
                                                    if (breast.equals("simultaneous")) {
                                                        tvFeeding.setText(getResources().getText(R.string.last_fed) + " " + feedingTime + " " + getResources().getText(R.string.simultaneous_breastfeeding));
                                                    } else {
                                                        tvFeeding.setText(getResources().getText(R.string.last_fed) + " " + feedingTime + " " + getResources().getText(R.string.started_with) + " " + breast + " " + getResources().getText(R.string.breast));
                                                    }
                                                } else if (feedingType.equals("food")) {
                                                    tvFeeding.setText(getResources().getText(R.string.last_fed) + " " + feedingTime + " " + getResources().getText(R.string.with) + " " + getResources().getText(R.string.food));
                                                } else if(feedingType.equals("adapted milk")){
                                                    tvFeeding.setText(getResources().getText(R.string.last_fed) + " " + feedingTime + " " + getResources().getText(R.string.with) + " " + getResources().getText(R.string.adapted_milk));
                                                } else if (feedingType.equals("expressed milk")){
                                                    tvFeeding.setText(getResources().getText(R.string.last_fed) + " " + feedingTime + " " + getResources().getText(R.string.with) + " " + getResources().getText(R.string.expressed_milk));
                                                }
                                            }

                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Query queryPumping = dbr.child(id).child("kids").child(kid).child("pumping").limitToLast(1);
                                queryPumping.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot pumpingSnapshot : snapshot.getChildren()) {
                                            PumpingBreastFeedingData pumpingBreastFeedingData = pumpingSnapshot.getValue(PumpingBreastFeedingData.class);
                                            if (pumpingBreastFeedingData != null) {
                                                String pumpingStarted = pumpingBreastFeedingData.getDateTimeStarted();
                                                String pumpingBreast = pumpingBreastFeedingData.getBreastStarted();
                                                if(pumpingBreast.equals("simultaneous")) {
                                                    tvPumping.setText(getResources().getText(R.string.started) + " " + getResources().getText(R.string.simultaneous_pumping) + " " + getResources().getText(R.string.at) + " " + pumpingStarted);
                                                } else if (pumpingBreast.equals("left")){
                                                    tvPumping.setText(getResources().getText(R.string.started) + " " + getResources().getText(R.string.left) + " " + getResources().getText(R.string.at) + " " + pumpingStarted);
                                                } else if (pumpingBreast.equals("right")){
                                                    tvPumping.setText(getResources().getText(R.string.started) + " " + getResources().getText(R.string.right) + " " + getResources().getText(R.string.at) + " " + pumpingStarted);
                                                }
                                            }

                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Query queryImportant = dbr.child(id).child("kids").child(kid).child("important_all").limitToLast(1);
                                queryImportant.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot importantSnapshot : snapshot.getChildren()) {
                                            ImportantData importantData = importantSnapshot.getValue(ImportantData.class);
                                            if (importantData != null) {
                                                String importantDate = importantData.getImportantDateTime();
                                                String importantType = importantData.getImportantType();

                                                if (importantType != null) {
                                                    if(importantType.equals("growth")){
                                                        tvImportant.setText(getResources().getText(R.string.last_added) + " " + getResources().getText(R.string.growth_home) + " " + getResources().getText(R.string.at) + " " + importantDate);
                                                    } else if(importantType.equals("memory")){
                                                        tvImportant.setText(getResources().getText(R.string.last_added) + " " + getResources().getText(R.string.memory_home) + " " + getResources().getText(R.string.at) + " " + importantDate);
                                                    } else if(importantType.equals("medication")){
                                                        tvImportant.setText(getResources().getText(R.string.last_added) + " " + getResources().getText(R.string.medication_home) + " " + getResources().getText(R.string.at) + " " + importantDate);
                                                    } else if(importantType.equals("vaccine")){
                                                        tvImportant.setText(getResources().getText(R.string.last_added) + " " + getResources().getText(R.string.vaccine_home) + " " + getResources().getText(R.string.at) + " " + importantDate);
                                                    } else if(importantType.equals("symptom")){
                                                        tvImportant.setText(getResources().getText(R.string.last_added) + " " + getResources().getText(R.string.symptom_home) + " " + getResources().getText(R.string.at) + " " + importantDate);
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Query queryReminder = dbr.child(id).child("kids").child(kid).child("reminders").limitToLast(1);
                                queryReminder.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot reminderSnapshot : snapshot.getChildren()) {
                                            ReminderData reminderData = reminderSnapshot.getValue(ReminderData.class);
                                            if (reminderData != null) {
                                                String reminderTime = reminderData.getTime();
                                                tvReminders.setText(getResources().getText(R.string.last_reminder) + " " + reminderTime);
                                            }

                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                restoreVisibility();
                                tvPic.setVisibility(View.GONE);
                                Glide.with(getApplicationContext()).load(kidsData.getPicture()).transform(new CircleCrop()).into(profilePic);
                            } catch (Exception t) {
                                name = "";
                                age = "";
                                tvHome.setText(String.format("%s%s", name, age));
                                tvPumping.setText("");
                                tvDiaper.setText("");
                                tvSleep.setText("");
                                tvFeeding.setText("");
                                removeVisibility();
                                tvPic.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext()).load(R.drawable.circle_image_button1).transform(new CircleCrop()).into(profilePic);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }; dbr.child(id).child("kids").child(kid).addValueEventListener(kidListener);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);
        pbHome.setVisibility(View.GONE);
    }

    public void initViews(){
        profilePic = findViewById(R.id.iv_profile);
        tvPic = findViewById(R.id.tv_pic);
        tvHome = findViewById(R.id.tv_home);
        tvSleep = findViewById(R.id.tv_sleep_home);
        tvDiaper = findViewById(R.id.tv_last_diaper);
        tvFeeding = findViewById(R.id.tv_feeding_home);
        tvPumping = findViewById(R.id.tv_pumping_home);
        tvImportant = findViewById(R.id.tv_important);
        tvReminders = findViewById(R.id.tv_reminders);
        btnFeeding = findViewById(R.id.btn_feeding);
        btnSleeping = findViewById(R.id.btn_sleeping);
        btnDiaper = findViewById(R.id.btn_diaper);
        btnPumping = findViewById(R.id.btn_pumping);
        btnImportant = findViewById(R.id.btn_important);
        btnAlarms = findViewById(R.id.btn_alarms);
        btnSettings = findViewById(R.id.btn_settings);
        btnAdd = findViewById(R.id.btn_add_kid_home);
        pbHome = findViewById(R.id.pb_home);
    }

    public void removeVisibility(){
        btnAdd.setVisibility(View.VISIBLE);
        btnImportant.setVisibility(View.GONE);
        btnFeeding.setVisibility(View.GONE);
        btnSleeping.setVisibility(View.GONE);
        btnAlarms.setVisibility(View.GONE);
        btnPumping.setVisibility(View.GONE);
        btnDiaper.setVisibility(View.GONE);
        tvFeeding.setVisibility(View.GONE);
        tvSleep.setVisibility(View.GONE);
        tvDiaper.setVisibility(View.GONE);
        tvPumping.setVisibility(View.GONE);
        tvImportant.setVisibility(View.GONE);
        tvReminders.setVisibility(View.GONE);
    }

    public void restoreVisibility(){
        btnAdd.setVisibility(View.GONE);
        btnImportant.setVisibility(View.VISIBLE);
        btnFeeding.setVisibility(View.VISIBLE);
        btnSleeping.setVisibility(View.VISIBLE);
        btnAlarms.setVisibility(View.VISIBLE);
        btnPumping.setVisibility(View.VISIBLE);
        btnDiaper.setVisibility(View.VISIBLE);
        tvFeeding.setVisibility(View.VISIBLE);
        tvSleep.setVisibility(View.VISIBLE);
        tvDiaper.setVisibility(View.VISIBLE);
        tvPumping.setVisibility(View.VISIBLE);
        tvImportant.setVisibility(View.VISIBLE);
        tvReminders.setVisibility(View.VISIBLE);
    }
}