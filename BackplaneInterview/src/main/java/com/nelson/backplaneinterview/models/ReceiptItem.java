package com.nelson.backplaneinterview.models;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptItem {
    private int count;
    private double totalPrice;
    private String name;
    private double itemPrice;

    public ReceiptItem(JSONObject jsonObject) throws JSONException {
        count = jsonObject.getInt("count");
        totalPrice = jsonObject.getDouble("total_price");
        name = jsonObject.getString("name");
        itemPrice = jsonObject.getDouble("item_price");
    }

    public int getCount() {
        return count;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getName() {
        return name;
    }

    public double getItemPrice() {
        return itemPrice;
    }
}
