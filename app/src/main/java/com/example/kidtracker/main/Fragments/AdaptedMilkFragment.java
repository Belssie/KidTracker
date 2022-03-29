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
import com.example.kidtracker.main.Data.AdaptedMilkData;
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

public class AdaptedMilkFragment extends Fragment {
    TextInputEditText etTimeAM, etAmountAM, etTypeAM, etNoteAM;
    Button saveAM, cancelAM;
    ProgressBar pbAm;

    String id, kid, dateAm, amount, type, note, strDateTime;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    Date date;
    LocalDate localDate;
    LocalTime localTime;
    LocalDateTime localDateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_adapted_milk, container, false);
        etTimeAM = v.findViewById(R.id.iet_ate_at_am);
        etAmountAM = v.findViewById(R.id.iet_amount_am);
        etTypeAM = v.findViewById(R.id.iet_type_am);
        etNoteAM = v.findViewById(R.id.iet_note_am);
        saveAM = v.findViewById(R.id.btn_save_am);
        cancelAM = v.findViewById(R.id.btn_cancel_am);
        pbAm = v.findViewById(R.id.pb_am);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        date = new Date();
        strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date);

        etTimeAM.setText(strDateTime);

        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);

        etTimeAM.setOnClickListener(v13 -> new SingleDateAndTimePickerDialog.Builder(getContext())
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

                        etTimeAM.setText(localDate.toString() + ", " + localTime.toString());

                    }
                })
                .display());

        saveAM.setOnClickListener(v12 -> {
            if (!etTypeAM.getText().toString().isEmpty() && !etAmountAM.getText().toString().isEmpty()){
                setSaveAM();
                Intent intentHome = new Intent(getContext(), HomeActivity.class);
                startActivity(intentHome);
            } else if(etTypeAM.getText().toString().isEmpty()) {
                etTypeAM.requestFocus();
                etTypeAM.setError("Add type");
            } else if(etAmountAM.getText().toString().isEmpty()){
                etAmountAM.requestFocus();
                etAmountAM.setError("Add amount");
            }
        });

        cancelAM.setOnClickListener(v1 -> {
            Intent intentHome = new Intent(getContext(), HomeActivity.class);
            startActivity(intentHome);
        });

        onStop();
        onDestroyView();
        return v;
    }

    public void setSaveAM(){
        dateAm = etTimeAM.getText().toString();
        type = etTypeAM.getText().toString();
        amount = etAmountAM.getText().toString();

        if(etNoteAM.getText().toString().isEmpty()) {
            note = "none";
        } else {
            note = etNoteAM.getText().toString();
        }

        AdaptedMilkData adaptedMilkData = new AdaptedMilkData(dateAm, amount, type, note);
        FeedingData feedingData = new FeedingData(dateAm, "adapted milk");
        dbr.child(id).child("kids").child(kid).child("feeding").child("adapted_milk").child(dateAm).setValue(adaptedMilkData);
        dbr.child(id).child("kids").child(kid).child("feeding_all").child(dateAm).setValue(feedingData);
    }

    @Override
    public void onStop() {
        super.onStop();
        etAmountAM.setText("");
        etTypeAM.setText("");
        etNoteAM.setText("");
    }
}