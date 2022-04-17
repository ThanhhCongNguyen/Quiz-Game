package com.example.quizgame;

import java.util.ArrayList;

public class User {
    private String name, email, pass;
    private long coins = 100;
    private ArrayList<Gift> gifts;
    private ArrayList<History> histories;


    public User() {
    }

    public User(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public User(String name, String email, String pass, long coins, ArrayList<Gift> gifts, ArrayList<History> histories) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.coins = coins;
        this.gifts = gifts;
        this.histories = histories;
    }

    public User(String name, long coins) {
        this.name = name;
        this.coins = coins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<Gift> gifts) {
        this.gifts = gifts;
    }

    public ArrayList<History> getHistories() {
        return histories;
    }

    public void setHistories(ArrayList<History> histories) {
        this.histories = histories;
    }
}
