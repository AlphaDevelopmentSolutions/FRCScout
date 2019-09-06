package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter
{

    private ArrayList<Fragment> fragmentList;
    private ArrayList<String> titleList;

    public FragmentViewPagerAdapter(@NonNull FragmentManager fragmentManager)
    {
        super(fragmentManager);

        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
    }

    /**
     * Adds a fragment to the viewpager
     * @param fragment to add
     * @param title of the fragment
     */
    public void addFragment(@NonNull Fragment fragment, @NonNull String title)
    {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return titleList.get(position);
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return fragmentList.size();
    }
}
