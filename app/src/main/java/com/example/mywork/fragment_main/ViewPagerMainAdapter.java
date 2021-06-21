package com.example.mywork.fragment_main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerMainAdapter extends FragmentStatePagerAdapter {

    public ViewPagerMainAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentMyWork();
            case 1:
                return new FragmentCalendar();
            default:
                return new FragmentMyWork();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
