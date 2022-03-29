package com.example.kidtracker.main.Adapters;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.kidtracker.main.Fragments.AdaptedMilkFragment;
import com.example.kidtracker.main.Fragments.BreastMilkFragment;
import com.example.kidtracker.main.Fragments.ExpressedMilkFragment;
import com.example.kidtracker.main.Fragments.FoodFragment;
import com.example.kidtracker.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.breastmilk_fragment, R.string.expressed_fragment, R.string.adaptedmilk_fragment, R.string.food_fragment};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new BreastMilkFragment();
                break;
            case 1:
                fragment = new ExpressedMilkFragment();
                break;
            case 2:
                fragment = new AdaptedMilkFragment();
                break;
            case 3:
                fragment = new FoodFragment();
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
        return 4;
    }
}