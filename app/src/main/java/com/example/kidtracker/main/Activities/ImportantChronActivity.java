package com.example.kidtracker.main.Activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.kidtracker.R;
import com.example.kidtracker.databinding.ActivityImportantChronBinding;
import com.example.kidtracker.main.Adapters.ImportantChronPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ImportantChronActivity extends AppCompatActivity {
    Button buttonImportantChronBack;
    ViewPager viewPager;
    TabLayout tabs;

    private ActivityImportantChronBinding binding;
    ImportantChronPagerAdapter importantChronPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);

        binding = ActivityImportantChronBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        importantChronPagerAdapter = new ImportantChronPagerAdapter(getBaseContext(), getSupportFragmentManager());

        buttonImportantChronBack = binding.btnImportantChronBack;

        viewPager = binding.viewPagerImportantChron;
        viewPager.setAdapter(importantChronPagerAdapter);

        tabs = binding.tabsImportantChron;
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        buttonImportantChronBack.setOnClickListener(view -> {
            finish();
        });

    }
}