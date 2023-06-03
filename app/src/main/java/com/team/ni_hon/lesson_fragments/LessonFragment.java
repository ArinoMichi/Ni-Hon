package com.team.ni_hon.lesson_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.team.ni_hon.LessonActivity;
import com.team.ni_hon.Practice1;
import com.team.ni_hon.R;
import com.team.ni_hon.main_recycler.Lesson;

public class LessonFragment extends Fragment {

    private static final String LESSON = "lesson";
    private static final String PAGE = "page";
    private int lesson;
    private int page;

    public LessonFragment() {

    }

    public static LessonFragment newInstance(int position) {
        LessonFragment fragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(PAGE);
            lesson = getArguments().getInt(LESSON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lesson, container, false);

        TextView text = view.findViewById(R.id.text);
        Button startPractice= view.findViewById(R.id.start_button);

        String stringId = "lesson_" + lesson + "_" + page;
        int resId = getResources().getIdentifier(stringId, "string", getActivity().getPackageName());
        text.setText(getString(resId));

        Intent intent = getActivity().getIntent();

        if (getArguments() != null && intent != null) {
            Lesson lesson = (Lesson) intent.getSerializableExtra("lesson");
            if (getArguments().getInt(PAGE) ==lesson.getPages()) {
                startPractice.setVisibility(View.VISIBLE);
            }
        }

        startPractice.setOnClickListener(v->{
            Intent intent1=new Intent(getContext(), Practice1.class);
            startActivity(intent1);
            getActivity().finish();
        });

        return view;
    }

}