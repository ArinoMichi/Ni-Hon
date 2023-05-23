package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.team.ni_hon.tutorial_fragments.TutorialPagerAdapter;

public class TutorialActivity extends AppCompatActivity {

    private TutorialPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);


        //fragments
        pagerAdapter = new TutorialPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
    }
}