package com.example.kidtracker.main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Activities.HomeActivity;
import com.example.kidtracker.main.Data.ExpressedMilkData;
import com.example.kidtracker.main.Data.FeedingData;
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

public class ExpressedMilkFragment extends Fragment {
    TextInputEditText etTimeExpressed, etAmountExpressed, etNoteExpressed;
    Button saveExpressed, cancelExpressed;
    ProgressBar pbExpressed;

    String id, kid, date, amount, note;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    LocalDate localDate;
    LocalTime localTime;
    LocalDateTime localDateTime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expressed_milk, container, false);
        etTimeExpressed = v.findViewById(R.id.iet_ate_at_expressed);
        etAmountExpressed = v.findViewById(R.id.iet_amount_expressed);
        etNoteExpressed = v.findViewById(R.id.iet_note_expressed);
        saveExpressed = v.findViewById(R.id.btn_save_expressed);
        cancelExpressed = v.findViewById(R.id.btn_cancel_expressed);
        pbExpressed = v.findViewById(R.id.pb_expressed);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        Date date = new Date();
        String strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date);
        etTimeExpressed.setText(strDateTime);

        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);

        etTimeExpressed.setOnClickListener(v1 -> new SingleDateAndTimePickerDialog.Builder(getContext())
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
                        DateTimeFormatter f = DateTimeFormatter.ofPattern( "EEE MMM dd HH:mm:ss zzz uuuu" ).withLocale( Locale.US );
                        ZonedDateTime zdt = ZonedDateTime.parse(newDate, f);

                        localDate = zdt.toLocalDate();
                        localTime = zdt.toLocalTime();
                        localDateTime = zdt.toLocalDateTime();

                        etTimeExpressed.setText(localDate.toString() + ", " + localTime.toString());
                    }
                })
                .display());

        saveExpressed.setOnClickListener(v12 -> {
            if (!etAmountExpressed.getText().toString().isEmpty()) {
                setSaveExpressed();
                Intent intentHome = new Intent(getContext(), HomeActivity.class);
                startActivity(intentHome);
            } else {
                etAmountExpressed.requestFocus();
                etAmountExpressed.setError("Add amount");
            }
        });

        cancelExpressed.setOnClickListener(v13 -> {
            Intent intentHome = new Intent(getContext(), HomeActivity.class);
            startActivity(intentHome);
        });

        onStop();
        onDestroyView();

        return v;
    }

    public void setSaveExpressed(){
        date = etTimeExpressed.getText().toString();
        amount = etAmountExpressed.getText().toString();

        if(etNoteExpressed.getText().toString().isEmpty()) {
            note = "none";
        } else {
            note = etNoteExpressed.getText().toString();
        }

        ExpressedMilkData expressedMilkData = new ExpressedMilkData(date, amount, note);
        FeedingData feedingData = new FeedingData(date, "exressed milk");
        dbr.child(id).child("kids").child(kid).child("feeding").child("expressed_milk").child(date).setValue(expressedMilkData);
        dbr.child(id).child("kids").child(kid).child("feeding_all").child(date).setValue(feedingData);

    }

    @Override
    public void onStop() {
        super.onStop();
        etAmountExpressed.setText("");
        etNoteExpressed.setText("");
    }
}