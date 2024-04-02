package com.example.flexhaven.helpers;

import java.util.ArrayList;
import java.util.List;

public class Item {
    public String userEmail;
    public String name;
    public ArrayList<String> category;
    public String condition;
    public String location;
    public String itemDescription;
    public String price;

    // Default constructor is needed for Firebase
    public Item() {
    }

    // Constructor
    public Item(String email, String itemName, ArrayList<String> category, String condition, String price, String location, String itemDescription) {
        this.userEmail = email;
        this.name = itemName;
        this.category = category;
        this.condition = condition;
        this.price = price;
        this.location = location;
        this.itemDescription = itemDescription;
    }
}