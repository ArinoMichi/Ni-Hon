package com.team.ni_hon.main_recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
    private int userLevel;
    private int previousId;
    private int currentId;
    private Context context;

    public MainLessonAdapter(Context context, ArrayList<Lesson> lessons,int level) {
        this.lessons = lessons;
        this.previousId = -1;
        this.currentId = -1;
        this.context = context;
        this.userLevel=level;
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

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.button.setText(Integer.toString(lesson.getId()));
        holder.lessonTitleText.setText(lessons.get(position).getTitle());
        holder.popupText.setText(lessons.get(position).getPopupText());

        setTanukiByLevel(holder.image);

        if(position+1>userLevel){
            holder.button.setBackground(holder.button.getResources().getDrawable(R.mipmap.img_locked));
            holder.button.setEnabled(false);
            holder.button.setText(null);
        }else{
            holder.button.setBackground(holder.button.getResources().getDrawable(R.drawable.button));
            holder.button.setEnabled(true);
        }

        if (position != currentId){
            holder.popup.setVisibility(View.INVISIBLE);
            holder.image.setVisibility(View.INVISIBLE);
        }
        if (position == currentId){
            holder.image.setVisibility(View.VISIBLE);
            MainActivity.getRecyclerView().smoothScrollToPosition(currentId);
            holder.popup.setVisibility(View.VISIBLE);
            holder.popupButton.setVisibility(View.VISIBLE);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.jump);
            holder.image.startAnimation(animation);

            holder.image.setOnClickListener(v->{
                Log.d("TAG","He detectado un toque");
                Animation animations = AnimationUtils.loadAnimation(context, R.anim.rotate);
                holder.image.startAnimation(animations);
            });
        }
    }

    private void setTanukiByLevel(ImageView tanuki) {
        switch(userLevel){
            case 1:
                tanuki.setImageResource(R.mipmap.img_locked);
                break;
            case 2:
                tanuki.setImageResource(R.drawable.moon_icon);
                break;
            case 3:
                tanuki.setImageResource(R.drawable.tanuki);
                break;
            case 4:
                tanuki.setImageResource(R.drawable.user_icon_default);
                break;
            case 5:
                tanuki.setImageResource(R.drawable.user_icon3);
                break;
            default:
                tanuki.setImageResource(R.drawable.tanuki);
                break;

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
        ImageView image;
        TextView popupText, lessonTitleText;

        public LessonViewHolder(View itemView, MyClickListener listener) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
            popupButton = itemView.findViewById(R.id.startLessonButton);
            image = itemView.findViewById(R.id.tanuki);
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