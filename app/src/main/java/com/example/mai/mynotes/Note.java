package com.example.mai.mynotes;

public class Note {

    private String title;
    private String description;
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (!title.equals(""))
            this.title = title;
        else
            this.title = "N/A";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
