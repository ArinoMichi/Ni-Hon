package com.team.ni_hon.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.team.ni_hon.MainActivity;
import com.team.ni_hon.R;

import java.util.ArrayList;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<Lesson> lessons;

    public LessonAdapter(Context context, ArrayList<Lesson> lessons) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.from(parent.getContext()).inflate(R.layout.item_component, parent, false);
        LessonViewHolder lvh = new LessonViewHolder(itemView, new MyClickListener(){
            @Override
            public void onClick(int p) {
                itemView.findViewById(R.id.popup).setVisibility(View.INVISIBLE);
                MainActivity.getRecyclerView().smoothScrollToPosition(lessons.get(p).getId());
                itemView.findViewById(R.id.popup).setVisibility(View.VISIBLE);
                // Toast.makeText(parent.getContext(), "Pulsado " + lessons.get(p).getId(), Toast.LENGTH_LONG).show();
            }
        });
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.button.setText(Integer.toString(lesson.getId()));
        // holder.bindLesson(lesson);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }


    public static class LessonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MyClickListener listener;
        Button button;
        ConstraintLayout popup;

        public LessonViewHolder(View itemView, MyClickListener listener) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
            popup = itemView.findViewById(R.id.popup);

            this.listener = listener;

            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button:
                    listener.onClick(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface MyClickListener {
        void onClick(int p);
    }


}