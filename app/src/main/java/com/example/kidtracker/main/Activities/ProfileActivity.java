package com.example.kidtracker.main.Activities;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.kidtracker.main.Data.KidsData;
import com.example.kidtracker.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Calendar;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 100;
    ImageView kidPic;
    TextInputEditText kidName, birthDate;
    Button saveProfile, btnCancel;
    RadioButton rbm, rbf;
    RadioGroup rg_gender;
    ProgressBar pbProfile;
    TextView pic;
    ConstraintLayout clProfile;

    Uri imageUri;
    int year, month, day;
    String id, gender, name, downloadUrl, bDate;

    DatabaseReference dbr, dbh, db;
    FirebaseAuth fbAuth;
    StorageReference stRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);
        setContentView(R.layout.activity_profile);
        loadPopUp();
        initViews();

        db = FirebaseDatabase.getInstance().getReference();
        dbr = FirebaseDatabase.getInstance().getReference("accounts");
        dbr.keepSynced(true);
        stRef = FirebaseStorage.getInstance().getReference("images");

        fbAuth = FirebaseAuth.getInstance();
        id = fbAuth.getUid();
        if (id != null) {
            dbh = dbr.child(id);
        }

        kidName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        getFromFirebase();

        kidPic.setOnClickListener(view -> openGallery());

        birthDate.setOnClickListener(v -> {
                Calendar currentDate = Calendar.getInstance();
                year = currentDate.get(Calendar.YEAR);
                month = currentDate.get(Calendar.MONTH);
                day = currentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(ProfileActivity.this, R.style.SpinnerDatePickerStyle, (datepicker, selectedyear, selectedmonth, selectedday) -> {
                    year = selectedyear;
                    month = selectedmonth;
                    day = selectedday;
                    birthDate.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
                }, year, month, day);

                Calendar minCal = Calendar.getInstance();
                minCal.set(Calendar.YEAR, minCal.get(Calendar.YEAR) - 7);
                Calendar maxCal = Calendar.getInstance();
                maxCal.set(Calendar.YEAR, maxCal.get(Calendar.YEAR));
                datePicker.getDatePicker().setMaxDate(maxCal.getTimeInMillis());
                datePicker.getDatePicker().setMinDate(minCal.getTimeInMillis());
                datePicker.setTitle("Please select date");
                datePicker.show();
        });

        rg_gender.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_m:
                    gender = "male";
                    break;
                case R.id.rb_f:
                    gender = "female";
                    break;
            }
        });

        saveProfile.setOnClickListener(v -> saveProfile());

        btnCancel.setOnClickListener(v -> finish());
    }

    public void loadPopUp(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setLayout((int) (width * .87), (int) (height * .85));
    }

    public void initViews(){
        pic = findViewById(R.id.tv_pic_change);
        kidPic = findViewById(R.id.iv_profile_pic);
        kidName = findViewById(R.id.iet_name);
        birthDate = findViewById(R.id.iet_bdate);
        saveProfile = findViewById(R.id.btn_profile_save);
        btnCancel = findViewById(R.id.btn_profile_cancel);
        rg_gender = findViewById(R.id.rg_gender);
        rbm = findViewById(R.id.rb_m);
        rbf = findViewById(R.id.rb_f);
        pbProfile = findViewById(R.id.pb_profile);
        clProfile = findViewById(R.id.cl_profile_pic);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            pbProfile.setVisibility(View.VISIBLE);
            pic.setVisibility(View.GONE);
            imageUri = data.getData();
            uploadFile();
            Glide.with(getApplicationContext()).load(imageUri).transform(new CircleCrop()).into(kidPic);
        }
    }

    private void uploadFile() {
        if (imageUri != null) {
            StorageReference fileReference = stRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(uri -> downloadUrl = uri.toString());
                Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                pbProfile.setVisibility(View.GONE);
            });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    public void saveProfile() {
        name = Objects.requireNonNull(kidName.getText()).toString();

        ValueEventListener kidListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(kidName.isFocusable() && snapshot.exists()){
                    kidName.setError("This name already has a profile");
                    kidName.requestFocus();
                } else {
                    kidName.setKeyListener(null);
                    kidName.setFocusable(false);

                    bDate = birthDate.getText().toString();

                    if (downloadUrl != null) {
                        KidsData kidsData = new KidsData(id, name, gender, downloadUrl, bDate);
                        dbr.child(id).child("kids").child(name).setValue(kidsData);
                    } else {
                        dbr.child(id).child("kids").child(name).child("name").setValue(name);
                        dbr.child(id).child("kids").child(name).child("gender").setValue(gender);
                        dbr.child(id).child("kids").child(name).child("birthDate").setValue(bDate);
                    }
                    dbr.child(id).child("last").setValue(name);

                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; db.child("accounts").child(id).child("kids").child(name).addListenerForSingleValueEvent(kidListener);
    }

    public void getFromFirebase(){
        ValueEventListener kidNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String kid = snapshot.getValue(String.class);
                if (kid == null){
                    kidName.setFocusable(true);
                    kidName.setText("");
                    birthDate.setText("");
                    Glide.with(getApplicationContext()).load(R.drawable.change_pic_popup).transform(new CircleCrop()).into(kidPic);
                    rbm.setChecked(false);
                    rbf.setChecked(false);
                } else if (kid.equals("none")){
                    kidName.setFocusable(true);
                    kidName.setText("");
                    birthDate.setText("");
                    Glide.with(getApplicationContext()).load(R.drawable.change_pic_popup).transform(new CircleCrop()).into(kidPic);
                    rbm.setChecked(false);
                    rbf.setChecked(false);
                } else {
                    ValueEventListener kidListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            KidsData kidsDataB = snapshot.getValue(KidsData.class);
                            kidName.setFocusable(false);
                           /* birthDate.setFocusable(false);
                            rbm.setFocusable(false);
                            rbf.setFocusable(false);*/
                            if (kidsDataB == null) {
                                kidName.setFocusable(true);
                                kidName.setText("");
                                birthDate.setText("");
                                Glide.with(getApplicationContext()).load(R.drawable.change_pic_popup).transform(new CircleCrop()).into(kidPic);
                                rbm.setChecked(false);
                                rbf.setChecked(false);
                            } else if (kidsDataB.getName()!=null) {
                                kidName.setText(kidsDataB.getName());
                                birthDate.setText(kidsDataB.getBirthDate());
                                Glide.with(getApplicationContext()).load(kidsDataB.getPicture()).transform(new CircleCrop()).into(kidPic);
                                if (kidsDataB.getGender() == null) {
                                    rbm.setChecked(false);
                                    rbf.setChecked(false);
                                } else if (kidsDataB.getGender().equals("female")) {
                                    rbf.setChecked(true);
                                    rbm.setChecked(false);
                                } else {
                                    rbf.setChecked(false);
                                    rbm.setChecked(true);
                                }
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
    }
}
