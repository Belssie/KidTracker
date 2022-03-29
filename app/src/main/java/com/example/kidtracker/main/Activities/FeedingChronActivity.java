package com.example.kidtracker.main.Activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.kidtracker.R;
import com.example.kidtracker.databinding.ActivityFeedingChronBinding;
import com.example.kidtracker.main.Adapters.FeedingChronPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FeedingChronActivity extends AppCompatActivity {
    Button buttonFeedingChronBack;
    ViewPager viewPager;
    TabLayout tabs;

    private ActivityFeedingChronBinding binding;
    FeedingChronPagerAdapter feedingChronPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Hardtest);

        binding = ActivityFeedingChronBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        feedingChronPagerAdapter = new FeedingChronPagerAdapter(getBaseContext(), getSupportFragmentManager());

        buttonFeedingChronBack = binding.btnFeedingChronBack;

        viewPager = binding.viewPagerFeedingChron;
        viewPager.setAdapter(feedingChronPagerAdapter);

        tabs = binding.tabsFeedingChron;
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);


        buttonFeedingChronBack.setOnClickListener(view -> {
            finish();
        });

    }
}