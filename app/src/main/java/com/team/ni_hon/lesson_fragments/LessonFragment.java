package com.team.ni_hon.lesson_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.team.ni_hon.LessonActivity;
import com.team.ni_hon.R;

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
        String textId = "lesson_" + lesson + "_" + page;
        int textResId = getResources().getIdentifier(textId, "string", getActivity().getPackageName());
        text.setText(getString(textResId));

        ImageView image = view.findViewById(R.id.image);
        String imageId = "lesson_" + lesson + "_" + page;
        int imageResId = getResources().getIdentifier(imageId, "drawable", getActivity().getPackageName());
        Glide.with(this).load(imageResId).into(image);

        return view;
    }

}