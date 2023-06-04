package com.team.ni_hon;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.team.ni_hon.databinding.ActivityPractice1Binding;
import com.team.ni_hon.local_data_base.OptionsSQLiteHelper;
import com.team.ni_hon.local_data_base.QuestionSQLiteHelper;
import com.team.ni_hon.local_data_base.database_models.Option;
import com.team.ni_hon.local_data_base.database_models.Question;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Practice1 extends NiHonActivity {

    private final String TAG = "TYPE01_PRACTICE";
    private TextView question, op1, op2, op3, op4;
    private LottieAnimationView animationView;
    private List<Question> listQuest;
    private List<Option> listOption;
    private FirebaseAuth mAuth;
    private FirebaseFirestore userDataBase;
    private CollectionReference userCollRef;
    private QuestionSQLiteHelper questionSQLiteHelper;
    private OptionsSQLiteHelper optionsSQLiteHelper;
    private int retries, Level,userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPractice1Binding bind = ActivityPractice1Binding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        question = bind.question;
        op1 = bind.an1;
        op2 = bind.an2;
        op3 = bind.an3;
        op4 = bind.an4;
        animationView = bind.animationView;

        SharedPreferences prefs=getSharedPreferences("PRACTICE",MODE_PRIVATE);
        Level = prefs.getInt("accessLevel", 1);

        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        userLevel = pref.getInt("level", 1);

        userDataBase = FirebaseFirestore.getInstance();
        userCollRef = userDataBase.collection("users");
        mAuth = FirebaseAuth.getInstance();

        questionSQLiteHelper = new QuestionSQLiteHelper(this);
        optionsSQLiteHelper = new OptionsSQLiteHelper(this);

        if (Level >= userLevel)
            loadQuestionList();
        else
            showAlreadyCompleteLesson();
    }

    public int getPosition() {
        SharedPreferences pref = getSharedPreferences("PRACTICE", MODE_PRIVATE);
        return pref.getInt("cnt", 0);
    }

    private void loadQuestionList() {
        new Thread(() -> {
            listQuest = getQuestionList();
            runOnUiThread(() -> {
                if (listQuest != null && !listQuest.isEmpty()) {
                    initComponent();
                } else {
                    Toast.makeText(this, R.string.dialogErrorTitle, Toast.LENGTH_SHORT).show();
                    remove();
                }
            });
        }).start();
    }

    private void initComponent() {

        if(getPosition() < listQuest.size()) {

            if (!listQuest.get(getPosition()).isComplete()) {
                SharedPreferences pref = getSharedPreferences("Config", MODE_PRIVATE);

                switch (pref.getString("language", null)) {
                    case "es":
                        question.setText(listQuest.get(getPosition()).getQuestionES());
                        break;
                    case "zh":
                        question.setText(listQuest.get(getPosition()).getQuestionCH());
                        break;
                    default:
                        question.setText(listQuest.get(getPosition()).getQuestionEN());
                        break;
                }

                loadOptions(listQuest.get(getPosition()).getIdQuestion());

            } else {
                for (int i = 0; i < listQuest.size(); i++) {
                    if (!listQuest.get(i).isComplete()) {
                        setCnt(i);
                        break;
                    }
                }

                SharedPreferences pref = getSharedPreferences("Config", MODE_PRIVATE);

                switch (pref.getString("language", null)) {
                    case "es":
                        question.setText(listQuest.get(getPosition()).getQuestionES());
                        break;
                    case "zh":
                        question.setText(listQuest.get(getPosition()).getQuestionCH());
                        break;
                    default:
                        question.setText(listQuest.get(getPosition()).getQuestionEN());
                        break;
                }

                loadOptions(listQuest.get(getPosition()).getIdQuestion());
            }
        } else {
            if (userLevel <= Level)
                LevelUp();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void loadOptions(String id) {
        new Thread(() -> {
            listOption = optionsSQLiteHelper.getOptionsByQuestionId(id);
            runOnUiThread(() -> {
                try {
                    op1.setText(listOption.get(0).getOption());
                    op2.setText(listOption.get(1).getOption());
                    op3.setText(listOption.get(2).getOption());
                    op4.setText(listOption.get(3).getOption());

                    op1.setBackground(getDrawable(R.drawable.round_ans));
                    op2.setBackground(getDrawable(R.drawable.round_ans));
                    op3.setBackground(getDrawable(R.drawable.round_ans));
                    op4.setBackground(getDrawable(R.drawable.round_ans));

                    listenClickEvent();
                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(this, R.string.dialogErrorTitle, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private List<Question> getQuestionList() {
        return questionSQLiteHelper.getQuestionsByLevelAndEmail(Level, getUserEmail());
    }

    private void LevelUp() {
        Query query = userCollRef.whereEqualTo("email", getUserEmail());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                    int newLevel = Level + 1;
                    document.getReference().update("level", newLevel)
                            .addOnSuccessListener(aVoid -> {
                                runOnUiThread(this::showLevelUpMessage);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Practice1.this, R.string.dialogErrorTitle, Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }

    private void showLevelUpMessage() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alertdialog, null);
        Button cancel = dialogView.findViewById(R.id.button_ok);
        TextView text = dialogView.findViewById(R.id.dialog_text);
        TextView titleD = dialogView.findViewById(R.id.text_title);

        int errors = 0;
        for (Question q : listQuest) {
            errors += q.getRetries();
        }

        text.setTextColor(getColor(R.color.green));
        text.setText(String.format(getString(R.string.dialog_complete_mes), errors));
        titleD.setTextColor(getColor(android.R.color.holo_orange_dark));
        titleD.setText(getString(R.string.dialog_complete_titl));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
            remove();
        });
    }

    private void showAlreadyCompleteLesson() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alertdialog, null);
        Button delete = dialogView.findViewById(R.id.button_ok);
        Button cancel = dialogView.findViewById(R.id.button_no);
        TextView text = dialogView.findViewById(R.id.dialog_text);
        TextView titleD = dialogView.findViewById(R.id.text_title);

        text.setText(getString(R.string.dialog_retries));
        titleD.setText(getString(R.string.dialog_AlreadyComplete));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        delete.setOnClickListener(v -> {
            alertDialog.dismiss();
            remove();
        });
    }

    private void listenClickEvent() {
        op1.setOnClickListener(v -> {
            isCorrect(op1, listOption.get(0).isRight());
        });

        op2.setOnClickListener(v -> {
            isCorrect(op2, listOption.get(1).isRight());
        });

        op3.setOnClickListener(v -> {
            isCorrect(op3, listOption.get(2).isRight());
        });

        op4.setOnClickListener(v -> {
            isCorrect(op4, listOption.get(3).isRight());
        });
    }

    private void isCorrect(TextView view, boolean correct) {
        if (correct) {
            view.setBackground(getDrawable(R.drawable.round_ans_correct));
            view.setEnabled(false);
            questionSQLiteHelper.updateCompleteStatus(listQuest.get(getPosition()).getIdQuestion(), true);
            animationView.playAnimation();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                animationView.cancelAnimation();
                onRestart();
                Intent intent = new Intent(this, Practice1.class);
                startActivity(intent);
                finish();
            }, 3000);

        } else {
            view.setBackground(getDrawable(R.drawable.round_ans_incorrect));
            retries++;
            // actualizo los reintentos.
            questionSQLiteHelper.updateRetriesById(listQuest.get(getPosition()).getIdQuestion(), retries);
        }
    }

    public void setCnt(int i) {
        SharedPreferences pref = getSharedPreferences("PRACTICE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("cnt", i);
        editor.apply();
    }

    public void remove() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRestart() {
        SharedPreferences pref = getSharedPreferences("PRACTICE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("cnt", pref.getInt("cnt", 0) + 1);
        editor.apply();

        super.onRestart();
    }
}
