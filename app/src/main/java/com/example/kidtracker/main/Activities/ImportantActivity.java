package com.example.kidtracker.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.kidtracker.R;
import com.example.kidtracker.databinding.ActivityImportantBinding;
import com.example.kidtracker.main.Adapters.ImportantPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ImportantActivity extends AppCompatActivity {
    Button buttonImportantBack, buttonImportantChron;
    ViewPager viewPager;
    TabLayout tabs;

    private ActivityImportantBinding binding;
    ImportantPagerAdapter importantPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);

        binding = ActivityImportantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        importantPagerAdapter = new ImportantPagerAdapter(getBaseContext(), getSupportFragmentManager());

        buttonImportantBack = binding.btnImportantBack;
        buttonImportantChron = binding.btnChronImportant;
        viewPager = binding.viewPagerImportant;
        viewPager.setAdapter(importantPagerAdapter);
        tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        buttonImportantBack.setOnClickListener(view -> {
            finish();
        });

        buttonImportantChron.setOnClickListener(v -> {
            Intent intentImportantChron = new Intent(ImportantActivity.this, ImportantChronActivity.class);
            startActivity(intentImportantChron);
            finish();
        });
    }
}