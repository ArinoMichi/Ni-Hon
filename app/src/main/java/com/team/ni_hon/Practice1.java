package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.team.ni_hon.databinding.ActivityPractice1Binding;
import com.team.ni_hon.local_data_base.OptionsSQLiteHelper;
import com.team.ni_hon.local_data_base.QuestionSQLiteHelper;
import com.team.ni_hon.local_data_base.database_models.Option;
import com.team.ni_hon.local_data_base.database_models.Question;

import java.util.List;

public class Practice1 extends NiHonActivity {

    private final String TAG = "TYPE01_PRACTICE";
    private TextView question, op1, op2, op3, op4;
    private List<Question> listQuest;
    private List<Option> listOption;
    private QuestionSQLiteHelper questionSQLiteHelper;
    private OptionsSQLiteHelper optionsSQLiteHelper;

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

        questionSQLiteHelper = new QuestionSQLiteHelper(this);
        optionsSQLiteHelper = new OptionsSQLiteHelper(this);

        listQuest = getQuestionList();

        initComponent();
    }

    public void initComponent() {

        if(listQuest!=null){
            SharedPreferences pref=getSharedPreferences("Config",MODE_PRIVATE);

            switch(pref.getString("language",null)){
                case "es":
                    question.setText(listQuest.get(0).getQuestionES());
                    break;
                case "zh":
                    question.setText(listQuest.get(0).getQuestionCH());
                    break;
                default:
                    question.setText(listQuest.get(0).getQuestionEN());
                    break;
            }

            loadOptions(listQuest.get(0).getIdQuestion());
        }

    }

    public void loadOptions(String id){
        listOption=optionsSQLiteHelper.getOptionsByQuestionId(id);

        try {
            op1.setText(listOption.get(0).getOption());
            op2.setText(listOption.get(1).getOption());
            op3.setText(listOption.get(2).getOption());
            op4.setText(listOption.get(3).getOption());
        }catch (IndexOutOfBoundsException e){
            Toast.makeText(this, R.string.dialogErrorTitle, Toast.LENGTH_SHORT).show();
        }
    }

    public List<Question> getQuestionList() {
        SharedPreferences pref=getSharedPreferences("UserSession",MODE_PRIVATE);
        int Level=pref.getInt("level",-1);

        return questionSQLiteHelper.getQuestionsByLevelAndEmail(Level,getUserEmail());
    }
}