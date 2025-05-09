package com.example.laptrinh_mobile;

public class Expense {
    private int id;
    private double amount;
    private String category;
    private String description;
    private String date;

    public Expense(int id, double amount, String category, String description, String date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("Số tiền: %.2f VND - %s - %s - Ngày: %s", amount, category, description, date);
    }
}