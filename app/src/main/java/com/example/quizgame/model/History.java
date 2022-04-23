package com.example.quizgame.model;

public class History {
    String image, categoryName, time;
    int correctAnswer;
    int total;

    public History() {
    }

    public History(String image, String categoryName, String time, int correctAnswer, int total) {
        this.image = image;
        this.categoryName = categoryName;
        this.time = time;
        this.correctAnswer = correctAnswer;
        this.total = total;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
