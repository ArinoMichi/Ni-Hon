package com.team.ni_hon.local_data_base.database_models;

public class Option {
    private String option;
    private boolean right;
    private String idQuestion;

    public Option(){}

    public Option(String option, boolean right, String idQuestion) {
        this.option = option;
        this.right = right;
        this.idQuestion = idQuestion;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }
}