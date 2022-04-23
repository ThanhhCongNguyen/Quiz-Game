package com.example.quizgame.model;

import java.util.ArrayList;

public class Question {
    private String question, answer;
    private ArrayList<String> options;

    public Question() {
    }

    public Question(String question, String answer, ArrayList<String> options) {
        this.question = question;
        this.answer = answer;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOption(ArrayList<String> options) {
        this.options = options;
    }
}
