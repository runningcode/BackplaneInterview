package com.nelson.backplaneinterview.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Receipt {

    private Date date;
    private double total;
    private int id;
    private String restraurantName;
    private List<ReceiptItem> receiptItems;

    public Receipt(Date date, double total, int id, String restraurantName) {
        this.date = date;
        this.total = total;
        this.id = id;
        this.restraurantName = restraurantName;
        receiptItems = new ArrayList<ReceiptItem>();
    }

    public Receipt(JSONObject jsonObject) throws JSONException, ParseException {
        String dateString = jsonObject.getString("date");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZZZ");
        date = simpleDateFormat.parse(dateString);
        total = jsonObject.getDouble("total");
        id = jsonObject.getInt("id");
        restraurantName = jsonObject.getString("restaurant");
        receiptItems = new ArrayList<ReceiptItem>();
    }

    public String getRestraurantName() {
        return restraurantName;
    }

    public Date getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public int getId() {
        return id;
    }

    public void setReceiptItems(List<ReceiptItem> receiptItems) {
        this.receiptItems = receiptItems;
    }

    public List<ReceiptItem> getReceiptItems() {
        return receiptItems;
    }
}
