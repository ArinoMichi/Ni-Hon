package com.team.ni_hon.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team.ni_hon.R;

import java.util.ArrayList;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private ArrayList<Lesson> data;

    public LessonAdapter(ArrayList<Lesson> data) {
        this.data = data;
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitulo, textSubtitulo;

        public LessonViewHolder(View itemView) {
            super(itemView);
            this.textTitulo = itemView.findViewById(R.id.titulo);
            this.textSubtitulo = itemView.findViewById(R.id.subtitulo);
        }

        public void bindLesson(Lesson lesson) {
            textTitulo.setText(lesson.getTitle());
            textSubtitulo.setText(lesson.getTitle());
        }
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_component, parent, false);
        LessonViewHolder tvh = new LessonViewHolder(itemView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = data.get(position);
        holder.bindLesson(lesson);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}