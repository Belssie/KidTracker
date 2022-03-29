package com.example.kidtracker.main.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.kidtracker.R;
import com.example.kidtracker.databinding.ActivityFeedingBinding;
import com.example.kidtracker.main.Adapters.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FeedingActivity extends AppCompatActivity {
    Button buttonFeedingBack, buttonFeedingChron;
    ViewPager viewPager;
    TabLayout tabs;

    private ActivityFeedingBinding binding;
    SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);

        binding = ActivityFeedingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sectionsPagerAdapter = new SectionsPagerAdapter(getBaseContext(), getSupportFragmentManager());

        buttonFeedingBack = binding.btnFeedingBack;
        buttonFeedingChron = binding.btnChronFeeding;

        viewPager = binding.viewPagerFeeding;
        viewPager.setAdapter(sectionsPagerAdapter);

        tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        buttonFeedingBack.setOnClickListener(view -> {
            finish();
        });

        buttonFeedingChron.setOnClickListener(v -> {
            Intent intentFeedingChron = new Intent(FeedingActivity.this, FeedingChronActivity.class);
            startActivity(intentFeedingChron);
            finish();
        });

    }
}