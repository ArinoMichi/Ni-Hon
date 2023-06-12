package com.team.ni_hon.lesson_fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.team.ni_hon.Practice1;
import com.team.ni_hon.R;
import com.team.ni_hon.main_recycler.Lesson;

public class LessonFragment extends Fragment {

    private static final String LESSON = "lesson";
    private static final String PAGE = "page";
    private int lesson;
    private int page;

    public LessonFragment() { }

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

        if(getNightMode()){
            text.setTextColor(getResources().getColor(R.color.white));
        }

        ImageView imageArrow = view.findViewById(R.id.imageArrow);

        Button startPractice= view.findViewById(R.id.start_button);

        Intent intent = getActivity().getIntent();

        if (getArguments() != null && intent != null) {
            Lesson lesson = (Lesson) intent.getSerializableExtra("lesson");
            if (getArguments().getInt(PAGE) ==lesson.getPages()) {
                startPractice.setVisibility(View.VISIBLE);
                imageArrow.setVisibility(View.INVISIBLE);
            }
        }

        startPractice.setOnClickListener(v->{
            SharedPreferences pref=getActivity().getSharedPreferences("PRACTICE",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("cnt",0);
            editor.putInt("accessLevel",lesson);
            editor.apply();

            Intent intent1=new Intent(getActivity(), Practice1.class);
            startActivity(intent1);
            getActivity().finish();
        });

        return view;
    }

    public boolean getNightMode(){
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Modo nocturno ya activado
                return true;
            case Configuration.UI_MODE_NIGHT_NO:
                // Modo nocturno no activado.
                return false;
        }
        return false;
    }

}