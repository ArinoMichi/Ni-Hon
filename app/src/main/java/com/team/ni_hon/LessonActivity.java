package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.team.ni_hon.lesson_fragments.LessonPagerAdapter;
import com.team.ni_hon.main_recycler.Lesson;
import com.team.ni_hon.utils.LanguageHelper;

public class LessonActivity extends AppCompatActivity {

    private LessonPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));

        Intent intent = getIntent();
        Lesson lesson = (Lesson) intent.getSerializableExtra("lesson");

        //fragments
        pagerAdapter = new LessonPagerAdapter(this, getSupportFragmentManager(), lesson);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);

    }
}