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
import com.example.kidtracker.main.Data.ImportantData;
import com.example.kidtracker.main.Data.SymptomsData;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.chip.Chip;
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

public class SymptomsFragment extends Fragment {
    TextInputEditText dateSymptoms, titleSymptom, noteSymptom;
    Button saveSymptoms, cancelSymptoms;
    ProgressBar pbSymptoms;
    Chip headache, stomachache, toothache, temperature, fever, cough, secretions, diarrhea, rash;

    String id, kid, date, title, note, strDateTime, symptoms;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    Date date1;
    LocalDate localDate;
    LocalTime localTime;
    LocalDateTime localDateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_symptoms, container, false);
        dateSymptoms = v.findViewById(R.id.iet_time_symptoms);
        titleSymptom = v.findViewById(R.id.iet_symptom);
        noteSymptom = v.findViewById(R.id.iet_note_symptom);
        saveSymptoms = v.findViewById(R.id.btn_save_symptoms);
        cancelSymptoms = v.findViewById(R.id.btn_cancel_symptom);
        pbSymptoms = v.findViewById(R.id.pb_symptoms);
        headache = v.findViewById(R.id.chip_headache);
        stomachache = v.findViewById(R.id.chip_stomachache);
        toothache = v.findViewById(R.id.chip_toothache);
        temperature = v.findViewById(R.id.chip_temperature);
        fever = v.findViewById(R.id.chip_fever);
        cough = v.findViewById(R.id.chip_cough);
        secretions = v.findViewById(R.id.chip_secretions);
        diarrhea = v.findViewById(R.id.chip_diarrhea);
        rash = v.findViewById(R.id.chip_rash);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        date1 = new Date();
        strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date1);
        dateSymptoms.setText(strDateTime);

        symptoms = "";

        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);

        dateSymptoms.setOnClickListener(v1 -> new SingleDateAndTimePickerDialog.Builder(getContext())
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
                        DateTimeFormatter f = DateTimeFormatter.ofPattern( "EEE MMM dd HH:mm:ss zzz uuuu" ).withLocale( Locale.US );
                        ZonedDateTime zdt = ZonedDateTime.parse(newDate, f);

                        localDate = zdt.toLocalDate();
                        localTime = zdt.toLocalTime();
                        localDateTime = zdt.toLocalDateTime();

                        dateSymptoms.setText(localDate.toString() + ", " + localTime.toString());

                    }
                })
                .display());

        rash.setOnClickListener(v12 -> {
            if (rash.isChecked() ) {
                if (symptoms.isEmpty()){
                    symptoms += rash.getText().toString();
                } else {
                    symptoms += ", " + rash.getText().toString();
                }
                titleSymptom.setText(symptoms);

            } else if (!rash.isChecked()) {
                symptoms = symptoms.replace(", " + rash.getText().toString(), "");
                symptoms = symptoms.replace(rash.getText().toString(), "");
                symptoms = symptoms.replace(rash.getText().toString() + ", ", "");
                titleSymptom.setText(symptoms);
            }
        });

        headache.setOnClickListener(v13 -> {
            if (headache.isChecked() ) {
                if (symptoms.isEmpty()) {
                    symptoms += headache.getText().toString();
                } else {
                    symptoms += ", " + headache.getText().toString();
                }
                titleSymptom.setText(symptoms);
            }else if (!headache.isChecked()) {
                symptoms = symptoms.replace(", " + headache.getText().toString(), "");
                symptoms = symptoms.replace(headache.getText().toString(), "");
                symptoms = symptoms.replace(headache.getText().toString() + ", ", "");
                titleSymptom.setText(symptoms);
            }
        });

        stomachache.setOnClickListener(v14 -> {
            if (stomachache.isChecked() ) {
                if (symptoms.isEmpty()){
                    symptoms += stomachache.getText().toString();
                } else {
                    symptoms += ", " + stomachache.getText().toString();
                }
                titleSymptom.setText(symptoms);
            } else if (!stomachache.isChecked()) {
                symptoms = symptoms.replace(", " + stomachache.getText().toString(), "");
                symptoms = symptoms.replace(stomachache.getText().toString(), "");
                symptoms = symptoms.replace(stomachache.getText().toString() + ", ", "");
                titleSymptom.setText(symptoms);
            }
        });

        toothache.setOnClickListener(v15 -> {
            if (toothache.isChecked() ) {
                if (symptoms.isEmpty()){
                    symptoms += toothache.getText().toString();
                } else {
                    symptoms += ", " + toothache.getText().toString();
                }
                titleSymptom.setText(symptoms);
            } else if (!toothache.isChecked()) {
                symptoms = symptoms.replace(", " + toothache.getText().toString(), "");
                symptoms = symptoms.replace(toothache.getText().toString(), "");
                symptoms = symptoms.replace(toothache.getText().toString() + ", ", "");
                titleSymptom.setText(symptoms);
            }
        });

        temperature.setOnClickListener(v16 -> {
            if (temperature.isChecked() ) {
                if (symptoms.isEmpty()){
                symptoms += temperature.getText().toString();
                } else {
                symptoms += ", " + temperature.getText().toString();
                }
                titleSymptom.setText(symptoms);
            } else if (!temperature.isChecked()) {
                symptoms = symptoms.replace(", " + temperature.getText().toString(), "");
                symptoms = symptoms.replace(temperature.getText().toString(), "");
                symptoms = symptoms.replace(temperature.getText().toString() + ", ", "");
                titleSymptom.setText(symptoms);
            }
        });

        fever.setOnClickListener(v17 -> {
            if (fever.isChecked() ) {
                if (symptoms.isEmpty()){
                    symptoms += fever.getText().toString();
                } else {
                    symptoms += ", " + fever.getText().toString();
                }
                titleSymptom.setText(symptoms);
            } else if (!fever.isChecked()) {
                symptoms = symptoms.replace(", " + fever.getText().toString(), "");
                symptoms = symptoms.replace(fever.getText().toString(), "");
                symptoms = symptoms.replace(fever.getText().toString() + ", ", "");
                titleSymptom.setText(symptoms);
            }
        });

        cough.setOnClickListener(v18 -> {
            if (cough.isChecked() ) {
                if (symptoms.isEmpty()){
                    symptoms += cough.getText().toString();
                } else {
                    symptoms += ", " + cough.getText().toString();
                }
                titleSymptom.setText(symptoms);
            } else if (!cough.isChecked()) {
                symptoms = symptoms.replace(", " + cough.getText().toString(), "");
                symptoms = symptoms.replace(cough.getText().toString(), "");
                symptoms = symptoms.replace(cough.getText().toString() + ", ", "");
                titleSymptom.setText(symptoms);
            }
        });

        secretions.setOnClickListener(v19 -> {
            if (secretions.isChecked() ) {
                if (symptoms.isEmpty()){
                    symptoms += secretions.getText().toString();
                } else {
                    symptoms += ", " + secretions.getText().toString();
                }
                titleSymptom.setText(symptoms);
            } else if (!secretions.isChecked()) {
                symptoms = symptoms.replace(", " + secretions.getText().toString(), "");
                symptoms = symptoms.replace(secretions.getText().toString(), "");
                symptoms = symptoms.replace(secretions.getText().toString() + ", ", "");
                titleSymptom.setText(symptoms);
            }
        });

        diarrhea.setOnClickListener(v110 -> {
            if (diarrhea.isChecked() ) {
                if (symptoms.isEmpty()){
                    symptoms += diarrhea.getText().toString();
                } else {
                    symptoms += ", " + diarrhea.getText().toString();
                }
                titleSymptom.setText(symptoms);
            } else if (!diarrhea.isChecked()) {
                symptoms = symptoms.replace(", " + diarrhea.getText().toString(), "");
                symptoms = symptoms.replace(diarrhea.getText().toString(), "");
                symptoms = symptoms.replace(diarrhea.getText().toString() + ", ", "");
                titleSymptom.setText(symptoms);
            }
        });

        saveSymptoms.setOnClickListener(v12 -> {
            if (!titleSymptom.getText().toString().isEmpty()) {
                setSaveSymptoms();

                Intent intentHome = new Intent(getContext(), HomeActivity.class);
                startActivity(intentHome);
            } else if (titleSymptom.getText().toString().isEmpty()){
                titleSymptom.requestFocus();
                titleSymptom.setError("Add symptom");
            }
        });

        cancelSymptoms.setOnClickListener(v13 -> {
            Intent intentHome = new Intent(getContext(), HomeActivity.class);
            startActivity(intentHome);
        });

        onStop();
        onDestroyView();

        return v;
    }

    public void setSaveSymptoms(){
        date = dateSymptoms.getText().toString();
        title = titleSymptom.getText().toString();

        if (noteSymptom.getText().toString().isEmpty()){
            note = "none";
        } else {
            note = noteSymptom.getText().toString();
        }

        SymptomsData symptomsData = new SymptomsData(date, title, note);
        ImportantData importantData = new ImportantData(date, "symptom");
        dbr.child(id).child("kids").child(kid).child("important").child("symptoms").child(date).setValue(symptomsData);
        dbr.child(id).child("kids").child(kid).child("important_all").child(date).setValue(importantData);
    }

    @Override
    public void onStop() {
        super.onStop();
        titleSymptom.setText("");
        noteSymptom.setText("");
    }
}