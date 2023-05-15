package com.team.ni_hon.lesson_fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LessonPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public LessonPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new LessonFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
