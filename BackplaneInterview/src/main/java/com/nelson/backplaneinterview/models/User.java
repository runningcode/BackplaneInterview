package com.nelson.backplaneinterview.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private double balance;
    private String name;

    public User(double balance, String name) {
        this.balance = balance;
        this.name = name;
    }

    public User(JSONObject jsonObject) throws JSONException {
        balance = jsonObject.getDouble("balance");
        name = jsonObject.getString("name");
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }
}
