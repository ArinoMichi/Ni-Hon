package com.team.ni_hon.utils;

import androidx.annotation.Nullable;

import com.team.ni_hon.local_data_base.database_models.LocalUser;
import com.team.ni_hon.local_data_base.database_models.Option;
import com.team.ni_hon.local_data_base.database_models.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyJsonParser {
    @Nullable
    public static LocalUser parseJsonToObjects(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("questions");

            List<Question> questions = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject questionObj = jsonArray.getJSONObject(i);
                int level = questionObj.getInt("level");
                String questionES = questionObj.getString("questionES");
                String questionEN = questionObj.getString("questionEN");
                String questionCH = questionObj.getString("questionCH");
                UUID uniqueId = UUID.randomUUID();
                String idQuestion =i+String.valueOf(uniqueId);
                boolean complete = questionObj.getBoolean("correct");
                int retries = questionObj.getInt("retries");
                String email = questionObj.getString("email");

                JSONArray optionsArray = questionObj.getJSONArray("options");
                List<Option> options = new ArrayList<>();

                for (int j = 0; j < optionsArray.length(); j++) {
                    JSONObject optionObj = optionsArray.getJSONObject(j);
                    String option = optionObj.getString("option");
                    boolean right = optionObj.getBoolean("right");

                    Option optionItem = new Option(option, right, idQuestion);
                    options.add(optionItem);
                }

                Question question = new Question();

                question.setIdQuestion(idQuestion);
                question.setQuestionCH(questionCH);
                question.setQuestionEN(questionEN);
                question.setQuestionES(questionES);
                question.setEmail(email);
                question.setLevel(level);
                question.setRetries(retries);
                question.setComplete(complete);
                question.setOptions(options);

                questions.add(question);
            }

            String email = null; // Set the appropriate email value

            LocalUser localUser = new LocalUser(email, questions);
            return localUser;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
