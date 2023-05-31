package com.team.ni_hon.lesson_fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.team.ni_hon.main_recycler.Lesson;

public class LessonPagerAdapter extends FragmentPagerAdapter {

    Context context;
    Lesson lesson;

    public LessonPagerAdapter(Context context, @NonNull FragmentManager fm, Lesson lesson) {
        super(fm);
        this.context = context;
        this.lesson = lesson;
    }

    @NonNull
    public Fragment getItem(int position) {
        LessonFragment lessonFragment = new LessonFragment();

        Bundle args = new Bundle();
        args.putInt("lesson", lesson.getId());
        args.putInt("page", position+1);
        lessonFragment.setArguments(args);
        return lessonFragment;
    }

    @Override
    public int getCount() {
        return lesson.getPages();
    }

}
