package com.example.flexhaven.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {
    public String userEmail, userUsername;
    public String name;
    public ArrayList<String> category;
    public String itemKey, condition, location, itemDescription, price, imageUrl;
    private User owner;
    private RentalStatus rentalStatus;

    // Default constructor is needed for Firebase
    public Item() {
    }

    // Constructor
    public Item(String email, String username, String itemName, ArrayList<String> category, String condition, String price, String location, String itemDescription, String uriString, User owner, RentalStatus rentalStatus) {
        this.userEmail = email;
        this.userUsername = username;
        this.name = itemName;
        this.category = category;
        this.condition = condition;
        this.price = price;
        this.location = location;
        this.itemDescription = itemDescription;
        this.imageUrl = uriString;

        this.itemKey = itemName + "_" + email + "_" + username + "_" + itemDescription;
        this.owner = owner;
        this.rentalStatus = rentalStatus;
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

    public RentalStatus getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(RentalStatus rentalStatus) {
        this.rentalStatus = rentalStatus;
    }
}