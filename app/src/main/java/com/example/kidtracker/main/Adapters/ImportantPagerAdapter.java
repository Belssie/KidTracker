package com.example.kidtracker.main.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.kidtracker.main.Fragments.GrowthFragment;
import com.example.kidtracker.main.Fragments.MedicationFragment;
import com.example.kidtracker.main.Fragments.MemoryFragment;
import com.example.kidtracker.R;
import com.example.kidtracker.main.Fragments.SymptomsFragment;
import com.example.kidtracker.main.Fragments.VaccineFragment;

public class ImportantPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.fragment_growth, R.string.fragment_memory, R.string.fragment_symptoms, R.string.fragment_medication, R.string.fragment_vaccine};
    private final Context mContext;
    public ImportantPagerAdapter(Context mContext,@NonNull FragmentManager fm) {
        super(fm);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new GrowthFragment();
                break;
            case 1:
                fragment = new MemoryFragment();
                break;
            case 2:
                fragment = new SymptomsFragment();
                break;
            case 3:
                fragment = new MedicationFragment();
                break;
            case 4:
                fragment = new VaccineFragment();
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
        return 5;
    }
}
