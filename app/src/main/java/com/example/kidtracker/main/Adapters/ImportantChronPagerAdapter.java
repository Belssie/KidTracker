package com.example.kidtracker.main.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.kidtracker.main.Fragments.GrowthChron;
import com.example.kidtracker.main.Fragments.MemoryChron;
import com.example.kidtracker.main.Fragments.SymptomsChron;
import com.example.kidtracker.main.Fragments.MedicationChron;
import com.example.kidtracker.main.Fragments.VaccineChron;
import com.example.kidtracker.R;

public class ImportantChronPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.fragment_growth, R.string.fragment_memory, R.string.fragment_symptoms, R.string.fragment_medication, R.string.fragment_vaccine};
    private final Context mContext;

    public ImportantChronPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new GrowthChron();
                break;
            case 1:
                fragment = new MemoryChron();
                break;
            case 2:
                fragment = new SymptomsChron();
                break;
            case 3:
                fragment = new MedicationChron();
                break;
            case 4:
                fragment = new VaccineChron();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 5;
    }
}