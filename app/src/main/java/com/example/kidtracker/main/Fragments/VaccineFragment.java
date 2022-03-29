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
import com.example.kidtracker.main.Data.VaccineData;
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

public class VaccineFragment extends Fragment {
    TextInputEditText dateVaccine, titleVaccine, noteVaccine;
    Button saveVaccine, cancelVaccine;
    Chip dtap, hepa, hepb, hib, influenza, ipv, mmr, pcv, rv;
    ProgressBar pbVaccine;

    String id, kid, date, title, note, vaccines, strDateTime;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    Date date1;
    LocalDate localDate;
    LocalTime localTime;
    LocalDateTime localDateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vaccine, container, false);
        dateVaccine = v.findViewById(R.id.iet_time_vaccine);
        titleVaccine = v.findViewById(R.id.iet_vaccine);
        noteVaccine = v.findViewById(R.id.iet_note_vaccine);
        saveVaccine = v.findViewById(R.id.btn_save_vaccine);
        cancelVaccine = v.findViewById(R.id.btn_cancel_vaccine);
        dtap = v.findViewById(R.id.chip_dtap);
        hepa = v.findViewById(R.id.chip_hepa);
        hepb = v.findViewById(R.id.chip_hepb);
        hib = v.findViewById(R.id.chip_hib);
        influenza = v.findViewById(R.id.chip_influenza);
        ipv = v.findViewById(R.id.chip_ipv);
        mmr = v.findViewById(R.id.chip_mmr);
        pcv = v.findViewById(R.id.chip_pcv);
        rv = v.findViewById(R.id.chip_rv);
        pbVaccine = v.findViewById(R.id.pb_vaccine);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        vaccines = "";
        date1 = new Date();
        strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date1);
        dateVaccine.setText(strDateTime);

        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);

        dateVaccine.setOnClickListener(v1 -> new SingleDateAndTimePickerDialog.Builder(getContext())
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

                        dateVaccine.setText(localDate.toString() + ", " + localTime.toString());
                    }
                })
                .display());

        dtap.setOnClickListener(v12 -> {
            if (dtap.isChecked() ) {
                if (vaccines.isEmpty()) {
                    vaccines += dtap.getText().toString();
                } else {
                    vaccines += ", " + dtap.getText().toString();
                }
                titleVaccine.setText(vaccines);
            } else if (!dtap.isChecked()) {
                vaccines = vaccines.replace(", " + dtap.getText().toString(), "");
                vaccines = vaccines.replace(dtap.getText().toString(), "");
                vaccines = vaccines.replace(dtap.getText().toString() + ", ", "");
                titleVaccine.setText(vaccines);
            }
        });

        hepa.setOnClickListener(v13 -> {
            if (hepa.isChecked() ) {
                if (vaccines.isEmpty()) {
                    vaccines += hepa.getText().toString();
                } else {
                    vaccines +=", " + hepa.getText().toString();
                }
                titleVaccine.setText(vaccines);
            } else if (!hepa.isChecked()) {
                vaccines = vaccines.replace(", " + hepa.getText().toString(), "");
                vaccines = vaccines.replace(hepa.getText().toString(), "");
                vaccines = vaccines.replace(hepa.getText().toString() + ", ", "");
                titleVaccine.setText(vaccines);
            }
        });

        hepb.setOnClickListener(v14 -> {
            if (hepb.isChecked() ) {
                if (vaccines.isEmpty()) {
                    vaccines += hepb.getText().toString();
                } else {
                    vaccines += ", " + hepb.getText().toString();
                }
                titleVaccine.setText(vaccines);
            } else if (!hepb.isChecked()) {
                vaccines = vaccines.replace(", " + hepb.getText().toString(), "");
                vaccines = vaccines.replace(hepb.getText().toString(), "");
                vaccines = vaccines.replace(hepb.getText().toString() + ", ", "");
                titleVaccine.setText(vaccines);
            }
        });

        hib.setOnClickListener(v15 -> {
            if (hib.isChecked() ) {
                if (vaccines.isEmpty()) {
                    vaccines += hib.getText().toString();
                } else {
                    vaccines += ", " + hib.getText().toString();
                }
                titleVaccine.setText(vaccines);
            } else if (!hib.isChecked()) {
                vaccines = vaccines.replace(", " + hib.getText().toString(), "");
                vaccines = vaccines.replace(hib.getText().toString(), "");
                vaccines = vaccines.replace(hib.getText().toString() + ", ", "");
                titleVaccine.setText(vaccines);
            }
        });

        influenza.setOnClickListener(v16 -> {
            if (influenza.isChecked() ) {
                if (vaccines.isEmpty()) {
                    vaccines += influenza.getText().toString();
                } else {
                    vaccines += ", " + influenza.getText().toString();
                }
                titleVaccine.setText(vaccines);
            } else if (!influenza.isChecked()) {
                vaccines = vaccines.replace(", " + influenza.getText().toString(), "");
                vaccines = vaccines.replace(influenza.getText().toString(), "");
                vaccines = vaccines.replace(influenza.getText().toString() + ", ", "");
                titleVaccine.setText(vaccines);
            }
        });

        ipv.setOnClickListener(v17 -> {
            if (ipv.isChecked() ) {
                if (vaccines.isEmpty()) {
                    vaccines += ipv.getText().toString();
                } else {
                    vaccines += ", " + ipv.getText().toString();
                }
                titleVaccine.setText(vaccines);
            } else if (!ipv.isChecked()) {
                vaccines = vaccines.replace(", " + ipv.getText().toString(), "");
                vaccines = vaccines.replace(ipv.getText().toString(), "");
                vaccines = vaccines.replace(ipv.getText().toString() + ", ", "");
                titleVaccine.setText(vaccines);
            }
        });

        mmr.setOnClickListener(v18 -> {
            if (mmr.isChecked() ) {
                if (vaccines.isEmpty()) {
                    vaccines += mmr.getText().toString();
                } else {
                    vaccines += ", " + mmr.getText().toString();
                }
                titleVaccine.setText(vaccines);
            } else if (!mmr.isChecked()) {
                vaccines = vaccines.replace(", " + mmr.getText().toString(), "");
                vaccines = vaccines.replace(mmr.getText().toString(), "");
                vaccines = vaccines.replace(mmr.getText().toString() + ", ", "");
                titleVaccine.setText(vaccines);
            }
        });

        pcv.setOnClickListener(v19 -> {
            if (pcv.isChecked() ) {
                if (vaccines.isEmpty()) {
                    vaccines += pcv.getText().toString();
                } else {
                    vaccines += ", " + pcv.getText().toString();
                }
                titleVaccine.setText(vaccines);
            } else if (!pcv.isChecked()) {
                vaccines = vaccines.replace(", " + pcv.getText().toString(), "");
                vaccines = vaccines.replace(pcv.getText().toString(), "");
                vaccines = vaccines.replace(pcv.getText().toString() + ", ", "");
                titleVaccine.setText(vaccines);
            }
        });

        rv.setOnClickListener(v110 -> {
            if (rv.isChecked() ) {
                if (vaccines.isEmpty()) {
                    vaccines += rv.getText().toString();
                } else {
                    vaccines += ", " + rv.getText().toString();
                }
                titleVaccine.setText(vaccines);
            } else if (!rv.isChecked()) {
                vaccines = vaccines.replace(", " + rv.getText().toString(), "");
                vaccines = vaccines.replace(rv.getText().toString(), "");
                vaccines = vaccines.replace(rv.getText().toString() + ", ", "");
                titleVaccine.setText(vaccines);
            }
        });

        saveVaccine.setOnClickListener(v111 -> {
            if(!titleVaccine.getText().toString().isEmpty()) {
                setSaveVaccine();
                Intent intentHome = new Intent(getContext(), HomeActivity.class);
                startActivity(intentHome);
            } else if (titleVaccine.getText().toString().isEmpty()){
                titleVaccine.requestFocus();
                titleVaccine.setError("Add vaccine");
            }
        });

        cancelVaccine.setOnClickListener(v112 -> {
            Intent intentHome = new Intent(getContext(), HomeActivity.class);
            startActivity(intentHome);
        });

        onStop();
        onDestroyView();

        return v;
    }
    public void setSaveVaccine(){
        date = dateVaccine.getText().toString();
        title = titleVaccine.getText().toString();

        if (noteVaccine.getText().toString().isEmpty()){
            note = "none";
        } else {
            note = noteVaccine.getText().toString();
        }

        VaccineData vaccineData = new VaccineData(date, title, note);
        ImportantData importantData = new ImportantData(date, "vaccine");
        dbr.child(id).child("kids").child(kid).child("important").child("vaccine").child(date).setValue(vaccineData);
        dbr.child(id).child("kids").child(kid).child("important_all").child(date).setValue(importantData);
    }

    @Override
    public void onStop() {
        super.onStop();
        titleVaccine.setText("");
        noteVaccine.setText("");

        dtap.setChecked(false);
        hepa.setChecked(false);
        hepb.setChecked(false);
        hib.setChecked(false);
        influenza.setChecked(false);
        ipv.setChecked(false);
        mmr.setChecked(false);
        pcv.setChecked(false);
        rv.setChecked(false);
    }
}