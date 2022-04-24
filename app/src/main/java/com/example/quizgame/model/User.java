package com.example.quizgame.model;

import com.example.quizgame.model.Gift;
import com.example.quizgame.model.History;

import java.util.ArrayList;

public class User {
    private String id;
    private String name, email, pass;
    private long coins;
    private ArrayList<Gift> gifts;
    private ArrayList<History> histories;


    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public User(String name, long coins) {
        this.name = name;
        this.coins = coins;
    }


    public User(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }



    public User(String id, String name, long coins) {
        this.id = id;
        this.name = name;
        this.coins = coins;
    }

    public User(String id, String name, String email, String pass, long coins, ArrayList<Gift> gifts, ArrayList<History> histories) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.coins = coins;
        this.gifts = gifts;
        this.histories = histories;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
