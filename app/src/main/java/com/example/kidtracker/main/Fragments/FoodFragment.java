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
import com.example.kidtracker.main.Data.FeedingData;
import com.example.kidtracker.main.Data.FoodData;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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

public class FoodFragment extends Fragment {
    TextInputEditText etTimeFood, etAmountFood, etTypeFood, etNoteFood;
    Button saveFood, cancelFood;
    ChipGroup chipGroupFood;
    Chip meat, veggies, fruit, cereal, water, juice, chocolate, milk, vitamins;
    ProgressBar pbFood;

    String id, kid, date, amount, type, note, food, strDateTime;

    DatabaseReference dbr;
    FirebaseAuth fbAuth;

    Date date1;
    LocalDate localDate;
    LocalTime localTime;
    LocalDateTime localDateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);
        etTimeFood = v.findViewById(R.id.iet_ate_at_food);
        etAmountFood = v.findViewById(R.id.iet_amount_food);
        etTypeFood = v.findViewById(R.id.iet_type_food);
        etNoteFood = v.findViewById(R.id.iet_note_food);
        saveFood = v.findViewById(R.id.btn_save_food);
        cancelFood = v.findViewById(R.id.btn_cancel_bm);
        chipGroupFood = v.findViewById(R.id.cg_food);
        meat = v.findViewById(R.id.chip_meat);
        veggies = v.findViewById(R.id.chip_veggies);
        fruit = v.findViewById(R.id.chip_fruit);
        cereal = v.findViewById(R.id.chip_cereal);
        water = v.findViewById(R.id.chip_water);
        juice = v.findViewById(R.id.chip_juice);
        chocolate = v.findViewById(R.id.chip_chocolate);
        milk = v.findViewById(R.id.chip_milk);
        vitamins = v.findViewById(R.id.chip_vitamins);
        pbFood = v.findViewById(R.id.pb_food);
        etTypeFood.setFocusable(false);

        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();

        food = "";
        date1 = new Date();
        strDateTime = new SimpleDateFormat("yyyy-MM-dd, HH:mm").format(date1);
        etTimeFood.setText(strDateTime);

        meat.setOnClickListener(v1 -> {
            if (meat.isChecked()) {
                if (food.isEmpty()){
                    food += meat.getText().toString();
                } else {
                    food += ", " + meat.getText().toString();
                }
                etTypeFood.setText(food);
            } else if (!meat.isChecked()) {
                food = food.replace(", " + meat.getText().toString(), "");
                food = food.replace(meat.getText().toString(), "");
                food = food.replace(meat.getText().toString() + ", ", "");
                etTypeFood.setText(food);
            }
        });

        veggies.setOnClickListener(v12 -> {
            if (veggies.isChecked() ) {
                if (food.isEmpty()) {
                    food += veggies.getText().toString();
                } else {
                    food += ", " + veggies.getText().toString();
                }
                etTypeFood.setText(food);
            } else if (!veggies.isChecked()) {
                food = food.replace(", " + veggies.getText().toString(), "");
                food = food.replace(veggies.getText().toString(), "");
                food = food.replace(veggies.getText().toString() + ", ", "");
                etTypeFood.setText(food);
            }
        });

        fruit.setOnClickListener(v13 -> {
            if (fruit.isChecked() ) {
                if (food.isEmpty()) {
                    food += fruit.getText().toString();
                } else {
                    food += ", " + fruit.getText().toString();
                }
                etTypeFood.setText(food);
            } else if (!fruit.isChecked()) {
                food = food.replace(", " + fruit.getText().toString(), "");
                food = food.replace(fruit.getText().toString(), "");
                food = food.replace(fruit.getText().toString() + ", ", "");
                etTypeFood.setText(food);
            }
        });

        cereal.setOnClickListener(v14 -> {
            if (cereal.isChecked() ) {
                if (food.isEmpty()) {
                    food += cereal.getText().toString();
                } else {
                    food += ", " + cereal.getText().toString();
                }
                etTypeFood.setText(food);
            } else if (!cereal.isChecked()) {
                food = food.replace(", " + cereal.getText().toString(), "");
                food = food.replace(cereal.getText().toString(), "");
                food = food.replace(cereal.getText().toString() + ", ", "");
                etTypeFood.setText(food);
            }
        });

        water.setOnClickListener(v15 -> {
            if (water.isChecked() ) {
                if (food.isEmpty()) {
                    food += water.getText().toString();
                } else {
                    food += ", " + water.getText().toString();
                }
                etTypeFood.setText(food);
            } else if (!water.isChecked()) {
                food = food.replace(", " + water.getText().toString(), "");
                food = food.replace(water.getText().toString(), "");
                food = food.replace(water.getText().toString() + ", ", "");
                etTypeFood.setText(food);
            }
        });

        juice.setOnClickListener(v16 -> {
            if (juice.isChecked() ) {
                if (food.isEmpty()) {
                    food += juice.getText().toString();
                } else {
                    food += ", " + juice.getText().toString();
                }
                etTypeFood.setText(food);
            } else if (!juice.isChecked()) {
                food = food.replace(", " + juice.getText().toString(), "");
                food = food.replace(juice.getText().toString(), "");
                food = food.replace(juice.getText().toString() + ", ", "");
                etTypeFood.setText(food);
            }
        });

        chocolate.setOnClickListener(v17 -> {
            if (chocolate.isChecked() ) {
                if (food.isEmpty()) {
                    food += chocolate.getText().toString();
                } else {
                    food += ", " + chocolate.getText().toString();
                }
                etTypeFood.setText(food);
            } else if (!chocolate.isChecked()) {
                food = food.replace(", " + chocolate.getText().toString(), "");
                food = food.replace(chocolate.getText().toString(), "");
                food = food.replace(chocolate.getText().toString() + ", ", "");
                etTypeFood.setText(food);
            }
        });

        milk.setOnClickListener(v18 -> {
            if (milk.isChecked() ) {
                if (food.isEmpty()) {
                    food += milk.getText().toString();
                } else {
                    food += ", " + milk.getText().toString();
                }
                etTypeFood.setText(food);
            } else if (!milk.isChecked()) {
                food = food.replace(", " + milk.getText().toString(), "");
                food = food.replace(milk.getText().toString(), "");
                food = food.replace(milk.getText().toString() + ", ", "");
                etTypeFood.setText(food);
            }
        });

        vitamins.setOnClickListener(v19 -> {
            if (vitamins.isChecked() ) {
                if (food.isEmpty()) {
                    food += vitamins.getText().toString();
                } else {
                    food += ", " + vitamins.getText().toString();
                }
                etTypeFood.setText(food);
            } else if (!vitamins.isChecked()){
                food = food.replace(", " + vitamins.getText().toString(), "");
                food = food.replace(vitamins.getText().toString(), "");
                food = food.replace(vitamins.getText().toString() + ", ", "");
                etTypeFood.setText(food);
            }
        });

        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kid = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; dbr.child(id).child("last").addValueEventListener(kidNameListener);

        etTimeFood.setOnClickListener(v111 -> new SingleDateAndTimePickerDialog.Builder(getContext())
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

                        etTimeFood.setText(localDate.toString() + ", " + localTime.toString());

                    }
                })
                .display());

        saveFood.setOnClickListener(v112 -> {
            if(!etTypeFood.getText().toString().isEmpty() && !etAmountFood.getText().toString().isEmpty()) {
                setSaveFood();
                Intent intentHome = new Intent(getContext(), HomeActivity.class);
                startActivity(intentHome);
            } else if (etTypeFood.getText().toString().isEmpty()){
                etTypeFood.requestFocus();
                etTypeFood.setError("Add type of food");
            } else if (etAmountFood.getText().toString().isEmpty()){
                etAmountFood.requestFocus();
                etAmountFood.setError("Add amount");
            }
        });

        cancelFood.setOnClickListener(v113 -> {
            Intent intentHome = new Intent(getContext(), HomeActivity.class);
            startActivity(intentHome);
        });

        onStop();
        onDestroyView();

        return v;
    }

    public void setSaveFood(){
        date = etTimeFood.getText().toString();
        amount = etAmountFood.getText().toString();
        type = etTypeFood.getText().toString();

        if(etNoteFood.getText().toString().isEmpty()) {
            note = "none";
        } else {
            note = etNoteFood.getText().toString();
        }

        FoodData foodData = new FoodData(date, amount, type, note);
        FeedingData feedingData = new FeedingData(date, "food");
        dbr.child(id).child("kids").child(kid).child("feeding").child("food").child(date).setValue(foodData);
        dbr.child(id).child("kids").child(kid).child("feeding_all").child(date).setValue(feedingData);
    }

    @Override
    public void onStop() {
        super.onStop();
        etAmountFood.setText("");
        etTypeFood.setText("");
        etNoteFood.setText("");

        meat.setChecked(false);
        veggies.setChecked(false);
        fruit.setChecked(false);
        cereal.setChecked(false);
        water.setChecked(false);
        juice.setChecked(false);
        chocolate.setChecked(false);
        milk.setChecked(false);
        vitamins.setChecked(false);
    }
}