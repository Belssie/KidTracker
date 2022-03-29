package com.example.kidtracker.main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Activities.HomeActivity;
import com.example.kidtracker.main.Data.GrowthData;
import com.example.kidtracker.main.Data.ImportantData;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class GrowthFragment extends Fragment {
    TextInputEditText dateGrowth, lengthGrowth, weightGrowth, noteGrowth;
    Button saveGrowth, cancelGrowth;
    ProgressBar pbGrowth;

    String id, kid, date, length, weight, note, strDateTime;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    Date date1;
    LocalDate localDate;
    LocalTime localTime;
    LocalDateTime localDateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_growth, container, false);
        dateGrowth = v.findViewById(R.id.iet_date_growth);
        lengthGrowth = v.findViewById(R.id.iet_length_growth);
        weightGrowth = v.findViewById(R.id.iet_weight_growth);
        noteGrowth = v.findViewById(R.id.iet_note_growth);
        saveGrowth = v.findViewById(R.id.btn_save_growth);
        cancelGrowth = v.findViewById(R.id.btn_cancel_growth);
        pbGrowth = v.findViewById(R.id.pb_growth);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        date1 = new Date();
        strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date1);

        dateGrowth.setText(strDateTime);

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

        dateGrowth.setOnClickListener(v1 -> new SingleDateAndTimePickerDialog.Builder(getContext())
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
                .listener(date -> {
                    String newDate = date.toString();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz uuuu").withLocale(Locale.US);
                        ZonedDateTime zdt = ZonedDateTime.parse(newDate, f);

                        localDate = zdt.toLocalDate();
                        localTime = zdt.toLocalTime();
                        localDateTime = zdt.toLocalDateTime();

                        dateGrowth.setText(localDate.toString() + ", " + localTime.toString());
                    }
                })
                .display());


        saveGrowth.setOnClickListener(v12 -> {
            if (!lengthGrowth.getText().toString().isEmpty() && !weightGrowth.getText().toString().isEmpty()) {
                setSaveGrowth();
                Intent intentHome = new Intent(getContext(), HomeActivity.class);
                startActivity(intentHome);
            } else if (lengthGrowth.getText().toString().isEmpty()){
                lengthGrowth.requestFocus();
                lengthGrowth.setError("Add length");
            } else if (weightGrowth.getText().toString().isEmpty()){
                weightGrowth.requestFocus();
                weightGrowth.setError("Add weight");
            }
        });

        cancelGrowth.setOnClickListener(v13 -> {
            Intent intentHome = new Intent(getContext(), HomeActivity.class);
            startActivity(intentHome);
        });

        onStop();
        onDestroyView();

        return v;
    }

    public void setSaveGrowth() {
        date = dateGrowth.getText().toString();
        length = lengthGrowth.getText().toString();
        weight = weightGrowth.getText().toString();

        if (noteGrowth.getText().toString().isEmpty()){
            note = "none";
        } else {
            note = noteGrowth.getText().toString();
        }

        GrowthData growthData = new GrowthData(date, length, weight, note);
        ImportantData importantData = new ImportantData(date, "growth");
        dbr.child(id).child("kids").child(kid).child("important").child("growth").child(date).setValue(growthData);
        dbr.child(id).child("kids").child(kid).child("important_all").child(date).setValue(importantData);
    }

    @Override
    public void onStop() {
        super.onStop();
        lengthGrowth.setText("");
        weightGrowth.setText("");
        noteGrowth.setText("");
    }
}