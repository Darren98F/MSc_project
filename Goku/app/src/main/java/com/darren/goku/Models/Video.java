package com.darren.goku.Models;

public class Video {
    private String title;
    private String path;
    private String comment;
    private String evaluate;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", comment='" + comment + '\'' +
                ", evaluate='" + evaluate + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
