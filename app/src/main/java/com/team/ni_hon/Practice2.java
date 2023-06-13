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

    private int Level, chance;
    private TextView question, letter;
    private Button check;
    private EditText answer;
    private LottieAnimationView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPractice2Binding bind = ActivityPractice2Binding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        question = bind.question;
        letter = bind.kana;
        check = bind.checkAns;
        answer = bind.answer;
        animation = bind.animationView;

        chance = 3;

        SharedPreferences prefs = getSharedPreferences("PRACTICE", MODE_PRIVATE);
        Level = prefs.getInt("accessLevel", 1);

        initComponent();
    }

    public void initComponent() {
        question.setText(R.string.kana);
        letter.setText(getLessons().get(Level).getQuestionEN());



        check.setOnClickListener(v -> {

            if (answer.getText().toString().trim() != null) {
                if (answer.getText().toString().trim().equalsIgnoreCase(getLessons().get(Level).getQuestionES())) {
                    animation.playAnimation();

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        animation.cancelAnimation();

                        if(Level==2 || Level==4){
                            Intent intent=new Intent(this,unlock.class);
                            startActivity(intent);
                            finish();
                        }
                        Intent intent=new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }, 2000);
                }else if (chance == 0) {
                    animation.setAnimationFromUrl(getString(R.string.lottie_bad));
                    animation.playAnimation();

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        animation.cancelAnimation();

                        if(Level==2 || Level==4){
                            Intent intent=new Intent(this,unlock.class);
                            startActivity(intent);
                            finish();
                        }

                        Intent intent=new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }, 3000);
                } else {
                    Toast.makeText(this, getString(R.string.fail_tries) + (chance--), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public List<Question> getLessons() {
        List<Question> quests = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Question question = new Question();
            switch (i) {
                case 1:
                    question.setQuestionEN("え");
                    question.setQuestionES("e");
                    question.setLevel(1);
                    break;
                case 2:
                    question.setQuestionEN("こ");
                    question.setQuestionES("ko");
                    question.setLevel(2);
                    break;
                case 3:
                    question.setQuestionEN("そ");
                    question.setQuestionES("so");
                    question.setLevel(3);
                    break;
                case 4:
                    question.setQuestionEN("と");
                    question.setQuestionES("to");
                    question.setLevel(4);
                    break;
                case 5:
                    question.setQuestionEN("の");
                    question.setQuestionES("no");
                    question.setLevel(5);
                    break;
            }
            question.setIdQuestion("1B" + i);
            question.setComplete(false);
            quests.add(question);
        }

        return quests;
    }

}