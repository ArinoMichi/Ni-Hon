package com.team.ni_hon.recycler;

public class Lesson {

    private int id;
    private String title, popupText;

    public Lesson(int id, String title, String popupText) {
        this.id = id;
        this.title = title;
        this.popupText = popupText;
    }

    public String getTitle() {
        return title;
    }

    public String getPopupText() {
        return popupText;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
