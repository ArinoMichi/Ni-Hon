package com.team.ni_hon.main_recycler;

import java.io.Serializable;

public class Lesson implements Serializable {

    private int id, pages;
    private String title, popupText;

    public Lesson(int id, String title, String popupText, int pages) {
        this.id = id;
        this.title = title;
        this.popupText = popupText;
        this.pages = pages;
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

    public int getPages() {
        return pages;
    }
}
