package com.team.ni_hon.recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team.ni_hon.MainActivity;
import com.team.ni_hon.R;

import java.util.ArrayList;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> implements View.OnClickListener {

    private final ArrayList<Lesson> lessons;
    private View.OnClickListener listener;

    public LessonAdapter(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_component, parent, false);
        itemView.setOnClickListener(this);
        LessonViewHolder tvh = new LessonViewHolder(itemView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.bindLesson(lesson);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {

        private final Button button;

        public LessonViewHolder(View itemView) {
            super(itemView);
            TextView textTitle = itemView.findViewById(R.id.title);
            this.button = itemView.findViewById(R.id.button);
        }

        public void bindLesson(Lesson lesson) {
            button.setText(lesson.getTitle());
        }
    }


}