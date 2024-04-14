package com.example.flexhaven.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {
    public String userEmail;
    public String name;
    public ArrayList<String> category;
    public String condition, location, itemDescription, price, imageUrl;
    private User owner;

    // Default constructor is needed for Firebase
    public Item() {
    }

    // Constructor
    public Item(String email, String itemName, ArrayList<String> category, String condition, String price, String location, String itemDescription, String uriString, User owner) {
        this.userEmail = email;
        this.name = itemName;
        this.category = category;
        this.condition = condition;
        this.price = price;
        this.location = location;
        this.itemDescription = itemDescription;
        this.imageUrl = uriString;
        this.owner = owner;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
}