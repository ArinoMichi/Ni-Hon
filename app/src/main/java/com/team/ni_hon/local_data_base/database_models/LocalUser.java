package com.team.ni_hon.local_data_base.database_models;

import java.util.List;

public class LocalUser {
    private String email;
    private List<Question> questions;

    public LocalUser(){}
    public LocalUser(String email, List<Question> questions) {
        this.email = email;
        this.questions=questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
