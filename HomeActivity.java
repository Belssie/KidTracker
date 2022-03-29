package com.example.kidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;


import com.google.android.material.imageview.ShapeableImageView;



public class HomeActivity extends AppCompatActivity {


    ShapeableImageView iv_profilepic;
    TextView tvHome;

    DataBaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        iv_profilepic = findViewById(R.id.iv_profile);
        tvHome = findViewById(R.id.tv_home);

        dbh = new DataBaseHelper(HomeActivity.this);

        Intent intentz = getIntent();
        Bundle b = intentz.getExtras();

        if (b!=null){
            String passed_name = (String) b.get("Name");

            tvHome.setText(passed_name);

            byte[] byteArray = getIntent().getByteArrayExtra("Image");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            int width = 400;
            int height = 400;
            bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
            iv_profilepic.setImageBitmap(bmp);

        }

        iv_profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iProfile = new Intent (getApplicationContext(), ProfileActivity.class);
                startActivity(iProfile);
            }
        });
    }
}