package com.team.ni_hon.main_recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.team.ni_hon.LessonActivity;
import com.team.ni_hon.MainActivity;
import com.team.ni_hon.R;

import java.util.ArrayList;

public class MainLessonAdapter extends RecyclerView.Adapter<MainLessonAdapter.LessonViewHolder> {

    private final ArrayList<Lesson> lessons;

    private int previousId;
    private int currentId;
    private Context context;

    public MainLessonAdapter(Context context, ArrayList<Lesson> lessons) {
        this.lessons = lessons;
        this.previousId = -1;
        this.currentId = -1;
        this.context = context;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_component, parent, false);
        LessonViewHolder lvh = new LessonViewHolder(itemView, new MyClickListener(){
            public void onLessonClick(int p) {
                previousId = currentId;
                currentId = lessons.get(p).getId() - 1;
                MainActivity.getRecyclerView().getAdapter().notifyItemChanged(previousId);
                MainActivity.getRecyclerView().getAdapter().notifyItemChanged(currentId);
            }
            public void onStartClick(int p) {
                Intent intent = new Intent(context, LessonActivity.class);
                intent.putExtra("lesson", lessons.get(p));
                context.startActivity(intent);
            }
        });
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.button.setText(Integer.toString(lesson.getId()));
        holder.lessonTitleText.setText(lessons.get(position).getTitle());
        holder.popupText.setText(lessons.get(position).getPopupText());
        if (position == previousId){
            holder.popup.setVisibility(View.INVISIBLE);
        }
        if (position == currentId){
            MainActivity.getRecyclerView().smoothScrollToPosition(currentId);
            holder.popup.setVisibility(View.VISIBLE);
            holder.popupButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }


    public static class LessonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MyClickListener listener;
        Button button, popupButton;
        ConstraintLayout popup;
        TextView popupText, lessonTitleText;

        public LessonViewHolder(View itemView, MyClickListener listener) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
            popupButton = itemView.findViewById(R.id.startLessonButton);
            popup = itemView.findViewById(R.id.popup);
            lessonTitleText = itemView.findViewById(R.id.lessonTitleText);
            popupText = itemView.findViewById(R.id.popupText);

            this.listener = listener;

            button.setOnClickListener(this);
            popupButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button) {
                listener.onLessonClick(this.getLayoutPosition());
            }
            if (v.getId() == R.id.startLessonButton) {
                listener.onStartClick(this.getLayoutPosition());
            }
        }
    }

    public interface MyClickListener {
        void onLessonClick(int p);
        void onStartClick(int p);
    }


}