package com.example.flexhaven.helpers;

public class Item {
    public String name;
    public String description;
    public String userEmail;
    public int price;

    // Default constructor is needed for Firebase
    public Item() {
    }

    // Constructor
    public Item(String name, String description, String userEmail, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}