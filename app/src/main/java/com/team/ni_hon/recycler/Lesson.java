package com.team.ni_hon.recycler;

public class Lesson {

    private int id;
    private String title;

    public Lesson(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
