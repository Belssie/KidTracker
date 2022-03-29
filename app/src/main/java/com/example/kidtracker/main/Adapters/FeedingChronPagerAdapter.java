package com.example.kidtracker.main.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.kidtracker.main.Fragments.AdaptedMilkChron;
import com.example.kidtracker.main.Fragments.BreastMilkChron;
import com.example.kidtracker.main.Fragments.ExpressedMilkChron;
import com.example.kidtracker.main.Fragments.FoodChronFragment;
import com.example.kidtracker.R;

public class FeedingChronPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.breastmilk_fragment, R.string.expressed_fragment, R.string.adaptedmilk_fragment, R.string.food_fragment};
    private final Context mContext;

    public FeedingChronPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new BreastMilkChron();
                break;
            case 1:
                fragment = new ExpressedMilkChron();
                break;
            case 2:
                fragment = new AdaptedMilkChron();
                break;
            case 3:
                fragment = new FoodChronFragment();
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


