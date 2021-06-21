package com.example.mywork.fragment_main.fragment_mywork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerTitleStrip;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerMyworkAdapter extends FragmentStatePagerAdapter {

    public ViewPagerMyworkAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentMwToDoList();
            case 1:
                return new FragmentMwStatistic();
            default:
                return new FragmentMwToDoList();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "To-Do List";
            case 1:
                return "Statistic";
            default:
                return "To-Do List";
        }
    }
}
