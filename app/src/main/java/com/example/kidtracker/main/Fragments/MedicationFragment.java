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
import com.example.kidtracker.main.Data.MedicationData;
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

public class MedicationFragment extends Fragment {
    TextInputEditText dateMedication, titleMedication, amountMedication, noteMedication;
    Button saveMedication, cancelMedication;
    ProgressBar pbMedication;

    String id, kid, date, title, amount, note, strDateTime;
    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    Date date1;
    LocalDate localDate;
    LocalTime localTime;
    LocalDateTime localDateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medication, container, false);
        dateMedication = v.findViewById(R.id.iet_time_medication);
        titleMedication = v.findViewById(R.id.iet_medication);
        amountMedication = v.findViewById(R.id.iet_amount_medication);
        noteMedication = v.findViewById(R.id.iet_note_medication);
        saveMedication = v.findViewById(R.id.btn_save_medication);
        cancelMedication = v.findViewById(R.id.btn_cancel_medication);
        pbMedication = v.findViewById(R.id.pb_medication);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        date1 = new Date();
        strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date1);
        dateMedication.setText(strDateTime);

        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);

        dateMedication.setOnClickListener(v1 -> {
            titleMedication.clearFocus();
            amountMedication.clearFocus();
            noteMedication.clearFocus();
            new SingleDateAndTimePickerDialog.Builder(getContext())
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

                            dateMedication.setText(localDate.toString() + ", " + localTime.toString());
                        }
                    })
                    .display();
        });

        saveMedication.setOnClickListener(v12 -> {
            if(!titleMedication.getText().toString().isEmpty() && !amountMedication.getText().toString().isEmpty()) {
                setSaveMedication();
                Intent intentHome = new Intent(getContext(), HomeActivity.class);
                startActivity(intentHome);
            } else if (titleMedication.getText().toString().isEmpty()){
                titleMedication.requestFocus();
                titleMedication.setError("Add medication");
            } else if (amountMedication.getText().toString().isEmpty()){
                amountMedication.requestFocus();
                amountMedication.setError("Add amount");
            }
        });

        cancelMedication.setOnClickListener(v13 -> {
            Intent intentHome = new Intent(getContext(), HomeActivity.class);
            startActivity(intentHome);
        });

        onStop();
        onDestroyView();

        return v;
    }

    public void setSaveMedication(){
        date = dateMedication.getText().toString();
        title = titleMedication.getText().toString();
        amount = amountMedication.getText().toString();

        if (noteMedication.getText().toString().isEmpty()){
            note = "none";
        } else {
            note = noteMedication.getText().toString();
        }

        MedicationData medicationData = new MedicationData(date, title, amount, note);
        ImportantData importantData = new ImportantData(date, "medication");
        dbr.child(id).child("kids").child(kid).child("important").child("medication").child(date).setValue(medicationData);
        dbr.child(id).child("kids").child(kid).child("important_all").child(date).setValue(importantData);
    }

    @Override
    public void onStop() {
        super.onStop();
        titleMedication.setText("");
        amountMedication.setText("");
        noteMedication.setText("");
    }
}