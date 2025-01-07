package com.example.myapplicationmanageexpenses;

public class Expense {
    private String description;
    private double amount;
    private String category;
    private String date;
    private String id;

    public Expense(String description, double amount, String category, String date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Expense(String id,String description, double amount, String category, String date) {
        this.id=id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getId(){ return id;}

}
