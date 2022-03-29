package com.example.kidtracker.main.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Data.DiaperData;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiaperActivity extends AppCompatActivity {
    Button saveDiaper, backDiaper, cancelDiaper, btnChronDiaper;
    RadioGroup rgDiaper;
    RadioButton rbWet, rbDirty, rbDry, rbMixed;
    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText, etNoteDiaper;
    ProgressBar pbDiaper;

    String id, kid, diaper, note,  time, strDateTime;
    int year, month, day;

    LocalDateTime localDateTime;
    LocalDate localDate;
    LocalTime localTime;
    Date date;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);
        setContentView(R.layout.activity_diaper);
        initViews();

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        date = new Date();
        strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date);

        textInputEditText.setText(strDateTime);
        diaper = "dry";
        rbDry.setChecked(true);
        rbDirty.setTypeface(Typeface.DEFAULT);
        rbDry.setTypeface(Typeface.DEFAULT_BOLD);
        rbWet.setTypeface(Typeface.DEFAULT);
        rbMixed.setTypeface(Typeface.DEFAULT);

        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);

        textInputEditText.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            Calendar currentDate = Calendar.getInstance();
            year = currentDate.get(Calendar.YEAR);
            month = currentDate.get(Calendar.MONTH);
            day = currentDate.get(Calendar.DAY_OF_MONTH);

            new SingleDateAndTimePickerDialog.Builder(DiaperActivity.this)
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

                            textInputEditText.setText(localDate.toString() + ", " + localTime.toString());
                        }
                    })
                    .display();
        });

        rgDiaper.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_wet:
                    diaper = "wet";
                    rbWet.setTypeface(Typeface.DEFAULT_BOLD);
                    rbMixed.setTypeface(Typeface.DEFAULT);
                    rbDirty.setTypeface(Typeface.DEFAULT);
                    rbDry.setTypeface(Typeface.DEFAULT);
                    break;
                case R.id.rb_dry:
                    diaper = "dry";
                    rbDirty.setTypeface(Typeface.DEFAULT);
                    rbDry.setTypeface(Typeface.DEFAULT_BOLD);
                    rbWet.setTypeface(Typeface.DEFAULT);
                    rbMixed.setTypeface(Typeface.DEFAULT);
                    break;
                case R.id.rb_dirty:
                    diaper = "dirty";
                    rbDirty.setTypeface(Typeface.DEFAULT_BOLD);
                    rbDry.setTypeface(Typeface.DEFAULT);
                    rbWet.setTypeface(Typeface.DEFAULT);
                    rbMixed.setTypeface(Typeface.DEFAULT);
                    break;
                case R.id.rb_mixed:
                    diaper = "mixed";
                    rbMixed.setTypeface(Typeface.DEFAULT_BOLD);
                    rbDirty.setTypeface(Typeface.DEFAULT);
                    rbDry.setTypeface(Typeface.DEFAULT);
                    rbWet.setTypeface(Typeface.DEFAULT);
                    break;
            }
        });

        saveDiaper.setOnClickListener(v -> {
            pbDiaper.setVisibility(View.VISIBLE);
            setSaveDiaper();
            finish();
        });

        backDiaper.setOnClickListener(v -> {
            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(homeIntent);
            finish();
        });

        cancelDiaper.setOnClickListener(v -> {
            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(homeIntent);
            finish();
        });

        btnChronDiaper.setOnClickListener(v -> {
            Intent diaperChronIntent = new Intent(getApplicationContext(), DiaperChronActivity.class);
            diaperChronIntent.putExtra("NAME", kid);
            startActivity(diaperChronIntent);
            finish();
        });
    }

    public void initViews(){
        rgDiaper = findViewById(R.id.rg_diaper);
        saveDiaper = findViewById(R.id.btn_save_diaper);
        backDiaper = findViewById(R.id.btn_back_diaper);
        cancelDiaper = findViewById(R.id.btn_cancel_diaper);
        textInputLayout = findViewById(R.id.ol_tf_diaper);
        textInputEditText = findViewById(R.id.txt_in_et_diaper);
        rbDirty = findViewById(R.id.rb_dirty);
        rbDry = findViewById(R.id.rb_dry);
        rbMixed = findViewById(R.id.rb_mixed);
        rbWet = findViewById(R.id.rb_wet);
        etNoteDiaper = findViewById(R.id.txt_in_et_note_diaper);
        pbDiaper = findViewById(R.id.pb_diaper);
        btnChronDiaper = findViewById(R.id.btn_chron_diaper);
    }

    public void setSaveDiaper(){
        time = textInputEditText.getText().toString();

        if (etNoteDiaper.getText().toString().isEmpty()){
            note = "none";
        } else {
            note = etNoteDiaper.getText().toString();
        }

        DiaperData diaperData = new DiaperData(time, diaper, note);
        dbr.child(id).child("kids").child(kid).child("diaper").child(time).setValue(diaperData);
        pbDiaper.setVisibility(View.GONE);
    }
}
