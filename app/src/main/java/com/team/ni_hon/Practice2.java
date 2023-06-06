package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.team.ni_hon.databinding.ActivityPractice2Binding;
import com.team.ni_hon.local_data_base.database_models.Question;

import java.util.ArrayList;
import java.util.List;

public class Practice2 extends AppCompatActivity {

    private int Level;
    private TextView question,letter;
    private Button check;
    private EditText answer;
    private LottieAnimationView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPractice2Binding bind=ActivityPractice2Binding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        question=bind.question;
        letter=bind.kana;
        check=bind.checkAns;
        answer=bind.answer;
        animation=bind.animationView;

        SharedPreferences prefs=getSharedPreferences("PRACTICE",MODE_PRIVATE);
        Level = prefs.getInt("accessLevel", 1);

        initComponent();
    }

    public void initComponent(){
        question.setText("Write the letter for the next kana:");
        letter.setText(getLessons().get(Level).getQuestionEN());

        check.setOnClickListener(v->{
            if(answer.getText().toString().trim()!=null){
                if(answer.getText().toString().trim().equalsIgnoreCase("e")){
                    animation.playAnimation();

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        animation.cancelAnimation();
                        Toast.makeText(this, "Nice!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }, 3000);
                }else{
                    Toast.makeText(this, "Ups! incorrect.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public List<Question> getLessons(){
        List<Question> quests=new ArrayList<>();
        for(int i=0;i<6;i++) {
            Question question = new Question();
            question.setQuestionEN("え");
            question.setIdQuestion("1"+i);
            question.setLevel(1);
            question.setComplete(false);
            quests.add(question);
        }

        return quests;
    }

}