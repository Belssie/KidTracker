package com.example.kidtracker;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 100;
    ShapeableImageView kid_pic;
    EditText kidName;
    CheckBox cbm, cbf;
    Button saveProfile;
    Uri imageUri;
    TextView tv_bd;
    RadioButton rbm, rbf;
    RadioGroup rg_gender;

    String gender;



    DataBaseHelper dbh;
    private Bitmap selectedImage;

    KidsData kidsData = new KidsData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        kid_pic = findViewById(R.id.iv_profile_pic);
        kidName = findViewById(R.id.et_kid_name);
        cbm = findViewById(R.id.cb_m);
        cbf = findViewById(R.id.cb_f);
        saveProfile = findViewById(R.id.btn_profile_save);

        rbm = findViewById(R.id.rb_m);
        rbf = findViewById(R.id.rb_f);
        rg_gender = findViewById(R.id.rg_gender);

        tv_bd = findViewById(R.id.tv_birth_date);

        dbh = new DataBaseHelper(ProfileActivity.this);


        kid_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();

            }
        });



        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            imageUri = data.getData();
            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);

                selectedImage = BitmapFactory.decodeStream(imageStream);
                int width = 400;
                int height = 400;
                selectedImage = Bitmap.createScaledBitmap(selectedImage, width, height, true);
                kid_pic.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveProfile(){
        String name = kidName.getText().toString();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_m:
                        gender = "female";
                        break;
                    case R.id.rb_f:
                        gender = "male";
                        break;
                }
            }
        });

        kidsData.setName(name);
        kidsData.setBirthDate(1);
        kidsData.setAge(0);
        kidsData.setGender(gender);
        kidsData.setPicture(byteArray);

        dbh.addNewKid(kidsData);

        Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();




        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("Name", name);
        intent.putExtra("Image", byteArray);
        startActivity(intent);
    }


}
