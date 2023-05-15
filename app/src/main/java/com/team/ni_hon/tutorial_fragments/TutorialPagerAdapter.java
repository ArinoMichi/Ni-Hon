package com.team.ni_hon.tutorial_fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TutorialPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public TutorialPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TutorialFragment1();
            case 1:
                return new TutorialFragment2();
            case 2:
                return new TutorialFragment3();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

