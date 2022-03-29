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
import com.example.kidtracker.main.Data.MemoryData;
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

public class MemoryFragment extends Fragment {

    TextInputEditText dateMemory, titleMemory, noteMemory;
    Button saveMemory;
    ProgressBar pbMemory;

    String id, kid, date, title, note, strDateTime;
    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    Date date1;
    LocalDate localDate;
    LocalTime localTime;
    LocalDateTime localDateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_memory, container, false);
        dateMemory = v.findViewById(R.id.iet_date_memory);
        titleMemory = v.findViewById(R.id.iet_memory);
        noteMemory = v.findViewById(R.id.iet_note_memory);
        saveMemory = v.findViewById(R.id.btn_save_memory);
        pbMemory = v.findViewById(R.id.pb_memory);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        date1 = new Date();
        strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date1);
        dateMemory.setText(strDateTime);

        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);

        dateMemory.setOnClickListener(v1 -> new SingleDateAndTimePickerDialog.Builder(getContext())
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

                        dateMemory.setText(localDate.toString() + ", " + localTime.toString());
                    }
                })
                .display());

        saveMemory.setOnClickListener(v12 -> {
            if(!titleMemory.getText().toString().isEmpty()) {
                setSaveMemory();
                Intent intentHome = new Intent(getContext(), HomeActivity.class);
                startActivity(intentHome);
            } else if (titleMemory.getText().toString().isEmpty()){
                titleMemory.requestFocus();
                titleMemory.setError("Add memory");
            }
        });

        onStop();
        onDestroyView();

        return v;
    }

    public void setSaveMemory() {
        date = dateMemory.getText().toString();
        title = titleMemory.getText().toString();

        if (noteMemory.getText().toString().isEmpty()){
            note = "none";
        } else {
            note = noteMemory.getText().toString();
        }

        MemoryData memoryData = new MemoryData(date, title, note);
        ImportantData importantData = new ImportantData(date, "memory");
        dbr.child(id).child("kids").child(kid).child("important").child("memory").child(date).setValue(memoryData);
        dbr.child(id).child("kids").child(kid).child("important_all").child(date).setValue(importantData);
    }

    @Override
    public void onStop() {
        super.onStop();
        titleMemory.setText("");
        noteMemory.setText("");
    }
}