package com.team.ni_hon.local_data_base.database_models;

import java.util.List;

public class Question {
    private int level;
    private String questionES;
    private String questionEN;
    private String questionCH;
    private String idQuestion;
    private boolean complete;
    private int retries;
    private String email;
    private List<Option> options;

    public Question(){}

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getQuestionES() {
        return questionES;
    }

    public void setQuestionES(String questionES) {
        this.questionES = questionES;
    }

    public String getQuestionEN() {
        return questionEN;
    }

    public void setQuestionEN(String questionEN) {
        this.questionEN = questionEN;
    }

    public String getQuestionCH() {
        return questionCH;
    }

    public void setQuestionCH(String questionCH) {
        this.questionCH = questionCH;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
